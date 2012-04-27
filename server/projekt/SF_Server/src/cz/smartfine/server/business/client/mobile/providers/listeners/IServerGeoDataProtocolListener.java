package cz.smartfine.server.business.client.mobile.providers.listeners;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího přenos geolokačních
 * dat.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:25
 */
public interface IServerGeoDataProtocolListener extends IProtocolListener, IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

}