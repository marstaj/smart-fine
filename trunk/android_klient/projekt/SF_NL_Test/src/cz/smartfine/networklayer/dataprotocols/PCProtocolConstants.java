package cz.smartfine.networklayer.dataprotocols;

public class PCProtocolConstants {

    /**
     * Žádné oprávnění.
     */
    public static final int MSG_SUC_AUTH_PERM_NONE = 0x0;
    /**
     * Oprávnění pro prohlížení vlastních PL.
     */
    public static final int MSG_SUC_AUTH_PERM_SHOW_OWN_TICKETS = 0x1;
    /**
     * Oprávnění pro změnu PINu.
     */
    public static final int MSG_SUC_AUTH_PERM_CHNG_PIN = 0x2;
    /**
     * Oprávnění pro správu mobilních zařízení.
     */
    public static final int MSG_SUC_AUTH_PERM_ADMIN_DEVICES = 0x4;
    /**
     * Oprávnění pro správu asociací mezi policisty a mobilnímy zařízeními.
     */
    public static final int MSG_SUC_AUTH_PERM_ADMIN_ASSOC = 0x8;
    /**
     * Oprávnění pro prohlížení geolokačních záznamů podřízených.
     */
    public static final int MSG_SUC_AUTH_PERM_SHOW_GEODATA = 0x10;
    /**
     * Oprávnění pro prohlížení PL podřízených.
     */
    public static final int MSG_SUC_AUTH_PERM_SHOW_TICKETS = 0x20;
    /**
     * Oprávnění pro mazání PL.
     */
    public static final int MSG_SUC_AUTH_PERM_DELETE_TICKETS = 0x40;
    /**
     * Autentizace selhala z neznámého důvodu.
     */
    public static final byte MSG_FAIL_AUTH_ERR_UNKNOWN = 0x0;
    /**
     * Autentizace selhala protože služební číslo nebo PIN byl chybný.
     */
    public static final byte MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN = 0x1;
    /**
     * Dotaz na server proběhl v pořádku.
     */
    public static final byte MSG_QUERY_REPLY_STATUS_OK = 0x0;
    /**
     * Dotaz na server skončil chybou.
     */
    public static final byte MSG_QUERY_REPLY_STATUS_ERR = 0x1;
    /**
     * Nový PIN byl vygenerován v pořádku.
     */
    public static final byte MSG_NEW_PIN_OK = 0x0;
    /**
     * Nový PIN nebyl vygenerován. Chybné služební číslo nebo PIN.
     */
    public static final byte MSG_NEW_PIN_WRONG_BN_OR_PIN = 0x1;
    /**
     * Nový PIN nebyl vygenerován. Neznámá chyba.
     */
    public static final byte MSG_NEW_PIN_UNKNOWN_ERR = 0x2;
    /**
     * Pole obsahující samé binární jedničky.
     */
    public static final byte DUMMY_FIELD = -1;
}
