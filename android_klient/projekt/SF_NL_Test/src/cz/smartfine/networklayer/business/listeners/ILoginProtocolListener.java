package cz.smartfine.networklayer.business.listeners;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.model.LoginFailReason;

/**
 * Interface poslucha�e ud�lost� protokolu zpracov�vaj�c�ho p�ihla�ov�n� na server.
 * 
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface ILoginProtocolListener extends IProtocolListener {

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