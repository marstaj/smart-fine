package cz.smartfine.android.networklayer.dataprotocols;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.networklayer.business.listeners.ITicketProtocolListener;
import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.NetworkTicket;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.MessageBuilder;

/**
 * Představuje třídu protokolu pro přenost PL na server.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:49
 */
public class TicketSyncProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro přístup k odesílání a příjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchač událostí z této třídy.
	 */
	private ITicketProtocolListener ticketProtocolListener;

	// ================================================== KONSTRUKTORY &
	// DESTRUKTORY ==================================================//

	public void finalize() throws Throwable {

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface
	 *            Rozhraní pro přenost dat.
	 */
	public TicketSyncProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface
	 *            Rozhraní pro přenost dat.
	 * @param ticketProtocolListener
	 *            Posluchač událostí z této třídy.
	 */
	public TicketSyncProtocol(INetworkInterface networkInterface,
			ITicketProtocolListener ticketProtocolListener) {
		this.networkInterface = networkInterface;
		this.ticketProtocolListener = ticketProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); // zaregistrování
																// se jako
																// posluchač
	}

	// ================================================== GET/SET
	// ==================================================//

	/**
	 * Odebere posluchače událostí protokolu pro odesílání PL.
	 * 
	 * @param ticketProtocolListener
	 *            Posluchač událostí protokolu pro odesílání PL.
	 */
	public void removeTicketProtocolListener(
			ITicketProtocolListener ticketProtocolListener) {
		this.ticketProtocolListener = null;
	}

	/**
	 * Přidá posluchače událostí protokolu pro odesílání PL.
	 * 
	 * @param ticketProtocolListener
	 *            Posluchač událostí protokolu pro odesílání PL.
	 */
	public void setTicketProtocolListener(
			ITicketProtocolListener ticketProtocolListener) {
		this.ticketProtocolListener = ticketProtocolListener;
	}

	// ================================================== HANDLERY UDÁLOSTÍ
	// ==================================================//

	/**
	 * Handler události ukončení spojení.
	 */
	public void onConnectionTerminated() {
		if (ticketProtocolListener != null) {
			ticketProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 * 
	 * @param sentData
	 *            Odeslaná data.
	 */
	public void onMessageSent(byte[] sentData) {
		if (ticketProtocolListener != null) {
			ticketProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler události příjmu dat.
	 * 
	 * @param receivedData
	 *            Přijmutá data uložená ve formě bytového pole.
	 */
	public void onReceivedData(byte[] receivedData) {
		// žádná data nepřijímá
	}

	// ================================================== VÝKONNÉ METODY
	// ==================================================//

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol() {
		if (networkInterface != null) {
			networkInterface.removeOnReceivedDataListener(this);
		}
	}

	/**
	 * Odesílá předaný PL na server.
	 * 
	 * @param ticket
	 *            PL, který se má odeslat na server.
	 * @param badgeNumber
	 *            Služební číslo policisty, který PL odesílá. Pozn.: nemusí se
	 *            shodovat s číslem policisty, který je přihlášen.
	 * @throws IOException
	 *             Problém při serializaci PL.
	 */
	public void sendTicket(Ticket ticket, int badgeNumber) throws IOException {
		if (networkInterface != null) {
			networkInterface.sendData(createTicketMessage(ticket, badgeNumber),
					this);
		}
	}

	// ================================================== PRIVÁTNÍ METODY
	// ==================================================//

	/**
	 * Vytváří zprávu pro nahrání PL na server.
	 * 
	 * @param ticket
	 *            Parkovací lístek.
	 * @param badgeNumber
	 *            Služební číslo policisty, který nahrává PL na server.
	 * @return Zpráva pro odeslání na server.
	 * @throws IOException
	 *             Problém při serializaci PL.
	 */
	protected byte[] createTicketMessage(Ticket ticket, int badgeNumber) throws IOException {
		MessageBuilder msg = new MessageBuilder();

		msg.putByte(MobileMessageIDs.ID_MSG_UPLOAD_TICKET); // identifikátor
															// zprávy
		msg.putInt(badgeNumber); // služební číslo policisty

		ByteArrayOutputStream ticketBytes = new ByteArrayOutputStream();
		ObjectOutputStream objOS = new ObjectOutputStream(ticketBytes);

		objOS.writeObject(new NetworkTicket(ticket)); // serializuje PL
		objOS.close();

		msg.putArrayWithIntLength(ticketBytes.toByteArray()); // vložení pole
																// objektu PL

		ticketBytes.close();

		return msg.getByteArray();
	}

}
