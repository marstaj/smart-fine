package cz.smartfine.android;

import java.io.Serializable;
import java.util.Date;

import cz.smartfine.android.R;
import cz.smartfine.android.helpers.SMSParkingActivityHelper;
import cz.smartfine.android.model.util.Internet;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.networklayer.model.mobile.ParkingStatus;
import cz.smartfine.networklayer.model.mobile.SMSParkingInfo;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Třída aktivity kontroly zaplaceného parkovaní pomocí SMS
 * 
 * @author Martin �tajner
 * 
 */
public class SMSParkingActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	ProgressDialog progDialog;

	SMSParkingActivityHelper helper;
	
	Bundle bundle;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_parking);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		// Nastavi helper
		helper = new SMSParkingActivityHelper(this);
		
		// Pokud jsou nejake informace v bundlu (kvuli pretoceni), tak je nastavi
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("SMSParkingInfo")) {
				setInfo(savedInstanceState.getSerializable("SMSParkingInfo"));		
			}					
		}
	}


	/**
	 * Obsluha tlačítka - Zkontrolovat kartu
	 * 
	 * @param button
	 */
	public void checkSMSParkingClick(View button) {

		// Zkontroluje prihlaseni k internetu
		if (Internet.isOnline(this)) {

			EditText vehicleSPZ = (EditText) findViewById(R.id.SMSParkingSPZ);
			helper.setVehicleRegistrationPlate(vehicleSPZ.getText().toString());

			if (helper.getVehicleRegistrationPlate().length() != 0) {

				// Nastavení dialogu a LogIN
				// TODO napsat do R
				progDialog = new ProgressDialog(this);
				progDialog.setTitle("Kontrola parkování");
				progDialog.setMessage("Prosím počkejte...");
				progDialog.setIndeterminate(true);
				progDialog.setCancelable(false);
				progDialog.show();

				// Kontrola, zda uz nahodou nejsme pripojeni. Kdyz ano, tak se rovnou ocekovat SPZ, kdyz ne, tak se nejdrive zkusime pripojit a prihlasit.
				if (app.getConnectionProvider().isConnected()) {

					helper.checkSPZ(this, helper.getVehicleRegistrationPlate());
				} else {
					helper.connectAndLogin();
				}

			} else {
				Toaster.toast("Musíte zadat SPZ.", Toaster.SHORT);
			}
		} else {
			// TODO do R.
			Toaster.toast("K ověření parkování musíte být připojeni k internetu.", Toaster.SHORT);
		}
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
		if (bundle != null) {
			savedInstanceState.putAll(bundle);
		}
		super.onSaveInstanceState(savedInstanceState);
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

	@Override
	public void finish() {

		// Odpoji protokol
		if (helper.getProtocol() != null) {
			helper.getProtocol().disconnectProtocol();
		}
		super.finish();
	}

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
				// TODO napsat do R
				((TextView) findViewById(R.id.smsParkingPermision)).setText("Povoleno");
			}
			if (status == ParkingStatus.NOT_ALLOWED) {
				// TODO napsat do R
				((TextView) findViewById(R.id.smsParkingPermision)).setText("Nepovoleno");
			}
			if (status == ParkingStatus.UNKNOWN_PARKING_STATUS) {
				// TODO napsat do R
				((TextView) findViewById(R.id.smsParkingPermision)).setText("Neznámé");
			}
		}
		
		if (bundle == null) {
			bundle = new Bundle();
		}
		bundle.putSerializable("SMSParkingInfo", info);
	}
	
}