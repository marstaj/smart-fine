package cz.smartfine.networklayer.model.mobile;

/**
 * Výčet, udávající možnosti, proč došlo k neúspěšnému ověření identity.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:45
 */
public enum AuthenticationFailReason {

	/**
	 * Neznámý důvod neúspěšného ověření identity.
	 */
	UNKNOWN_REASON,
	/**
	 * Chybné služební číslo nebo PIN.
	 */
	WRONG_BADGE_NUMBER_OR_PIN

}