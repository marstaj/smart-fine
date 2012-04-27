package cz.smartfine.android.networklayer.model;

/**
 * Výèet, udávající možnosti, proè došlo k neúspìšnému ovìøení identity.
 * @author Pavel Brož
 * @version 1.0
 */
public enum AuthenticationFailReason {
	/**
	 * Neznámý dùvod neúspìšného ovìøení identity.
	 */
	UNKNOWN_REASON,
	/**
	 * Chybné služební èíslo nebo PIN.
	 */
	WRONG_BADGE_NUMBER_OR_PIN,
}