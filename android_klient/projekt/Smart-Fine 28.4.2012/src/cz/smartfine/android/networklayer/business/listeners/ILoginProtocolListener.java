package cz.smartfine.android.networklayer.business.listeners;
import cz.smartfine.android.networklayer.model.LoginFailReason;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího přihlašování na server.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:41
 */
public interface ILoginProtocolListener extends IProtocolListener {

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
	 * @param reason    Důvod selhání přihlášení.
	 */
	public void onLoginFailed(LoginFailReason reason);

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

}