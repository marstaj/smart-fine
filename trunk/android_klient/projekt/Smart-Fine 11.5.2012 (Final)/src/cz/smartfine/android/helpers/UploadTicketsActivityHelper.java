package cz.smartfine.android.helpers;

import java.io.IOException;
import cz.smartfine.android.TicketListActivity;
import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.business.listeners.ITicketProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.TicketSyncProtocol;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Třída reprezentující pomocný objekt, který slouží k nahrávání parkovacích
 * lístků na server
 * 
 * @author Martin Štajner
 * 
 */
public class UploadTicketsActivityHelper extends Handler implements ITicketProtocolListener {

	/**
	 * Objekt handleru obsluhující zprávy z jiného vlákna
	 */
	private Handler handler = this;

	/**
	 * Služební číslo policisty
	 */
	private int badgeNumber;

	/**
	 * Reference na aktivitu, která s pomocnou třídou komunikuje
	 */
	private TicketListActivity activity;

	/**
	 * Komunikační protokol
	 */
	private TicketSyncProtocol tsp;

	/**
	 * Konstruktor třídy
	 * 
	 * @param ticketListActivity
	 *            Instance aktivity
	 */
	public UploadTicketsActivityHelper(TicketListActivity ticketListActivity) {
		this.activity = ticketListActivity;
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
				sendTicket(activity, getBadgeNumber());
			} else {
				// TODO napsat do R
				activity.dismissDialog();
				Toaster.toast("Nepodařilo se připojit k serveru.", Toaster.SHORT);
			}
		}
	}
	// ==================================== LISTENER METHODS ==================================== //

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.networklayer.business.listeners.ITicketProtocolListener
	 * #onConnectionTerminated()
	 */
	public void onConnectionTerminated() {
		activity.dismissDialog();
		// TODO napsat to do R 
		Messenger.sendStringMessage(handler, "ConnectionTerminated.");
		activity.finishUpload();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.networklayer.business.listeners.ITicketProtocolListener
	 * #onMessageSent()
	 */
	public void onMessageSent() {
		// Odstrani se poslany listek
		Ticket ticketToRemove = activity.getList().remove(0);
		activity.getApp().getPhotoDAO().deleteAllPhotosFromTicket(ticketToRemove);
		activity.updateProggressBar();

		// Pokud neni list s listkama uz prazdny, posle se dalsi listek
		if (!activity.getList().isEmpty()) {
			sendTicket(activity, getBadgeNumber());

		} else {
			// Kdyz je list s listkama uz prazdny, ukonci dialog, posle informacni zpravu a reloadne aktivitu
			activity.dismissDialog();
			// TODO to R.
			Messenger.sendStringMessage(handler, "Data byla úspěšné nahrána na server.");
			activity.finishUpload();
		}
	}
	// ==================================== GET / SET ==================================== //

	/**
	 * Metoda nastaví služební číslo policisty
	 * 
	 * @return Služební číslo policisty
	 */
	public int getBadgeNumber() {
		return badgeNumber;
	}

	/**
	 * Metoda Vrátí služební číslo policisty
	 * 
	 * @param badgeNumber
	 *            Služební číslo policisty
	 */
	public void setBadgeNumber(int badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	/**
	 * Metoda vrátí komunikační protokol
	 * 
	 * @return Komunikační protokol
	 */
	public TicketSyncProtocol getProtocol() {
		return tsp;
	}

	// ==================================== OTHER ==================================== //

	/**
	 * Metoda vytvoří komunikační protokol a odešle parkovací lístek.
	 * 
	 * @param ac
	 *            Aktivita, která žádá o kontrolu karty
	 * @param badgeNumber
	 *            Služební čislo policisty
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
