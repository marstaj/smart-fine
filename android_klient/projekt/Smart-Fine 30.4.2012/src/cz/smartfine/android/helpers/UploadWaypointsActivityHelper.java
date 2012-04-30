package cz.smartfine.android.helpers;

import java.io.IOException;
import java.util.ArrayList;

import cz.smartfine.android.TicketListActivity;
import cz.smartfine.android.model.Waypoint;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.listeners.IGeoDataProtocolListener;

import cz.smartfine.android.networklayer.dataprotocols.GeoDataProtocol;
import cz.smartfine.android.networklayer.dataprotocols.TicketSyncProtocol;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * TODO doplnit
 * 
 * @author Martin Stajner
 * 
 */
public class UploadWaypointsActivityHelper extends Handler implements IGeoDataProtocolListener {

	/**
	 * Handler obsluhující zprávy z jiného vlákna
	 */
	private Handler handler = this;

	/**
	 * 
	 */
	private ArrayList<Waypoint> waypoints;

	/**
	 * 
	 */
	private TicketListActivity activity;

	/**
	 * 
	 */
	private GeoDataProtocol gdp;

	/**
	 * Konstruktor
	 * 
	 * @param app
	 *            Instance aplikace
	 */
	public UploadWaypointsActivityHelper(TicketListActivity ticketListActivity) {
		this.activity = ticketListActivity;
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
				sendWaypoints(activity, this.getWaypoints());
			} else {
				// TODO napsat do R
				activity.getProggressDialog().dismiss();
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
	// ==================================== METODY POSLUCHAČE ==================================== //

	public void onConnectionTerminated() {
		activity.getProggressDialog().dismiss();
		// TODO napsat to do R 
		Messenger.sendStringMessage(handler, "ConnectionTerminated.");

	}

	public void onMessageSent() {
		activity.getProggressDialog().dismiss();
		Messenger.sendCommand(handler, "uploadTickets");
	}

	// ==================================== GET / SET ==================================== //

	public ArrayList<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(ArrayList<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

	public GeoDataProtocol getProtocol() {
		return gdp;
	}

	// ==================================== OSTATNÍ ==================================== //

	/**
	 * @param ac
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
