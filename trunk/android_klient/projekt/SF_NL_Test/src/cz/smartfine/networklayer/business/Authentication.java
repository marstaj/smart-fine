package cz.smartfine.networklayer.business;
import cz.smartfine.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.networklayer.model.AuthenticationFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;


/**
 * T��da zaji��uj�c� ov��en� identity.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public class Authentication implements IAuthenticationProtocolListener {

	public Authentication(){

	}

	public void finalize() throws Throwable {

	}
	
	/**
	 * Handler zpracov�vaj�c� ud�lost ztr�ty spojen�.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler, reaguj�c� na ud�lost odesl�n� zpr�vy na server.
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