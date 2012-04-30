package cz.smartfine.android.helpers;

import java.io.IOException;

import cz.smartfine.android.TicketListActivity;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.listeners.ITicketProtocolListener;
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
public class UploadTicketsActivityHelper extends Handler implements ITicketProtocolListener {

	/**
	 * Handler obsluhující zprávy z jiného vlákna
	 */
	private Handler handler = this;

	/**
	 * 
	 */
	private int badgeNumber;

	/**
	 * 
	 */
	private TicketListActivity activity;

	/**
	 * 
	 */
	private TicketSyncProtocol tsp;

	/**
	 * Konstruktor
	 * 
	 * @param app
	 *            Instance aplikace
	 */
	public UploadTicketsActivityHelper(TicketListActivity ticketListActivity) {
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
				sendTicket(activity, getBadgeNumber());
			} else {
				// TODO napsat do R
				activity.getProggressDialog().dismiss();
				Toaster.toast("Nepodařilo se připojit k serveru.", Toaster.SHORT);
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
		// Odstrani se poslany listek
		activity.getList().remove(0);
		activity.updateProggressBar();
		
		// Pokud neni list s listkama uz prazdny, posle se dalsi listek
		if (!activity.getList().isEmpty()) {
			sendTicket(activity, getBadgeNumber());
			
		} else {
			// Kdyz je list s listkama uz prazdny, ukonci dialog, posle informacni zpravu a reloadne aktivitu
			activity.getProggressDialog().dismiss();
			// TODO to R.
			Messenger.sendStringMessage(handler, "Data byla úspěšné nahrána na server.");
			activity.reload();
		}
	}
	// ==================================== GET / SET ==================================== //

	public int getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(int badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public TicketSyncProtocol getProtocol() {
		return tsp;
	}

	// ==================================== OSTATNÍ ==================================== //

	/**
	 * @param ac
	 */
	public void sendTicket(TicketListActivity ac, int badgeNumber) {
		if (tsp == null) {
			tsp = new TicketSyncProtocol(activity.getApp().getConnectionProvider().getNetworkInterface(), this);
		}
		try {
			tsp.sendTicket(ac.getList().get(0), badgeNumber);
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
