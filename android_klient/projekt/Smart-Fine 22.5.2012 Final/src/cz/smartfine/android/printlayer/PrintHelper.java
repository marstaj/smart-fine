package cz.smartfine.android.printlayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.starmicronics.stario.StarIOPortException;

import cz.smartfine.android.R;
import cz.smartfine.android.TicketDetailActivity;
import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;

/**
 * Třída reprezentující pomocný objekt, který slouží k tisku parkovacích lístků
 * na mobilní tiskárně.
 * 
 * @author Martin Štajner
 * 
 */
public class PrintHelper extends Handler {

	/**
	 * Mobilní tiskárna
	 */
	IPrinter printer;

	/**
	 * Objekt handleru obsluhující zprávy z jiného vlákna
	 */
	Handler handler;

	/**
	 * Reference na aktivitu, která s pomocnou třídou komunikujes
	 */
	TicketDetailActivity activity;

	/**
	 * Konstruktor třídy
	 * 
	 * @param activity
	 *            Aktivita, ze které je objekt vytvářen.
	 * @param printer
	 *            Mobilní tiskárna
	 */
	public PrintHelper(TicketDetailActivity activity, IPrinter printer) {
		this.printer = printer;
		this.activity = activity;
		this.handler = this;
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
			activity.dismissDialog();
		}

		// V pripade toastu oznameni z vlakna
		if (data.containsKey("command")) {
			if (data.getString("command").equals("success")) {
				activity.onSuccessPrint();
			}
		}

	}

	/**
	 * Metoda se pokusí připojit k tiskárně a vytisknout parkovací lístek. V
	 * případě chyb, odešle zprávy do objektu handleru.
	 * 
	 * @param mac
	 *            MAC adresa mobilní tiskárny
	 * @param ticket
	 *            Parkovací lístek určený k tisku
	 */
	public void print(final String mac, final Ticket ticket) {

		Runnable rable = new Runnable() {
			public void run() {
				Looper.prepare();

				try {
					printer.openPort(mac);
				} catch (StarIOPortException e) {
					Messenger.sendStringMessage(handler, activity.getText(R.string.printer_connection_unsuccessful).toString());
					return;
				}

				try {
					printer.printTicket(ticket);
				} catch (StarIOPortException e) {
					Messenger.sendStringMessage(handler, activity.getText(R.string.printer_connection_unsuccessful).toString());
					return;
				}

				try {
					printer.closePort();
				} catch (StarIOPortException e) {
				}

				Messenger.sendCommand(handler, "success");
			}
		};
		final Thread thread = new Thread(rable);
		thread.start();

	}
}
