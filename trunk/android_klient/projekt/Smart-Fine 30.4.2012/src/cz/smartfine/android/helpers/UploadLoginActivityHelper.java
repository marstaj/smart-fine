package cz.smartfine.android.helpers;

import cz.smartfine.android.UploadLoginActivity;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.AuthenticationProtocol;
import cz.smartfine.networklayer.model.mobile.AuthenticationFailReason;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * TODO doplnit
 * 
 * @author Martin Stajner
 * 
 */
public class UploadLoginActivityHelper extends Handler implements IAuthenticationProtocolListener {

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
	private AuthenticationProtocol authp;

	/**
	 * 
	 */
	private UploadLoginActivity activity;

	/**
	 * Konstruktor
	 * 
	 * @param app
	 *            Instance aplikace
	 */
	public UploadLoginActivityHelper(UploadLoginActivity uploadLoginActivity) {
		this.activity = uploadLoginActivity;
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

				// Prihlaseni
				authenticate(activity, Integer.valueOf(badgeNumber), Integer.valueOf(pin));

			} else {
				// TODO napsat do R
				activity.getProggressDialog().dismiss();
				Toaster.toast("Nepodařilo se připojit k serveru.", Toaster.SHORT);
			}
		}

	}

	// ==================================== METODY POSLUCHAČE ==================================== //

	public void onAuthenticationConfirmed() {
		// Zrusi dialog
		activity.getProggressDialog().dismiss();
		// Spusti MainActivity
		activity.finishSuccessfulLogin();

	}

	public void onAuthenticationFailed(AuthenticationFailReason reason) {
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
		}
		Messenger.sendStringMessage(handler, error);

	}

	public void onConnectionTerminated() {
		activity.getProggressDialog().dismiss();
		// TODO napsat to do R 
		Messenger.sendStringMessage(handler, "ConnectionTerminated.");

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

	public AuthenticationProtocol getProtocol() {
		return authp;
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
	public AuthenticationProtocol authenticate(UploadLoginActivity ac, int badgeNumber, int pin) {
		authp = new AuthenticationProtocol(ac.getApp().getConnectionProvider().getNetworkInterface(), this);
		authp.authenticate(badgeNumber, pin);
		return authp;
	}

	/**
	 * 
	 */
	public void connectAndLogin() {
		// Pripojeni na server ve zvlastnim vlakne
		Runnable rable = new Runnable() {
			public void run() {
				boolean conected = activity.getApp().getConnectionProvider().connectAndLogin();
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
