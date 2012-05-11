package cz.smartfine.server.business.client;

import java.util.ArrayList;

/**
 * Třída pro správu všech připojených serverů.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:25
 */
public class ClientList {

    protected ArrayList<IClientServer> servers = new ArrayList<IClientServer>();

    /**
     * Ukončí všechna spojení s klienty.
     */
    public synchronized void closeAll() {
        for (IClientServer server : servers) {
            server.close();
        }
    }

    /**
     * Zjistí zda, je připojen policista s daným služebním číslem a vrátí objekt serveru.
     *
     * @param badgeNumber Služební číslo policisty připojeného k serveru.
     */
    public synchronized IClientServer containBadgeNumber(int badgeNumber) {
        for (IClientServer server : servers) {
            if (server.getBadgeNumber() == badgeNumber) {
                return server;
            }
        }
        return null;
    }

    /**
     * Vloží server pro klienta mezi připojené servery.
     *
     * @param clientServer Objekt serveru, který tvoří protistranu klientovy.
     */
    public synchronized void put(IClientServer clientServer) {
        if (!servers.contains(clientServer)) {
            servers.add(clientServer);
        }
    }

    /**
     * Odebere server pro klienta ze seznamu připojených serverů.
     *
     * @param clientServer Objekt serveru, který tvoří protistranu klientovy.
     */
    public synchronized void remove(IClientServer clientServer) {
        if (clientServer != null) {
            servers.remove(clientServer);
        }
    }

    /**
     * Získá seznam serverů.
     *
     * @return Seznam serverů.
     */
    public synchronized ArrayList<IClientServer> getServers() {
        return servers;
    }
}