package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.networklayer.model.mobile.LoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.mobile.MobileClientServer;
import cz.smartfine.server.business.client.mobile.providers.listeners.IBasicServiceProtocolListener;
import cz.smartfine.server.networklayer.dataprotocols.mobile.BasicServiceProtocol;
import java.util.Date;

/**
 * Třída provádějící autentizaci a pomocné úkony při správě spojení.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:24
 */
public class BasicServiceProvider implements IBasicServiceProtocolListener {

    /**
     * Interní třída pro předání výsledku autentizace
     */
    private class AuthenticationStatus{
        public boolean authenticated = false;
        public LoginFailReason failReason = LoginFailReason.UNKNOWN_REASON;

        public AuthenticationStatus(boolean authenticated) {
            this(authenticated, LoginFailReason.UNKNOWN_REASON);
        }
        public AuthenticationStatus(boolean authenticated, LoginFailReason failReason) {
            this.authenticated = authenticated;
            this.failReason = failReason;
        }
    }
    
    /**
     * Odkaz na server.
     */
    private MobileClientServer clientServer;
    /**
     * Datový protokol pro přenos dat.
     */
    private BasicServiceProtocol protocol;
    
    INetworkInterface networkInterface;
    
    private boolean loggedIn = false;
    
    public BasicServiceProvider(INetworkInterface networkInterface, MobileClientServer clientServer) {
        this.clientServer = clientServer;
        this.networkInterface = networkInterface;
        protocol = new BasicServiceProtocol(networkInterface, this);
        clientServer.setLastContactTime(new Date());
    }

    @Override
    public void onLoginRequest(int badgeNumber, int pin, String imei) {
        AuthenticationStatus loginStatus = authenticate(badgeNumber, pin, imei);
        if (loginStatus.authenticated){
            //nastaví serveru služební číslo a imei//
            clientServer.setBadgeNumber(badgeNumber);
            clientServer.setImei(imei);
            
            //odstraní předchozí server a zaregistruje tento//
            clientServer.closePreviousServer();
            clientServer.registerThisServer();
            
            //vytvoření dalších služeb serveru//
            clientServer.setGeoProvider(new GeoDataProvider(networkInterface));
            clientServer.setSmsParkingProvider(new SMSParkingProvider(networkInterface));
            clientServer.setSpcCheckProvider(new SPCCheckProvider(networkInterface));
            clientServer.setTicketProvider(new TicketSyncProvider(networkInterface));
            
            System.out.println("SERVER: LOGIN->BG: " + badgeNumber + " PIN: " + pin + " IMEI: " + imei);
            protocol.authenticationSuccessful(); //odeslání zprávy o úspěšném přihlášení
        }else{ //nepřihlášen
            protocol.authenticationFail(loginStatus.failReason); //odeslání zprávy o neúspěšném přihlášení
        }
    }

    @Override
    public void onAuthenticationRequest(int badgeNumber, int pin) {
        AuthenticationStatus aStatus = authenticate(badgeNumber, pin);
        if (aStatus.authenticated){
            System.out.println("SERVER: AUTHENTICATION->BG: " + badgeNumber + " PIN: " + pin);
            protocol.authenticationSuccessful(); //odeslání zprávy o úspěšné autentizaci
        }else{ //nepřihlášen
            protocol.authenticationFail(aStatus.failReason); //odeslání zprávy o neúspěšné autentizaci
        }
    }

    @Override
    public void onNonLoginMessageReceived() {
        if (!loggedIn){
            System.out.println("SERVER: NON LOGIN MESSAGE RECEIVED");
            clientServer.close();
        }
    }

    @Override
    public void onLogoutMessageReceived() {
        System.out.println("SERVER: LOGOUT MESSAGE RECEIVED");
        clientServer.close();
    }

    @Override
    public void onConnectionTerminated() {
        System.out.println("SERVER: CONNECTION TERMINATED");
        clientServer.close();
    }

    @Override
    public void onMessageSent() {
        
    }

    @Override
    public void onMessageReceived() {
        System.out.println("SERVER: MESSAGE RECEIVED DATE: " + (new Date()).toString());
        clientServer.setLastContactTime(new Date());
    }
    
    
    private AuthenticationStatus authenticate(int badgeNumber, int pin, String imei){
        return new AuthenticationStatus(true);
        //TODO: DODĚLAT
    }
    
    private AuthenticationStatus authenticate(int badgeNumber, int pin){
        return new AuthenticationStatus(true);
        //TODO: DODĚLAT
    }
}