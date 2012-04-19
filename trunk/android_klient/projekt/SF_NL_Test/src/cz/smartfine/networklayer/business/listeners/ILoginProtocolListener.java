package cz.smartfine.networklayer.business.listeners;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.model.LoginFailReason;

/**
 * Interface posluchaèe událostí protokolu zpracovávajícího pøihlašování na server.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface ILoginProtocolListener extends IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler události správného pøihlášení.
	 */
	public void onLoginConfirmed();

	/**
	 * Handler události chybného pøihlášení.
	 * 
	 * @param reason    Dùvod selhání pøihlášení.
	 */
	public void onLoginFailed(LoginFailReason reason);

	/**
	 * Handler události odhlášení od serveru
	 */
	public void onLogout();
	
	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

}