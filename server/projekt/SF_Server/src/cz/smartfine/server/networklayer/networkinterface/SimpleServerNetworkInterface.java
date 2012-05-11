package cz.smartfine.server.networklayer.networkinterface;

import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.networklayer.networkinterface.BaseProtocolConstants;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.networkinterface.NetworkInterfaceSender;
import cz.smartfine.networklayer.util.DataPackage;
import cz.smartfine.networklayer.util.InterThreadType;
import java.util.ArrayList;

/**
 * Tvoří rozhraní mezi protokoly pro přenos specifických dat a sítí.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:27
 */
public class SimpleServerNetworkInterface implements INetworkInterface {

    /**
     * Link pro komunikaci se serverem
     */
    private ILink link;
    /**
     * Datový protokol, který využívá služby této třídy
     */
    private ArrayList<IDataProtocol> dataProtocols = new ArrayList<IDataProtocol>();
    /**
     * Interní třída, která asynchroně odesílá data
     */
    private NetworkInterfaceSender sender;
    /**
     * Vlákno pro odesílání dat
     */
    private Thread senderThread;
    /**
     * Odchozí data
     */
    private InterThreadType<DataPackage> out = new InterThreadType<DataPackage>();

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    @Override
    public void finalize() throws Throwable {
        super.finalize();
        if (senderThread != null && senderThread.isAlive()) {
            senderThread.interrupt();
        }
    }

    /**
     * Konstruktor.
     */
    public SimpleServerNetworkInterface() {
        this(null);
    }

    /**
     * Konstruktor.
     *
     * @param link Link pro odesílání dat.
     */
    public SimpleServerNetworkInterface(ILink link) {
        this.link = link;

        if (link != null) {
            link.setOnReceivedDataListener(this); //registrace sebe sama jako posluchače událostí příjmu dat v objektu linku
        }

        //vytvoření a nastartování objektu pro odesílání dat v novém vlákně//
        sender = new NetworkInterfaceSender(this.link, this.out, this);
        senderThread = new Thread(sender, "senderThread");
        senderThread.start();
    }

    //================================================== GET/SET ==================================================//
    /**
     * Odebere posluchače události příjmu dat.
     *
     * @param dataProtocol Datový protokol poslouchající událost příjmu dat.
     */
    @Override
    public void removeOnReceivedDataListener(IDataProtocol dataProtocol) {
        if (dataProtocol != null) {
            this.dataProtocols.remove(dataProtocol);
        }
    }

    /**
     * Přidá posluchače události příjmu dat.
     *
     * @param dataProtocol Datový protokol poslouchající událost příjmu dat.
     */
    @Override
    public void setOnReceivedDataListener(IDataProtocol dataProtocol) {
        if (dataProtocol != null && !this.dataProtocols.contains(dataProtocol)) {
            this.dataProtocols.add(dataProtocol);
        }
    }

    /**
     * Nastaví link pro připojení na síť.
     *
     * @param link Síťové rozhraní.
     */
    @Override
    public void setLink(ILink link) {
        if (this.link != null) {
            this.link.removeOnReceivedDataListener(this); //odregistrování se v současném linku
        }

        this.link = link;

        if (this.link != null) {
            link.setOnReceivedDataListener(this); //zaregistrování se v novém linku
        }

        sender.setLink(link); //změna linku u senderu
    }

    //================================================== HANDLERY UDÁLOSTÍ ==================================================//
    /**
     * Handler události příjmu dat.
     *
     * @param receivedData Přijmutá data uložená ve formě bytového pole.
     */
    @Override
    public void onReceivedData(byte[] receivedData) {
        byte[] protocolData = new byte[receivedData.length - BaseProtocolConstants.HEADER_SIZE]; //vytvoří pole na tělo přijaté zprávy

        //z přijatých dat zkopíruje tělo zprávy (hlavičku vynechá)
        System.arraycopy(receivedData, BaseProtocolConstants.HEADER_SIZE, protocolData, 0, receivedData.length - BaseProtocolConstants.HEADER_SIZE);

        for (IDataProtocol dataProtocol : this.dataProtocols) {
            dataProtocol.onReceivedData(protocolData); //pošle přijatá data datovému protokolu
        }
    }

    /**
     * Handler pro zpracování události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        for (IDataProtocol dataProtocol : this.dataProtocols) {
            dataProtocol.onConnectionTerminated();
        }
    }

    //================================================== VÝKONNÉ METODY ==================================================//
    /**
     * Posílá data na server.
     *
     * @param dataToSend Data k odeslání.
     * @param sender	Datový protokol, který odesílá data.
     */
    @Override
    public void sendData(byte[] dataToSend, IDataProtocol sender) {
        try {
            out.put(new DataPackage(dataToSend, sender)); //vloží data do inter thread objektu, aby si je mohlo vyzvednout vlákno senderu
        } catch (InterruptedException e) {
        }
    }
}