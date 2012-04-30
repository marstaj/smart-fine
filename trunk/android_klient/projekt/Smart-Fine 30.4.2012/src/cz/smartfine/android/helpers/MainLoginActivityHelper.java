package cz.smartfine.android.helpers;

import cz.smartfine.android.MainLoginActivity;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.LoginProvider;
import cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener;
import cz.smartfine.networklayer.model.mobile.LoginFailReason;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

/**
 * TODO doplnit
 * 
 * @author Martin Stajner
 * 
 */
public class MainLoginActivityHelper extends Handler implements ILoginProviderListener {

	/**
	 * 
	 */
	private String badgeNumber;

	/**
	 * 
	 */
	private String pin;

	/**
	 * Handler obsluhující zprávy z jiného vlákna
	 */
	private Handler handler = this;

	/**
	 * 
	 */
	private MainLoginActivity activity;

	/**
	 * Konstruktor
	 * 
	 * @param app
	 *            Instance aplikace
	 */
	public MainLoginActivityHelper(MainLoginActivity mainLoginActivity) {
		this.activity = mainLoginActivity;
	}

	@Override
	public void handleMessage(Message msg) {
		Bundle data = msg.getData();

		// V pripade toastu oznameni z vlakna
		if (data.containsKey("msg")) {
			Toaster.toast(data.getString("msg"), Toaster.SHORT);
		}

		// Odpoved z vlakna, ktere se snazilo pripojit k serveru
		if (data.containsKey("connect")) {
			if (data.getBoolean("connect")) {

				// Ziskani TelephonyManager kvuli zjisteni IMEI cisla

				// Prihlaseni
				login(activity, Integer.valueOf(badgeNumber), Integer.valueOf(pin), getIMEI());

			} else {
				// TODO napsat do R
				activity.getProggressDialog().dismiss();
				Toaster.toast("Nepodařilo se připojit k serveru.", Toaster.SHORT);
			}
		}
	}

	// ==================================== METODY POSLUCHAČE ==================================== //

	private boolean logout = false;

	public void onConnectionTerminated() {
		if (logout) {
			// TODO napsat to do R 
			logout = false;
			Messenger.sendStringMessage(handler, "Odhlášení proběhlo úspěšně.");
		} else {
			activity.getProggressDialog().dismiss();
			// TODO napsat to do R 
			Messenger.sendStringMessage(handler, "ConnectionTerminated.");
		}
	}

	public void onLoginConfirmed() {
		// Zrusi dialog
		activity.getProggressDialog().dismiss();
		// Spusti MainActivity
		activity.continueToMain();
	}

	public void onLoginFailed(LoginFailReason reason) {
		// Zrusi dialog
		activity.getProggressDialog().dismiss();

		// TODO napsat to do R
		String error = "Došlo k chybě při přihlašování.";
		switch (reason) {
			case UNKNOWN_REASON : {
				// TODO napsat to do R 
				error = "Neznámá chyba";
				break;
			}
			case WRONG_BADGE_NUMBER_OR_PIN : {
				// TODO napsat to do R 
				error = "Špatné přihlašovací údaje";
				break;
			}
			case IMEI_AND_BADGE_NUMBER_DONT_MATCH : {
				// TODO napsat to do R 
				error = "Služebním číslo není přiřazeno k tomuto zařízení.";
				break;
			}
			case UNKNOWN_IMEI : {
				// TODO napsat to do R 
				error = "Neznámé zařízení.";
				break;
			}
			case CONNECTION_TERMINATED_FROM_SERVER : {
				// TODO napsat to do R 
				error = "Spojení ukončeno serverem.";
				break;
			}
		}
		Messenger.sendStringMessage(handler, error);
	}

	public void onLogout() {
		logout = true;
		// Odpojení ze serveru
		activity.getApp().getConnectionProvider().disconnect();
	}

	public void onMessageSent() {

	}

	// ==================================== GET / SET ==================================== //

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getIMEI() {
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	// ==================================== OSTATNÍ ==================================== //

	/**
	 * Metoda zvaliduje délku řetězce "Služební číslo" a "PIN". Služební číslo
	 * musí mít délku 6, PIN 5. V případě, že alespoň jedno z nich (nebo oboje)
	 * nedosahují požadované délky, metoda vytvoří a vrátí chybovou hlášku. V
	 * opačném případě vrátí null.
	 * 
	 * @param badgeNumber
	 *            Služební číslo
	 * @param pin
	 *            PIN
	 * @return Chybovou hlášku v případě problému, jinak null
	 */
	public String validateForm(String badgeNumber, String pin) {
		boolean badgeNumberError = false;
		String loginError = "";
		// Kontrola zda uzivatel napsal sluzebni cislo
		if (badgeNumber.length() != 6) {
			// TODO dat do R
			loginError += "Služební číslo nemá požadovanou délku 6.";
			badgeNumberError = true;
		}
		// Kontrola zda uzivatel napsal heslo
		if (pin.length() != 5) {
			if (badgeNumberError) {
				loginError += "\n";
			}
			loginError += "Heslo nemá požadovanou délku 5.";
		}
		if (loginError.length() == 0) {
			loginError = null;
		}
		return loginError;
	}

	/**
	 * @param ac
	 */
	public void login(MainLoginActivity ac, int badgeNumber, int pin, String imei) {
		LoginProvider lp = new LoginProvider(ac.getApp().getConnectionProvider().getNetworkInterface(), ac, this);
		lp.login(badgeNumber, pin, imei);
	}

	/**
	 * @param ac
	 */
	public void logout(MainLoginActivity ac) {
		LoginProvider lp = new LoginProvider(ac.getApp().getConnectionProvider().getNetworkInterface(), ac, this);
		lp.logout();
	}

	/**
	 * 
	 */
	public void connect() {
		// Pripojeni na server ve zvlastnim vlakne
		Runnable rable = new Runnable() {
			public void run() {
				boolean conected = activity.getApp().getConnectionProvider().connect();
				Message msg = Message.obtain();
				Bundle data = new Bundle();
				data.putBoolean("connect", conected);
				msg.setData(data);
				handler.sendMessage(msg);
			}
		};
		final Thread thread = new Thread(rable);
		thread.start();

		// Přidat nejakej timeout casovac?? TODO
		//		handler.postDelayed(new Runnable() {
		//			public void run() {
		//				thread.stop(); // tohle je deprecated :/				
		//			}			
		//		}, 20000);
	}

}
