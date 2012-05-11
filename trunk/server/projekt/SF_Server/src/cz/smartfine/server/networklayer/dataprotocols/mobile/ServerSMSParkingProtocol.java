package cz.smartfine.server.networklayer.dataprotocols.mobile;

import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.MobileProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.mobile.SMSParkingInfo;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.networklayer.util.MessageBuilder;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerSMSParkingProtocolListener;

/**
 * Představuje protokol pro kontrolu času parkování pomocí SMS.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:27
 */
public class ServerSMSParkingProtocol implements IDataProtocol {

    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Posluchač událostí z této třídy.
     */
    private IServerSMSParkingProtocolListener smsParkingProtocolListener;
    /**
     * Interní třída, která asynchroně zpracovává přijaté zprávy a volá metody posluchače.
     */
    private ServerSMSParkingProtocol.MessageProcessor processor;
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
    public ServerSMSParkingProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenos dat.
     * @param smsParkingProtocolListener Posluchač událostí z této třídy.
     */
    public ServerSMSParkingProtocol(INetworkInterface networkInterface, IServerSMSParkingProtocolListener smsParkingProtocolListener) {
        this.networkInterface = networkInterface;
        this.smsParkingProtocolListener = smsParkingProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač

        //vytvoření a nastartování objektu pro zpracování dat v novém vlákně//
        processor = new ServerSMSParkingProtocol.MessageProcessor(this.in, this.smsParkingProtocolListener);
        processorThread = new Thread(processor, "smsParkingProcessorThread");
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
     * Odebere posluchače událostí protokolu pro kontrolu času parkování vozidel.
     *
     * @param smsParkingProtocolListener Posluchač událostí protokolu pro kontrolu parkování.
     */
    public void removeServerSMSParkingProtocolListener(IServerSMSParkingProtocolListener smsParkingProtocolListener) {
        this.smsParkingProtocolListener = null;
        this.processor.setProtocolListener(null);
    }

    /**
     * Přidá posluchače událostí protokolu pro kontrolu času parkování vozidel.
     *
     * @param smsParkingProtocolListener Posluchač událostí protokolu pro kontrolu parkování.
     */
    public void setServerSMSParkingProtocolListener(IServerSMSParkingProtocolListener smsParkingProtocolListener) {
        this.smsParkingProtocolListener = smsParkingProtocolListener;
        this.processor.setProtocolListener(smsParkingProtocolListener);
    }

    //================================================== HANDLERY UDÁLOSTÍ ==================================================//
    /**
     * Handler události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (smsParkingProtocolListener != null) {
            smsParkingProtocolListener.onConnectionTerminated();
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
        if (smsParkingProtocolListener != null) {
            smsParkingProtocolListener.onMessageSent();
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
                case MobileMessageIDs.ID_MSG_CHECK_PSMS:
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
     * Odesílá zprávu se stavem parkování vozidla.
     *
     * @param parkingInfo Objekt se stavem parkování vozidla.
     */
    public void sendSMSParkingInfo(SMSParkingInfo parkingInfo) {
        if (networkInterface != null) {
            networkInterface.sendData(createPSMSMessage(parkingInfo), this);
        }
    }

    //================================================== PRIVÁTNÍ METODY ==================================================//
    /**
     * Vytváří zprávu se stavem parkování vozidla.
     *
     * @param parkingInfo Objekt se stavem parkování vozidla.
     * @return Zpráva pro odeslání klientovy.
     */
    protected byte[] createPSMSMessage(SMSParkingInfo parkingInfo) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(MobileMessageIDs.ID_MSG_STATUS_PSMS); //identifikátor zprávy

        //vložení stavu parkování//
        switch (parkingInfo.getParkingStatus()) {
            //parkování je povoleno//
            case ALLOWED:
                msg.putByte(MobileProtocolConstants.MSG_STATUS_PSMS_STATUS_ALLOWED);
                break;
            //parkování není povoleno//
            case NOT_ALLOWED:
                msg.putByte(MobileProtocolConstants.MSG_STATUS_PSMS_STATUS_NOT_ALLOWED);
                break;
            //stav se nepodařilo určit//
            case UNKNOWN_PARKING_STATUS:
                msg.putByte(MobileProtocolConstants.MSG_STATUS_PSMS_STATUS_UNKNOWN);
                break;
        }
        msg.putArray(Conventer.longToByteArray(parkingInfo.getParkingSince().getTime())); //parkování od
        msg.putArray(Conventer.longToByteArray(parkingInfo.getParkingUntil().getTime())); //parkování do

        msg.putArrayWithIntLength(parkingInfo.getVehicleRegistrationPlate().getBytes()); //SPZ

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
        private IServerSMSParkingProtocolListener protocolListener;

        /**
         * Konstruktor.
         *
         * @param in Objekt pro předávání zpráv.
         * @param protocolListener Posluchač událostí z datového protokolu.
         */
        public MessageProcessor(InterThreadType<byte[]> in, IServerSMSParkingProtocolListener protocolListener) {
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
                        if (receivedData[0] == MobileMessageIDs.ID_MSG_CHECK_PSMS) { //parking sms check zpráva //
                            int rvpLength = Conventer.byteArrayToInt(receivedData, 1); //zjištění délky pole s SPZ
                            String vehicleRegistrationPlate = new String(receivedData, 5, rvpLength); //převedení pole bytů na string

                            protocolListener.onSMSParkingCheckRequest(vehicleRegistrationPlate);
                        }
                    }
                }
            } catch (InterruptedException e) {
                //není potřeba dělat nic
            }
        }

        public synchronized IServerSMSParkingProtocolListener getProtocolListener() {
            return protocolListener;
        }

        public synchronized void setProtocolListener(IServerSMSParkingProtocolListener protocolListener) {
            this.protocolListener = protocolListener;
        }
    }
}