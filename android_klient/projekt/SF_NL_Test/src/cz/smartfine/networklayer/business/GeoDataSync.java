package cz.smartfine.networklayer.business;
import cz.smartfine.networklayer.ConnectionProvider;
import cz.smartfine.networklayer.business.listeners.IGeoDataProtocolListener;
import cz.smartfine.networklayer.dataprotocols.GeoDataProtocol;

/**
 * Tøída zajišující nahrání geolokaèních dat na server.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:46
 */
public class GeoDataSync implements IGeoDataProtocolListener {

	public GeoDataSync(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent(){

	}

}