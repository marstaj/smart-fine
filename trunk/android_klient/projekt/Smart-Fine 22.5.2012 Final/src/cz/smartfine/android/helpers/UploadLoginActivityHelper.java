package cz.smartfine.android.helpers;

import cz.smartfine.android.R;
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
 * Třída reprezentující pomocný objekt, který slouží k přihlašování během
 * uploadu parkovacích lístků a geolokačních dat na server.
 * 
 * @author Martin Štajner
 * 
 */
public class UploadLoginActivityHelper extends Handler implements IAuthenticationProtocolListener {

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
	 * Komunikační protokol
	 */
	private AuthenticationProtocol authp;

	/**
	 * Reference na aktivitu, která s pomocnou třídou komunikuje
	 */
	private UploadLoginActivity activity;

	/**
	 * Konstruktor třídy
	 * 
	 * @param uploadLoginActivity
	 *            Instance aktivity
	 */
	public UploadLoginActivityHelper(UploadLoginActivity uploadLoginActivity) {
		this.activity = uploadLoginActivity;
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

				// Prihlaseni
				authenticate(activity, Integer.valueOf(badgeNumber), Integer.valueOf(pin));

			} else {
				activity.dismissDialog();
				Toaster.toast(R.string.connection_unsuccessful, Toaster.SHORT);
			}
		}

	}

	// ==================================== LISTENER METHODS ==================================== //

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.networklayer.business.listeners.
	 * IAuthenticationProtocolListener#onAuthenticationConfirmed()
	 */
	public void onAuthenticationConfirmed() {
		// Zrusi dialog
		activity.dismissDialog();
		// Spusti MainActivity
		activity.finishSuccessfulLogin();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.networklayer.business.listeners.
	 * IAuthenticationProtocolListener
	 * #onAuthenticationFailed(cz.smartfine.networklayer
	 * .model.mobile.AuthenticationFailReason)
	 */
	public void onAuthenticationFailed(AuthenticationFailReason reason) {
		// Zrusi dialog
		activity.dismissDialog();
		String error = activity.getText(R.string.login_error).toString();
		switch (reason) {
			case UNKNOWN_REASON : {
				error = activity.getText(R.string.login_unknownerror).toString();
				break;
			}
			case WRONG_BADGE_NUMBER_OR_PIN : {
				error = activity.getText(R.string.val_login_wrongvalues).toString();
				break;
			}
		}
		Messenger.sendStringMessage(handler, error);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.networklayer.business.listeners.
	 * IAuthenticationProtocolListener#onConnectionTerminated()
	 */
	public void onConnectionTerminated() {
		activity.dismissDialog(); 
		Messenger.sendStringMessage(handler, activity.getText(R.string.connection_terminated).toString());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.networklayer.business.listeners.
	 * IAuthenticationProtocolListener#onMessageSent()
	 */
	public void onMessageSent() {
	}

	// ==================================== GET / SET ==================================== //

	/**
	 * Metoda vrátí služební číslo policisty.
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
	 * Metoda vrátí komunikační protokol.
	 * 
	 * @return Komunikační protokol
	 */
	public AuthenticationProtocol getProtocol() {
		return authp;
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
			loginError += activity.getText(R.string.val_login_err_badgeNumber);
			badgeNumberError = true;
		}
		// Kontrola zda uzivatel napsal heslo
		if (pin.length() != 5) {
			if (badgeNumberError) {
				loginError += "\n";
			}
			loginError += activity.getText(R.string.val_login_err_pin);
		}
		if (loginError.length() == 0) {
			loginError = null;
		}
		return loginError;
	}

	/**
	 * Metoda vytvoří komunikační protokol a pokusí se authentizovat
	 * přihlašovací údaje.
	 * 
	 * @param ac
	 *            Aktivita, která žádá o autentifikaci
	 * @param badgeNumber
	 *            Služební číslo policisty
	 * @param pin
	 *            PIN policisty
	 * @return Komunikační protokol
	 */
	public AuthenticationProtocol authenticate(UploadLoginActivity ac, int badgeNumber, int pin) {
		authp = new AuthenticationProtocol(ac.getApp().getConnectionProvider().getNetworkInterface(), this);
		authp.authenticate(badgeNumber, pin);
		return authp;
	}

	/**
	 * Metoda se v novém vlákně pokusí připojit k serveru a přihlásit.
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

		// Přidat nejakej timeout casovac?? TODO casovac
		//		handler.postDelayed(new Runnable() {
		//			public void run() {
		//				thread.stop(); // tohle je deprecated :/				
		//			}			
		//		}, 20000);
	}

}
