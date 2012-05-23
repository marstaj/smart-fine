package cz.smartfine.networklayer.networkinterface;

import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.networklayer.util.DataPackage;
import cz.smartfine.networklayer.util.InterThreadType;

/**
 * Tvoří rozhraní mezi protokoly pro přenos specifických dat a sítí.
 *
 * @author Pavel Brož
 * @version 1.0 
 */
public class SimpleNetworkInterface implements INetworkInterface {

    /**
     * Link pro komunikaci se serverem
     */
    private ILink link;
    /**
     * Datový protokol, který využívá služby této třídy
     */
    private IDataProtocol dataProtocol;
    /**
     * Interní třída, která asynchroně odesílá data
     */
    private NetworkInterfaceSender sender;
    /**
     * Interní třída, která asynchroně zpracovává přijatá data
     */
    private Receiver receiver;
    /**
     * Vlákno pro odesílání dat
     */
    private Thread senderThread;
    /**
     * Vlákno pro příjem dat
     */
    private Thread receiverThread;
    /**
     * Příchozí data
     */
    private InterThreadType<byte[]> in = new InterThreadType<byte[]>();
    /**
     * Odchozí data
     */
    private InterThreadType<DataPackage> out = new InterThreadType<DataPackage>();

    // ================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    @Override
    public void finalize() throws Throwable {
        super.finalize();

        if (senderThread != null && senderThread.isAlive()) {
            senderThread.interrupt();
        }
        if (receiverThread != null && receiverThread.isAlive()) {
            receiverThread.interrupt();
        }
    }

    /**
     * Konstruktor.
     *
     */
    public SimpleNetworkInterface() {
        this(null);
    }

    /**
     * Konstruktor.
     *
     * @param link Link pro odesílání dat.
     */
    public SimpleNetworkInterface(ILink link) {
        this.link = link;

        // vytvoření a nastartování objektu pro příjímání dat v novém vlákně//
        receiver = new Receiver(this.in, this.dataProtocol);
        receiverThread = new Thread(receiver, "receiverThread");
        receiverThread.start();

        if (link != null) {
            link.setOnReceivedDataListener(this); // registrace sebe sama jako posluchače událostí příjmu dat v objektu linku
        }

        // vytvoření a nastartování objektu pro odesílání dat v novém vlákně//
        sender = new NetworkInterfaceSender(this.link, this.out, this);
        senderThread = new Thread(sender, "senderThread");
        senderThread.start();
    }

    // ================================================== GET/SET ==================================================//
    /**
     * Odebere posluchače události příjmu dat.
     *
     * @param dataProtocol Datový protokol poslouchající událost příjmu dat.
     */
    @Override
    public void removeOnReceivedDataListener(IDataProtocol dataProtocol) {
        this.dataProtocol = null;
        this.receiver.setDataProtocol(null);
    }

    /**
     * Přidá posluchače události příjmu dat.
     *
     * @param dataProtocol Datový protokol poslouchající událost příjmu dat.
     */
    @Override
    public void setOnReceivedDataListener(IDataProtocol dataProtocol) {
        this.dataProtocol = dataProtocol;
        this.receiver.setDataProtocol(dataProtocol);
    }

    /**
     * Nastaví link pro připojení na síť.
     *
     * @param link Síťové rozhraní.
     */
    @Override
    public void setLink(ILink link) {
        if (this.link != null) {
            this.link.removeOnReceivedDataListener(this); // odregistrování se v současném linku
        }

        this.link = link;

        if (this.link != null) {
            link.setOnReceivedDataListener(this); // zaregistrování se v novém linku
        }

        sender.setLink(link); // změna linku u senderu
    }

    // ================================================== HANDLERY UDÁLOSTÍ ==================================================//
    /**
     * Handler události příjmu dat.
     *
     * @param receivedData Přijmutá data uložená ve formě bytového pole.
     */
    @Override
    public void onReceivedData(byte[] receivedData) {
        try {
            in.put(receivedData); // vloží data do inter thread objektu, aby si je mohlo vyzvednout vlákno receiveru
        } catch (InterruptedException e) {
        }
    }

    /**
     * Handler pro zpracování události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (dataProtocol != null) {
            dataProtocol.onConnectionTerminated();
        }
    }

    // ================================================== VÝKONNÉ METODY ==================================================//
    /**
     * Posílá data na server.
     *
     * @param dataToSend Data k odeslání.
     * @param sender Datový protokol, který odesílá data.
     */
    @Override
    public void sendData(byte[] dataToSend, IDataProtocol sender) {
        try {
            out.put(new DataPackage(dataToSend, sender)); // vloží data do inter thread objektu, aby si je mohlo vyzvednout vlákno senderu
        } catch (InterruptedException e) {
        }
    }

    // ================================================== INTERNÍ TŘÍDY ==================================================//
    /**
     * Třída zajišťující příjem dat v jiném vlákně
     *
     * @author Pavel Brož
     * @version 1.0
     */
    private class Receiver implements Runnable {

        private InterThreadType<byte[]> in;
        private IDataProtocol dataProtocol;

        /**
         *
         * @param in Objekt pro předávání zpráv
         * @param dataProtocol Datový protokol, který má být informován o přijetí zprávy.
         */
        public Receiver(InterThreadType<byte[]> in, IDataProtocol dataProtocol) {
            super();
            this.in = in;
            this.dataProtocol = dataProtocol;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    byte[] receivedData = in.get(); // načte přijatá data
                    byte[] protocolData = new byte[receivedData.length - BaseProtocolConstants.HEADER_SIZE]; // vytvoří pole na tělo přijaté zprávy

                    // z přijatých dat zkopíruje tělo zprávy (hlavičku vynechá)
                    System.arraycopy(receivedData, BaseProtocolConstants.HEADER_SIZE, protocolData, 0, receivedData.length - BaseProtocolConstants.HEADER_SIZE);

                    if (dataProtocol != null) {
                        dataProtocol.onReceivedData(protocolData); // pošle přijatá data datovému protokolu
                    }
                }
            } catch (InterruptedException e) {
                // není potřeba dělat nic
            }
        }

        public synchronized void setDataProtocol(IDataProtocol dataProtocol) {
            this.dataProtocol = dataProtocol;
        }
    }
}