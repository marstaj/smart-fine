package cz.smartfine.server.business.client.mobile;

import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.IClientServer;
import cz.smartfine.server.business.client.mobile.providers.*;
import cz.smartfine.server.networklayer.links.SecuredServerLink;
import cz.smartfine.server.networklayer.networkinterface.SimpleServerNetworkInterface;
import java.util.Date;
import javax.net.ssl.SSLSocket;

/**
 * Třída, která tvoří protistranu mobilnímu klientovy.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:26
 */
public class MobileClientServer implements IClientServer {

    protected SSLSocket socket;
    protected MobileClientList mobileClientList;
    protected INetworkInterface networkInterface;
    protected SecuredServerLink link;
    
    protected int badgeNumber;
    protected String imei;
    protected Date lastContactTime;
    
    protected BasicServiceProvider mainProvider;
    protected GeoDataProvider geoProvider;
    protected SMSParkingProvider smsParkingProvider;
    protected SPCCheckProvider spcCheckProvider;
    protected TicketSyncProvider ticketProvider;

    /**
     * Konstruktor.
     *
     * @param socket Soket na klienta.
     * @param mobileClientList Seznam všech připojených serverů.
     */
    public MobileClientServer(SSLSocket socket, MobileClientList mobileClientList) {
        this.socket = socket;
        this.mobileClientList = mobileClientList;
    }

    /**
     * Ukončí spojení s klientem.
     */
    @Override
    public synchronized void close() {
        if (link != null) {
            link.disconnect();
        }
        mobileClientList.remove(this);
    }

    /**
     * Vrací čas posledního kontaktu s klientem.
     */
    @Override
    public synchronized Date getLastContactTime() {
        return this.lastContactTime;
    }

    @Override
    public synchronized void setLastContactTime(Date lastContactTime) {
        this.lastContactTime = lastContactTime;
    }

    /**
     * Vrací služební číslo policisty, který je připojen k serveru.
     */
    @Override
    public synchronized int getBadgeNumber() {
        return this.badgeNumber;
    }

    @Override
    public synchronized void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    /**
     * Získá IMEI připojeného zařízení.
     *
     * @return IMEI připojeného zařízení.
     */
    public synchronized String getImei() {
        return imei;
    }

    /**
     * Nastaví IMEI připojeného zařízení.
     *
     * @param imei IMEI připojeného zařízení.
     */
    public synchronized void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public void start() {
        link = new SecuredServerLink(socket); //vytvoří link
        networkInterface = new SimpleServerNetworkInterface(link); //vytvoří k linku rozhraní
        mainProvider = new BasicServiceProvider(networkInterface, this); //vytvoří základní službu
        link.listen(); //spustí příjem dat z klienta
    }

    /**
     * Ukončí předchozí server, pokud takový existuje.
     */
    public void closePreviousServer() {
        mobileClientList.remove(mobileClientList.containIMEI(this.getImei())); //odebere předchozí instanci serveru, která zpracovávala stejného klienta (pokud existuje)
    }

    @Override
    public void registerThisServer() {
        mobileClientList.put(this);
    }

    public synchronized GeoDataProvider getGeoProvider() {
        return geoProvider;
    }

    public synchronized void setGeoProvider(GeoDataProvider geoProvider) {
        this.geoProvider = geoProvider;
    }

    public synchronized BasicServiceProvider getMainProvider() {
        return mainProvider;
    }

    public synchronized void setMainProvider(BasicServiceProvider mainProvider) {
        this.mainProvider = mainProvider;
    }

    public synchronized SMSParkingProvider getSmsParkingProvider() {
        return smsParkingProvider;
    }

    public synchronized void setSmsParkingProvider(SMSParkingProvider smsParkingProvider) {
        this.smsParkingProvider = smsParkingProvider;
    }

    public synchronized SPCCheckProvider getSpcCheckProvider() {
        return spcCheckProvider;
    }

    public synchronized void setSpcCheckProvider(SPCCheckProvider spcCheckProvider) {
        this.spcCheckProvider = spcCheckProvider;
    }

    public synchronized TicketSyncProvider getTicketProvider() {
        return ticketProvider;
    }

    public synchronized void setTicketProvider(TicketSyncProvider ticketProvider) {
        this.ticketProvider = ticketProvider;
    }
}