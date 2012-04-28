package cz.smartfine.server;

import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.server.business.MobileClientAcceptor;
import cz.smartfine.server.business.PCClientAcceptor;
import cz.smartfine.server.business.TimeoutCloser;
import cz.smartfine.server.business.client.ClientList;
import cz.smartfine.server.business.client.mobile.MobileClientList;

/**
 * Hlavní třída aplikace Smart-Fine serveru.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:27
 */
public class SFServer {

    public static final int MOBILE_CLIENT_SERVER_PORT = 25000;
    public static final int PC_CLIENT_SERVER_PORT = 25100;
    public static final int ADMIN_SERVER_PORT = 25200;
    /**
     * Třída přijímající síťová připojení na server od mobilních klientů.
     */
    private static MobileClientAcceptor mobileClientAcceptor;
    private static Thread mobileClientAcceptorThread;
    /**
     * Třída se seznamem připojených mobilních klientů.
     */
    private static MobileClientList mobileClientList = new MobileClientList();
    /**
     * Třída přijímající síťová připojení na server od PC klientů.
     */
    private static PCClientAcceptor pcClientAcceptor;
    private static Thread pcClientAcceptorThread;
    /**
     * Třída se seznamem připojených PC klientů.
     */
    private static ClientList pcClientList = new ClientList();
    /**
     * Třída zajišťující zavření neaktivních síťových spojení.
     */
    private static TimeoutCloser timeoutCloser;
    private static Thread timeoutCloserThread;
    /**
     * Objekt nedovolí serveru skončit, dokud se do něj nezapíše.
     */
    private static InterThreadType<Boolean> run = new InterThreadType<Boolean>();

    public void finalize() throws Throwable {
    }

    public static void main(String[] args) {

        mobileClientAcceptor = new MobileClientAcceptor(MOBILE_CLIENT_SERVER_PORT, mobileClientList);
        mobileClientAcceptorThread = new Thread(mobileClientAcceptor, "mobileClientAcceptor");
        mobileClientAcceptorThread.start();

        /*
         * pcClientAcceptor = new PCClientAcceptor(); pcClientAcceptorThread = new Thread(pcClientAcceptor, "pcClientAcceptor"); pcClientAcceptorThread.start();
         *
         * timeoutCloser = new TimeoutCloser(); timeoutCloserThread = new Thread(timeoutCloser, "timeoutCloser"); timeoutCloserThread.start();
         */

        try {
            run.get();
            //TODO: UKONČIT VLÁKNA
        } catch (InterruptedException ex) {
        }
    }
}