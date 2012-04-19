package cz.smartfine.networklayer.dataprotocols;
import model.Ticket;
import cz.smartfine.networklayer.business.listeners.ITicketProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Pøedstavuje tøídu protokolu pro pøenost PL na server.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:49
 */
public class TicketSyncProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro pøístup k odesílání a pøíjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchaè událostí z této tøídy.
	 */
	private ITicketProtocolListener ticketProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public TicketSyncProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro pøenost dat.
	 * @param ticketProtocolListener Posluchaè událostí z této tøídy.
	 */
	public TicketSyncProtocol(INetworkInterface networkInterface, ITicketProtocolListener ticketProtocolListener){
		this.networkInterface = networkInterface;
		this.ticketProtocolListener = ticketProtocolListener;
	}

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Handler události ukonèení spojení.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

	/**
	 * Odebere posluchaèe událostí protokolu pro odesílání PL.
	 * 
	 * @param ticketProtocolListener    Posluchaè událostí protokolu pro odesílání PL.
	 */
	public void removeTicketProtocolListener(ITicketProtocolListener ticketProtocolListener){

	}

	/**
	 * Odesílá pøedaný PL na server.
	 * 
	 * @param ticket    PL, který se má odeslat na server.
	 * @param badgeNumber    Služební èíslo policisty, který PL odesílá. Pozn.: nemusí
	 * se shodovat s èíslem policisty, který je pøihlášen.
	 */
	public void sendTicket(Ticket ticket, int badgeNumber){

	}

	/**
	 * Pøidá posluchaèe událostí protokolu pro odesílání PL.
	 * 
	 * @param ticketProtocolListener    Posluchaè událostí protokolu pro odesílání PL.
	 */
	public void setTicketProtocolListener(ITicketProtocolListener ticketProtocolListener){

	}

}