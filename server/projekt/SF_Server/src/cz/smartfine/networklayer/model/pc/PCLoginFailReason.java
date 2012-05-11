package cz.smartfine.networklayer.model.pc;

/**
 * Výčet, udávající možnosti, proč došlo k neúspěšnému přihlášení.
 *
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:45
 */
public enum PCLoginFailReason {

    /**
     * Neznámý důvod neúspěšného přihlášení.
     */
    UNKNOWN_REASON,
    /**
     * Chybné služební číslo nebo PIN.
     */
    WRONG_BADGE_NUMBER_OR_PIN
}