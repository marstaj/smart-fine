package cz.smartfine.pc.networklayer.dataprotocols;

import cz.smartfine.networklayer.dataprotocols.PCMessageIDs;
import cz.smartfine.networklayer.dataprotocols.PCProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.pc.PCClientPermission;
import cz.smartfine.networklayer.model.pc.PCLoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.MessageBuilder;
import cz.smartfine.pc.networklayer.business.listeners.ILoginProtocolListener;

/**
 * Představuje třídu protokolu pro přihlášení na server.
 *
 * @author Pavel Brož
 * @version 1.0 @created 14-4-2012 18:48:48
 */
public class LoginProtocol implements IDataProtocol {

    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Posluchač událostí z této třídy.
     */
    private ILoginProtocolListener loginProtocolListener;

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    
    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     */
    public LoginProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     * @param loginProtocolListener Posluchač událostí z této třídy.
     */
    public LoginProtocol(INetworkInterface networkInterface, ILoginProtocolListener loginProtocolListener) {
        this.networkInterface = networkInterface;
        this.loginProtocolListener = loginProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač
    }

    //================================================== GET/SET ==================================================//
    
    /**
     * Odebere posluchače událostí protokolu pro přihlášení na server.
     *
     * @param loginProtocolListener Posluchač událostí z přihlašovacího protokolu.
     */
    public void removeLoginProtocolListener(ILoginProtocolListener loginProtocolListener) {
        this.loginProtocolListener = null;
    }

    /**
     * Přidá posluchače událostí protokolu pro přihlášení na server.
     *
     * @param loginProtocolListener Posluchač událostí z přihlašovacího protokolu.
     */
    public void setLoginProtocolListener(ILoginProtocolListener loginProtocolListener) {
        this.loginProtocolListener = loginProtocolListener;
    }

    //================================================== HANDLERY UDÁLOSTÍ ==================================================//
    /**
     * Handler události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (loginProtocolListener != null) {
            loginProtocolListener.onConnectionTerminated();
        }
    }

    /**
     * Handler na zpracování události odeslání zprávy.
     *
     * @param sentData Odeslaná data.
     */
    @Override
    public void onMessageSent(byte[] sentData) {
        if (loginProtocolListener != null) {
            loginProtocolListener.onMessageSent();
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
        if (loginProtocolListener != null) {

            //kontrola typu zprávy//
            switch (receivedData[0]) {
                case PCMessageIDs.ID_MSG_SUC_AUTH: //úspěšné přihlášení//
                    PCClientPermission perm = new PCClientPermission(Conventer.byteArrayToInt(receivedData, 1)); //oprávnění
                    
                    loginProtocolListener.onLoginConfirmed(perm); //zavolání handleru události
                    break;
                case PCMessageIDs.ID_MSG_FAIL_AUTH: //neúspěšná autentizace//
                    PCLoginFailReason reason; //důvod neúspěšné autentizace

                    //zjištění důvodu neúspěšné autentizace//
                    switch (receivedData[1]) {
                        //neznámá chyba//
                        case PCProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN:
                            reason = PCLoginFailReason.UNKNOWN_REASON;
                            break;
                        //chybné služební číslo nebo PIN//
                        case PCProtocolConstants.MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN:
                            reason = PCLoginFailReason.WRONG_BADGE_NUMBER_OR_PIN;
                            break;
                        //neznámá chyba//
                        default:
                            reason = PCLoginFailReason.UNKNOWN_REASON;
                    }

                    loginProtocolListener.onLoginFailed(reason); //zavolání handleru události
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
     * Přihlašuje mobilního klienta k serveru.
     *
     * @param badgeNumber Služební číslo přihlašovaného policisty.
     * @param pin PIN přihlašovaného policisty.
     */
    public void loginToServer(int badgeNumber, int pin) {
        if (networkInterface != null) {
            System.out.println("PC: LOGIN PROTOCOL SEND LOGIN");
            networkInterface.sendData(createLoginMessage(badgeNumber, pin), this);
        }
    }

    /**
     * Odhlašuje mobilního klienta ze serveru.
     */
    public void logoutFromServer() {
        if (networkInterface != null) {
            System.out.println("PC: LOGIN PROTOCOL SEND LOGOUT");
            networkInterface.sendData(createLogoutMessage(), this);
        }
    }

    //================================================== PRIVÁTNÍ METODY ==================================================//
    
    /**
     * Vytváří přihlašovací zprávu.
     *
     * @param badgeNumber Služební číslo přihlašovaného policisty.
     * @param pin PIN přihlašovaného policisty.
     * @return Zpráva pro odeslání na server.
     */
    protected byte[] createLoginMessage(int badgeNumber, int pin) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(PCMessageIDs.ID_MSG_AUTHENTICATE); //identifikátor zprávy
        msg.putInt(badgeNumber); //služební číslo
        msg.putInt(pin); //PIN

        return msg.getByteArray();
    }

    /**
     * Vytváří odhlašovací zprávu.
     *
     * @return Zpráva pro odeslání na server.
     */
    protected byte[] createLogoutMessage() {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(PCMessageIDs.ID_MSG_LOGOUT); //identifikátor zprávy
        msg.putByte(PCProtocolConstants.DUMMY_FIELD);

        return msg.getByteArray();
    }
}
