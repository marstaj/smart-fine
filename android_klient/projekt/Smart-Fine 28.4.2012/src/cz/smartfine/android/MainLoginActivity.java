package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.model.Settings;
import cz.smartfine.android.model.util.Internet;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.LoginProvider;
import cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener;
import cz.smartfine.android.networklayer.model.LoginFailReason;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

public class MainLoginActivity extends Activity implements ILoginProviderListener {

	// TODO Doplnit komentare a javadoc

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	Settings settings;

	EditText formId;
	EditText formPass;

	String idNumber;
	String pin;

	/**
	 * Globální hodnota reprezentující Vytvoøení nového PL
	 */
	private static final int LOG_OUT = 0;

	ProgressDialog progDialog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//nastaví defaultní hodnoty nastavení tam, kde nejsou zatím žádné hodnoty
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		// Nastavi pristup do settings
		settings = Settings.getInstance();

		// Zjisti, zda je uzivatel prihlasen, pokud ano, spusti se Main Activity. Pokud ne, probìhne pøihlášení
		if (settings.isLogged(this)) {
			continueToMain();
		} else {

			setContentView(R.layout.login);

			// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
			app = (MyApp) this.getApplication();

			// ziska pristup k formularovym polim
			formId = (EditText) findViewById(R.id.idNumber);
			formPass = (EditText) findViewById(R.id.password);
			formPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

			// Zkusi ziskad id cislo pro predvyplneni
			String idNumber = settings.getLoginIdNumber(this);
			if (idNumber != null) {
				formId.setText(idNumber);
			}
		}
	}

	/**
	 * 
	 */
	private void continueToMain() {
		startActivityForResult(new Intent(this, MainActivity.class), LOG_OUT);
	}

	/**
	 * Obsluha tlaèítka - Prihlasit se
	 * <p>
	 * Metodazkontroluje, zda jsou vyplnìna obì pøihlašovací pole, a v pøípadì,
	 * že ano, zkusí se pøihlásit.
	 * 
	 * @param button
	 */
	public void loginClick(View button) {

		// Zkontroluje prihlaseni k internetu
		if (Internet.isOnline(this)) {
			idNumber = formId.getText().toString();
			pin = formPass.getText().toString();

			// Kontrola zda uzivatel napsal sluzebni cislo odznaku
			String loginError = validateForm(idNumber, pin);

			// Kdyz chybi nejaky udaj, tak vyhodit error. jinak pokracovat
			if (loginError != null) {
				Toaster.toast(loginError, Toaster.SHORT);
			} else {
								
				// Ziskani TelephonyManager kvuli zjisteni IMEI cisla
				TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

				// Nastavení dialogu a LogIN
				// TODO napsat do R
				progDialog = new ProgressDialog(this);
				progDialog.setTitle("Prihlašování");
				progDialog.setMessage("Ovìøování údajù, prosím poèkejte.");
				progDialog.setIndeterminate(true);
				progDialog.show();
				
				// connect TODO

				//			LoginProvider lp = new LoginProvider(app.getConnectionProvider().getNetworkInterface(), this.getBaseContext(), this);
				//			lp.login(Integer.valueOf(idNumber), Integer.valueOf(pin), tm.getDeviceId());

				onLoginConfirmed();
			}
		} else {
			// TODO do R.
			Toaster.toast("K pøihlášení musíte být pøipojeni k internetu.", Toaster.SHORT);
		}

	}

	/**
	 * Obsluha tlaèítka - Zrusit
	 * 
	 * @param button
	 */
	public void cancelClick(View button) {
		finish();
	}

	/**
	 * Metoda zkontroluje, zda jsou vyplnìna obì pøihlašovací pole, a v pøípadì,
	 * že ano, zkusí se pøihlásit.
	 * 
	 * @param id
	 * @param pass
	 * @return
	 */
	private String validateForm(String id, String pass) {
		String loginError = "";
		if (id.length() == 0) { //6
			// TODO dat do R
			loginError += "Služební èíslo není vyplnìno";
		}
		// Kontrola zda uzivatel napsal heslo
		if (pass.length() == 0) { //5
			if (loginError.length() != 0) {
				loginError += "\n";
			}
			loginError += "Heslo není vyplnìno";
		}
		if (loginError.length() == 0) {
			loginError = null;
		}
		return loginError;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Navrat z MainActivity pri odhlaseni
		if (requestCode == LOG_OUT && resultCode == RESULT_OK) {
			LoginProvider lp = new LoginProvider(app.getConnectionProvider().getNetworkInterface(), this.getBaseContext(), this);
			lp.logout();
		} else {
			finish();
		}

	}

	// ==================================== METODY POSLUCHAÈE ILoginProviderListener ==================================== //

	public void onConnectionTerminated() {
		progDialog.dismiss();
		// TODO napsat to do R 
		Toaster.toast("ConnectionTerminated", Toaster.SHORT);
	}

	public void onLoginConfirmed() {
		// Zrusi dialog
		progDialog.dismiss();

		// Ulozi ID do preferenci
		// neukladat a tahat to z connectionprovider TODO
		settings.setLoginIdNumber(this, idNumber);
		settings.setLoginPassword(this, pin);
		settings.setLogged(this, true);

		// Spusti MainActivity
		continueToMain();
	}

	public void onLoginFailed(LoginFailReason reason) {
		// Zrusi dialog
		progDialog.dismiss();

		String message;
		switch (reason) {
			case UNKNOWN_REASON : {
				break;
			}
			case WRONG_BADGE_NUMBER_OR_PIN : {
				break;
			}
			case IMEI_AND_BADGE_NUMBER_DONT_MATCH : {
				break;
			}
			case UNKNOWN_IMEI : {
				break;
			}
			case CONNECTION_TERMINATED_FROM_SERVER : {
				break;
			}
		}
	}

	public void onLogout() {
		settings.setLogged(this, false);
		finish();
	}

	public void onMessageSent() {

	}

}
