package cz.smartfine.networklayer.dataprotocols;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import model.Ticket;
import cz.smartfine.networklayer.business.listeners.ITicketProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.MessageBuilder;

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
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public TicketSyncProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
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
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchaè
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere posluchaèe událostí protokolu pro odesílání PL.
	 * 
	 * @param ticketProtocolListener    Posluchaè událostí protokolu pro odesílání PL.
	 */
	public void removeTicketProtocolListener(ITicketProtocolListener ticketProtocolListener){
		this.ticketProtocolListener = null;
	}
	
	/**
	 * Pøidá posluchaèe událostí protokolu pro odesílání PL.
	 * 
	 * @param ticketProtocolListener    Posluchaè událostí protokolu pro odesílání PL.
	 */
	public void setTicketProtocolListener(ITicketProtocolListener ticketProtocolListener){
		this.ticketProtocolListener = ticketProtocolListener;
	}
	
	//================================================== HANDLERY UDÁLOSTÍ ==================================================//

	/**
	 * Handler události ukonèení spojení.
	 */
	public void onConnectionTerminated(){
		if (ticketProtocolListener != null){
			ticketProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){
		if (ticketProtocolListener != null){
			ticketProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//žádná data nepøijímá
	}
	
	//================================================== VÝKONNÉ METODY ==================================================//
	
	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){
		if(networkInterface != null){
			networkInterface.removeOnReceivedDataListener(this);
		}
	}

	/**
	 * Odesílá pøedaný PL na server.
	 * 
	 * @param ticket    PL, který se má odeslat na server.
	 * @param badgeNumber    Služební èíslo policisty, který PL odesílá. Pozn.: nemusí
	 * se shodovat s èíslem policisty, který je pøihlášen.
	 * @throws IOException Problém pøi serializaci PL.
	 */
	public void sendTicket(Ticket ticket, int badgeNumber) throws IOException{
		if(networkInterface != null){
			networkInterface.sendData(createTicketMessage(ticket, badgeNumber));
		}
	}

	//================================================== PRIVÁTNÍ METODY ==================================================//
	
	/**
	 * Vytváøí zprávu pro nahrání PL na server.
	 * @param ticket Parkovací lístek.
	 * @param badgeNumber Služební èíslo policisty, který nahrává PL na server.
	 * @return Zpráva pro odeslání na server.
	 * @throws IOException Problém pøi serializaci PL.
	 */
	protected byte[] createTicketMessage(Ticket ticket, int badgeNumber) throws IOException{
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_UPLOAD_TICKET); //identifikátor zprávy
		msg.putInt(badgeNumber); //služební èíslo policisty
		
		ByteArrayOutputStream ticketBytes = new ByteArrayOutputStream();
		ObjectOutputStream objOS = new ObjectOutputStream(ticketBytes);
		objOS.writeObject(ticket); //serializuje PL
		objOS.close();
		
		msg.putArrayWithIntLength(ticketBytes.toByteArray()); //vložení pole objektu PL
		
		ticketBytes.close();
		
		return msg.getByteArray();
	}
	
}
