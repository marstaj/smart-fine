package cz.smartfine.android.helpers;

import cz.smartfine.android.SMSParkingActivity;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.listeners.ISMSParkingProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.SMSParkingProtocol;
import cz.smartfine.networklayer.model.mobile.SMSParkingInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * TODO doplnit
 * 
 * @author Martin Stajner
 * 
 */
public class SMSParkingActivityHelper extends Handler implements ISMSParkingProtocolListener {

	/**
	 * Handler obsluhující zprávy z jiného vlákna
	 */
	private Handler handler = this;

	private String vehicleRegistrationPlate;

	/**
	 * 
	 */
	private SMSParkingActivity activity;

	private SMSParkingProtocol smsp;

	/**
	 * Konstruktor
	 * 
	 * @param app
	 *            Instance aplikace
	 */
	public SMSParkingActivityHelper(SMSParkingActivity smsParkingActivity) {
		this.activity = smsParkingActivity;
	}

	@Override
	public void handleMessage(Message msg) {
		Bundle data = msg.getData();

		// V pripade toastu oznameni z vlakna
		if (data.containsKey("msg")) {
			Toaster.toast(data.getString("msg"), Toaster.SHORT);
		}

		// Odpoved z vlakna, ktere se snazilo pripojit k serveru
		if (data.containsKey("connect")) {
			if (data.getBoolean("connect")) {
				checkSPZ(activity, getVehicleRegistrationPlate());
			} else {
				// TODO napsat do R
				activity.getProggressDialog().dismiss();
				Toaster.toast("Nepodařilo se připojit k serveru.", Toaster.SHORT);
			}
		}

		if (data.containsKey("data")) {
			data.getSerializable("data");
			activity.setInfo(data.getSerializable("data"));
		}

	}

	// ==================================== METODY POSLUCHAČE ==================================== //

	public void onConnectionTerminated() {
		activity.getProggressDialog().dismiss();
		// TODO napsat to do R 
		Messenger.sendStringMessage(handler, "ConnectionTerminated.");
	}

	public void onMessageSent() {
		
	}

	public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo) {
		activity.getProggressDialog().dismiss();
		Messenger.sendSerializableObjectMessage(handler, parkingInfo);
	}

	// ==================================== GET / SET ==================================== //

	public String getVehicleRegistrationPlate() {
		return vehicleRegistrationPlate;
	}

	public void setVehicleRegistrationPlate(String vehicleRegistrationPlate) {
		this.vehicleRegistrationPlate = vehicleRegistrationPlate;
	}

	public SMSParkingProtocol getProtocol() {
		return smsp;
	}

	// ==================================== OSTATNÍ ==================================== //

	/**
	 * @param ac
	 */
	public void checkSPZ(SMSParkingActivity ac, String vehicleRegistrationPlate) {
		smsp = new SMSParkingProtocol(activity.getApp().getConnectionProvider().getNetworkInterface(), this);
		smsp.checkParking(vehicleRegistrationPlate);
	}

	/**
	 * 
	 */
	public void connectAndLogin() {
		// Pripojeni na server ve zvlastnim vlakne
		Runnable rable = new Runnable() {
			public void run() {
				boolean conected = activity.getApp().getConnectionProvider().connectAndLogin();
				Message msg = Message.obtain();
				Bundle data = new Bundle();
				data.putBoolean("connect", conected);
				msg.setData(data);
				handler.sendMessage(msg);
			}
		};
		final Thread thread = new Thread(rable);
		thread.start();

		// Přidat nejakej timeout casovac?? TODO
		//		handler.postDelayed(new Runnable() {
		//			public void run() {
		//				thread.stop(); // tohle je deprecated :/				
		//			}			
		//		}, 20000);
	}

}
