package cz.smartfine.android.networklayer.model;

/**
 * Výèet, udávající možnosti, proè došlo k neúspìšnému pøihlášení.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public enum LoginFailReason {
	/**
	 * Neznámý dùvod neúspìšného pøihlášení.
	 */
	UNKNOWN_REASON,
	/**
	 * Chybné služební èíslo nebo PIN.
	 */
	WRONG_BADGE_NUMBER_OR_PIN,
	/**
	 * Identifikaèní èíslo mobilního zaøízení (IMEI) neodpovídá služebnímu èíslu tj.
	 * policista nemùže používat mobilní zaøízení s pøíslušným IMEI (není s ním
	 * spárován).
	 */
	IMEI_AND_BADGE_NUMBER_DONT_MATCH,
	/**
	 * Identifikaèní èíslo mobilního zaøízení (IMEI) není v databázi registrovaných
	 * mobilních zaøízení.
	 */
	UNKNOWN_IMEI,
	/**
	 * Pøihlášení bylo neúspìšné, protože došlo k ukonèení spojení ze strany serveru.
	 */
	CONNECTION_TERMINATED_FROM_SERVER
}