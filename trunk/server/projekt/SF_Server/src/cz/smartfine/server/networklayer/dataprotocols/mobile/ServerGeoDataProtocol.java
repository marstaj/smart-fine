package cz.smartfine.server.networklayer.mobile.dataprotocols;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerGeoDataProtocolListener;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import smartfine.networklayer.networkinterface.INetworkInterface;
import smartfine.networklayer.business.listeners.IGeoDataProtocolListener;

/**
 * Představuje třídu protokolu pro přenost geolokačních dat.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:25
 */
public class ServerGeoDataProtocol implements IDataProtocol {

	/**
	 * Posluchač událostí z této třídy.
	 */
	private IServerGeoDataProtocolListener geoDataProtocolListener;
	/**
	 * Rozhraní pro přístup k odesílání a příjímání dat.
	 */
	private INetworkInterface networkInterface;

	public ServerGeoDataProtocol(){

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenost dat.
	 */
	public ServerGeoDataProtocol(INetworkInterface networkInterface){

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenos dat.
	 * @param geoDataProtocolListener    Posluchač událostí z této třídy.
	 */
	public ServerGeoDataProtocol(INetworkInterface networkInterface, IServerGeoDataProtocolListener geoDataProtocolListener){

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
	 * TODO FORMÁT GEO DAT
	 * 
	 * @param geoData
	 * @exception IOException
	 */
	protected byte[] createGeoMessage(List geoData)
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
	 * Odebere posluchače událostí protokolu pro odesílání geolokačních dat.
	 * 
	 * @param geoDataProtocolListener    Posluchač událostí z protokolu pro odesílání
	 * geolokačních dat.
	 */
	public void removeGeoDataProtocolListener(IServerGeoDataProtocolListener geoDataProtocolListener){

	}

	/**
	 * Odešle geolokační data na server.
	 * 
	 * @param geoData    Seznam geolokačních dat pro poslání na server.
	 * @exception IOException IOException
	 */
	public void sendGeoData(List geoData)
	  throws IOException{

	}

	/**
	 * Přidá posluchače událostí protokolu pro odesílání geolokačních dat.
	 * 
	 * @param geoDataProtocolListener    Posluchač událostí z protokolu pro odesílání
	 * geolokačních dat.
	 */
	public void setGeoDataProtocolListener(IServerGeoDataProtocolListener geoDataProtocolListener){

	}

}