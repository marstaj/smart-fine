package cz.smartfine.server.business.client.pc;

import cz.smartfine.networklayer.model.pc.PCClientPermission;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.ClientList;
import cz.smartfine.server.business.client.IClientServer;
import cz.smartfine.server.business.client.pc.providers.BasicServiceProvider;
import cz.smartfine.server.business.client.pc.providers.PinProvider;
import cz.smartfine.server.business.client.pc.providers.QueryProvider;
import cz.smartfine.server.networklayer.links.SecuredServerLink;
import cz.smartfine.server.networklayer.networkinterface.SimpleServerNetworkInterface;
import java.util.Date;
import javax.net.ssl.SSLSocket;

/**
 * Třída, která tvoří protistranu PC klientovy.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:26
 */
public class PCClientServer implements IClientServer {

    protected SSLSocket socket;
    protected ClientList clientList;
    protected INetworkInterface networkInterface;
    protected SecuredServerLink link;
    
    protected int badgeNumber;
    protected PCClientPermission permissions;
    protected Date lastContactTime;
    
    protected BasicServiceProvider mainProvider;
    protected PinProvider pinProvider;
    protected QueryProvider queryProvider;

    /**
     * Konstruktor.
     *
     * @param socket Soket na klienta.
     * @param clientList Seznam všech připojených serverů.
     */
    public PCClientServer(SSLSocket socket, ClientList clientList) {
        this.socket = socket;
        this.clientList = clientList;
    }

    /**
     * Ukončí spojení s klientem.
     */
    @Override
    public synchronized void close() {
        if (link != null) {
            link.disconnect();
        }
        clientList.remove(this);
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

    @Override
    public void start() {
        link = new SecuredServerLink(socket); //vytvoří link
        networkInterface = new SimpleServerNetworkInterface(link); //vytvoří k linku rozhraní
        mainProvider = new BasicServiceProvider(networkInterface, this); //vytvoří základní službu
        link.listen(); //spustí příjem dat z klienta
    }

    @Override
    public void registerThisServer() {
        clientList.put(this);
    }

    public synchronized BasicServiceProvider getMainProvider() {
        return mainProvider;
    }

    public synchronized void setMainProvider(BasicServiceProvider mainProvider) {
        this.mainProvider = mainProvider;
    }

    public synchronized PCClientPermission getPermissions() {
        return permissions;
    }

    public synchronized void setPermissions(PCClientPermission permissions) {
        this.permissions = permissions;
    }

    public synchronized PinProvider getPinProvider() {
        return pinProvider;
    }

    public synchronized void setPinProvider(PinProvider pinProvider) {
        this.pinProvider = pinProvider;
    }

    public synchronized QueryProvider getQueryProvider() {
        return queryProvider;
    }

    public synchronized void setQueryProvider(QueryProvider queryProvider) {
        this.queryProvider = queryProvider;
    }
}