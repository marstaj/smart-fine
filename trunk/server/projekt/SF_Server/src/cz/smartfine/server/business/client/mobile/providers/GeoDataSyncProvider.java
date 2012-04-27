package cz.smartfine.server.business.client.mobile.providers;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerGeoDataProtocolListener;

/**
 * Třída zajišťující nahrání geolokačních dat na server.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:25
 */
public class GeoDataSyncProvider implements IServerGeoDataProtocolListener {

	public GeoDataSyncProvider(){

	}

	/**
	 * 
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

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