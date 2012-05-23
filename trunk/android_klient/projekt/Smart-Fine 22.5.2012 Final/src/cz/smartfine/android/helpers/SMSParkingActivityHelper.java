package cz.smartfine.android.helpers;

import cz.smartfine.android.R;
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
 * Třída reprezentující pomocný objekt, který slouží ověřování parkování
 * zaplaceného pomocí SMS.
 * 
 * @author Martin Štajner
 * 
 */
public class SMSParkingActivityHelper extends Handler implements ISMSParkingProtocolListener {

	/**
	 * Objekt handleru obsluhující zprávy z jiného vlákna
	 */
	private Handler handler = this;

	/**
	 * SPZ vozidla
	 */
	private String vehicleRegistrationPlate;

	/**
	 * Reference na aktivitu, která s pomocnou třídou komunikuje
	 */
	private SMSParkingActivity activity;

	/**
	 * Komunikační protokol
	 */
	private SMSParkingProtocol smsp;

	/**
	 * Konstruktor třídy
	 * 
	 * @param smsParkingActivity
	 *            Instance aktivity
	 */
	public SMSParkingActivityHelper(SMSParkingActivity smsParkingActivity) {
		this.activity = smsParkingActivity;
	}

	/**
	 * Metoda provádí akci na základě obdržené zprávy z jiného vlákna
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
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
				activity.dismissDialog();
				Toaster.toast(R.string.connection_unsuccessful, Toaster.SHORT);
			}
		}

		if (data.containsKey("data")) {
			data.getSerializable("data");
			activity.setInfo(data.getSerializable("data"));
		}

	}

	// ==================================== LISTENER METHODS ==================================== //

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.networklayer.business.listeners.
	 * ISMSParkingProtocolListener#onConnectionTerminated()
	 */
	public void onConnectionTerminated() {
		activity.dismissDialog();
		Messenger.sendStringMessage(handler, activity.getText(R.string.connection_terminated).toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.networklayer.business.listeners.
	 * ISMSParkingProtocolListener#onMessageSent()
	 */
	public void onMessageSent() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.networklayer.business.listeners.
	 * ISMSParkingProtocolListener
	 * #onReceivedSMSParkingInfo(cz.smartfine.networklayer
	 * .model.mobile.SMSParkingInfo)
	 */
	public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo) {
		activity.dismissDialog();
		Messenger.sendSerializableObjectMessage(handler, parkingInfo);
	}

	// ==================================== GET / SET ==================================== //

	/**
	 * Metoda vrátí SPZ vozidla
	 * 
	 * @return SPZ vozidla
	 */
	public String getVehicleRegistrationPlate() {
		return vehicleRegistrationPlate;
	}

	/**
	 * MEtoda nastaví SPZ vozidla
	 * 
	 * @param vehicleRegistrationPlate
	 *            SPZ vozidla
	 */
	public void setVehicleRegistrationPlate(String vehicleRegistrationPlate) {
		this.vehicleRegistrationPlate = vehicleRegistrationPlate;
	}

	/**
	 * Metoda vrátí komunikační protokol
	 * 
	 * @return Komunikační protokol
	 */
	public SMSParkingProtocol getProtocol() {
		return smsp;
	}

	// ==================================== OSTATNÍ ==================================== //

	/**
	 * Metoda vytvoří komunikační protokol a zažádá o kontrolu parkování
	 * 
	 * @param ac
	 *            Aktivita, která žádá o kontrolu karty
	 * @param vehicleRegistrationPlate
	 *            SPZ vozidla
	 */
	public void checkSPZ(SMSParkingActivity ac, String vehicleRegistrationPlate) {
		smsp = new SMSParkingProtocol(activity.getApp().getConnectionProvider().getNetworkInterface(), this);
		smsp.checkParking(vehicleRegistrationPlate);
	}

	/**
	 * Metoda se v novém vlákně pokusí připojit k serveru a přihlásit.
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

		// Přidat nejakej timeout casovac?? TODO casovac
		//		handler.postDelayed(new Runnable() {
		//			public void run() {
		//				thread.stop(); // tohle je deprecated :/				
		//			}			
		//		}, 20000);
	}

}
