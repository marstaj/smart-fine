package cz.smartfine.server.business.client.mobile;

import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.ClientList;
import cz.smartfine.server.business.client.IClientServer;
import cz.smartfine.server.business.client.mobile.providers.MainProvider;
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

    private SSLSocket socket;
    private MobileClientList mobileClientList;
    private INetworkInterface networkInterface;
    private SecuredServerLink link;
    private MainProvider mainProvider;
    private int badgeNumber;
    private String imei;
    private Date lastContactTime;

    public MobileClientServer(SSLSocket socket, MobileClientList mobileClientList) {
        this.socket = socket;
        this.mobileClientList = mobileClientList;
    }

    @Override
    public void finalize() throws Throwable {
    }

    /**
     * Ukončí spojení s klientem.
     */
    @Override
    public void close() {
        
    }

    /**
     * Vrací čas posledního kontaktu s klientem.
     */
    @Override
    public Date getLastContactTime() {
        return this.lastContactTime;
    }

    @Override
    public void setLastContactTime(Date lastContactTime) {
        this.lastContactTime = lastContactTime;
    }

    /**
     * Vrací služební číslo policisty, který je připojen k serveru.
     */
    @Override
    public int getBadgeNumber() {
        return this.badgeNumber;
    }

    @Override
    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    @Override
    public void start() {
        
        link = new SecuredServerLink(socket);
        networkInterface = new SimpleServerNetworkInterface(link);
        mainProvider = new MainProvider(networkInterface, this);
        link.listen();
    }
}