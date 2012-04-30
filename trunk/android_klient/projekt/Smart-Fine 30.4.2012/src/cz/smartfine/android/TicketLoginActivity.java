package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.model.Settings;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.LoginProvider;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TicketLoginActivity extends Activity {

	// TODO Doplnit komentare a javadoc

	Settings settings;

	EditText formId;
	EditText formPass;

	int badgeNumber;
	int pin;

	ProgressDialog progDialog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Nastavi pristup do settings
		settings = Settings.getInstance();

		setContentView(R.layout.login);		
		
		// Zablokuje nadpis
		((TextView) findViewById(R.id.loginTitle)).setVisibility(View.GONE);

		// ziska pristup k formularovym polim
		formId = (EditText) findViewById(R.id.idNumber);
		formId.setEnabled(false);
		formPass = (EditText) findViewById(R.id.password);
		formPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

		// Zkusi ziskad id cislo pro predvyplneni
		badgeNumber = LoginProvider.getBadgeNumber(this);
		if (badgeNumber != -1) {
			formId.setText(String.valueOf(badgeNumber));
		}
		
		pin = LoginProvider.getPIN(this);

	}

	/**
	 * Obsluha tlačítka - Prihlasit se
	 * 
	 * @param button
	 */
	public void loginClick(View button) {

		String password = formPass.getText().toString();

		// Kontrola zda uzivatel napsal sluzebni cislo odznaku
		String loginError = validatePin(password);

		// Kdyz chybi nejaky udaj, tak vyhodit error. jinak pokracovat
		if (loginError != null) {
			Toaster.toast(loginError, Toaster.SHORT);
		} else {

			// Zkontroluje login			
			if (Integer.valueOf(password) != (pin)) {
				// TODO do R
				Toaster.toast("Heslo nesouhlasi.", Toaster.SHORT);
			} else {
				setResult(RESULT_OK);
				finish();
			}
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

	/**
	 * @param id
	 * @param pin
	 * @return
	 */
	private String validatePin(String pass) {
		String loginError = "";
		// Kontrola zda uzivatel napsal heslo 
		if (pass.length() != 5) {			
			// Do R TODO
			loginError += "Heslo nemá požadovanou délku 5.";
		}
		if (loginError.length() == 0) {
			loginError = null;
		}
		return loginError;
	}

}
