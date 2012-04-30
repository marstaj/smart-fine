package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.helpers.MainLoginActivityHelper;
import cz.smartfine.android.model.util.Internet;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.LoginProvider;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * TODO doplnit javadoc!
 * 
 * @author Martin Stajner
 * 
 */
public class MainLoginActivity extends Activity {

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
	 * Globální hodnota reprezentující odhlášení ze systému
	 */
	private static final int LOG_OUT = 0;

	/**
	 * 
	 */
	MainLoginActivity ac = this;

	/**
	 * 
	 */
	MainLoginActivityHelper helper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		//nastaví defaultní hodnoty nastavení tam, kde nejsou zatím žádné hodnoty
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		// Nastavi helper
		helper = new MainLoginActivityHelper(this);

		// ziska pristup k formularovym polim
		formBadgeNumber = (EditText) findViewById(R.id.idNumber);
		formPIN = (EditText) findViewById(R.id.password);

		// Nastavi aby se do formulare na heslo psaly hvezdicky
		formPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());

		// Inicializuje pripojeni na server. Kdyz nastane chyba behem inicializace, tak aplikaci nelze pouzit.
		try {
			app.inicilizeConnectionToServer();

		} catch (Exception e) {
			// Tlacitko prihlaseni
			((Button) findViewById(R.id.loginButton)).setEnabled(false);

			// TODO nacpat do R
			Toaster.toast("Chyba při inicializaci připojení na server.", Toaster.LONG);
			return;
		}

		// Zjisti, zda je uzivatel prihlasen, pokud ano, spusti se Main Activity. Pokud ne, proběhne přihlšení
		if (LoginProvider.isAvaibleLoginInformation(this)) {
			continueToMain();
		}
	}

	/**
	 * Obsluha tlačítka "Přihlásit se"
	 * <p>
	 * Metoda zkontroluje, zda jsou vyplněna obě přihlašovací pole, a v případě,
	 * že ano, zkusí se přihlásit.
	 * 
	 * @param button
	 *            Stisknuté tlačítko
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
					helper.login(this, Integer.valueOf(helper.getBadgeNumber()), Integer.valueOf(helper.getPin()), helper.getIMEI());
				} else {
					helper.connect();
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
	 * Obsluha tlačítka "Zrušit"
	 * <p>
	 * Metoda ukončí aktivitu.
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void cancelClick(View button) {
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Navrat z MainActivity pri odhlaseni
		if (requestCode == LOG_OUT && resultCode == RESULT_OK) {
			helper.logout(this);
		} else {
			finish();
		}
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

	/**
	 * Metoda spustí hlavní aktivitu programu - "MainActivity".
	 */
	public void continueToMain() {
		startActivityForResult(new Intent(this, MainActivity.class), LOG_OUT);
	}
}
