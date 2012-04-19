package cz.smartfine.networklayer.dataprotocols;
import model.Ticket;
import cz.smartfine.networklayer.business.listeners.ITicketProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * P�edstavuje t��du protokolu pro p�enost PL na server.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:49
 */
public class TicketSyncProtocol implements IDataProtocol {

	/**
	 * Rozhran� pro p��stup k odes�l�n� a p��j�m�n� dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� z t�to t��dy.
	 */
	private ITicketProtocolListener ticketProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public TicketSyncProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhran� pro p�enost dat.
	 * @param ticketProtocolListener Poslucha� ud�lost� z t�to t��dy.
	 */
	public TicketSyncProtocol(INetworkInterface networkInterface, ITicketProtocolListener ticketProtocolListener){
		this.networkInterface = networkInterface;
		this.ticketProtocolListener = ticketProtocolListener;
	}

	/**
	 * Odpoj� datov� protokol od z�kladn�ho protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

	/**
	 * Odebere poslucha�e ud�lost� protokolu pro odes�l�n� PL.
	 * 
	 * @param ticketProtocolListener    Poslucha� ud�lost� protokolu pro odes�l�n� PL.
	 */
	public void removeTicketProtocolListener(ITicketProtocolListener ticketProtocolListener){

	}

	/**
	 * Odes�l� p�edan� PL na server.
	 * 
	 * @param ticket    PL, kter� se m� odeslat na server.
	 * @param badgeNumber    Slu�ebn� ��slo policisty, kter� PL odes�l�. Pozn.: nemus�
	 * se shodovat s ��slem policisty, kter� je p�ihl�en.
	 */
	public void sendTicket(Ticket ticket, int badgeNumber){

	}

	/**
	 * P�id� poslucha�e ud�lost� protokolu pro odes�l�n� PL.
	 * 
	 * @param ticketProtocolListener    Poslucha� ud�lost� protokolu pro odes�l�n� PL.
	 */
	public void setTicketProtocolListener(ITicketProtocolListener ticketProtocolListener){

	}

}