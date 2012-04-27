package cz.smartfine.android.networklayer.business.listeners;

import cz.smartfine.android.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.android.networklayer.model.LoginFailReason;

/**
 * Interface poslucha�e ud�lost� p�ihla�ovac� t��dy.
 * 
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface ILoginProviderListener extends IProtocolListener {

	/**
	 * Handler zpracov�vaj�c� ud�lost ztr�ty spojen�.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler ud�losti spr�vn�ho p�ihl�en�.
	 */
	public void onLoginConfirmed();

	/**
	 * Handler ud�losti chybn�ho p�ihl�en�.
	 * 
	 * @param reason    D�vod selh�n� p�ihl�en�.
	 */
	public void onLoginFailed(LoginFailReason reason);

	/**
	 * Handler ud�losti odhl�en� od serveru
	 */
	public void onLogout();
	
	/**
	 * Handler, reaguj�c� na ud�lost odesl�n� zpr�vy na server.
	 */
	public void onMessageSent();

}