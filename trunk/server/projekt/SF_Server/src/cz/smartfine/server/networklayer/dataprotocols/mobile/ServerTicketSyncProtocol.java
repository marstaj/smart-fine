package cz.smartfine.server.networklayer.dataprotocols.mobile;

import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.model.NetworkTicket;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerTicketProtocolListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Představuje třídu protokolu pro přenost PL.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:28
 */
public class ServerTicketSyncProtocol implements IDataProtocol {

    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Posluchač událostí z této třídy.
     */
    private IServerTicketProtocolListener ticketProtocolListener;
    /**
     * Interní třída, která asynchroně zpracovává přijaté zprávy a volá metody posluchače.
     */
    private ServerTicketSyncProtocol.MessageProcessor processor;
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
    public ServerTicketSyncProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     * @param ticketProtocolListener Posluchač událostí z této třídy.
     */
    public ServerTicketSyncProtocol(INetworkInterface networkInterface, IServerTicketProtocolListener ticketProtocolListener) {
        this.networkInterface = networkInterface;
        this.ticketProtocolListener = ticketProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač

        //vytvoření a nastartování objektu pro zpracování dat v novém vlákně//
        processor = new ServerTicketSyncProtocol.MessageProcessor(this.in, this.ticketProtocolListener);
        processorThread = new Thread(processor, "ticketProcessorThread");
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
     * Odebere posluchače událostí protokolu pro příjem PL.
     *
     * @param ticketProtocolListener Posluchač událostí protokolu pro příjem PL.
     */
    public void removeServerTicketProtocolListener(IServerTicketProtocolListener ticketProtocolListener) {
        this.ticketProtocolListener = null;
        this.processor.setProtocolListener(null);
    }

    /**
     * Přidá posluchače událostí protokolu pro příjem PL.
     *
     * @param ticketProtocolListener Posluchač událostí protokolu pro příjem PL.
     */
    public void setServerTicketProtocolListener(IServerTicketProtocolListener ticketProtocolListener) {
        this.ticketProtocolListener = ticketProtocolListener;
        this.processor.setProtocolListener(ticketProtocolListener);
    }

    //================================================== HANDLERY UDÁLOSTÍ ==================================================//
    /**
     * Handler události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (ticketProtocolListener != null) {
            ticketProtocolListener.onConnectionTerminated();
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
        if (ticketProtocolListener != null) {
            ticketProtocolListener.onMessageSent();
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
                case MobileMessageIDs.ID_MSG_UPLOAD_TICKET:
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
        private IServerTicketProtocolListener protocolListener;

        /**
         * Konstruktor.
         *
         * @param in Objekt pro předávání zpráv.
         * @param protocolListener Posluchač událostí z datového protokolu.
         */
        public MessageProcessor(InterThreadType<byte[]> in, IServerTicketProtocolListener protocolListener) {
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
                        if (receivedData[0] == MobileMessageIDs.ID_MSG_UPLOAD_TICKET) { //ticket zpráva //
                            int badgeNumber = Conventer.byteArrayToInt(receivedData, 1); //zjištění služebního čísla

                            int ticketLength = Conventer.byteArrayToInt(receivedData, 5); //zjištění délky pole
                            byte[] dataField = new byte[ticketLength];
                            System.arraycopy(receivedData, 9, dataField, 0, ticketLength);

                            NetworkTicket ticket = deserializeTicketData(dataField);
                            ticket.setUploaderBadgeNumber(badgeNumber);

                            protocolListener.onTicketReceived(ticket);
                        }
                    }
                }
            } catch (InterruptedException e) {
                //není potřeba dělat nic
            }
        }

        /**
         * Deserializuje PL.
         *
         * @param data
         * @return
         */
        private NetworkTicket deserializeTicketData(byte[] data) {
            ObjectInputStream objIS = null;
            try {
                ByteArrayInputStream ticketBytes = new ByteArrayInputStream(data);
                objIS = new ObjectInputStream(ticketBytes);

                NetworkTicket ticket = (NetworkTicket) objIS.readObject();

                objIS.close();
                ticketBytes.close();

                return ticket;
            } catch (Exception ex) {
                return null;
            } finally {
                try {
                    objIS.close();
                } catch (IOException ex) {
                }
            }
        }

        public synchronized IServerTicketProtocolListener getProtocolListener() {
            return protocolListener;
        }

        public synchronized void setProtocolListener(IServerTicketProtocolListener protocolListener) {
            this.protocolListener = protocolListener;
        }
    }
}