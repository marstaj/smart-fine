package cz.smartfine.server.business;

import cz.smartfine.server.business.client.ClientList;
import cz.smartfine.server.business.client.pc.PCClientServer;
import java.io.IOException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * Třída, která přijímá připojení od PC klientů.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:26
 */
public class PCClientAcceptor implements Runnable {

    private ClientList pcClientList;
    //private SSLContext context;
    private SSLServerSocketFactory sslServerSocketFactory;
    private SSLServerSocket sslServerSocket = null;
    private final int port;

    public PCClientAcceptor(int port, ClientList pcClientList) {
        this.pcClientList = pcClientList;
        this.port = port;
    }

    @Override
    public void run() {
        sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        try {
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
        } catch (IOException ex) {
            System.err.println("SERVER: PC KLIENTSKY SERVER NEMUZE NASLOUCHAT NA PORTU: " + port);
            return;
        }
        System.out.println("SERVER: PC KLIENTSKY SERVER SPUSTEN");
        try {
            SSLSocket clientSocket;
            //smyčka pro příjem klientů//
            while (true) {
                clientSocket = (SSLSocket) sslServerSocket.accept(); //přijímá klientské sokety

                PCClientServer pcs = new PCClientServer(clientSocket, pcClientList); //vytvoří nový server
                pcs.start(); //spustí server
            }
        } catch (IOException e) {
            //není potřeba nic dělat
        } finally {
            if (sslServerSocket != null && !sslServerSocket.isClosed()) {
                try {
                    sslServerSocket.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * Ukončí naslouchání serveru.
     */
    public void stopMobileClientAcceptor() {
        if (sslServerSocket != null && !sslServerSocket.isClosed()) {
            try {
                sslServerSocket.close();
            } catch (IOException ex) {
            }
        }
    }
}