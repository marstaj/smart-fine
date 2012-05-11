package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.model.MobileDeviceDB;
import cz.smartfine.model.PolicemanDB;
import cz.smartfine.networklayer.model.mobile.LoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.HibernateUtil;
import cz.smartfine.server.business.client.mobile.MobileClientServer;
import cz.smartfine.server.business.client.mobile.providers.listeners.IBasicServiceProtocolListener;
import cz.smartfine.server.business.client.model.PolicemanLoginDB;
import cz.smartfine.server.networklayer.dataprotocols.mobile.BasicServiceProtocol;
import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Třída provádějící autentizaci a pomocné úkony při správě spojení mobilního klienta.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:24
 */
public class BasicServiceProvider implements IBasicServiceProtocolListener {

    /**
     * Interní třída pro předání výsledku autentizace
     */
    private class AuthenticationStatus {

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
        //System.out.println("SERVER: LOGIN->BG: " + badgeNumber + " PIN: " + pin + " IMEI: " + imei);
        AuthenticationStatus loginStatus = authenticate(badgeNumber, pin, imei);
        if (loginStatus.authenticated) {
            //nastaví serveru služební číslo a imei//
            clientServer.setBadgeNumber(badgeNumber);
            clientServer.setImei(imei);

            //odstraní předchozí server a zaregistruje tento//
            clientServer.closePreviousServer();
            clientServer.registerThisServer();

            //vytvoření dalších služeb serveru//
            clientServer.setGeoProvider(new GeoDataProvider(networkInterface, badgeNumber));
            clientServer.setSmsParkingProvider(new SMSParkingProvider(networkInterface));
            clientServer.setSpcCheckProvider(new SPCCheckProvider(networkInterface));
            clientServer.setTicketProvider(new TicketSyncProvider(networkInterface));

            //System.out.println("SERVER: -------------------- AUTHENTICATION SUCCESFUL --------------------");
            protocol.authenticationSuccessful(); //odeslání zprávy o úspěšném přihlášení
        } else { //nepřihlášen
            protocol.authenticationFail(loginStatus.failReason); //odeslání zprávy o neúspěšném přihlášení
        }
    }

    @Override
    public void onAuthenticationRequest(int badgeNumber, int pin) {
        //System.out.println("SERVER: AUTHENTICATION->BG: " + badgeNumber + " PIN: " + pin);
        AuthenticationStatus aStatus = authenticate(badgeNumber, pin);
        if (aStatus.authenticated) {
            protocol.authenticationSuccessful(); //odeslání zprávy o úspěšné autentizaci
        } else { //nepřihlášen
            protocol.authenticationFail(aStatus.failReason); //odeslání zprávy o neúspěšné autentizaci
        }
    }

    @Override
    public void onNonLoginMessageReceived() {
        if (!loggedIn) {
            clientServer.close();
        }
    }

    @Override
    public void onLogoutMessageReceived() {
        clientServer.close();
    }

    @Override
    public void onConnectionTerminated() {
        clientServer.close();
    }

    @Override
    public void onMessageSent() {
    }

    @Override
    public void onMessageReceived() {
        clientServer.setLastContactTime(new Date());
    }

    private AuthenticationStatus authenticate(int badgeNumber, int pin, String imei) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //KONTROLA EXISTENCE ZAŘÍZENÍ//

            //vybere záznamy o zařízení ze kterého přichází požadavek na autentizaci//
            Query devQuery = session.getNamedQuery("cz.smartfine.getdevice.by.imei");
            devQuery.setParameter("imei", imei); //nastaví číslo IMEI
            devQuery.setMaxResults(1); //maximálně jeden výsledek

            MobileDeviceDB dev = (MobileDeviceDB) devQuery.uniqueResult();
            //dotaz musí vrátit nějaká data, jinak zařízení neexistuje//
            if (dev == null) {
                return new AuthenticationStatus(false, LoginFailReason.UNKNOWN_IMEI);
            }
            //kontrola jestli není zařízení blokováno//
            if (dev.isBlocked()) {
                return new AuthenticationStatus(false, LoginFailReason.UNKNOWN_REASON);
            }

            //KONTROLA EXISTENCE SLUŽEBNÍHO ČÍSLA A SPRÁVNOSTI PINU//

            //vybere záznamy o policistovy, který se chce autentizovat//
            Query polQuery = session.getNamedQuery("cz.smartfine.getpolicemanlogin.by.bn.pin");
            polQuery.setParameter("bn", badgeNumber); //nastaví služební číslo
            polQuery.setParameter("pin", pin); //nastaví PIN
            polQuery.setMaxResults(1); //maximálně jeden výsledek

            PolicemanLoginDB pol = (PolicemanLoginDB) polQuery.uniqueResult();
            //dotaz musí vrátit nějaká data, jinak policista neexistuje//
            if (pol == null) {
                return new AuthenticationStatus(false, LoginFailReason.WRONG_BADGE_NUMBER_OR_PIN);
            }

            //KONTROLA ASOCIACE MEZI POLICISTOU A MOBILNÍM ZAŘÍZENÍM//
            //projde asociace zařízení a porovná jestli je přítomen záznam přihlašovaného policisty//
            for (PolicemanDB p : dev.getAssociations()) {
                if (p.getId() == pol.getId()) {
                    return new AuthenticationStatus(true);
                }
            }
            return new AuthenticationStatus(false, LoginFailReason.IMEI_AND_BADGE_NUMBER_DONT_MATCH);

        } catch (HibernateException e) {
            //e.printStackTrace();
            return new AuthenticationStatus(false, LoginFailReason.UNKNOWN_REASON);
        } finally {
            session.getTransaction().commit();
        }
    }

    private AuthenticationStatus authenticate(int badgeNumber, int pin) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //KONTROLA EXISTENCE SLUŽEBNÍHO ČÍSLA A SPRÁVNOSTI PINU//

            //vybere záznamy o policistovy, který se chce autentizovat//
            Query polQuery = session.getNamedQuery("cz.smartfine.getpolicemanlogin.by.bn.pin");
            polQuery.setParameter("bn", badgeNumber); //nastaví služební číslo
            polQuery.setParameter("pin", pin); //nastaví PIN
            polQuery.setMaxResults(1); //maximálně jeden výsledek

            PolicemanLoginDB pol = (PolicemanLoginDB) polQuery.uniqueResult();
            //dotaz musí vrátit nějaká data, jinak policista neexistuje//
            if (pol == null) {
                return new AuthenticationStatus(false, LoginFailReason.WRONG_BADGE_NUMBER_OR_PIN);
            } else {
                return new AuthenticationStatus(true);
            }
        } catch (HibernateException e) {
            //e.printStackTrace();
            return new AuthenticationStatus(false, LoginFailReason.UNKNOWN_REASON);
        } finally {
            session.getTransaction().commit();
        }
    }
}