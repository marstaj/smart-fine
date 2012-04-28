package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.model.util.Internet;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.listeners.ISMSParkingProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.SMSParkingProtocol;
import cz.smartfine.android.networklayer.model.SMSParkingInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Tøída aktivity kontroly zaplaceneho parkovaní pomoci SMS
 * 
 * @author Martin Štajner
 * 
 */
public class SMSParkingActivity extends Activity implements ISMSParkingProtocolListener {

	/**
	 * Instance aplikace
	 */
	private MyApp app;
	
	SMSParkingProtocol smsp;
	
	boolean connectionTerminated;
	boolean messageSent;
	String vehicleRegistrationPlate;
	
	ProgressDialog progDialog;

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
	}

	/**
	 * Obsluha tlaèítka - Zkontrolovat kartu
	 * 
	 * @param button
	 */
	public void checkSMSParkingClick(View button) {
		
		connectionTerminated = false;
		messageSent = false;
		
		// Zkontroluje prihlaseni k internetu
		if (Internet.isOnline(this)) {

		EditText vehicleSPZ = (EditText) findViewById(R.id.SMSParkingSPZ);
		vehicleRegistrationPlate = vehicleSPZ.getText().toString();

		if (vehicleRegistrationPlate.length() == 0) {
			Toaster.toast("Musíte zadat SPZ.", Toaster.SHORT);
		} else {

			// Nastavení dialogu a LogIN
			// TODO napsat do R
			progDialog = new ProgressDialog(this);
			progDialog.setTitle("Kontrola parkování");
			progDialog.setMessage("Ovìøování údajù, prosím poèkejte.");
			progDialog.setIndeterminate(true);
			progDialog.show();
			
//			smsp = new SMSParkingProtocol(app.getConnectionProvider().getNetworkInterface(), this);
//			smsp.checkParking(vehicleRegistrationPlate);
		}
		
		} else {
		// TODO do R.
		Toaster.toast("K ovìøení parkování musíte být pøipojeni k internetu.", Toaster.SHORT);
	}

	}
	// ==================================== METODY POSLUCHAÈE ISMSParkingProtocolListener ==================================== //

	public void onConnectionTerminated() {		
		smsp.checkParking(vehicleRegistrationPlate);
	}

	public void onMessageSent() {
	}

	public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo) {
		progDialog.dismiss();
	}

	@Override
	public void finish() {
		
		// Odpoji protokol
		if (smsp != null) {
			smsp.disconnectProtocol();
		}
		super.finish();
	}

}