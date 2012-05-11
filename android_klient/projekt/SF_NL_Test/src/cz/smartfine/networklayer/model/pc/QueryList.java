package cz.smartfine.networklayer.model.pc;

/**
 * Seznam dotazů na databázi.
 * @author Pavel Brož
 */
public class QueryList {
    
    /**
     * Dotaz na načtení vlastních PL z databáze.
     */
    public static final byte QUERY_GET_OWN_TICKETS = 0;
    /**
     * Dotaz na načtení PL z databáze podřízených.
     */
    public static final byte QUERY_GET_TICKETS = 1;
    /**
     * Dotaz na načtení seznamu podřízených policistů.
     */
    public static final byte QUERY_GET_POLICEMEN_LIST = 2;
    /**
     * Dotaz na načtení geolokačních dat.
     */
    public static final byte QUERY_GET_GEOLOCATION_DATA = 3;
    /**
     * Dotaz na registrace nového zařízení.
     */
    public static final byte QUERY_REGISTER_NEW_DEVICE = 4;
    /**
     * Dotaz na odstranění zařízení z databáze.
     */
    public static final byte QUERY_DELETE_DEVICE = 5;
    /**
     * Dotaz na načtení seznamu mobilních zařízení.
     */
    public static final byte QUERY_GET_DEVICE_LIST = 6;
    /**
     * Dotaz na editaci spárování zařízení a policisty.
     */
    public static final byte QUERY_EDIT_ASSOC = 7;
    /**
     * Dotaz na odebrání PL z databáze.
     */
    public static final byte QUERY_DELETE_TICKET = 8;
    
}
