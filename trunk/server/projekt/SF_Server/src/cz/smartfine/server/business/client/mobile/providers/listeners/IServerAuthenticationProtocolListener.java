package cz.smartfine.server.business.client.mobile.providers.listeners;
import smartfine.networklayer.model.AuthenticationFailReason;
import smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího autentizaci policisty.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:25
 */
public interface IServerAuthenticationProtocolListener extends IProtocolListener, IProtocolListener {

	/**
	 * Handler události úspěšného ověření identity.
	 */
	public void onAuthenticationConfirmed();

	/**
	 * Handler události neúspěšného ověření identity..
	 * 
	 * @param reason    Důvod selhání autentizace.
	 */
	public void onAuthenticationFailed(AuthenticationFailReason reason);

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

}