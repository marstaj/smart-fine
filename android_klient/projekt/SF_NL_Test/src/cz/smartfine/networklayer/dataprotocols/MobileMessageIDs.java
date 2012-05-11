package cz.smartfine.networklayer.dataprotocols;

public class MobileMessageIDs {

    /**
     * ID autentizační zprávy.
     */
    public static final byte ID_MSG_AUTHENTICATE = 0x1;
    /**
     * ID zprávy s potvrzením úspěšné autentizace.
     */
    public static final byte ID_MSG_SUC_AUTH = 0x2;
    /**
     * ID zprávy s informací o neúspěšné autentizaci.
     */
    public static final byte ID_MSG_FAIL_AUTH = 0x3;
    /**
     * ID zprávy pro nahrání PL na server.
     */
    public static final byte ID_MSG_UPLOAD_TICKET = 0x4;
    /**
     * ID zprávy pro kontrolu odcizení přenosné parkovací karty (PPK, angl. SPC).
     */
    public static final byte ID_MSG_CHECK_SPC = 0x6;
    /**
     * ID zprávy se stavem o odcizení přenosné parkovací karty (PPK, angl. SPC).
     */
    public static final byte ID_MSG_STATUS_SPC = 0x7;
    /**
     * ID zprávy pro kontrolu času parkování přes SMS.
     */
    public static final byte ID_MSG_CHECK_PSMS = 0x8;
    /**
     * ID zprávy se stavem parkování přes SMS.
     */
    public static final byte ID_MSG_STATUS_PSMS = 0x9;
    /**
     * ID zprávy pro nahrání geolokačních dat na server.
     */
    public static final byte ID_MSG_UPLOAD_GEO = 0xA;
    /**
     * ID zprávy pro odhlášení ze serveru.
     */
    public static final byte ID_MSG_LOGOUT = 0xB;
}
