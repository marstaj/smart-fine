package cz.smartfine.pc.view.util;

/**
 * Třída obsahující metody pro validaci uživatelských vstupů.
 *
 * @author Pavel Brož
 */
public class UIValidator {

    /**
     * Délka pinu.
     */
    public static final int PIN_CHAR_LEN = 5;
    /**
     * Délka služebního čísla.
     */
    public static final int BG_CHAR_LEN = 6;
    public static final int IMEI_LENGTH = 15;
    public static final int DEV_NAME_LENGTH = 20;
    public static final int DEV_DESC_LENGTH = 255;

    /**
     * Validuje služební číslo a PIN.
     *
     * @param badgeNumber Služební číslo.
     * @param passwd PIN.
     * @return true, pokud jsou obě hodnoty správné, jinak false.
     */
    public static boolean checkLoginFormat(String badgeNumber, char[] passwd) {
        boolean valid = true;//validita přihlašovacích údajů

        //zkusí jesli jsou všechy znaky čísla//
        for (char c : passwd) {
            if (!Character.isDigit(c)) {
                valid = false;
                break;
            }
        }
        //ověří délku PINu//
        if (valid && passwd.length != PIN_CHAR_LEN) {
            valid = false;
        }
        //ověří délku služebního čísla//
        if (badgeNumber.length() != BG_CHAR_LEN) {
            valid = false;
        }

        return valid;
    }

    /**
     * Validuje nové mobilní zařízení.
     *
     * @param imei IMEI.
     * @param name Název.
     * @param desc Popis.
     * @return
     */
    public static boolean checkDeviceFormat(String imei, String name, String desc) {
        boolean valid = true;

        if (imei.length() != IMEI_LENGTH) {
            valid = false;
        }

        if (name.length() == 0 || name.length() > DEV_NAME_LENGTH) {
            valid = false;
        }

        if (desc.length() > DEV_DESC_LENGTH) {
            valid = false;
        }

        return valid;
    }
}
