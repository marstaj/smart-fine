package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.helpers.UploadLoginActivityHelper;
import cz.smartfine.android.model.util.Phone;
import cz.smartfine.android.model.util.Toaster;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Třída představující objekt typu Activity, který slouží k přihlášení během
 * nahrávání parkovacích lístků na server.
 * 
 * @author Martin Štajner
 * 
 */
public class UploadLoginActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Formulářové pole Služebního čísla
	 */
	private EditText formBadgeNumber;

	/**
	 * Formulářové pole PINu
	 */
	private EditText formPIN;

	/**
	 * Dialog, který se zobrazí když se aplikace pokouší přihlašovat.
	 */
	private ProgressDialog progDialog;

	/**
	 * Instance pomocného objektu pro přihlášení
	 */
	private UploadLoginActivityHelper helper;

	// ==================================== INITIALIZATON ==================================== //

	/**
	 * Metoda inicializuje objekt UploadLoginActivity a je volána při jeho
	 * vytváření.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Nastaví layout
		setContentView(R.layout.login);

		// Zablokuje nadpis
		((TextView) findViewById(R.id.loginTitle)).setVisibility(View.GONE);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		// Nastavi helper
		helper = new UploadLoginActivityHelper(this);

		// ziska pristup k formularovym polim
		formBadgeNumber = (EditText) findViewById(R.id.idNumber);
		formPIN = (EditText) findViewById(R.id.password);

		// Nastavi aby se do formulare na heslo psaly hvezdicky
		formPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}

	// ==================================== BUTTONS ==================================== //

	/**
	 * Obsluha tlačítka přihlášení se
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void loginClick(View button) {

		// Zkontroluje prihlaseni k internetu
		if (Phone.isConnectedToInternet(getApplicationContext())) {

			helper.setBadgeNumber(formBadgeNumber.getText().toString());
			helper.setPin(formPIN.getText().toString());

			// Kontrola zda uzivatel napsal sluzebni cislo odznaku
			String loginError = helper.validateForm(helper.getBadgeNumber(), helper.getPin());

			// Kdyz nechybi nejaky udaj, tak pokracovat, jinak vyhodit error.
			if (loginError == null) {

				// Nastavení dialogu a LogIN
				progDialog = new ProgressDialog(this);
				progDialog.setTitle(getText(R.string.view_dialog_login));
				progDialog.setMessage(getText(R.string.please_wait));
				progDialog.setIndeterminate(true);
				progDialog.setCancelable(false);
				progDialog.show();

				// Kontrola, zda uz nahodou nejsme pripojeni. Kdyz ano, tak se rovnou zkusime prihlasit, kdyz ne, tak se nejdrive zkusime pripojit.
				if (app.getConnectionProvider().isConnected()) {
					helper.authenticate(this, Integer.valueOf(helper.getBadgeNumber()), Integer.valueOf(helper.getPin()));
				} else {
					helper.connectAndLogin();
				}

			} else {
				Toaster.toast(loginError, Toaster.SHORT);
			}
		} else {
			Toaster.toast(R.string.internet_need_login, Toaster.SHORT);
		}
	}

	/**
	 * Obsluha tlačítka zrušení přihlášení
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void cancelClick(View button) {
		finish();
	}

	// ==================================== OVERRIDE METHODS ==================================== //

	/**
	 * Metoda odpojí komunikační protokol pomocné třídy helper. Je volána při
	 * ukončení aktivity.
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		// Odpoji protokol
		if (helper.getProtocol() != null) {
			helper.getProtocol().disconnectProtocol();
		}
		super.finish();
	}

	// ==================================== GET / SET ==================================== //

	/**
	 * Vrátí instanci aplikace.
	 * 
	 * @return Instance aplikace
	 */
	public MyApp getApp() {
		return app;
	}

	// ==================================== OTHER ==================================== //

	/**
	 * Metoda ukončí aktivitu po úspěšném přihlášení.
	 */
	public void finishSuccessfulLogin() {
		setResult(RESULT_OK, new Intent().putExtra("badgeNumber", helper.getBadgeNumber()));
		finish();
	}

	/**
	 * Metoda zruší dialog.
	 */
	public void dismissDialog() {
		progDialog.dismiss();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

}
