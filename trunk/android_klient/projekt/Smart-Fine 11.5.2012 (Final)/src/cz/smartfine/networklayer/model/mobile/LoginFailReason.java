package cz.smartfine.networklayer.model.mobile;

/**
 * Výčet, udávající možnosti, proč došlo k neúspěšnému přihlášení.
 * 
 * @author Pavel Brož
 * @version 1.0
 */
public enum LoginFailReason {

	/**
	 * Neznámý důvod neúspěšného přihlášení.
	 */
	UNKNOWN_REASON,
	/**
	 * Chybné služební číslo nebo PIN.
	 */
	WRONG_BADGE_NUMBER_OR_PIN,
	/**
	 * Identifikační číslo mobilního zařízení (IMEI) neodpovídá služebnímu číslu
	 * tj. policista nemůže používat mobilní zařízení s příslušným IMEI (není s
	 * ním spárován).
	 */
	IMEI_AND_BADGE_NUMBER_DONT_MATCH,
	/**
	 * Identifikační číslo mobilního zařízení (IMEI) není v databázi
	 * registrovaných mobilních zařízení.
	 */
	UNKNOWN_IMEI,
	/**
	 * Přihlášení bylo neúspěšné, protože došlo k ukončení spojení ze strany
	 * serveru.
	 */
	CONNECTION_TERMINATED_FROM_SERVER

}