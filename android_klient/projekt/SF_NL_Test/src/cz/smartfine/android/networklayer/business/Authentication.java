package cz.smartfine.android.networklayer.business;

import cz.smartfine.android.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.networklayer.model.mobile.AuthenticationFailReason;

/**
 * Třída zajišťující ověření identity.
 *
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:40
 */
public class Authentication implements IAuthenticationProtocolListener {

    public Authentication() {
    }

    @Override
    public void finalize() throws Throwable {
    }

    /**
     * Handler zpracovávající událost ztráty spojení.
     */
    @Override
    public void onConnectionTerminated() {
    }

    /**
     * Handler, reagující na událost odeslání zprávy na server.
     */
    @Override
    public void onMessageSent() {
    }

    public void onLogout() {
    }

    @Override
    public void onAuthenticationConfirmed() {
    }

    @Override
    public void onAuthenticationFailed(AuthenticationFailReason reason) {
    }
}