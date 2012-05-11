package cz.smartfine.android;

import java.net.InetSocketAddress;

import cz.smartfine.android.R;
import cz.smartfine.android.helpers.MainLoginActivityHelper;
import cz.smartfine.android.model.PrefManager;
import cz.smartfine.android.model.util.Phone;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.LoginProvider;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Třída představující objekt typu Activity, který slouží k hlavnímu přihlášení
 * do aplikace. Objekt kontroluje přihlašovací údaje, přihlašuje k serveru,
 * kontroluje dostupnost providerů polohy a nabízí možnost změny adresy serveru,
 * se kterým aplikace komunikuje.
 * 
 * @author Martin Štajner
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
	 * Formulářové pole IP adresy serveru
	 */
	private EditText formIP;

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
	 * Globální hodnota reprezentující odhlášení ze systému
	 */
	private static final int PROVIDER_CHECK = 1;


	/**
	 * Instance pomocné přihlašovací třídy
	 */
	private MainLoginActivityHelper helper;

	/**
	 * Přístup k nastavení aplikace
	 */
	private PrefManager prefManager;

	// ==================================== INITIALIZATON ==================================== //

	/**
	 * Metoda inicializuje objekt MainLoginActivity a je volána při jeho
	 * vytváření.
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

		// Nastaví prefManager
		prefManager = PrefManager.getInstance();

		// ziska pristup k formularovym polim
		formBadgeNumber = (EditText) findViewById(R.id.idNumber);
		formPIN = (EditText) findViewById(R.id.password);

		// Nastavi aby se do formulare na heslo psaly hvezdicky
		formPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());

		// Nastavi tlacitko zmeny adresy serveru
		((Button) this.findViewById(R.id.changeSyncAddressButton)).setVisibility(View.VISIBLE);

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

	// ==================================== BUTTONS ==================================== //

	/**
	 * Obsluha tlačítka přihlášení se
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void loginClick(View button) {
		// Zkontroluje locaton providery
		checkLocationProviders();
	}

	/**
	 * Obsluha tlačítka změny adresy serveru
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void changeSyncAddressClick(View button) {

		// Zmizi stisknute tlacitko		
		button.setVisibility(View.INVISIBLE);

		// Zobrazi layout
		((LinearLayout) findViewById(R.id.syncAddressLayout)).setVisibility(View.VISIBLE);

		// Nastavi policko s IP adresou
		String address = prefManager.getSyncUrl(this);
		formIP = (EditText) findViewById(R.id.syncAddress);
		formIP.setText(address);
	}

	/**
	 * Obsluha tlačítka změnění adresy serveru
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void syncAddressClick(View button) {
		String address = formIP.getText().toString();
		// Přidání adresy do preferencí.
		if (prefManager.setSyncUrl(this, address)) {
			// Ulozi novou adresu serveru
			InetSocketAddress inetAddress = new InetSocketAddress(address, 25000);
			app.getConnectionProvider().setNewAddress(inetAddress);
			app.getConnectionProvider().disconnect();
			// TODO do R.
			Toaster.toast("Nová adresa uložena.", Toaster.SHORT);
		} else {
			// TODO do R
			Toaster.toast("Adresa je ve špatném tvaru.", Toaster.SHORT);
		}
	}

	/**
	 * Obsluha tlačítka zrušení přihlašování
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void cancelClick(View button) {
		// Ukonceni aplikace
		finish();
	}

	// ==================================== OVERRIDE METHODS ==================================== //

	/**
	 * Metoda zavolá akci po návratu z Aktivity, která byla spuštěna z
	 * MainLoginActivity
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 *      android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Navrat z MainActivity pri odhlaseni
		if (requestCode == LOG_OUT) {
			if (resultCode == RESULT_OK) {
				helper.logout(this);
			} else {
				finish();
			}
		}

		// Navrat z MainActivity pri odhlaseni
		if (requestCode == PROVIDER_CHECK) {
			this.checkLocationProviders();
		}

	}

	// ==================================== GET / SET ==================================== //

	/**
	 * Metoda vrání instanci aplikace
	 * 
	 * @return Instance aplikace
	 */
	public MyApp getApp() {
		return app;
	}

	// ==================================== OTHER ==================================== //

	/**
	 * Metoda kontroluje dostupnost poskytovatelů polohy. V případě, že neni
	 * žádny dostupný, zobrazí dialog, který navede uživatele do nastavení, aby
	 * mohl poskytovatele povolit.
	 */
	private void checkLocationProviders() {
		boolean providers[] = Phone.checkLocationProviders(getApplicationContext());

		if (!(providers[0] || providers[1])) {
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Po použití aplikace je potřeba povolit alespoň jeden zdroj Mojí polohy v systému. Velmi doporučujeme povolit oba (GPS & NET). Chcete je povolit nyní?");
			builder.setCancelable(false);
			builder.setPositiveButton("Yes", new OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), PROVIDER_CHECK);
				}
			}).setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				}
			}).show();
		} else {
			continueWithLogin();
		}

	}

	/**
	 * Metoda zkontroluje, zda jsou vyplněna obě přihlašovací pole, a v případě,
	 * že ano, zavolá přihlášení.
	 */
	private void continueWithLogin() {
		// Zkontroluje prihlaseni k internetu
		if (Phone.isConnectedToInternet(getApplicationContext())) {

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

				// Pripojeni
				helper.connect();

			} else {
				Toaster.toast(loginError, Toaster.SHORT);
			}
		} else {
			// TODO do R.
			Toaster.toast("K přihlášení musíte být připojeni k internetu.", Toaster.SHORT);
		}
	}

	/**
	 * Metoda spustí hlavní aktivitu programu MainActivity
	 */
	public void continueToMain() {
		startActivityForResult(new Intent(this, MainActivity.class), LOG_OUT);
	}

	/**
	 * Metoda zruší dialog.
	 */
	public void dismissDialog() {
		progDialog.dismiss();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}
}
