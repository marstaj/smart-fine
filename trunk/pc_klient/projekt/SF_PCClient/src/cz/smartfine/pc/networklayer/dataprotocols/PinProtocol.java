package cz.smartfine.pc.networklayer.dataprotocols;

import cz.smartfine.networklayer.dataprotocols.PCMessageIDs;
import cz.smartfine.networklayer.dataprotocols.PCProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.pc.PINChangeFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.MessageBuilder;
import cz.smartfine.pc.networklayer.business.listeners.IPinProtocolListener;

/**
 * Představuje třídu protokolu pro změnu PINu.
 *
 * @author Pavel Brož
 * @version 1.0 @created 14-4-2012 18:48:48
 */
public class PinProtocol implements IDataProtocol {

    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Posluchač událostí z této třídy.
     */
    private IPinProtocolListener pinProtocolListener;

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    
    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     */
    public PinProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     * @param pinProtocolListener Posluchač událostí z této třídy.
     */
    public PinProtocol(INetworkInterface networkInterface, IPinProtocolListener pinProtocolListener) {
        this.networkInterface = networkInterface;
        this.pinProtocolListener = pinProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač
    }

    //================================================== GET/SET ==================================================//
    
    /**
     * Odebere posluchače událostí protokolu pro změnu PINu.
     *
     * @param pinProtocolListener Posluchač událostí z protokolu.
     */
    public void removePinProtocolListener(IPinProtocolListener pinProtocolListener) {
        this.pinProtocolListener = null;
    }

    /**
     * Přidá posluchače událostí protokolu pro změnu PINu.
     *
     * @param pinProtocolListener Posluchač událostí z protokolu.
     */
    public void setPinProtocolListener(IPinProtocolListener pinProtocolListener) {
        this.pinProtocolListener = pinProtocolListener;
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
        //pokud není žádný posluchač není nutné zprávy zpracovávat//
        if (pinProtocolListener != null) {

            //kontrola typu zprávy//
            switch (receivedData[0]) {
                case PCMessageIDs.ID_MSG_NEW_PIN: //nový pin//
                    //zjištění stavu//
                    switch (receivedData[1]) {
                        //PIN byl vygenerován//
                        case PCProtocolConstants.MSG_NEW_PIN_OK:
                            int pin = Conventer.byteArrayToInt(receivedData, 2);
                            pinProtocolListener.onPinChanged(pin); //zavolání handleru události
                            break;
                        //chybné služební číslo nebo PIN//
                        case PCProtocolConstants.MSG_NEW_PIN_WRONG_BN_OR_PIN:
                            pinProtocolListener.onPinChangeError(PINChangeFailReason.WRONG_BADGE_NUMBER_OR_PIN); //zavolání handleru události
                            break;
                        //neznámá chyba//
                        case PCProtocolConstants.MSG_NEW_PIN_UNKNOWN_ERR:
                            pinProtocolListener.onPinChangeError(PINChangeFailReason.UNKNOWN_REASON); //zavolání handleru události
                            break;
                        //neznámá hodnota//
                        default:
                            pinProtocolListener.onPinChangeError(PINChangeFailReason.UNKNOWN_REASON); //zavolání handleru události
                    }
                    break;
            }

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
     * Nechá vygenerovat nový PIN.
     *
     * @param badgeNumber Služební číslo policisty.
     * @param pin Aktuální PIN policisty.
     */
    public void changePin(int badgeNumber, int pin) {
        if (networkInterface != null) {
            //System.out.println("PC: PIN PROTOCOL CHANGE PIN");
            networkInterface.sendData(createPinChangeMessage(badgeNumber, pin), this);
        }
    }

    //================================================== PRIVÁTNÍ METODY ==================================================//
    
    /**
     * Vytváří zprávu pro změnu PINu.
     *
     * @param badgeNumber Služební číslo policisty.
     * @param pin PIN policisty.
     * @return Zpráva pro odeslání na server.
     */
    protected byte[] createPinChangeMessage(int badgeNumber, int pin) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(PCMessageIDs.ID_MSG_CHANGE_PIN); //identifikátor zprávy
        msg.putInt(badgeNumber); //služební číslo
        msg.putInt(pin); //PIN

        return msg.getByteArray();
    }

}
