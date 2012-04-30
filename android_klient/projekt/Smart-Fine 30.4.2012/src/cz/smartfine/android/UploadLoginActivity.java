package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.helpers.MainLoginActivityHelper;
import cz.smartfine.android.helpers.UploadLoginActivityHelper;
import cz.smartfine.android.model.util.Internet;
import cz.smartfine.android.model.util.Toaster;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UploadLoginActivity extends Activity {

	// TODO Doplnit komentare a javadoc

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
	 * 
	 */
	UploadLoginActivity ac = this;

	/**
	 * 
	 */
	UploadLoginActivityHelper helper;

	/*
	 * (non-Javadoc)
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

	/**
	 * Obsluha tlačítka "Přihlásit se"
	 * 
	 * @param button
	 */
	public void loginClick(View button) {

		// Zkontroluje prihlaseni k internetu
		if (Internet.isOnline(this)) {

			helper.setBadgeNumber(formBadgeNumber.getText().toString());
			helper.setPin(formPIN.getText().toString());

			// Kontrola zda uzivatel napsal sluzebni cislo odznaku
			String loginError = helper.validateForm(helper.getBadgeNumber(), helper.getPin());

			// Kdyz nechybi nejaky udaj, tak pokracovat, jinak vyhodit error.
			if (loginError == null) {

				// Nastavení dialogu a LogIN
				// TODO napsat do R
				progDialog = new ProgressDialog(this);
				progDialog.setTitle("Prihlašování");
				progDialog.setMessage("Prosím počkejte...");
				progDialog.setIndeterminate(true);
				progDialog.setCancelable(false);
				progDialog.show();

				// Kontrola, zda uz nahodou nejsme pripojeni. Kdyz ano, tak se rovnou zkusime prihlasit, kdyz ne, tak se nejdrive zkusime pripojit.
				if (app.getConnectionProvider().isConnected()) {
					helper.authenticate(this, Integer.valueOf(helper.getBadgeNumber()), Integer.valueOf(helper.getPin()));
				} else {
					helper.connectAndLogin();
				}

				// TODO tohle se pak zrusi samozrejme
				//onLoginConfirmed();				

			} else {
				Toaster.toast(loginError, Toaster.SHORT);
			}
		} else {
			// TODO do R.
			Toaster.toast("K přihlášení musíte být připojeni k internetu.", Toaster.SHORT);
		}
	}

	/**
	 * Obsluha tlačítka - Zrusit
	 * 
	 * @param button
	 */
	public void cancelClick(View button) {
		finish();
	}

	// ==================================== GET / SET ==================================== //

	/**
	 * @return
	 */
	public Dialog getProggressDialog() {
		return progDialog;
	}

	/**
	 * @return
	 */
	public MyApp getApp() {
		return app;
	}

	// ==================================== OSTATNÍ ==================================== //

	public void finishSuccessfulLogin() {
		setResult(RESULT_OK, new Intent().putExtra("badgeNumber", helper.getBadgeNumber()));
		finish();
	}

	@Override
	public void finish() {

		// Odpoji protokol
		if (helper.getProtocol() != null) {
			helper.getProtocol().disconnectProtocol();
		}
		super.finish();
	}

}
