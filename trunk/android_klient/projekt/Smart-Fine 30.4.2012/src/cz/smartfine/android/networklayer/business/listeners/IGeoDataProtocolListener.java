package cz.smartfine.android.networklayer.business.listeners;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího přenos geolokačních
 * dat.
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:41
 */
public interface IGeoDataProtocolListener extends IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

}