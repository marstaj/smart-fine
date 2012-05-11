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
 * Třída reprezentující pomocný objekt, který slouží ověřování stavu parkovací
 * karty opravňující parkovat v modré zóně.
 * 
 * @author Martin Štajner
 * 
 */
public class BlueCardActivityHelper extends Handler implements ISPCCheckProtocolListener {

	/**
	 * Objekt handleru obsluhující zprávy z jiného vlákna
	 */
	private Handler handler = this;

	/**
	 * Číslo parkovací karty
	 */
	private String spcNumber;

	/**
	 * Reference na aktivitu, která s pomocnou třídou komunikuje
	 */
	private BlueCardActivity activity;

	/**
	 * Komunikační protokol
	 */
	private SPCCheckProtocol cardp;

	/**
	 * Konstruktor třídy
	 * 
	 * @param blueCardActivity
	 *            Instance aktivity
	 */
	public BlueCardActivityHelper(BlueCardActivity blueCardActivity) {
		this.activity = blueCardActivity;
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
				checkSPC(activity, getSpcNumber());
			} else {
				// TODO napsat do R
				activity.dismissDialog();
				Toaster.toast("Nepodařilo se připojit k serveru.", Toaster.SHORT);
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
	 * ISPCCheckProtocolListener#onConnectionTerminated()
	 */
	public void onConnectionTerminated() {
		activity.dismissDialog();
		// TODO napsat to do R 
		Messenger.sendStringMessage(handler, "ConnectionTerminated.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.networklayer.business.listeners.
	 * ISPCCheckProtocolListener#onMessageSent()
	 */
	public void onMessageSent() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.networklayer.business.listeners.
	 * ISPCCheckProtocolListener
	 * #onReceivedSPCInfo(cz.smartfine.networklayer.model.mobile.SPCInfo)
	 */
	public void onReceivedSPCInfo(SPCInfo spcInfo) {
		activity.dismissDialog();
		Messenger.sendSerializableObjectMessage(handler, spcInfo);

	}

	// ==================================== GET / SET ==================================== //

	/**
	 * Metoda vrátí číslo parkovací karty
	 * 
	 * @return číslo parkovací karty
	 */
	public String getSpcNumber() {
		return spcNumber;
	}

	/**
	 * MEtoda nastaví číslo parkovací karty
	 * 
	 * @param blueCardNumber
	 *            číslo parkovací karty
	 */
	public void setSpcNumber(String blueCardNumber) {
		this.spcNumber = blueCardNumber;
	}

	/**
	 * Metoda vrátí komunikační protokol
	 * 
	 * @return Komunikační protokol
	 */
	public SPCCheckProtocol getProtocol() {
		return cardp;
	}

	// ==================================== OSTATNÍ ==================================== //

	/**
	 * Metoda vytvoří komunikační protokol a zažádá o kontrolu parkovací karty
	 * 
	 * @param ac
	 *            Aktivita, která žádá o kontrolu karty
	 * @param spcNumber
	 *            Číslo parkovací karty
	 */
	public void checkSPC(BlueCardActivity ac, String spcNumber) {
		cardp = new SPCCheckProtocol(ac.getApp().getConnectionProvider().getNetworkInterface(), this);
		cardp.checkSPC(spcNumber);
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
