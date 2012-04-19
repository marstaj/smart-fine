package cz.smartfine.networklayer.dataprotocols;
import java.util.List;

import cz.smartfine.networklayer.business.listeners.IGeoDataProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

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
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public GeoDataProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
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
	 * Odebere posluchaèe událostí protokolu pro odesílání geolokaèních dat.
	 * 
	 * @param geoDataProtocolListener    Posluchaè událostí z protokolu pro odesílání
	 * geolokaèních dat.
	 */
	public void removeGeoDataProtocolListener(IGeoDataProtocolListener geoDataProtocolListener){

	}

	/**
	 * Odešle geolokaèní data na server.
	 * 
	 * @param geodata    Seznam geolokaèních dat pro poslání na server.
	 */
	public void sendGeoData(List geodata){

	}

	/**
	 * Pøidá posluchaèe událostí protokolu pro odesílání geolokaèních dat.
	 * 
	 * @param geoDataProtocolListener    Posluchaè událostí z protokolu pro odesílání
	 * geolokaèních dat.
	 */
	public void setGeoDataProtocolListener(IGeoDataProtocolListener geoDataProtocolListener){

	}

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

}