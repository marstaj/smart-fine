package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.LoginProvider;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Třída představující objekt typu Activity, který slouží k ověření identity
 * uživatele při vytváření patkovacích lístků.
 * 
 * @author Martin Štajner
 * 
 */
public class TicketLoginActivity extends Activity {

	/**
	 * Formulářové pole služebního čísla policisty
	 */
	private EditText formBadgeNumber;

	/**
	 * Formulářové pole PINu policisty
	 */
	private EditText formPin;

	/**
	 * PIN policisty
	 */
	private int badgeNumber;

	/**
	 * Služební číslo policisty
	 */
	private int pin;

	/**
	 * Metoda inicializuje objekt TicketEditActivity a je volána při jeho
	 * vytváření.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);

		// Zablokuje nadpis
		((TextView) findViewById(R.id.loginTitle)).setVisibility(View.GONE);

		// ziska pristup k formularovym polim
		formBadgeNumber = (EditText) findViewById(R.id.idNumber);
		formBadgeNumber.setEnabled(false);
		formPin = (EditText) findViewById(R.id.password);
		formPin.setTransformationMethod(PasswordTransformationMethod.getInstance());

		// Zkusi ziskad id cislo pro predvyplneni
		badgeNumber = LoginProvider.getBadgeNumber(this);
		if (badgeNumber != -1) {
			formBadgeNumber.setText(String.valueOf(badgeNumber));
		}

		pin = LoginProvider.getPIN(this);

	}

	/**
	 * Obsluha tlačítka přihlášení se.
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void loginClick(View button) {

		String password = formPin.getText().toString();

		// Kontrola zda uzivatel napsal sluzebni cislo odznaku
		String loginError = validatePin(password);

		// Kdyz chybi nejaky udaj, tak vyhodit error. jinak pokracovat
		if (loginError != null) {
			Toaster.toast(loginError, Toaster.SHORT);
		} else {

			// Zkontroluje login			
			if (Integer.valueOf(password) != (pin)) {
				Toaster.toast(R.string.val_login_wrongpin, Toaster.SHORT);
			} else {
				setResult(RESULT_OK);
				finish();
			}
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

	/**
	 * Funkce zvaliduje PIN
	 * 
	 * @param pass
	 *            PIN
	 * @return Chybová hláška
	 */
	private String validatePin(String pass) {
		String loginError = "";
		// Kontrola zda uzivatel napsal heslo 
		if (pass.length() != 5) {
			loginError += getText(R.string.val_login_err_pin);
		}
		if (loginError.length() == 0) {
			loginError = null;
		}
		return loginError;
	}

}
