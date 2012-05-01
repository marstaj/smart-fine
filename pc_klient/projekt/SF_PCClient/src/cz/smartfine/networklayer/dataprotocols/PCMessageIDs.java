package cz.smartfine.networklayer.dataprotocols;

public class PCMessageIDs {

    /**
     * ID autentizační zprávy.
     */
    public static final byte ID_MSG_AUTHENTICATE = -127;
    /**
     * ID zprávy s potvrzením úspěšné autentizace.
     */
    public static final byte ID_MSG_SUC_AUTH = -126;
    /**
     * ID zprávy s informací o neúspěšné autentizaci.
     */
    public static final byte ID_MSG_FAIL_AUTH = -125;
    /**
     * ID zprávy pro odhlášení ze serveru.
     */
    public static final byte ID_MSG_LOGOUT = -124;
    /**
     * ID zprávy pro zavolání dotazu na server.
     */
    public static final byte ID_MSG_QUERY_REQUEST = -123;
    /**
     * ID zprávy pro odpověď na dotaz.
     */
    public static final byte ID_MSG_QUERY_REPLY = -122;
    /**
     * ID zprávy pro změnu PINu.
     */
    public static final byte ID_MSG_CHANGE_PIN = -121;
    /**
     * ID zprávy s novým PINem.
     */
    public static final byte ID_MSG_NEW_PIN = -120;

    
}
