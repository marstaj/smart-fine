package cz.smartfine.server.business.client.pc.providers;

import cz.smartfine.networklayer.model.pc.PINChangeFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.HibernateUtil;
import cz.smartfine.server.business.client.model.PolicemanLoginDB;
import cz.smartfine.server.business.client.pc.PCClientServer;
import cz.smartfine.server.business.client.pc.providers.listeners.IServerPinProtocolListener;
import cz.smartfine.server.networklayer.dataprotocols.pc.ServerPinProtocol;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Zprostředkovatel změny PINu.
 *
 * @author Pavel Brož
 */
public class PinProvider implements IServerPinProtocolListener {

    /**
     * Nejvyšší hodnota PINu (bez horní hranice).
     */
    private final int MAX_PIN_VALUE = 100000;
    /**
     * Odkaz na server.
     */
    private PCClientServer clientServer;
    /**
     * Datový protokol pro přenos dat.
     */
    private ServerPinProtocol protocol;
    /**
     * Nový PIN.
     */
    private int newPIN = -1;
    private int bn = -1;
    private int oldPIN = -1;

    public PinProvider(INetworkInterface networkInterface, PCClientServer clientServer) {
        this.clientServer = clientServer;
        this.protocol = new ServerPinProtocol(networkInterface, this);
    }

    @Override
    public void onNewPinRequest(int badgeNumber, int pin) {
        if (clientServer.getPermissions().isPermChngPin()) {
            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();

                //KONTROLA EXISTENCE SLUŽEBNÍHO ČÍSLA A SPRÁVNOSTI PINU//

                //vybere záznamy o policistovy, který se chce změnit PIN//
                Query polQuery = session.getNamedQuery("cz.smartfine.getpolicemanlogin.by.bn.pin");
                polQuery.setParameter("bn", badgeNumber); //nastaví služební číslo
                polQuery.setParameter("pin", pin); //nastaví PIN

                List polList = polQuery.list(); //spustí dotaz na DB
                PolicemanLoginDB pol;

                //dotaz musí vrátit nějaká data, jinak policista neexistuje//
                if (polList.size() > 0) {
                    pol = ((List<PolicemanLoginDB>) polList).get(0); //první záznam

                    oldPIN = pin;
                    bn = badgeNumber;

                    newPIN = generateNewPIN(); //vygeneruje nový PIN

                    protocol.pinChanged(newPIN); //odešle nový PIN
                } else { //žádný záznam
                    protocol.errorDuringChangingPIN(PINChangeFailReason.WRONG_BADGE_NUMBER_OR_PIN);
                }

            } catch (HibernateException e) {
                //e.printStackTrace();
                protocol.errorDuringChangingPIN(PINChangeFailReason.UNKNOWN_REASON);
            } finally {
                session.getTransaction().commit();
            }
        } else {
            clientServer.close();
        }
    }

    @Override
    public void onConnectionTerminated() {
    }

    @Override
    public void onMessageSent() {
        //teprve až když je zpráva s novým PINem úspěšně odeslána, je nový PIN uložen do DB//
        if (newPIN > 0) {
            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();

                //vybere záznamy o policistovy, který se chce autentizovat//
                Query polQuery = session.getNamedQuery("cz.smartfine.getpolicemanlogin.by.bn.pin");
                polQuery.setParameter("bn", bn); //nastaví služební číslo
                polQuery.setParameter("pin", oldPIN); //nastaví starý PIN

                List polList = polQuery.list(); //spustí dotaz na DB
                PolicemanLoginDB pol;

                //dotaz musí vrátit nějaká data, jinak policista neexistuje//
                if (polList.size() > 0) {
                    pol = ((List<PolicemanLoginDB>) polList).get(0); //první záznam
                    pol.setPin(newPIN); //změna PINU
                }

            } catch (HibernateException e) {
                //e.printStackTrace();
            } finally {
                session.getTransaction().commit();
            }
        }
    }

    /**
     * Vygeneruje nový PIN.
     *
     * @return Nový PIN.
     */
    protected int generateNewPIN() {
        Random rnd = new Random((new Date()).getTime() - bn * oldPIN);
        return rnd.nextInt(MAX_PIN_VALUE);
    }
}
