package cz.smartfine.server.business.client.pc;

import cz.smartfine.server.business.client.IClientServer;
import java.util.Date;

/**
 * Třída, která tvoří protistranu PC klientovy.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:26
 */
public class PCClientServer implements IClientServer {

    public PCClientServer() {
    }

    /**
     * Ukončí spojení s klientem.
     */
    public void close() {
    }

    /**
     * Vrací čas posledního kontaktu s klientem.
     */
    public Date getLastContactTime() {
        return null;
    }

    /**
     * Vrací služební číslo policisty, který je připojen k serveru.
     */
    public int getBadgeNumber() {
        return 0;
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBadgeNumber(int badgeNumber) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLastContactTime(Date lastContactTime) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}