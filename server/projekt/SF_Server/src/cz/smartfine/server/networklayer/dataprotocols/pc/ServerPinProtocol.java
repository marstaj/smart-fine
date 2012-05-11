package cz.smartfine.server.networklayer.dataprotocols.pc;

import cz.smartfine.networklayer.dataprotocols.PCMessageIDs;
import cz.smartfine.networklayer.dataprotocols.PCProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.pc.PINChangeFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.networklayer.util.MessageBuilder;
import cz.smartfine.server.business.client.pc.providers.listeners.IServerPinProtocolListener;

/**
 * Datový protokol pro změnu PINu.
 *
 * @author Pavel Brož
 */
public class ServerPinProtocol implements IDataProtocol {

    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Posluchač událostí z této třídy.
     */
    private IServerPinProtocolListener pinProtocolListener;
    /**
     * Interní třída, která asynchroně zpracovává přijaté zprávy a volá metody posluchače.
     */
    private ServerPinProtocol.MessageProcessor processor;
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
    public ServerPinProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenos dat.
     * @param pinProtocolListener Posluchač událostí z této třídy.
     */
    public ServerPinProtocol(INetworkInterface networkInterface, IServerPinProtocolListener pinProtocolListener) {
        this.networkInterface = networkInterface;
        this.pinProtocolListener = pinProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač

        //vytvoření a nastartování objektu pro zpracování dat v novém vlákně//
        processor = new ServerPinProtocol.MessageProcessor(this.in, this.pinProtocolListener);
        processorThread = new Thread(processor, "pinProcessorThread");
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
     * Odebere posluchače událostí protokolu pro změnu PINu..
     *
     * @param pinProtocolListener Posluchač událostí.
     */
    public void removeServerPinProtocolListener(IServerPinProtocolListener pinProtocolListener) {
        this.pinProtocolListener = null;
        this.processor.setProtocolListener(null);
    }

    /**
     * Přidá posluchače událostí protokolu pro změnu PINu.
     *
     * @param pinProtocolListener Posluchač událostí.
     */
    public void setServerPinProtocolListener(IServerPinProtocolListener pinProtocolListener) {
        this.pinProtocolListener = pinProtocolListener;
        this.processor.setProtocolListener(pinProtocolListener);
    }

    //================================================== HANDLERY UDÁLOSTÍ ==================================================//
    /**
     * Handler události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (pinProtocolListener != null) {
            pinProtocolListener.onConnectionTerminated();
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
        if (pinProtocolListener != null) {
            pinProtocolListener.onMessageSent();
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
                case PCMessageIDs.ID_MSG_CHANGE_PIN:
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
     * Pošle zprávu s novým PINem.
     *
     * @param newPin Nový PIN.
     */
    public void pinChanged(int newPin) {
        if (networkInterface != null) {
            networkInterface.sendData(createSuccPINChangedMessage(newPin), this);
        }
    }

    /**
     * Pošle zprávu s důvodem přoč nedošlo ke změně PINu.
     *
     * @param failReason Důvod selhání.
     */
    public void errorDuringChangingPIN(PINChangeFailReason failReason) {
        if (networkInterface != null) {
            networkInterface.sendData(createFailPINChangedMessage(failReason), this);
        }
    }

    //================================================== PRIVÁTNÍ METODY ==================================================//
    /**
     * Vytvoří zprávu s novým PINem.
     *
     * @param newPin Nový PIN.
     */
    protected byte[] createSuccPINChangedMessage(int newPin) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(PCMessageIDs.ID_MSG_NEW_PIN); //identifikátor zprávy

        msg.putByte(PCProtocolConstants.MSG_NEW_PIN_OK); //pin byl vygenerován
        msg.putInt(newPin); //nový PIN

        return msg.getByteArray();
    }

    /**
     * Vytvoří zprávu s důvodem přoč nedošlo ke změně PINu.
     *
     * @param failReason Důvod selhání.
     */
    protected byte[] createFailPINChangedMessage(PINChangeFailReason failReason) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(PCMessageIDs.ID_MSG_NEW_PIN); //identifikátor zprávy

        //vložení chyby//
        switch (failReason) {
            //neznámá chyba//
            case UNKNOWN_REASON:
                msg.putByte(PCProtocolConstants.MSG_NEW_PIN_UNKNOWN_ERR);
                break;
            //chybné služební číslo nebo PIN//
            case WRONG_BADGE_NUMBER_OR_PIN:
                msg.putByte(PCProtocolConstants.MSG_NEW_PIN_WRONG_BN_OR_PIN);
                break;
        }
        msg.putInt(0); //vložení povinného pole - nemá žádnou informační hodnotu

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
        private IServerPinProtocolListener protocolListener;

        /**
         * Konstruktor.
         *
         * @param in Objekt pro předávání zpráv.
         * @param protocolListener Posluchač událostí z datového protokolu.
         */
        public MessageProcessor(InterThreadType<byte[]> in, IServerPinProtocolListener protocolListener) {
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
                        if (receivedData[0] == PCMessageIDs.ID_MSG_CHANGE_PIN) { //změna pinu //
                            int bg = Conventer.byteArrayToInt(receivedData, 1); //zjištění služebního čísla
                            int pin = Conventer.byteArrayToInt(receivedData, 5); //zjištění PINu
                            protocolListener.onNewPinRequest(bg, pin);
                        }
                    }
                }
            } catch (InterruptedException e) {
                //není potřeba dělat nic
            }
        }

        public synchronized IServerPinProtocolListener getProtocolListener() {
            return protocolListener;
        }

        public synchronized void setProtocolListener(IServerPinProtocolListener protocolListener) {
            this.protocolListener = protocolListener;
        }
    }
}
