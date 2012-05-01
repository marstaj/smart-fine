package cz.smartfine.android.networklayer.business.listeners;

import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.model.mobile.LoginFailReason;

/**
 * Interface posluchače událostí přihlašovací třídy.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:42
 */
public interface ILoginProviderListener extends IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler události správného přihlášení.
	 */
	public void onLoginConfirmed();

	/**
	 * Handler události chybného přihlášení.
	 * 
	 * @param reason
	 *            Důvod selhání přihlášení.
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