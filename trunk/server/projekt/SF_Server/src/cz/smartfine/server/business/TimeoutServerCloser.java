package cz.smartfine.server.business;

import cz.smartfine.server.business.client.ClientList;
import cz.smartfine.server.business.client.IClientServer;
import java.util.Date;
import java.util.List;

/**
 * Třída, periodicky po daném čase uzavírá neaktivní spojení na server.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:28
 */
public class TimeoutServerCloser implements Runnable {

    private ClientList[] clientLists;
    private long timeout;

    public TimeoutServerCloser(ClientList[] clientLists, long timeout) {
        this.clientLists = clientLists;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.currentThread().sleep(timeout); //uspí vlákno na požadovaný čas

                for (ClientList clientList : clientLists) {
                    List<IClientServer> servers = clientList.getServers();

                    long now = (new Date()).getTime();
                    for (IClientServer server : servers) {
                         //pokud je rozdíl mezi současným časem a časem posledního kontaktu větší než timeout, server se ukončí//
                        if (now - server.getLastContactTime().getTime() > timeout) {
                             server.close();
                        }
                    }
                }
            } catch (InterruptedException ex) {
            }
        }
    }
}