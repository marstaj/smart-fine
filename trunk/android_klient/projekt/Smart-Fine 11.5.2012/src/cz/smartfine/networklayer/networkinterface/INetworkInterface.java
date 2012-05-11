package cz.smartfine.networklayer.networkinterface;

import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataReceiverListener;
import cz.smartfine.networklayer.links.ILink;

/**
 * Představuje základní rozhraní pro přenost dat mezi datovými protokoly a objektem linku.
 *
 * @author Pavel Brož
 * @version 1.0 
 */
public interface INetworkInterface extends IDataReceiverListener {

    /**
     * Handler pro zpracování události ukončení spojení.
     */
    public void onConnectionTerminated();

    /**
     * Odebere posluchače události příjmu dat.
     *
     * @param dataProtocol Datový protokol poslouchající událost příjmu dat.
     */
    public void removeOnReceivedDataListener(IDataProtocol dataProtocol);

    /**
     * Posílá data na síť.
     *
     * @param dataToSend Data k odeslání.
     * @param sender	Datový protokol, který odesílá data.
     */
    public void sendData(byte[] dataToSend, IDataProtocol sender);

    /**
     * Přidá posluchače události příjmu dat.
     *
     * @param dataProtocol Datový protokol poslouchající událost příjmu dat.
     */
    public void setOnReceivedDataListener(IDataProtocol dataProtocol);

    /**
     * Nastaví link pro připojení na síť.
     *
     * @param link Síťové rozhraní.
     */
    public void setLink(ILink link);
}