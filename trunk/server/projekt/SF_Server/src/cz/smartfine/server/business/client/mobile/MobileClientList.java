package cz.smartfine.server.business.client.mobile;

import cz.smartfine.server.business.client.ClientList;
import cz.smartfine.server.business.client.IClientServer;

/**
 * Rošiřuje třídu ClientList a přidává možnost hledat servery podle čísla IMEI.
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:26
 */
public class MobileClientList extends ClientList {

    /**
     * Zjistí zda je připojeno mobilní zařízení s daným IMEI a vrátí jeho objekt serveru.
     *
     * @param imei IMEI mobilního zařízení.
     */
    public IClientServer containIMEI(String imei) {
        for (IClientServer server : servers) {
            //ověří, jestli je typu MobileClientServer
            if (server instanceof MobileClientServer){
                //přetypuje a porovná imei//
                if (((MobileClientServer) server).getImei().equals(imei)){
                    return server;
                }
            }
        }
        return null;
    }
}