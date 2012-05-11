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
 * Třída reprezentující pomocný objekt, který slouží k přihlašování na server.
 * 
 * @author Martin Štajner
 * 
 */
public class MainLoginActivityHelper extends Handler implements ILoginProviderListener {

	/**
	 * Služební číslo policisty
	 */
	private String badgeNumber;

	/**
	 * PIN policisty
	 */
	private String pin;

	/**
	 * Objekt handleru obsluhující zprávy z jiného vlákna
	 */
	private Handler handler = this;

	/**
	 * Reference na aktivitu, která s pomocnou třídou komunikuje
	 */
	private MainLoginActivity activity;

	/**
	 * Konstruktor třídy
	 * 
	 * @param mainLoginActivity
	 *            Instance aktivity
	 */
	public MainLoginActivityHelper(MainLoginActivity mainLoginActivity) {
		this.activity = mainLoginActivity;
	}

	/**
	 * Metoda provádí akci na základě obdržené zprávy z jiného vlákna
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
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
				activity.dismissDialog();
				Toaster.toast("Nepodařilo se připojit k serveru.", Toaster.SHORT);
			}
		}
	}

	// ==================================== LISTENER METHODS ==================================== //

	/**
	 * Přiznak značící, zda se jedná o logout
	 */
	private boolean logout = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener
	 * #onConnectionTerminated()
	 */
	public void onConnectionTerminated() {
		if (logout) {
			// TODO napsat to do R 
			logout = false;
			Messenger.sendStringMessage(handler, "Odhlášení proběhlo úspěšně.");
		} else {
			activity.dismissDialog();
			// TODO napsat to do R 
			Messenger.sendStringMessage(handler, "ConnectionTerminated.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener
	 * #onLoginConfirmed()
	 */
	public void onLoginConfirmed() {
		// Zrusi dialog
		activity.dismissDialog();
		// Spusti MainActivity
		activity.continueToMain();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener
	 * #onLoginFailed(cz.smartfine.networklayer.model.mobile.LoginFailReason)
	 */
	public void onLoginFailed(LoginFailReason reason) {
		// Zrusi dialog
		activity.dismissDialog();

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener
	 * #onLogout()
	 */
	public void onLogout() {
		logout = true;
		// Odpojení ze serveru
		activity.getApp().getConnectionProvider().disconnect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener
	 * #onMessageSent()
	 */
	public void onMessageSent() {

	}

	// ==================================== GET / SET ==================================== //

	/**
	 * Metoda vrátí služební číslo policisty
	 * 
	 * @return Služební číslo policisty
	 */
	public String getBadgeNumber() {
		return badgeNumber;
	}

	/**
	 * Metoda nastaví služební číslo policisty.
	 * 
	 * @param badgeNumber
	 *            Služební číslo policisty
	 */
	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	/**
	 * Metoda vrátí PIN policisty.
	 * 
	 * @return PIN
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * Metoda nastaví PIN policisty
	 * 
	 * @param pin
	 *            PIN
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * MEtoda zjistí a vrátí IMEI mobilního zařízení
	 * 
	 * @return IMEI mobilního zařízení
	 */
	public String getIMEI() {
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	// ==================================== OTHER ==================================== //

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
	 * Metoda vytvoří poskytovatele přihlášení a zažádá o přihlášení
	 * 
	 * @param ac
	 *            Aktivita, která žádá o přihlášení
	 * @param badgeNumber
	 *            Služební číslo policisty
	 * @param pin
	 *            PIN policisty
	 * @param imei
	 *            IMEI mobilního zařízení
	 */
	public void login(MainLoginActivity ac, int badgeNumber, int pin, String imei) {
		LoginProvider lp = new LoginProvider(ac.getApp().getConnectionProvider().getNetworkInterface(), ac, this);
		lp.login(badgeNumber, pin, imei);
	}

	/**
	 * Metoda vytvoří poskytovatele přihlášení a zažádá o odhlášení
	 * 
	 * @param ac
	 *            Aktivita, která žádá o odhlášení
	 */
	public void logout(MainLoginActivity ac) {
		LoginProvider lp = new LoginProvider(ac.getApp().getConnectionProvider().getNetworkInterface(), ac, this);
		lp.logout();
	}

	/**
	 * Metoda se v nocém vlákně pokusí připojit k serveru
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

		// Přidat nejakej timeout casovac?? TODO casovac
		//		handler.postDelayed(new Runnable() {
		//			public void run() {
		//				thread.stop(); // tohle je deprecated :/				
		//			}			
		//		}, 20000);
	}

}
