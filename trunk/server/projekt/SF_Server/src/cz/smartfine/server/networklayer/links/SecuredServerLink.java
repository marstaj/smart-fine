package cz.smartfine.server.networklayer.links;

import cz.smartfine.networklayer.links.Receiver;
import cz.smartfine.networklayer.links.SecuredLink;
import java.io.IOException;
import javax.net.ssl.SSLSocket;

/**
 * Třída pro zabezpečenou komunikaci.
 *
 * @author Pavel Brož
 * @version 1.0 
 * @created 27-4-2012 17:00:26
 */
public class SecuredServerLink extends SecuredLink {

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    
    /**
     * Konstruktor.
     *
     * @param socket Serverový soket pro spojení s klientem.
     */
    public SecuredServerLink(SSLSocket socket) {
        super(socket);
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }
    
    //================================================== VÝKONNÉ METODY ==================================================//

    /**
     * Připojí se k serveru.
     *
     * @exception IOException Problém při vytváření socketu.
     */
    @Override
    public void connect() throws IOException {
        //serverový socket se nepřipojuje
    }


    /**
     * Začne naslouchat na soketu.
     */
    @Override
    public void listen() {
        receiver = new Receiver(this.in, this.networkInterface, this);
        receiverThread = new Thread(receiver, "linkReceiverThread");
        receiverThread.start();
    }
}