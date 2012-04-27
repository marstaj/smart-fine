package cz.smartfine.android.networklayer.business.listeners;

import cz.smartfine.android.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.android.networklayer.model.LoginFailReason;

/**
 * Interface posluchaèe událostí pøihlašovací tøídy.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface ILoginProviderListener extends IProtocolListener {

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