package cz.smartfine.networklayer.dataprotocols;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import cz.smartfine.networklayer.business.listeners.IGeoDataProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.LoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.MessageBuilder;

/**
 * P�edstavuje t��du protokolu pro p�enost geoloka�n�ch dat.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:46
 */
public class GeoDataProtocol implements IDataProtocol {

	/**
	 * Rozhran� pro p��stup k odes�l�n� a p��j�m�n� dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� z t�to t��dy.
	 */
	private IGeoDataProtocolListener geoDataProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public GeoDataProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhran� pro p�enos dat.
	 * @param geoDataProtocolListener Poslucha� ud�lost� z t�to t��dy.
	 */
	public GeoDataProtocol(INetworkInterface networkInterface, IGeoDataProtocolListener geoDataProtocolListener){
		this.networkInterface = networkInterface;
		this.geoDataProtocolListener = geoDataProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrov�n� se jako poslucha�
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere poslucha�e ud�lost� protokolu pro odes�l�n� geoloka�n�ch dat.
	 * 
	 * @param geoDataProtocolListener    Poslucha� ud�lost� z protokolu pro odes�l�n�
	 * geoloka�n�ch dat.
	 */
	public void removeGeoDataProtocolListener(IGeoDataProtocolListener geoDataProtocolListener){
		this.geoDataProtocolListener = null;
	}
	
	/**
	 * P�id� poslucha�e ud�lost� protokolu pro odes�l�n� geoloka�n�ch dat.
	 * 
	 * @param geoDataProtocolListener    Poslucha� ud�lost� z protokolu pro odes�l�n�
	 * geoloka�n�ch dat.
	 */
	public void setGeoDataProtocolListener(IGeoDataProtocolListener geoDataProtocolListener){
		this.geoDataProtocolListener = geoDataProtocolListener;
	}
	
	//================================================== HANDLERY UD�LOST� ==================================================//
	
	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){
		if (geoDataProtocolListener != null){
			geoDataProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent(){
		if (geoDataProtocolListener != null){
			geoDataProtocolListener.onMessageSent();
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
	 * Ode�le geoloka�n� data na server.
	 * 
	 * @param geoData    Seznam geoloka�n�ch dat pro posl�n� na server.
	 * @throws IOException 
	 */
	public void sendGeoData(List geoData) throws IOException{
		if(networkInterface != null){
			networkInterface.sendData(createGeoMessage(geoData));
		}
	}

	//================================================== PRIV�TN� METODY ==================================================//
	
	//TODO FORM�T GEO DAT
	protected byte[] createGeoMessage(List geoData) throws IOException{
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_UPLOAD_GEO); //identifik�tor zpr�vy
		
		ByteArrayOutputStream geoBytes = new ByteArrayOutputStream();
		ObjectOutputStream objOS = new ObjectOutputStream(geoBytes);
		objOS.writeObject(geoData); //serializuje PL
		objOS.close();
		
		msg.putArrayWithIntLength(geoBytes.toByteArray()); //vlo�en� pole serializovan� kolekce s geo daty
		
		geoBytes.close();
		
		return msg.getByteArray();
	}
	
}
