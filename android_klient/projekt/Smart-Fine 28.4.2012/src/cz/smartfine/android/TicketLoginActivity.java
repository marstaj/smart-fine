package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.model.Settings;
import cz.smartfine.android.model.util.Toaster;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

public class TicketLoginActivity extends Activity {

	// TODO Doplnit komentare a javadoc

	Settings settings;

	EditText formId;
	EditText formPass;

	String idNumber;
	String pass;

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

		// ziska pristup k formularovym polim
		formId = (EditText) findViewById(R.id.idNumber);
		formId.setEnabled(false);
		formPass = (EditText) findViewById(R.id.password);
		formPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

		// Zkusi ziskad id cislo pro predvyplneni
		idNumber = settings.getLoginIdNumber(this);
		if (idNumber != null) {
			formId.setText(idNumber);
		}
		
		pass = settings.getLoginPassword(this);

	}

	/**
	 * Obsluha tlaèítka - Prihlasit se
	 * 
	 * @param button
	 */
	public void loginClick(View button) {

		String password = formPass.getText().toString();

		// Kontrola zda uzivatel napsal sluzebni cislo odznaku
		String loginError = validateForm(idNumber, password);

		// Kdyz chybi nejaky udaj, tak vyhodit error. jinak pokracovat
		if (loginError != null) {
			Toaster.toast(loginError, Toaster.SHORT);
		} else {

			// Zkontroluje login			
			if (!password.equals(pass)) {
				Toaster.toast("Heslo nesouhlasi.", Toaster.SHORT);
			} else {
				setResult(RESULT_OK);
				finish();
			}
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
	 * @param id
	 * @param pass
	 * @return
	 */
	private String validateForm(String id, String pass) {
		String loginError = "";
		// Kontrola zda uzivatel napsal heslo
		if (pass.length() == 0) {			
			loginError += "Heslo není vyplnìno";
		}
		if (loginError.length() == 0) {
			loginError = null;
		}
		return loginError;
	}

}
