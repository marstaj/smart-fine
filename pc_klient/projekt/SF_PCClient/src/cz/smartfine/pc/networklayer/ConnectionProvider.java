package cz.smartfine.pc.networklayer;

import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import java.io.IOException;

/**
 * Zprostředkovává přístup k základním síťovým službám.
 *
 * @author Pavel Brož
 * @version 1.0 @created 14-4-2012 18:48:46
 */
public class ConnectionProvider {

    /**
     * Rozhraní pro NetworkInterface.
     */
    private ILink mLink;
    /**
     * Síťové rozhraní pro datové protokoly.
     */
    private INetworkInterface mNetworkInterface;

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    /**
     * Konstruktor.
     *
     * @param link Objekt implementující ILink pro transfer dat.
     * @param networkInterface Objekt reprezentující rozhraní, se kterým mohou komunikovat třídy datových protokolů.
     */
    public ConnectionProvider(ILink link, INetworkInterface networkInterface) {
        this.mLink = link;
        this.mNetworkInterface = networkInterface;
        this.mNetworkInterface.setLink(link);
    }

    //================================================== GET/SET ==================================================//
    
    /**
     * Vrací rozhraní pro transfer dat přes síť.
     */
    public ILink getLink() {
        return mLink;
    }

    /**
     * Vrací základní rozhraní pro komunikaci datových protokolů se serverem.
     */
    public INetworkInterface getNetworkInterface() {
        return mNetworkInterface;
    }

    //================================================== VÝKONNÉ METODY ==================================================//
    
    /**
     * Ukončí spojení na server.
     */
    public void disconnect() {
        mLink.disconnect();
    }

    /**
     * Zjišťuje, zda je vytvořen a připojen socket.
     */
    public boolean isConnected() {
        return mLink.isConnected();
    }

    /**
     * Vytvoří spojení na server.
     */
    public boolean connect() {
        try {
            mLink.connect();
            return mLink.isConnected();
        } catch (IOException e) {
            return false;
        }
    }
}