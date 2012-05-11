package cz.smartfine.android;

import java.io.Serializable;
import cz.smartfine.android.R;
import cz.smartfine.android.helpers.BlueCardActivityHelper;
import cz.smartfine.android.model.util.Phone;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.networklayer.model.mobile.SPCInfo;
import cz.smartfine.networklayer.model.mobile.SPCStatus;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Třída představující objekt typu Activity, který slouží ke kontrole odcizení
 * parkovací karty opravňující parkovat v modré zóně.
 * 
 * @author Martin Štajner
 * 
 */
public class BlueCardActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Dialog, který se zobrazí během zjišťování informací ze serveru
	 */
	private ProgressDialog progDialog;

	/**
	 * Instance pomocného objektu pro kontrolu parkovací karty
	 */
	private BlueCardActivityHelper helper;

	/**
	 * Bundle sloužící k udržení dat během restartu aplikace
	 */
	private Bundle bundle;

	// ==================================== INITIALIZATON ==================================== //

	/**
	 * Metoda inicializuje objekt BlueCardActivity a je volána při jeho
	 * vytváření.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Nastavení View
		setContentView(R.layout.blue_zone);

		// Přiřazení instance aplikace - kvůli přístupu k datům z různych aktivit
		app = (MyApp) getApplication();

		// Nastavení helperu
		helper = new BlueCardActivityHelper(this);

		// Pokud jsou nejaké informace v bundlu (kvuli restartu aplikace), tak je nastaví
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("SPCInfo")) {
				setInfo(savedInstanceState.getSerializable("SPCInfo"));
			}
		}
	}

	// ==================================== BUTTONS ==================================== //

	/**
	 * Obsluha tlačítka kontroly parkovací karty
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void checkCardClick(View button) {

		// Zkontroluje přihlašení k internetu
		if (Phone.isConnectedToInternet(getApplicationContext())) {

			EditText cardNumber = (EditText) findViewById(R.id.cardNumber);
			helper.setSpcNumber(cardNumber.getText().toString());

			if (helper.getSpcNumber().length() == 10) {

				// Nastavení dialogu a login
				// TODO napsat do R
				progDialog = new ProgressDialog(this);
				progDialog.setTitle("Kontrola parkovací karty");
				progDialog.setMessage("Prosím počkejte...");
				progDialog.setIndeterminate(true);
				progDialog.setCancelable(false);
				progDialog.show();

				// Zablokuje otoceni obrazovky
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

				// Kontrola, zda uz nahodou nejsme pripojeni. Kdyz ano, tak se rovnou ocekovat SPZ, kdyz ne, tak se nejdrive zkusime pripojit a prihlasit.
				if (app.getConnectionProvider().isConnected()) {

					helper.checkSPC(this, helper.getSpcNumber());
				} else {
					helper.connectAndLogin();
				}

			} else {
				// TODO do R
				Toaster.toast("Číslo parkovací karty musí mít 10 znaků.", Toaster.SHORT);
			}
		} else {
			// TODO do R.
			Toaster.toast("K ověření parkovací karty musíte být připojeni k internetu.", Toaster.SHORT);
		}
	}

	// ==================================== GET / SET ==================================== //

	/**
	 * Metoda vrátí instanci aplikace
	 * 
	 * @return Instance aplikace
	 */
	public MyApp getApp() {
		return app;
	}

	// ==================================== OVERRIDE METHODS ==================================== //

	/**
	 * Metoda uloží data do bundlu při restartu aktivity
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	public void onSaveInstanceState(Bundle savedInstanceState) {
		if (bundle != null) {
			savedInstanceState.putAll(bundle);
		}
		super.onSaveInstanceState(savedInstanceState);
	}

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

	// ==================================== OTHER ==================================== //

	/**
	 * Metoda objektu SPCInfo přečte informace o parkovací kartě poslané ze
	 * serveru a zobrazí je na obrazovce.
	 * 
	 * @param infoFromServer
	 *            Informace ze serveru o parkovací kartě.
	 */
	public void setInfo(Serializable infoFromServer) {
		SPCInfo info = (SPCInfo) infoFromServer;
		String cardNumber = info.getSpcNumber();
		SPCStatus status = info.getSpcStatus();

		((LinearLayout) findViewById(R.id.blueCardInfo)).setVisibility(View.VISIBLE);

		if (cardNumber != null) {
			((TextView) findViewById(R.id.blueCardNumber)).setText(cardNumber);
		}
		if (status != null) {
			if (status == SPCStatus.OK_SPC) {
				// TODO napsat do R
				((TextView) findViewById(R.id.blueCardStatus)).setText("Karta je v pořádku.");
			}
			if (status == SPCStatus.STOLEN_SPC) {
				// TODO napsat do R
				((TextView) findViewById(R.id.blueCardStatus)).setText("Karta byla ukradena.");
			}
			if (status == SPCStatus.UKNOWN_SPC_STATUS) {
				// TODO napsat do R
				((TextView) findViewById(R.id.blueCardStatus)).setText("Neznámý");
			}
		}

		// Uloží data do bundlu
		if (bundle == null) {
			bundle = new Bundle();
		}
		bundle.putSerializable("SPCInfo", info);
	}

	/**
	 * Metoda zruší dialog.
	 */
	public void dismissDialog() {
		progDialog.dismiss();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

}