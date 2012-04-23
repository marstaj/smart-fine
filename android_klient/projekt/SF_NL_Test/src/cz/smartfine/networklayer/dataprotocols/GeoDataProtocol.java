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
 * Pøedstavuje tøídu protokolu pro pøenost geolokaèních dat.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:46
 */
public class GeoDataProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro pøístup k odesílání a pøíjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchaè událostí z této tøídy.
	 */
	private IGeoDataProtocolListener geoDataProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public GeoDataProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro pøenos dat.
	 * @param geoDataProtocolListener Posluchaè událostí z této tøídy.
	 */
	public GeoDataProtocol(INetworkInterface networkInterface, IGeoDataProtocolListener geoDataProtocolListener){
		this.networkInterface = networkInterface;
		this.geoDataProtocolListener = geoDataProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchaè
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere posluchaèe událostí protokolu pro odesílání geolokaèních dat.
	 * 
	 * @param geoDataProtocolListener    Posluchaè událostí z protokolu pro odesílání
	 * geolokaèních dat.
	 */
	public void removeGeoDataProtocolListener(IGeoDataProtocolListener geoDataProtocolListener){
		this.geoDataProtocolListener = null;
	}
	
	/**
	 * Pøidá posluchaèe událostí protokolu pro odesílání geolokaèních dat.
	 * 
	 * @param geoDataProtocolListener    Posluchaè událostí z protokolu pro odesílání
	 * geolokaèních dat.
	 */
	public void setGeoDataProtocolListener(IGeoDataProtocolListener geoDataProtocolListener){
		this.geoDataProtocolListener = geoDataProtocolListener;
	}
	
	//================================================== HANDLERY UDÁLOSTÍ ==================================================//
	
	/**
	 * Handler události ukonèení spojení.
	 */
	public void onConnectionTerminated(){
		if (geoDataProtocolListener != null){
			geoDataProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){
		if (geoDataProtocolListener != null){
			geoDataProtocolListener.onMessageSent();
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
	 * Odešle geolokaèní data na server.
	 * 
	 * @param geoData    Seznam geolokaèních dat pro poslání na server.
	 * @throws IOException 
	 */
	public void sendGeoData(List geoData) throws IOException{
		if(networkInterface != null){
			networkInterface.sendData(createGeoMessage(geoData));
		}
	}

	//================================================== PRIVÁTNÍ METODY ==================================================//
	
	//TODO FORMÁT GEO DAT
	protected byte[] createGeoMessage(List geoData) throws IOException{
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_UPLOAD_GEO); //identifikátor zprávy
		
		ByteArrayOutputStream geoBytes = new ByteArrayOutputStream();
		ObjectOutputStream objOS = new ObjectOutputStream(geoBytes);
		objOS.writeObject(geoData); //serializuje PL
		objOS.close();
		
		msg.putArrayWithIntLength(geoBytes.toByteArray()); //vložení pole serializované kolekce s geo daty
		
		geoBytes.close();
		
		return msg.getByteArray();
	}
	
}
