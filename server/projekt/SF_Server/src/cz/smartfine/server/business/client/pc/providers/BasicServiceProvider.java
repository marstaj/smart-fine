package cz.smartfine.server.business.client.pc.providers;

import cz.smartfine.networklayer.model.pc.PCClientPermission;
import cz.smartfine.networklayer.model.pc.PCLoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.HibernateUtil;
import cz.smartfine.server.business.client.model.PolicemanLoginDB;
import cz.smartfine.server.business.client.pc.PCClientServer;
import cz.smartfine.server.business.client.pc.providers.listeners.IBasicServiceProtocolListener;
import cz.smartfine.server.networklayer.dataprotocols.pc.BasicServiceProtocol;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Zprostředkovatel základních služeb serveru pro PC klienta.
 *
 * @author Pavel Brož
 */
public class BasicServiceProvider implements IBasicServiceProtocolListener {

    /**
     * Interní třída pro předání výsledku autentizace
     */
    private class AuthenticationStatus {

        public PCClientPermission permissions = null;
        public boolean authenticated = false;
        public PCLoginFailReason failReason = PCLoginFailReason.UNKNOWN_REASON;

        public AuthenticationStatus(boolean authenticated, PCClientPermission permissions) {
            this(authenticated, PCLoginFailReason.UNKNOWN_REASON, permissions);
        }

        public AuthenticationStatus(boolean authenticated, PCLoginFailReason failReason, PCClientPermission permissions) {
            this.authenticated = authenticated;
            this.failReason = failReason;
            this.permissions = permissions;
        }
    }
    /**
     * Odkaz na server.
     */
    private PCClientServer clientServer;
    /**
     * Datový protokol pro přenos dat.
     */
    private BasicServiceProtocol protocol;
    INetworkInterface networkInterface;
    private boolean loggedIn = false;

    public BasicServiceProvider(INetworkInterface networkInterface, PCClientServer clientServer) {
        this.clientServer = clientServer;
        this.networkInterface = networkInterface;
        protocol = new BasicServiceProtocol(networkInterface, this);
        clientServer.setLastContactTime(new Date());
    }

    @Override
    public void onLoginRequest(int badgeNumber, int pin) {
        //System.out.println("SERVER PC: LOGIN->BG: " + badgeNumber + " PIN: " + pin);
        AuthenticationStatus loginStatus = authenticate(badgeNumber, pin);

        if (loginStatus.authenticated) {
            clientServer.setBadgeNumber(badgeNumber);//nastaví serveru služební číslo
            clientServer.setPermissions(loginStatus.permissions); //nastaví serveru oprávnění

            clientServer.registerThisServer();//zaregistruje tento server

            //vytvoření dalších služeb serveru//
            clientServer.setPinProvider(new PinProvider(networkInterface, clientServer));
            clientServer.setQueryProvider(new QueryProvider(networkInterface, clientServer));

            //System.out.println("SERVER: -------------------- AUTHENTICATION SUCCESFUL --------------------");
            protocol.authenticationSuccessful(loginStatus.permissions); //odeslání zprávy o úspěšném přihlášení
        } else { //nepřihlášen
            protocol.authenticationFail(loginStatus.failReason); //odeslání zprávy o neúspěšném přihlášení
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

            List polList = polQuery.list(); //spustí dotaz na DB
            PolicemanLoginDB pol;

            //dotaz musí vrátit nějaká data, jinak policista neexistuje//
            if (polList.size() > 0) {
                pol = ((List<PolicemanLoginDB>) polList).get(0); //první záznam
                return new AuthenticationStatus(true, new PCClientPermission(pol.getPermissions())); //nastaví oprávnění a vrátí výsledek přihlášení
            } else { //žádný záznam
                return new AuthenticationStatus(false, PCLoginFailReason.WRONG_BADGE_NUMBER_OR_PIN, null);
            }

        } catch (HibernateException e) {
            //e.printStackTrace();
            return new AuthenticationStatus(false, PCLoginFailReason.UNKNOWN_REASON, null);
        } finally {
            session.getTransaction().commit();
        }
    }
}
