package cz.smartfine.android.helpers;

import cz.smartfine.android.BlueCardActivity;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.listeners.ISPCCheckProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.SPCCheckProtocol;
import cz.smartfine.networklayer.model.mobile.SPCInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * TODO doplnit
 * 
 * @author Martin Stajner
 * 
 */
public class BlueCardActivityHelper extends Handler implements ISPCCheckProtocolListener {

	/**
	 * Handler obsluhující zprávy z jiného vlákna
	 */
	private Handler handler = this;

	private String spcNumber;

	/**
	 * 
	 */
	private BlueCardActivity activity;

	private SPCCheckProtocol cardp;

	/**
	 * Konstruktor
	 * 
	 * @param app
	 *            Instance aplikace
	 */
	public BlueCardActivityHelper(BlueCardActivity blueCardActivity) {
		this.activity = blueCardActivity;
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
				checkSPC(activity, getSpcNumber());
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

	public void onReceivedSPCInfo(SPCInfo spcInfo) {
		activity.getProggressDialog().dismiss();
		Messenger.sendSerializableObjectMessage(handler, spcInfo);
		
	}

	// ==================================== GET / SET ==================================== //

	public String getSpcNumber() {
		return spcNumber;
	}

	public void setSpcNumber(String vehicleRegistrationPlate) {
		this.spcNumber = vehicleRegistrationPlate;
	}

	public SPCCheckProtocol getProtocol() {
		return cardp;
	}

	// ==================================== OSTATNÍ ==================================== //

	/**
	 * @param ac
	 */
	public void checkSPC(BlueCardActivity ac, String spcNumber) {
		cardp = new SPCCheckProtocol(ac.getApp().getConnectionProvider().getNetworkInterface(), this);
		cardp.checkSPC(spcNumber);
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
