package cz.smartfine.networklayer.business.listeners;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.model.AuthenticationFailReason;

/**
 * Interface posluchaèe událostí protokolu zpracovávajícího autentizaci policisty.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface IAuthenticationProtocolListener extends IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler události úspìšného ovìøení identity.
	 */
	public void onAuthenticationConfirmed();

	/**
	 * Handler události neúspìšného ovìøení identity..
	 * 
	 * @param reason    Dùvod selhání autentizace.
	 */
	public void onAuthenticationFailed(AuthenticationFailReason reason);

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

}