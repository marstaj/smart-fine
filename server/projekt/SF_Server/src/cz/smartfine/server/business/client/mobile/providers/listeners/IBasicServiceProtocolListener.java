package cz.smartfine.server.business.client.mobile.providers.listeners;

import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu, který provádí autentizaci a pomocné úkony při správě spojení.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:25
 */
public interface IBasicServiceProtocolListener extends IProtocolListener {

    /**
     * Handler události příjmu požadavku na přihlášení.
     *
     * @param badgeNumber Služební číslo přihlašovaného policisty.
     * @param pin PIN přihlašovaného policisty.
     * @param imei Identifikační číslo mobilního zařízení (IMEI), ze kterého se přihlašuje.
     */
    public void onLoginRequest(int badgeNumber, int pin, String imei);

    /**
     * Handler události příjmu požadavku na autentizaci.
     *
     * @param badgeNumber Služební číslo přihlašovaného policisty.
     * @param pin PIN přihlašovaného policisty.
     */
    public void onAuthenticationRequest(int badgeNumber, int pin);

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

    /**
     * Handler zpracovávající událost ztráty spojení.
     */
    @Override
    public void onConnectionTerminated();

    /**
     * Handler, reagující na událost odeslání zprávy na server.
     */
    @Override
    public void onMessageSent();
}