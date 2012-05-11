package cz.smartfine.android.networklayer.business.listeners;

import cz.smartfine.networklayer.model.mobile.AuthenticationFailReason;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího autentizaci policisty.
 *
 * @author Pavel Brož
 * @version 1.0 
 */
public interface IAuthenticationProtocolListener extends IProtocolListener {

    /**
     * Handler zpracovávající událost ztráty spojení.
     */
    @Override
    public void onConnectionTerminated();

    /**
     * Handler události úspěšného ověření identity.
     */
    public void onAuthenticationConfirmed();

    /**
     * Handler události neúspěšného ověření identity..
     *
     * @param reason Důvod selhání autentizace.
     */
    public void onAuthenticationFailed(AuthenticationFailReason reason);

    /**
     * Handler, reagující na událost odeslání zprávy na server.
     */
    @Override
    public void onMessageSent();
}