package cz.smartfine.android.networklayer.business.listeners;

import cz.smartfine.networklayer.model.mobile.LoginFailReason;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího přihlašování na server.
 *
 * @author Pavel Brož
 * @version 1.0
 */
public interface ILoginProtocolListener extends IProtocolListener {

    /**
     * Handler zpracovávající událost ztráty spojení.
     */
    @Override
    public void onConnectionTerminated();

    /**
     * Handler události správného přihlášení.
     */
    public void onLoginConfirmed();

    /**
     * Handler události chybného přihlášení.
     *
     * @param reason Důvod selhání přihlášení.
     */
    public void onLoginFailed(LoginFailReason reason);

    /**
     * Handler, reagující na událost odeslání zprávy na server.
     */
    @Override
    public void onMessageSent();
}