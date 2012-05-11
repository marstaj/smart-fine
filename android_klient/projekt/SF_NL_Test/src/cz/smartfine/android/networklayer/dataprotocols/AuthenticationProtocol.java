package cz.smartfine.android.networklayer.dataprotocols;

import cz.smartfine.android.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.networklayer.model.mobile.AuthenticationFailReason;
import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.MobileProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.MessageBuilder;

/**
 * Představuje třídu protokolu pro ověření identity.
 *
 * @author Pavel Brož
 * @version 1.0
 */
public class AuthenticationProtocol implements IDataProtocol {

    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Posluchač událostí z této třídy.
     */
    private IAuthenticationProtocolListener authenticationProtocolListener;

    // ================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    @Override
    public void finalize() throws Throwable {
        super.finalize();

    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     */
    public AuthenticationProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     * @param authenticationProtocolListener Posluchač událostí z této třídy.
     */
    public AuthenticationProtocol(INetworkInterface networkInterface, IAuthenticationProtocolListener authenticationProtocolListener) {
        this.networkInterface = networkInterface;
        this.authenticationProtocolListener = authenticationProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); // zaregistrování se jako posluchač
    }

    // ================================================== GET/SET ==================================================//
    /**
     * Odebere posluchače událostí protokolu pro ověření identity.
     *
     * @param authenticationProtocolListener Posluchač událostí z autentizačního protokolu.
     */
    public void removeAuthenticationProtocolListener(IAuthenticationProtocolListener authenticationProtocolListener) {
        this.authenticationProtocolListener = null;
    }

    /**
     * Přidá posluchače událostí protokolu pro ověření identity.
     *
     * @param authenticationProtocolListener Posluchač událostí z autentizačního protokolu.
     */
    public void setAuthenticationProtocolListener(IAuthenticationProtocolListener authenticationProtocolListener) {
        this.authenticationProtocolListener = authenticationProtocolListener;
    }

    // ================================================== HANDLERY UDÁLOSTÍ ==================================================//
    /**
     * Handler události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (authenticationProtocolListener != null) {
            authenticationProtocolListener.onConnectionTerminated();
        }
    }

    /**
     * Handler na zpracování události odeslání zprávy.
     *
     * @param sentData Odeslaná data.
     */
    @Override
    public void onMessageSent(byte[] sentData) {
        if (authenticationProtocolListener != null) {
            authenticationProtocolListener.onMessageSent();
        }
    }

    /**
     * Handler události příjmu dat.
     *
     * @param receivedData Přijmutá data uložená ve formě bytového pole.
     */
    @Override
    public void onReceivedData(byte[] receivedData) {
        // pokud není žádný posluchač není nutné zprávy zpracovávat//
        if (authenticationProtocolListener != null) {

            // kontrola typu zprávy//
            switch (receivedData[0]) {
                case MobileMessageIDs.ID_MSG_SUC_AUTH: // úspěšná autentizace//
                    authenticationProtocolListener.onAuthenticationConfirmed(); // zavolání handleru události
                    break;
                case MobileMessageIDs.ID_MSG_FAIL_AUTH: // neúspěšná autentizace//
                    AuthenticationFailReason reason; // důvod neúspěšné autentizace

                    // zjištění důvodu neúspěšné autentizace//
                    switch (receivedData[1]) {
                        // neznámá chyba//
                        case MobileProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN:
                            reason = AuthenticationFailReason.UNKNOWN_REASON;
                            break;
                        // chybné služební číslo nebo PIN//
                        case MobileProtocolConstants.MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN:
                            reason = AuthenticationFailReason.WRONG_BADGE_NUMBER_OR_PIN;
                            break;
                        // neznámá chyba//
                        default:
                            reason = AuthenticationFailReason.UNKNOWN_REASON;
                    }

                    authenticationProtocolListener.onAuthenticationFailed(reason); // zavolání handleru události
                    break;
            }
        }
    }

    // ================================================== VÝKONNÉ METODY ==================================================//
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
     * Ověřuje identitu policisty.
     *
     * @param badgeNumber Služební číslo policisty.
     * @param pin PIN policisty.
     */
    public void authenticate(int badgeNumber, int pin) {
        if (networkInterface != null) {
            networkInterface.sendData(createAuthenticationMessage(badgeNumber, pin), this);
        }
    }

    // ================================================== PRIVÁTNÍ METODY ==================================================//
    /**
     * Vytváří autentizační zprávu.
     *
     * @param badgeNumber Služební číslo policisty.
     * @param pin PIN policisty.
     * @return Zpráva pro odeslání na server
     */
    private byte[] createAuthenticationMessage(int badgeNumber, int pin) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(MobileMessageIDs.ID_MSG_AUTHENTICATE); // identifikátor zprávy
        msg.putByte(MobileProtocolConstants.MSG_AUTHENTICATE_REASON_AUTHENTICATION); // důvod autentizace - autentizace policisty
        msg.putInt(badgeNumber); // služební číslo
        msg.putInt(pin); // PIN
        msg.putArray(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}); // pole nahrazující IMEI

        return msg.getByteArray();
    }
}
