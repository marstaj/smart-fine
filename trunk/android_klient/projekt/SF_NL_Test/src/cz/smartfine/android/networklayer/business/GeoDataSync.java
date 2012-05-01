package cz.smartfine.android.networklayer.business;

import cz.smartfine.android.networklayer.ConnectionProvider;
import cz.smartfine.android.networklayer.business.listeners.IGeoDataProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.GeoDataProtocol;

/**
 * Třída zajišťující nahrání geolokačních dat na server.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:40
 */
public class GeoDataSync implements IGeoDataProtocolListener {

	public GeoDataSync() {

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated() {

	}

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent() {

	}

}