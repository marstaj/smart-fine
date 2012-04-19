package cz.smartfine.networklayer.business.listeners;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.model.AuthenticationFailReason;

/**
 * Interface poslucha�e ud�lost� protokolu zpracov�vaj�c�ho autentizaci policisty.
 * 
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface IAuthenticationProtocolListener extends IProtocolListener {

	/**
	 * Handler zpracov�vaj�c� ud�lost ztr�ty spojen�.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler ud�losti �sp�n�ho ov��en� identity.
	 */
	public void onAuthenticationConfirmed();

	/**
	 * Handler ud�losti ne�sp�n�ho ov��en� identity..
	 * 
	 * @param reason    D�vod selh�n� autentizace.
	 */
	public void onAuthenticationFailed(AuthenticationFailReason reason);

	/**
	 * Handler, reaguj�c� na ud�lost odesl�n� zpr�vy na server.
	 */
	public void onMessageSent();

}