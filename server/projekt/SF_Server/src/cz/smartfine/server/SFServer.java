package cz.smartfine.server;

import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.server.business.MobileClientAcceptor;
import cz.smartfine.server.business.PCClientAcceptor;
import cz.smartfine.server.business.TimeoutServerCloser;
import cz.smartfine.server.business.client.ClientList;
import cz.smartfine.server.business.client.mobile.MobileClientList;

/**
 * Hlavní třída aplikace Smart-Fine serveru.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:27
 */
public class SFServer {

    private static SFServer server;
    
    public static final int MOBILE_CLIENT_SERVER_PORT = 25000;
    public static final int PC_CLIENT_SERVER_PORT = 25100;
    public static final int ADMIN_SERVER_PORT = 25200;
    
    /**
     * Timeout, po kterém se ukončují neaktivní servery.
     */
    public static final long SERVER_TIMEOUT = 36000000; // 36 000 000 ms = 10 h TODO: ČAS
    /**
     * Po jaké době se má spouštět ukončování serverů.
     */
    public static final long EXECUTE_TIMEOUT = 36000000; // 36 000 000 ms = 10 h TODO: ČAS
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
    private static TimeoutServerCloser timeoutCloser;
    private static Thread timeoutCloserThread;
    /**
     * Objekt nedovolí serveru skončit, dokud se do něj nezapíše.
     */
    private static InterThreadType<Boolean> run = new InterThreadType<Boolean>();

    private SFServer() {
        
    }

    /**
     * Spustí server
     */
    public void start(){
        mobileClientAcceptor = new MobileClientAcceptor(MOBILE_CLIENT_SERVER_PORT, mobileClientList);
        mobileClientAcceptorThread = new Thread(mobileClientAcceptor, "mobileClientAcceptor");
        mobileClientAcceptorThread.start();

        /*
         * pcClientAcceptor = new PCClientAcceptor(); pcClientAcceptorThread = new Thread(pcClientAcceptor, "pcClientAcceptor"); pcClientAcceptorThread.start();
         */
        timeoutCloser = new TimeoutServerCloser(new ClientList[] {mobileClientList, pcClientList}, SERVER_TIMEOUT, EXECUTE_TIMEOUT);
        timeoutCloserThread = new Thread(timeoutCloser, "timeoutCloser");
        timeoutCloserThread.start();

        try {
            run.get();
            //TODO: UKONČIT VLÁKNA
        } catch (InterruptedException ex) {
        }
    }
    
    public static SFServer getServer(){
        return server;
    }
    
    public static void main(String[] args) {
        new SFServer().start();
    }
}