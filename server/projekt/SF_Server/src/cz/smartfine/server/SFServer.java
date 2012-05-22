package cz.smartfine.server;

import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.server.business.MobileClientAcceptor;
import cz.smartfine.server.business.PCClientAcceptor;
import cz.smartfine.server.business.TimeoutServerCloser;
import cz.smartfine.server.business.client.ClientList;
import cz.smartfine.server.business.client.mobile.MobileClientList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Hlavní třída aplikace Smart-Fine serveru.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:27
 */
public class SFServer {

    private static SFServer server;
    public final int MOBILE_CLIENT_SERVER_PORT = 25000;
    public final int PC_CLIENT_SERVER_PORT = 25100;
    /**
     * Timeout, po kterém se ukončují neaktivní servery.
     */
    public final long SERVER_TIMEOUT = 36000000; // 36 000 000 ms = 10 h
    /**
     * Po jaké době se má spouštět ukončování serverů.
     */
    public final long EXECUTE_TIMEOUT = 36000000; // 36 000 000 ms = 10 h
    /**
     * Třída přijímající síťová připojení na server od mobilních klientů.
     */
    private MobileClientAcceptor mobileClientAcceptor;
    private Thread mobileClientAcceptorThread;
    /**
     * Třída se seznamem připojených mobilních klientů.
     */
    private MobileClientList mobileClientList = new MobileClientList();
    /**
     * Třída přijímající síťová připojení na server od PC klientů.
     */
    private PCClientAcceptor pcClientAcceptor;
    private Thread pcClientAcceptorThread;
    /**
     * Třída se seznamem připojených PC klientů.
     */
    private ClientList pcClientList = new ClientList();
    /**
     * Třída zajišťující zavření neaktivních síťových spojení.
     */
    private TimeoutServerCloser timeoutCloser;
    private Thread timeoutCloserThread;
    /**
     * Objekt nedovolí serveru skončit, dokud se do něj nezapíše.
     */
    private InterThreadType<Boolean> run = new InterThreadType<Boolean>();

    private SFServer() {
    }

    /**
     * Spustí server
     */
    public void start() {
        BufferedReader br = null;
        String kspasswd = null;
        try {
            br = new BufferedReader(new FileReader("sfserver.passwd"));
            kspasswd = br.readLine();
            if (kspasswd == null) {
                System.err.println("SERVER: NEPODARILO SE NACIST HESLO KE KEYSTORE");
                return;
            }
        } catch (FileNotFoundException ex) {
            System.err.println("SERVER: NEBYL NALEZEN SOUBOR S HESLEM KE KEYSTORE");
            return;
        } catch (IOException ex ){
            System.err.println("SERVER: NEPODARILO SE NACIST HESLO KE KEYSTORE");
            return;
        } finally {
            try {
                br.close();
            } catch (Exception ex) {}
        }
        
        System.setProperty("javax.net.ssl.keyStore", "sfserver.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", kspasswd);
    
        //*********************************************************************************************//
        
        mobileClientAcceptor = new MobileClientAcceptor(MOBILE_CLIENT_SERVER_PORT, mobileClientList);
        mobileClientAcceptorThread = new Thread(mobileClientAcceptor, "mobileClientAcceptor");
        mobileClientAcceptorThread.start();

        pcClientAcceptor = new PCClientAcceptor(PC_CLIENT_SERVER_PORT, pcClientList);
        pcClientAcceptorThread = new Thread(pcClientAcceptor, "pcClientAcceptor");
        pcClientAcceptorThread.start();

        timeoutCloser = new TimeoutServerCloser(new ClientList[]{mobileClientList, pcClientList}, SERVER_TIMEOUT, EXECUTE_TIMEOUT);
        timeoutCloserThread = new Thread(timeoutCloser, "timeoutCloser");
        timeoutCloserThread.start();

        Runtime.getRuntime().addShutdownHook(
                new Thread() {

                    @Override
                    public void run() {
                        mobileClientAcceptor.stopMobileClientAcceptor();
                        pcClientAcceptor.stopMobileClientAcceptor();
                        timeoutCloserThread.interrupt();
                        mobileClientList.closeAll();
                        pcClientList.closeAll();
                    }
                });
    }

    public static SFServer getServer() {
        return server;
    }

    public static void main(String[] args) {
        new SFServer().start();
    }
}
