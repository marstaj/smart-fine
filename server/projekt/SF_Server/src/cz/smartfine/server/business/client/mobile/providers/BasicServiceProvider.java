package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.networklayer.model.MobileDeviceDB;
import cz.smartfine.networklayer.model.PolicemanDB;
import cz.smartfine.networklayer.model.mobile.LoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.HibernateUtil;
import cz.smartfine.server.business.client.mobile.MobileClientServer;
import cz.smartfine.server.business.client.mobile.providers.listeners.IBasicServiceProtocolListener;
import cz.smartfine.server.business.client.model.PolicemanLoginDB;
import cz.smartfine.server.networklayer.dataprotocols.mobile.BasicServiceProtocol;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

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
        System.out.println("SERVER: LOGIN->BG: " + badgeNumber + " PIN: " + pin + " IMEI: " + imei);
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

            System.out.println("SERVER: -------------------- AUTHENTICATION SUCCESFUL --------------------");
            protocol.authenticationSuccessful(); //odeslání zprávy o úspěšném přihlášení
        } else { //nepřihlášen
            protocol.authenticationFail(loginStatus.failReason); //odeslání zprávy o neúspěšném přihlášení
        }
    }

    @Override
    public void onAuthenticationRequest(int badgeNumber, int pin) {
        System.out.println("SERVER: AUTHENTICATION->BG: " + badgeNumber + " PIN: " + pin);
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

    private AuthenticationStatus authenticate(int badgeNumber, int pin, String imei) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //KONTROLA EXISTENCE ZAŘÍZENÍ//

            //vybere záznamy o zařízení ze kterého přichází požadavek na autentizaci//
            Query devQuery = session.createQuery("FROM MobileDeviceDB dev WHERE dev.imei = :imei");
            devQuery.setParameter("imei", imei); //nastaví číslo IMEI

            List devList = devQuery.list(); //spustí dotaz na DB
            MobileDeviceDB dev;

            //dotaz musí vrátit nějaká data, jinak zařízení neexistuje//
            if (devList.size() > 0) {
                dev = ((List<MobileDeviceDB>) devList).get(0); //první záznam
            } else { //žádný záznam
                return new AuthenticationStatus(false, LoginFailReason.UNKNOWN_IMEI);
            }

            //KONTROLA EXISTENCE SLUŽEBNÍHO ČÍSLA A SPRÁVNOSTI PINU//

            //vybere záznamy o policistovy, který se chce autentizovat//
            Query polQuery = session.createQuery("FROM PolicemanLoginDB pol WHERE pol.badgeNumber = :bg AND pol.pin = :pin");
            polQuery.setParameter("bg", badgeNumber); //nastaví služební číslo
            polQuery.setParameter("pin", pin); //nastaví PIN

            List polList = polQuery.list(); //spustí dotaz na DB
            PolicemanLoginDB pol;

            //dotaz musí vrátit nějaká data, jinak policista neexistuje//
            if (polList.size() > 0) {
                pol = ((List<PolicemanLoginDB>) polList).get(0); //první záznam
            } else { //žádný záznam
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
            e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
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
            Query polQuery = session.createQuery("FROM PolicemanLoginDB pol WHERE pol.badgeNumber = :bg AND pol.pin = :pin");
            polQuery.setParameter("bg", badgeNumber); //nastaví služební číslo
            polQuery.setParameter("pin", pin); //nastaví PIN

            List polList = polQuery.list(); //spustí dotaz na DB

            //dotaz musí vrátit nějaká data, jinak policista neexistuje//
            if (polList.size() > 0) {
                return new AuthenticationStatus(true);
            } else { //žádný záznam
                return new AuthenticationStatus(false, LoginFailReason.WRONG_BADGE_NUMBER_OR_PIN);
            }
        } catch (HibernateException e) {
            e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
            return new AuthenticationStatus(false, LoginFailReason.UNKNOWN_REASON);
        } finally {
            session.getTransaction().commit();
        }
    }
}