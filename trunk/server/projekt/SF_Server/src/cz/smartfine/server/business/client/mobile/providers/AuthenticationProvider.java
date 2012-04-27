package cz.smartfine.server.business.client.mobile.providers;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerAuthenticationProtocolListener;
import smartfine.networklayer.model.AuthenticationFailReason;

/**
 * Třída zajišťující ověření identity.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:24
 */
public class AuthenticationProvider implements IServerAuthenticationProtocolListener {

	public AuthenticationProvider(){

	}

	/**
	 * 
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

	}

	public void onAuthenticationConfirmed(){

	}

	/**
	 * 
	 * @param reason
	 */
	public void onAuthenticationFailed(AuthenticationFailReason reason){

	}

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated(){

	}

	public void onLogout(){

	}

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent(){

	}

}