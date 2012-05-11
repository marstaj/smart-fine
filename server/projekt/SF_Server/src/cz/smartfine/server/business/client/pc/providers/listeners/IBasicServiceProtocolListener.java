package cz.smartfine.server.business.client.pc.providers.listeners;

import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí ze základního protokolu.
 *
 * @author Pavel Brož
 */
public interface IBasicServiceProtocolListener extends IProtocolListener {

    /**
     * Handler události příjmu požadavku na přihlášení.
     *
     * @param badgeNumber Služební číslo přihlašovaného policisty.
     * @param pin PIN přihlašovaného policisty.
     */
    public void onLoginRequest(int badgeNumber, int pin);

    /**
     * Handler události příjmu zprávy která není přihlašovací.
     */
    public void onNonLoginMessageReceived();

    /**
     * Handler události příjmu odhlašovací zprávy.
     */
    public void onLogoutMessageReceived();

    /**
     * Handler události příjmu jakékoliv zprávy.
     */
    public void onMessageReceived();
}
