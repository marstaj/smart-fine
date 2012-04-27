package cz.smartfine.server.business.client.mobile.providers.listeners;
import smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího přenos parkovacích
 * lístků.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:26
 */
public interface IServerTicketProtocolListener extends IProtocolListener, IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

}