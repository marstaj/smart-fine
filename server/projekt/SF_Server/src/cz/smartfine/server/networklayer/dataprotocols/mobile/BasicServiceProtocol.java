package cz.smartfine.server.networklayer.dataprotocols.mobile;

import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.MobileProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.mobile.LoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.networklayer.util.MessageBuilder;
import cz.smartfine.server.business.client.mobile.providers.listeners.IBasicServiceProtocolListener;
import java.io.UnsupportedEncodingException;

/**
 * Představuje třídu protokolu, který provádí autentizaci a pomocné úkony při správě spojení.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:24
 */
public class BasicServiceProtocol implements IDataProtocol {

    /**
     * Posluchač událostí z této třídy.
     */
    private IBasicServiceProtocolListener basicServiceProtocolListener;
    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Interní třída, která asynchroně zpracovává přijaté zprávy a volá metody posluchače.
     */
    private MessageProcessor processor;
    /**
     * Vlákno pro zpracování zpráv.
     */
    private Thread processorThread;
    /**
     * Příchozí data.
     */
    private InterThreadType<byte[]> in = new InterThreadType<byte[]>();
    /**
     * Příznak, klient není přihlášen a že se přijímají všechny typy zpráv. Před přihlášením je nutné v případě obdržení jiné zprávy než přihlašovací, ukončit spojení -> je nutné kontrolovat všechny
     * zprávy. (true) Po přihlášení již není nutné příjímat všechny typy zpráv. (false)
     */
    private boolean notLoggedIn = true;

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     */
    public BasicServiceProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     * @param basicServiceProtocolListener Posluchač událostí z této třídy.
     */
    public BasicServiceProtocol(INetworkInterface networkInterface, IBasicServiceProtocolListener basicServiceProtocolListener) {
        this.networkInterface = networkInterface;
        this.basicServiceProtocolListener = basicServiceProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač

        //vytvoření a nastartování objektu pro zpracování dat v novém vlákně//
        processor = new MessageProcessor(this.in, this.basicServiceProtocolListener);
        processorThread = new Thread(processor, "basicServiceProcessorThread");
        processorThread.start();
    }

    /**
     *
     * @exception Throwable
     */
    @Override
    public void finalize() throws Throwable {
        super.finalize();
        if (processorThread != null && processorThread.isAlive()) {
            processorThread.interrupt();
        }
    }

    //================================================== GET/SET ==================================================//
    /**
     * Odebere posluchače událostí protokolu pro ověření identity.
     *
     * @param basicServiceProtocolListener Posluchač událostí z autentizačního protokolu.
     */
    public void removeBasicServiceProtocolListener(IBasicServiceProtocolListener basicServiceProtocolListener) {
        this.basicServiceProtocolListener = null;
        this.processor.setProtocolListener(null);
    }

    /**
     * Přidá posluchače událostí protokolu pro ověření identity.
     *
     * @param basicServiceProtocolListener Posluchač událostí z autentizačního protokolu.
     */
    public void setBasicServiceProtocolListener(IBasicServiceProtocolListener basicServiceProtocolListener) {
        this.basicServiceProtocolListener = basicServiceProtocolListener;
        this.processor.setProtocolListener(basicServiceProtocolListener);
    }

    //================================================== HANDLERY UDÁLOSTÍ ==================================================//
    /**
     * Handler události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (basicServiceProtocolListener != null) {
            basicServiceProtocolListener.onConnectionTerminated();
        }
        if (processorThread != null) {
            processorThread.interrupt();
        }
    }

    /**
     * Handler události příjmu dat.
     *
     * @param receivedData Přijmutá data uložená ve formě bytového pole.
     */
    @Override
    public void onReceivedData(byte[] receivedData) {
        if (basicServiceProtocolListener != null) {
            basicServiceProtocolListener.onMessageReceived(); //událost příchodu dat.
        }
        try {
            boolean receiving; //zda přijme zprávu nebo ne
            if (notLoggedIn) {
                receiving = true;
            } else {
                switch (receivedData[0]) {
                    case MobileMessageIDs.ID_MSG_AUTHENTICATE:
                        receiving = true;
                        break;
                    case MobileMessageIDs.ID_MSG_LOGOUT:
                        receiving = true;
                        break;
                    default:
                        receiving = false;
                }
            }
            if (receiving) {
                in.put(receivedData); //vloží data do inter thread objektu, aby si je mohlo vyzvednout vlákno procesoru
            }
        } catch (InterruptedException e) {
        }
    }

    /**
     * Handler na zpracování události odeslání zprávy.
     *
     * @param sentData Odeslaná data.
     */
    @Override
    public void onMessageSent(byte[] sentData) {
        if (basicServiceProtocolListener != null) {
            basicServiceProtocolListener.onMessageSent();
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
     * Odesílá zprávu s potvrzením úspěšné autentizace.
     */
    public void authenticationSuccessful() {
        notLoggedIn = false;

        if (networkInterface != null) {
            networkInterface.sendData(createAuthenticationSuccessfulMessage(), this);
        }
    }

    /**
     * Posílá zprávu s informací o neúspěšné autentizaci.
     *
     * @param failReason Důvod neúspěchu.
     */
    public void authenticationFail(LoginFailReason failReason) {
        if (networkInterface != null) {
            networkInterface.sendData(createAuthenticationFailMessage(failReason), this);
        }
    }

    //================================================== PRIVÁTNÍ METODY ==================================================//
    /**
     * Vytváří zprávu o úspěšném přihlášení.
     *
     * @return Zpráva.
     */
    protected byte[] createAuthenticationSuccessfulMessage() {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(MobileMessageIDs.ID_MSG_SUC_AUTH); //identifikátor zprávy
        msg.putByte(MobileProtocolConstants.DUMMY_FIELD);

        return msg.getByteArray();
    }

    /**
     * Vytváří zprávu o neúspěšném přihlášení.
     *
     * @param failReason Důvod selhání.
     * @return Zpráva.
     */
    protected byte[] createAuthenticationFailMessage(LoginFailReason failReason) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(MobileMessageIDs.ID_MSG_FAIL_AUTH); //identifikátor zprávy
        switch (failReason) {
            case UNKNOWN_REASON:
                msg.putByte(MobileProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN);
                break;
            case WRONG_BADGE_NUMBER_OR_PIN:
                msg.putByte(MobileProtocolConstants.MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN);
                break;
            case IMEI_AND_BADGE_NUMBER_DONT_MATCH:
                msg.putByte(MobileProtocolConstants.MSG_FAIL_AUTH_ERR_IMEI_AND_BN_DONT_MATCH);
                break;
            case UNKNOWN_IMEI:
                msg.putByte(MobileProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN_IMEI);
                break;
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
        private IBasicServiceProtocolListener protocolListener;

        /**
         * Konstruktor.
         *
         * @param in Objekt pro předávání zpráv.
         * @param protocolListener Posluchač událostí z datového protokolu.
         */
        public MessageProcessor(InterThreadType<byte[]> in, IBasicServiceProtocolListener protocolListener) {
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
                        switch (receivedData[0]) {
                            //autentizační zpráva//
                            case MobileMessageIDs.ID_MSG_AUTHENTICATE:
                                if (receivedData[1] == MobileProtocolConstants.MSG_AUTHENTICATE_REASON_LOGIN) { //přihlášení//
                                    int bn = Conventer.byteArrayToInt(receivedData, 2);
                                    int pin = Conventer.byteArrayToInt(receivedData, 6);
                                    String imei = "000000000000000";
                                    try {
                                        imei = new String(receivedData, 10, 15, "US-ASCII");
                                    } catch (UnsupportedEncodingException ex) {
                                    }

                                    protocolListener.onLoginRequest(bn, pin, imei);
                                } else if (notLoggedIn && receivedData[1] == MobileProtocolConstants.MSG_AUTHENTICATE_REASON_AUTHENTICATION) { //autorizace bez přihlášení//
                                    protocolListener.onNonLoginMessageReceived();
                                } else if (receivedData[1] == MobileProtocolConstants.MSG_AUTHENTICATE_REASON_AUTHENTICATION) { //autorizace//
                                    int bn = Conventer.byteArrayToInt(receivedData, 2);
                                    int pin = Conventer.byteArrayToInt(receivedData, 6);
                                    protocolListener.onAuthenticationRequest(bn, pin);
                                } else {
                                    protocolListener.onNonLoginMessageReceived();
                                }
                                break;

                            //odhlašovací zpráva//
                            case MobileMessageIDs.ID_MSG_LOGOUT:
                                if (notLoggedIn) {
                                    protocolListener.onNonLoginMessageReceived();
                                } else {
                                    protocolListener.onLogoutMessageReceived();
                                }
                                break;
                            default:
                                protocolListener.onNonLoginMessageReceived();
                        }
                    }
                }
            } catch (InterruptedException e) {
                //není potřeba dělat nic
            }
        }

        public synchronized IBasicServiceProtocolListener getProtocolListener() {
            return protocolListener;
        }

        public synchronized void setProtocolListener(IBasicServiceProtocolListener protocolListener) {
            this.protocolListener = protocolListener;
        }
    }
}
