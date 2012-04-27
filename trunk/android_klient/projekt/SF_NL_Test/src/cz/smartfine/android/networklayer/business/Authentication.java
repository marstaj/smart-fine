package cz.smartfine.android.networklayer.business;
import cz.smartfine.android.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.android.networklayer.model.AuthenticationFailReason;
import cz.smartfine.android.networklayer.networkinterface.INetworkInterface;


/**
 * Tøída zajišující ovìøení identity.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public class Authentication implements IAuthenticationProtocolListener {

	public Authentication(){

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

	public void onLogout() {
		
		
	}

	public void onAuthenticationConfirmed() {
		
		
	}

	public void onAuthenticationFailed(AuthenticationFailReason reason) {
		
		
	}

}