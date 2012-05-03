package cz.smartfine.server.business.client;

import java.util.Date;

/**
 * Rozhraní třídy serveru, který zprostředkovává spojení s klientem.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:25
 */
public interface IClientServer {

    /**
     * Spustí server.
     */
    public void start();
    /**
     * Ukončí spojení s klientem.
     */
    public void close();

    /**
     * Vrací služební číslo policisty, který je připojen k serveru.
     */
    public int getBadgeNumber();

    /**
     * Nastavuje služební číslo policisty, který je připojen k serveru.
     */
    public void setBadgeNumber(int badgeNumber);
    
    /**
     * Vrací čas posledního kontaktu s klientem.
     */
    public Date getLastContactTime();
    /**
     * Nastavuje čas posledního kontaktu s klientem.
     */
    public void setLastContactTime(Date lastContactTime);
}