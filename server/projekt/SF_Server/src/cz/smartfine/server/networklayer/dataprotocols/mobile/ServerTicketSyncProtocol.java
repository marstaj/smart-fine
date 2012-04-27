package cz.smartfine.server.networklayer.mobile.dataprotocols;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerTicketProtocolListener;
import smartfine.networklayer.business.listeners.ITicketProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Představuje třídu protokolu pro přenost PL na server.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:28
 */
public class ServerTicketSyncProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro přístup k odesílání a příjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchač událostí z této třídy.
	 */
	private IServerTicketProtocolListener ticketProtocolListener;

	public ServerTicketSyncProtocol(){

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenost dat.
	 */
	public ServerTicketSyncProtocol(INetworkInterface networkInterface){

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenost dat.
	 * @param ticketProtocolListener    Posluchač událostí z této třídy.
	 */
	public ServerTicketSyncProtocol(INetworkInterface networkInterface, IServerTicketProtocolListener ticketProtocolListener){

	}

	/**
	 * ================================================== KONSTRUKTORY & DESTRUKTORY
	 * ==================================================
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

	}

	/**
	 * Vytváří zprávu pro nahrání PL na server.
	 * @return Zpráva pro odeslání na server.
	 * 
	 * @param ticket    Parkovací lístek.
	 * @param badgeNumber    Služební číslo policisty, který nahrává PL na server.
	 * @exception IOException Problém při serializaci PL.
	 */
	protected byte[] createTicketMessage(Ticket ticket, int badgeNumber)
	  throws IOException{
		return 0;
	}

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Handler události ukončení spojení.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler události příjmu dat.
	 * 
	 * @param receivedData    Přijmutá data uložená ve formě bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

	/**
	 * Odebere posluchače událostí protokolu pro odesílání PL.
	 * 
	 * @param ticketProtocolListener    Posluchač událostí protokolu pro odesílání PL.
	 */
	public void removeTicketProtocolListener(IServerTicketProtocolListener ticketProtocolListener){

	}

	/**
	 * Odesílá předaný PL na server.
	 * 
	 * @param ticket    PL, který se má odeslat na server.
	 * @param badgeNumber    Služební číslo policisty, který PL odesílá. Pozn.: nemusí
	 * se shodovat s číslem policisty, který je přihlášen.
	 * @exception IOException Problém při serializaci PL.
	 */
	public void sendTicket(Ticket ticket, int badgeNumber)
	  throws IOException{

	}

	/**
	 * Přidá posluchače událostí protokolu pro odesílání PL.
	 * 
	 * @param ticketProtocolListener    Posluchač událostí protokolu pro odesílání PL.
	 */
	public void setTicketProtocolListener(IServerTicketProtocolListener ticketProtocolListener){

	}

}