package cz.smartfine.android;

import java.io.Serializable;
import java.util.Date;
import cz.smartfine.android.R;
import cz.smartfine.android.helpers.SMSParkingActivityHelper;
import cz.smartfine.android.model.util.Phone;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.networklayer.model.mobile.ParkingStatus;
import cz.smartfine.networklayer.model.mobile.SMSParkingInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Třída představující objekt typu Activity, který slouží ke kontroly parkovaní
 * zaplaceného pomocí SMS
 * 
 * @author Martin Štajner
 * 
 */
public class SMSParkingActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Dialog, který se zobrazí během zjišťování informací ze serveru
	 */
	private ProgressDialog progDialog;

	/**
	 * Instance pomocného objektu pro kontrolu parkování
	 */
	private SMSParkingActivityHelper helper;

	/**
	 * Bundle sloužící k udržení dat během restartu aplikace
	 */
	private Bundle bundle;

	// ==================================== INITIALIZATON ==================================== //

	/**
	 * Metoda inicializuje objekt SMSParkingActivity a je volána při jeho
	 * vytváření.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Nastavení View
		setContentView(R.layout.sms_parking);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		// Nastavi helper
		helper = new SMSParkingActivityHelper(this);

		// Pokud jsou nejaké informace v bundlu (kvuli restartu aplikace), tak je nastaví
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("SMSParkingInfo")) {
				setInfo(savedInstanceState.getSerializable("SMSParkingInfo"));
			}
		}
	}

	// ==================================== BUTTONS ==================================== //

	/**
	 * Obsluha tlačítka kontroly spz vozidla
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void checkSMSParkingClick(View button) {

		// Zkontroluje prihlaseni k internetu
		if (Phone.isConnectedToInternet(getApplicationContext())) {

			EditText vehicleSPZ = (EditText) findViewById(R.id.SMSParkingSPZ);
			helper.setVehicleRegistrationPlate(vehicleSPZ.getText().toString());

			if (helper.getVehicleRegistrationPlate().length() > 4 && helper.getVehicleRegistrationPlate().length() < 11) {

				// Nastavení dialogu a LogIN
				progDialog = new ProgressDialog(this);
				progDialog.setTitle(getText(R.string.view_dialog_sms));
				progDialog.setMessage(getText(R.string.please_wait));
				progDialog.setIndeterminate(true);
				progDialog.setCancelable(false);
				progDialog.show();

				// Zablokuje otoceni obrazovky
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

				// Kontrola, zda uz nahodou nejsme pripojeni. Kdyz ano, tak se rovnou ocekovat SPZ, kdyz ne, tak se nejdrive zkusime pripojit a prihlasit.
				if (app.getConnectionProvider().isConnected()) {

					helper.checkSPZ(this, helper.getVehicleRegistrationPlate());
				} else {
					helper.connectAndLogin();
				}

			} else {
				Toaster.toast(R.string.val_ticket_err_spz, Toaster.SHORT);
			}
		} else {
			Toaster.toast(R.string.internet_need_sms, Toaster.SHORT);
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
	 * Z objektu SMSParkingInfo přečte informace o povolení parkování poslaná ze
	 * serveru a zobrazí je na obrazovce.
	 * 
	 * @param infoFromServer
	 *            Informace ze serveru o povolení parkování.
	 */
	public void setInfo(Serializable infoFromServer) {
		SMSParkingInfo info = (SMSParkingInfo) infoFromServer;
		String spz = info.getVehicleRegistrationPlate();
		Date since = info.getParkingSince();
		Date until = info.getParkingSince();
		ParkingStatus status = info.getParkingStatus();

		((LinearLayout) findViewById(R.id.smsParkingInfo)).setVisibility(View.VISIBLE);

		if (spz != null) {
			((TextView) findViewById(R.id.smsParkingSPZ)).setText(spz);
		}
		if (since != null) {
			((TextView) findViewById(R.id.smsParkingSince)).setText(since.toLocaleString());
		}
		if (until != null) {
			((TextView) findViewById(R.id.smsParkingUntil)).setText(until.toLocaleString());
		}
		if (status != null) {
			if (status == ParkingStatus.ALLOWED) {
				((TextView) findViewById(R.id.smsParkingPermision)).setText(getText(R.string.view_sms_allowed));
			}
			if (status == ParkingStatus.NOT_ALLOWED) {
				((TextView) findViewById(R.id.smsParkingPermision)).setText(getText(R.string.view_sms_notallowed));
			}
			if (status == ParkingStatus.UNKNOWN_PARKING_STATUS) {
				((TextView) findViewById(R.id.smsParkingPermision)).setText(getText(R.string.view_sms_unknown));
			}
		}

		// Uloží data do bundlu
		if (bundle == null) {
			bundle = new Bundle();
		}
		bundle.putSerializable("SMSParkingInfo", info);
	}

	/**
	 * Metoda zruší dialog.
	 */
	public void dismissDialog() {
		progDialog.dismiss();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

}