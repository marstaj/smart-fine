package cz.smartfine.networklayer.dataprotocols;

public class MobileProtocolConstants {

    /**
     * Důvod autentizace je přihlášení na server.
     */
    public static final byte MSG_AUTHENTICATE_REASON_LOGIN = 0x0;
    /**
     * Důvod autentizace je autentizace policisty.
     */
    public static final byte MSG_AUTHENTICATE_REASON_AUTHENTICATION = 0x1;
    /**
     * Autentizace selhala z neznámého důvodu.
     */
    public static final byte MSG_FAIL_AUTH_ERR_UNKNOWN = 0x0;
    /**
     * Autentizace selhala protože služební číslo nebo PIN byl chybný.
     */
    public static final byte MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN = 0x1;
    /**
     * Autentizace selhala protože IMEI se nepárovalo se služebním číslem.
     */
    public static final byte MSG_FAIL_AUTH_ERR_IMEI_AND_BN_DONT_MATCH = 0x2;
    /**
     * Autentizace selhala protože IMEI bylo neznámé.
     */
    public static final byte MSG_FAIL_AUTH_ERR_UNKNOWN_IMEI = 0x3;
    /**
     * Přenosná parkovací karta není hlášena jako kradená.
     */
    public static final byte MSG_STATUS_SPC_STATUS_OK = 0x0;
    /**
     * Přenosná parkovací karta je hlášena jako kradená.
     */
    public static final byte MSG_STATUS_SPC_STATUS_STOLEN = 0x1;
    /**
     * Stav přenosné parkovací karty se nepodařil určit.
     */
    public static final byte MSG_STATUS_SPC_STATUS_UKNOWN = 0x2;
    /**
     * Parkování je povolené.
     */
    public static final byte MSG_STATUS_PSMS_STATUS_ALLOWED = 0x0;
    /**
     * Parkování není povolené.
     */
    public static final byte MSG_STATUS_PSMS_STATUS_NOT_ALLOWED = 0x1;
    /**
     * Stav parkování není znám.
     */
    public static final byte MSG_STATUS_PSMS_STATUS_UNKNOWN = 0x2;
    /**
     * Pole obsahující samé binární jedničky.
     */
    public static final byte DUMMY_FIELD = -1;
}
