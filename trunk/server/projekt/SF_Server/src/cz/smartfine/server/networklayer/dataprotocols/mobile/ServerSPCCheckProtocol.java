package cz.smartfine.server.networklayer.dataprotocols.mobile;

import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.MobileProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.mobile.SPCInfo;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.networklayer.util.MessageBuilder;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerSPCCheckProtocolListener;

/**
 * Představuje třídu pro kontrolu odcizení přenosné parkovací karty (PPK angl. SPC - SUBSCRIBER PARKING CARD).
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:27
 */
public class ServerSPCCheckProtocol implements IDataProtocol {

    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Posluchač událostí z této třídy.
     */
    private IServerSPCCheckProtocolListener spcCheckProtocolListener;
    /**
     * Interní třída, která asynchroně zpracovává přijaté zprávy a volá metody posluchače.
     */
    private ServerSPCCheckProtocol.MessageProcessor processor;
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
    public ServerSPCCheckProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenos dat.
     * @param spcCheckProtocolListener Posluchač událostí z této třídy.
     */
    public ServerSPCCheckProtocol(INetworkInterface networkInterface, IServerSPCCheckProtocolListener spcCheckProtocolListener) {
        this.networkInterface = networkInterface;
        this.spcCheckProtocolListener = spcCheckProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač

        //vytvoření a nastartování objektu pro zpracování dat v novém vlákně//
        processor = new ServerSPCCheckProtocol.MessageProcessor(this.in, this.spcCheckProtocolListener);
        processorThread = new Thread(processor, "spcProcessorThread");
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
     * Odebere posluchače událostí protokolu pro kontrolu odcizení PPK.
     *
     * @param spcCheckProtocolListener Posluchač událostí protokolu pro kontrolu odcizení PPK.
     */
    public void removeServerSPCCheckProtocolListener(IServerSPCCheckProtocolListener spcCheckProtocolListener) {
        this.spcCheckProtocolListener = null;
        this.processor.setProtocolListener(null);
    }

    /**
     * Přidá posluchače událostí protokolu pro kontrolu odcizení PPK.
     *
     * @param spcCheckProtocolListener Posluchač událostí protokolu pro kontrolu odcizení PPK.
     */
    public void setServerSPCCheckProtocolListener(IServerSPCCheckProtocolListener spcCheckProtocolListener) {
        this.spcCheckProtocolListener = spcCheckProtocolListener;
        this.processor.setProtocolListener(spcCheckProtocolListener);
    }

    //================================================== HANDLERY UDÁLOSTÍ ==================================================//
    
    /**
     * Handler události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (spcCheckProtocolListener != null) {
            spcCheckProtocolListener.onConnectionTerminated();
        }
        if (processorThread != null) {
            processorThread.interrupt();
        }
    }

    /**
     * Handler na zpracování události odeslání zprávy.
     *
     * @param sentData Odeslaná data.
     */
    @Override
    public void onMessageSent(byte[] sentData) {
        if (spcCheckProtocolListener != null) {
            spcCheckProtocolListener.onMessageSent();
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
                case MobileMessageIDs.ID_MSG_CHECK_SPC:
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

    /**
     * Odesílá zprávu se stavem PPK.
     *
     * @param spcInfo Objekt se stavem PPK.
     */
    public void sendSPCInfo(SPCInfo spcInfo) {
        if (networkInterface != null) {
            networkInterface.sendData(createSPCMessage(spcInfo), this);
        }
    }

    //================================================== PRIVÁTNÍ METODY ==================================================//
    
    /**
     * Vytváří zprávu se stavem odcizení PPK.
     *
     * @param spcInfo Objekt se stavem PPK.
     * @return Zpráva pro odeslání na server.
     */
    protected byte[] createSPCMessage(SPCInfo spcInfo) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(MobileMessageIDs.ID_MSG_STATUS_SPC); //identifikátor zprávy

        //vložení stavu PPK//
        switch (spcInfo.getSpcStatus()) {
            //PPK není hlášena jako kradená//
            case OK_SPC:
                msg.putByte(MobileProtocolConstants.MSG_STATUS_SPC_STATUS_OK);
                break;
            //PPK není hlášena jako kradená//
            case STOLEN_SPC:
                msg.putByte(MobileProtocolConstants.MSG_STATUS_SPC_STATUS_STOLEN);
                break;
            //stav se nepodařilo určit//
            case UKNOWN_SPC_STATUS:
                msg.putByte(MobileProtocolConstants.MSG_STATUS_SPC_STATUS_UKNOWN);
                break;
        }

        msg.putArrayWithIntLength(spcInfo.getSpcNumber().getBytes()); //číslo PPK

        return msg.getByteArray();
    }

    //================================================== INTERNÍ TŘÍDY ==================================================//
    
    /**
     * Třída zajišťující příjem dat v jiném vlákně.
     *
     * @author Pavel Brož
     */
    private class MessageProcessor implements Runnable {

        private InterThreadType<byte[]> in;
        private IServerSPCCheckProtocolListener protocolListener;

        /**
         * Konstruktor.
         *
         * @param in Objekt pro předávání zpráv.
         * @param protocolListener Posluchač událostí z datového protokolu.
         */
        public MessageProcessor(InterThreadType<byte[]> in, IServerSPCCheckProtocolListener protocolListener) {
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
                        if (receivedData[0] == MobileMessageIDs.ID_MSG_CHECK_SPC) { //PPK check zpráva //
                            int spcNumberLength = Conventer.byteArrayToInt(receivedData, 1); //zjištění délky pole s číslem PPK
                            String spcNumber = new String(receivedData, 5, spcNumberLength); //převedení pole bytů na string

                            protocolListener.onSPCCheckRequest(spcNumber);
                        }
                    }
                }
            } catch (InterruptedException e) {
                //není potřeba dělat nic
            }
        }

        public synchronized IServerSPCCheckProtocolListener getProtocolListener() {
            return protocolListener;
        }

        public synchronized void setProtocolListener(IServerSPCCheckProtocolListener protocolListener) {
            this.protocolListener = protocolListener;
        }
    }
}