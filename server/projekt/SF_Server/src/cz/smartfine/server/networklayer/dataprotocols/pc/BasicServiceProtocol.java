package cz.smartfine.server.networklayer.dataprotocols.pc;

import cz.smartfine.networklayer.dataprotocols.PCMessageIDs;
import cz.smartfine.networklayer.dataprotocols.PCProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.pc.PCClientPermission;
import cz.smartfine.networklayer.model.pc.PCLoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.networklayer.util.MessageBuilder;
import cz.smartfine.server.business.client.pc.providers.listeners.IBasicServiceProtocolListener;

/**
 * Představuje třídu protokolu, který provádí autentizaci a pomocné úkony při správě spojení s PC klientem.
 *
 * @author Pavel Brož
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
    private BasicServiceProtocol.MessageProcessor processor;
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
        processor = new BasicServiceProtocol.MessageProcessor(this.in, this.basicServiceProtocolListener);
        processorThread = new Thread(processor, "PCBasicServiceProcessorThread");
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
                //výběr přijímaných zpráv//
                switch (receivedData[0]) {
                    case PCMessageIDs.ID_MSG_AUTHENTICATE:
                        receiving = true;
                        break;
                    case PCMessageIDs.ID_MSG_LOGOUT:
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
     * Odesílá zprávu s potvrzením úspěšné autentizace a s oprávněními.
     *
     * @param permissions Oprávnění přihlášeného policisty.
     */
    public void authenticationSuccessful(PCClientPermission permissions) {
        notLoggedIn = false;

        if (networkInterface != null) {
            networkInterface.sendData(createAuthenticationSuccessfulMessage(permissions), this);
        }
    }

    /**
     * Posílá zprávu s informací o neúspěšné autentizaci.
     *
     * @param failReason Důvod neúspěchu.
     */
    public void authenticationFail(PCLoginFailReason failReason) {
        if (networkInterface != null) {
            networkInterface.sendData(createAuthenticationFailMessage(failReason), this);
        }
    }

    //================================================== PRIVÁTNÍ METODY ==================================================//
    /**
     * Vytváří zprávu o úspěšném přihlášení.
     *
     * @param permissions Oprávnění policisty.
     * @return Zpráva.
     */
    protected byte[] createAuthenticationSuccessfulMessage(PCClientPermission permissions) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(PCMessageIDs.ID_MSG_SUC_AUTH); //identifikátor zprávy
        msg.putInt(permissions.getPermissions());

        return msg.getByteArray();
    }

    /**
     * Vytváří zprávu o neúspěšném přihlášení.
     *
     * @param failReason Důvod selhání.
     * @return Zpráva.
     */
    protected byte[] createAuthenticationFailMessage(PCLoginFailReason failReason) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(PCMessageIDs.ID_MSG_FAIL_AUTH); //identifikátor zprávy
        switch (failReason) {
            case UNKNOWN_REASON:
                msg.putByte(PCProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN);
                break;
            case WRONG_BADGE_NUMBER_OR_PIN:
                msg.putByte(PCProtocolConstants.MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN);
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
                            case PCMessageIDs.ID_MSG_AUTHENTICATE:
                                int bn = Conventer.byteArrayToInt(receivedData, 1);
                                int pin = Conventer.byteArrayToInt(receivedData, 5);

                                protocolListener.onLoginRequest(bn, pin);
                                break;
                            //odhlašovací zpráva//
                            case PCMessageIDs.ID_MSG_LOGOUT:
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
