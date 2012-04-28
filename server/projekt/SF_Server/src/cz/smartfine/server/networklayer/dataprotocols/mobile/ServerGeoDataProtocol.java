package cz.smartfine.server.networklayer.dataprotocols.mobile;

import cz.smartfine.android.model.Waypoint;
import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerGeoDataProtocolListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Představuje třídu protokolu pro přenost geolokačních dat.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:25
 */
public class ServerGeoDataProtocol implements IDataProtocol {

    /**
     * Posluchač událostí z této třídy.
     */
    private IServerGeoDataProtocolListener geoDataProtocolListener;
    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Interní třída, která asynchroně zpracovává přijaté zprávy a volá metody posluchače.
     */
    private ServerGeoDataProtocol.MessageProcessor processor;
    /**
     * Vlákno pro zpracování zpráv.
     */
    private Thread processorThread;
    /**
     * Příchozí data.
     */
    private InterThreadType<byte[]> in = new InterThreadType<byte[]>();

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    
    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     */
    public ServerGeoDataProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenos dat.
     * @param geoDataProtocolListener Posluchač událostí z této třídy.
     */
    public ServerGeoDataProtocol(INetworkInterface networkInterface, IServerGeoDataProtocolListener geoDataProtocolListener) {
        this.networkInterface = networkInterface;
        this.geoDataProtocolListener = geoDataProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač

        //vytvoření a nastartování objektu pro zpracování dat v novém vlákně//
        processor = new ServerGeoDataProtocol.MessageProcessor(this.in, this.geoDataProtocolListener);
        processorThread = new Thread(processor, "geoProcessorThread");
        processorThread.start();
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        if (processorThread != null && processorThread.isAlive()) {
            processorThread.interrupt();
        }
    }

    //================================================== GET/SET ==================================================//
    
    /**
     * Odebere posluchače událostí protokolu pro odesílání geolokačních dat.
     *
     * @param geoDataProtocolListener Posluchač událostí z protokolu pro odesílání geolokačních dat.
     */
    public void removeServerGeoDataProtocolListener(IServerGeoDataProtocolListener geoDataProtocolListener) {
        this.geoDataProtocolListener = null;
        this.processor.setProtocolListener(null);
    }

    /**
     * Přidá posluchače událostí protokolu pro odesílání geolokačních dat.
     *
     * @param geoDataProtocolListener Posluchač událostí z protokolu pro odesílání geolokačních dat.
     */
    public void setServerGeoDataProtocolListener(IServerGeoDataProtocolListener geoDataProtocolListener) {
        this.geoDataProtocolListener = geoDataProtocolListener;
        this.processor.setProtocolListener(geoDataProtocolListener);
    }

    //================================================== HANDLERY UDÁLOSTÍ ==================================================//
    
    /**
     * Handler události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (geoDataProtocolListener != null) {
            geoDataProtocolListener.onConnectionTerminated();
        }
    }

    /**
     * Handler na zpracování události odeslání zprávy.
     *
     * @param sentData Odeslaná data.
     */
    @Override
    public void onMessageSent(byte[] sentData) {
        if (geoDataProtocolListener != null) {
            geoDataProtocolListener.onMessageSent();
        }
    }

    /**
     * Handler události příjmu dat.
     *
     * @param receivedData Přijmutá data uložená ve formě bytového pole.
     */
    @Override
    public void onReceivedData(byte[] receivedData) {
        try {
            boolean receiving; //zda přijme zprávu nebo ne

            switch (receivedData[0]) {
                case MobileMessageIDs.ID_MSG_UPLOAD_GEO:
                    receiving = true;
                    break;
                default:
                    receiving = false;
            }
            if (receiving) {
                in.put(receivedData); //vloží data do inter thread objektu, aby si je mohlo vyzvednout vlákno procesoru
            }
        } catch (InterruptedException e) {
        }
    }

    //================================================== VÝKONNÉ METODY ==================================================//
    /**
     * Odpojí datový protokol od základního protokolu.
     */
    @Override
    public void disconnectProtocol() {
        if (networkInterface != null) {
            networkInterface.removeOnReceivedDataListener(this);
        }
    }

    //================================================== INTERNÍ TŘÍDY ==================================================//
    /**
     * Třída zajišťující příjem dat v jiném vlákně.
     *
     * @author Pavel Brož
     */
    private class MessageProcessor implements Runnable {

        private InterThreadType<byte[]> in;
        private IServerGeoDataProtocolListener protocolListener;

        /**
         * Konstruktor.
         *
         * @param in Objekt pro předávání zpráv.
         * @param protocolListener Posluchač událostí z datového protokolu.
         */
        public MessageProcessor(InterThreadType<byte[]> in, IServerGeoDataProtocolListener protocolListener) {
            super();
            this.in = in;
            this.protocolListener = protocolListener;
        }

        /**
         * Zpracovává přijaté zprávy a volá metody posluchače.
         */
        @Override
        public void run() {
            try {
                while (true) {
                    byte[] receivedData = in.get(); //načte přijatá data

                    //pokud není žádný posluchač, není nutné zprávy zpracovávat//
                    if (protocolListener != null) {
                        //kontrola typu zprávy//
                        if (receivedData[0] == MobileMessageIDs.ID_MSG_UPLOAD_GEO) { //geo zpráva//
                            int geoLength = Conventer.byteArrayToInt(receivedData, 1); //zjištění délky pole
                            byte[] dataField = new byte[geoLength];
                            System.arraycopy(receivedData, 5, dataField, 0, geoLength);

                            List<Waypoint> geoData = deserializeGeoData(dataField);
                            protocolListener.geoDataReceived(geoData);
                        }
                    }
                }
            } catch (InterruptedException e) {
                //není potřeba dělat nic
            }
        }

        /**
         * Deserializuje geo data.
         * @param data
         * @return 
         */
        private List<Waypoint> deserializeGeoData(byte[] data) {
            ObjectInputStream objIS = null;
            try {
                ByteArrayInputStream geoBytes = new ByteArrayInputStream(data);
                objIS = new ObjectInputStream(geoBytes);

                List<Waypoint> geoData = (List<Waypoint>) objIS.readObject();

                objIS.close();
                geoBytes.close();

                return geoData;
            } catch (Exception ex) {
                return new ArrayList<Waypoint>();
            } finally {
                try {
                    objIS.close();
                } catch (IOException ex) {
                }
            }
        }

        public synchronized IServerGeoDataProtocolListener getProtocolListener() {
            return protocolListener;
        }

        public synchronized void setProtocolListener(IServerGeoDataProtocolListener protocolListener) {
            this.protocolListener = protocolListener;
        }
    }
}