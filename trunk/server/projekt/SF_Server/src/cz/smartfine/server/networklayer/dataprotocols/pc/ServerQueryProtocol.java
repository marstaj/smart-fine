package cz.smartfine.server.networklayer.dataprotocols.pc;

import cz.smartfine.networklayer.dataprotocols.PCMessageIDs;
import cz.smartfine.networklayer.dataprotocols.PCProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.pc.QueryState;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.networklayer.util.MessageBuilder;
import cz.smartfine.server.business.client.pc.providers.listeners.IServerQueryProtocolListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Protokol pro provádění dotazů na server.
 *
 * @author Pavel Brož
 */
public class ServerQueryProtocol implements IDataProtocol {

    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Posluchač událostí z této třídy.
     */
    private IServerQueryProtocolListener queryProtocolListener;
    /**
     * Interní třída, která asynchroně zpracovává přijaté zprávy a volá metody posluchače.
     */
    private ServerQueryProtocol.MessageProcessor processor;
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
    public ServerQueryProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenos dat.
     * @param queryProtocolListener Posluchač událostí z této třídy.
     */
    public ServerQueryProtocol(INetworkInterface networkInterface, IServerQueryProtocolListener queryProtocolListener) {
        this.networkInterface = networkInterface;
        this.queryProtocolListener = queryProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač

        //vytvoření a nastartování objektu pro zpracování dat v novém vlákně//
        processor = new ServerQueryProtocol.MessageProcessor(this.in, this.queryProtocolListener);
        processorThread = new Thread(processor, "queryProcessorThread");
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
     * Odebere posluchače událostí protokolu pro zpracování dotazů na server.
     *
     * @param queryProtocolListener Posluchač událostí.
     */
    public void removeServerQueryProtocolListener(IServerQueryProtocolListener queryProtocolListener) {
        this.queryProtocolListener = null;
        this.processor.setProtocolListener(null);
    }

    /**
     * Přidá posluchače událostí protokolu pro zpracování dotazů na server.
     *
     * @param queryProtocolListener Posluchač událostí.
     */
    public void setServerQueryProtocolListener(IServerQueryProtocolListener queryProtocolListener) {
        this.queryProtocolListener = queryProtocolListener;
        this.processor.setProtocolListener(queryProtocolListener);
    }

    //================================================== HANDLERY UDÁLOSTÍ ==================================================//
    /**
     * Handler události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (queryProtocolListener != null) {
            queryProtocolListener.onConnectionTerminated();
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
        if (queryProtocolListener != null) {
            queryProtocolListener.onMessageSent();
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
                case PCMessageIDs.ID_MSG_QUERY_REQUEST:
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
     * Pošle zprávu s výsledkem dotazu.
     *
     * @param id Identifikátor zprávy.
     * @param state Stav dotazu.
     * @param result Výsledek dotazu.
     */
    public void sendQueryResult(int id, QueryState state, Object result) throws IOException {
        if (networkInterface != null) {
            networkInterface.sendData(createQueryResultMessage(id, state, result), this);
        }
    }

    //================================================== PRIVÁTNÍ METODY ==================================================//
    /**
     * Vytvoří zprávu s výsledkem dotazu.
     *
     * @param id Identifikátor zprávy.
     * @param state Stav dotazu.
     * @param result Výsledek dotazu.
     */
    protected byte[] createQueryResultMessage(int id, QueryState state, Object result) throws IOException {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(PCMessageIDs.ID_MSG_QUERY_REPLY); // identifikátor zprávy
        msg.putInt(id); //identifikátor dotazu

        switch (state) {
            case QUERY_OK:
                msg.putByte(PCProtocolConstants.MSG_QUERY_REPLY_STATUS_OK);
                break;
            case QUERY_ERROR:
                msg.putByte(PCProtocolConstants.MSG_QUERY_REPLY_STATUS_ERR);
                break;
        }
        //pokud dotaz vrací hodnotu, je serializována / pokud nevrací, je vložena jen délka 0//
        if (result != null) {
            ByteArrayOutputStream resultBytes = new ByteArrayOutputStream();
            ObjectOutputStream objOS = new ObjectOutputStream(resultBytes);

            objOS.writeObject(result); //serializuje výsledek
            objOS.close();

            msg.putArrayWithIntLength(resultBytes.toByteArray()); // vložení pole objektu

            resultBytes.close();
        } else {
            msg.putInt(0);
        }
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
        private IServerQueryProtocolListener protocolListener;

        /**
         * Konstruktor.
         *
         * @param in Objekt pro předávání zpráv.
         * @param protocolListener Posluchač událostí z datového protokolu.
         */
        public MessageProcessor(InterThreadType<byte[]> in, IServerQueryProtocolListener protocolListener) {
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
                        if (receivedData[0] == PCMessageIDs.ID_MSG_QUERY_REQUEST) { //dotaz na server //
                            int id = Conventer.byteArrayToInt(receivedData, 1); //zjištění identifikátoru dotazu
                            byte type = receivedData[5]; //zjištění typu dotazu
                            int paramsLength = Conventer.byteArrayToInt(receivedData, 6); //zjištění délky pole s parametry
                            String params = new String(receivedData, 10, paramsLength); //převedení pole bytů na string

                            protocolListener.onQueryRequest(id, type, params);
                        }
                    }
                }
            } catch (InterruptedException e) {
                //není potřeba dělat nic
            }
        }

        public synchronized IServerQueryProtocolListener getProtocolListener() {
            return protocolListener;
        }

        public synchronized void setProtocolListener(IServerQueryProtocolListener protocolListener) {
            this.protocolListener = protocolListener;
        }
    }
}
