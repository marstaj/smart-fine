package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.IClientServer;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerMainProtocolListener;

/**
 * Třída provádějící autentizaci a pomocné úkony při správě spojení.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:24
 */
public class MainProvider implements IServerMainProtocolListener {

    private INetworkInterface networkInterface;
    private IClientServer clientServer;

    public MainProvider(INetworkInterface networkInterface, IClientServer clientServer) {
        this.networkInterface = networkInterface;
        this.clientServer = clientServer;
    }
    


    @Override
    public void onLoginRequest(int badgeNumber, int pin, String imei) {
        throw new UnsupportedOperationException("Not supported yet.");
        //TODO: ZAVŘÍT PŘEDCHOZÍ SPOJENÍ
        //TODO: NASTAVIT ČAS
        //TODO: PŘIPOJIT OSTATNÍ PROTOKOLY
        //TODO: ODESLAT LOGIN CONFIRMED
    }

    @Override
    public void onAuthenticationRequest(int badgeNumber, int pin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onNonLoginMessageReceived() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onLogoutMessageReceived() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onConnectionTerminated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onMessageSent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}