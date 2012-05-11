package cz.smartfine.android.helpers;

import java.io.IOException;
import java.util.ArrayList;
import cz.smartfine.android.TicketListActivity;
import cz.smartfine.android.model.Waypoint;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.listeners.IGeoDataProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.GeoDataProtocol;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Třída reprezentující pomocný objekt, který slouží k nahrávání geolokačních
 * dat na server.
 * 
 * @author Martin Štajner
 * 
 */
public class UploadWaypointsActivityHelper extends Handler implements IGeoDataProtocolListener {

	/**
	 * Objekt handleru obsluhující zprávy z jiného vlákna
	 */
	private Handler handler = this;

	/**
	 * Seznam geolokačních dat
	 */
	private ArrayList<Waypoint> waypoints;

	/**
	 * Reference na aktivitu, která s pomocnou třídou komunikuje
	 */
	private TicketListActivity activity;

	/**
	 * Komunikační protokol
	 */
	private GeoDataProtocol gdp;

	/**
	 * Konstruktor třídy
	 * 
	 * @param ticketListActivity
	 */
	public UploadWaypointsActivityHelper(TicketListActivity ticketListActivity) {
		this.activity = ticketListActivity;
	}

	/**
	 * Metoda provádí akci na základě obdržené zprávy z jiného vlákn
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
				sendWaypoints(activity, this.getWaypoints());
			} else {
				// TODO napsat do R
				activity.dismissDialog();
				Toaster.toast("Nepodařilo se připojit k serveru.", Toaster.SHORT);
			}
		}

		// V pripade prikazu z vlakna
		if (data.containsKey("command")) {
			if (data.getString("command").equals("uploadTickets")) {
				activity.uploadTickets();
			}
		}
	}
	// ==================================== LISTENER METHODS ==================================== //

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.networklayer.business.listeners.IGeoDataProtocolListener
	 * #onConnectionTerminated()
	 */
	public void onConnectionTerminated() {
		activity.dismissDialog();
		// TODO napsat to do R 
		Messenger.sendStringMessage(handler, "ConnectionTerminated.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.networklayer.business.listeners.IGeoDataProtocolListener
	 * #onMessageSent()
	 */
	public void onMessageSent() {
		activity.dismissDialog();
		Messenger.sendCommand(handler, "uploadTickets");
	}

	// ==================================== GET / SET ==================================== //

	/**
	 * Metoda vrátí seznam geolokačních dat.
	 * 
	 * @return Seznam geolokačních dat
	 */
	public ArrayList<Waypoint> getWaypoints() {
		return waypoints;
	}

	/**
	 * Metoda nastaví seznam geolokačních dat.
	 * 
	 * @param waypoints
	 *            Seznam geolokačních dat
	 */
	public void setWaypoints(ArrayList<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

	/**
	 * Metoda vrátí komunikační protokol
	 * 
	 * @return Komunikační protoko
	 */
	public GeoDataProtocol getProtocol() {
		return gdp;
	}

	// ==================================== OTHER ==================================== //

	/**
	 * Metoda vytvoří komunikační protokol a odešle geolokační data na server.
	 * 
	 * @param ac
	 *            Aktivita, která žádá o kontrolu karty
	 * @param list
	 *            Služební čislo policisty
	 */
	public void sendWaypoints(TicketListActivity ac, ArrayList<Waypoint> list) {
		if (gdp == null) {
			gdp = new GeoDataProtocol(activity.getApp().getConnectionProvider().getNetworkInterface(), this);
		}
		try {
			gdp.sendGeoData(list);
		} catch (IOException e) {
			// TODO tady neco udelat
		}
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
