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
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public TicketSyncProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
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
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrov�n� se jako poslucha�
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere poslucha�e ud�lost� protokolu pro odes�l�n� PL.
	 * 
	 * @param ticketProtocolListener    Poslucha� ud�lost� protokolu pro odes�l�n� PL.
	 */
	public void removeTicketProtocolListener(ITicketProtocolListener ticketProtocolListener){
		this.ticketProtocolListener = null;
	}
	
	/**
	 * P�id� poslucha�e ud�lost� protokolu pro odes�l�n� PL.
	 * 
	 * @param ticketProtocolListener    Poslucha� ud�lost� protokolu pro odes�l�n� PL.
	 */
	public void setTicketProtocolListener(ITicketProtocolListener ticketProtocolListener){
		this.ticketProtocolListener = ticketProtocolListener;
	}
	
	//================================================== HANDLERY UD�LOST� ==================================================//

	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){
		if (ticketProtocolListener != null){
			ticketProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent(){
		if (ticketProtocolListener != null){
			ticketProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//��dn� data nep�ij�m�
	}
	
	//================================================== V�KONN� METODY ==================================================//
	
	/**
	 * Odpoj� datov� protokol od z�kladn�ho protokolu.
	 */
	public void disconnectProtocol(){
		if(networkInterface != null){
			networkInterface.removeOnReceivedDataListener(this);
		}
	}

	/**
	 * Odes�l� p�edan� PL na server.
	 * 
	 * @param ticket    PL, kter� se m� odeslat na server.
	 * @param badgeNumber    Slu�ebn� ��slo policisty, kter� PL odes�l�. Pozn.: nemus�
	 * se shodovat s ��slem policisty, kter� je p�ihl�en.
	 * @throws IOException Probl�m p�i serializaci PL.
	 */
	public void sendTicket(Ticket ticket, int badgeNumber) throws IOException{
		if(networkInterface != null){
			networkInterface.sendData(createTicketMessage(ticket, badgeNumber));
		}
	}

	//================================================== PRIV�TN� METODY ==================================================//
	
	/**
	 * Vytv��� zpr�vu pro nahr�n� PL na server.
	 * @param ticket Parkovac� l�stek.
	 * @param badgeNumber Slu�ebn� ��slo policisty, kter� nahr�v� PL na server.
	 * @return Zpr�va pro odesl�n� na server.
	 * @throws IOException Probl�m p�i serializaci PL.
	 */
	protected byte[] createTicketMessage(Ticket ticket, int badgeNumber) throws IOException{
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_UPLOAD_TICKET); //identifik�tor zpr�vy
		msg.putInt(badgeNumber); //slu�ebn� ��slo policisty
		
		ByteArrayOutputStream ticketBytes = new ByteArrayOutputStream();
		ObjectOutputStream objOS = new ObjectOutputStream(ticketBytes);
		objOS.writeObject(ticket); //serializuje PL
		objOS.close();
		
		msg.putArrayWithIntLength(ticketBytes.toByteArray()); //vlo�en� pole objektu PL
		
		ticketBytes.close();
		
		return msg.getByteArray();
	}
	
}
