package cz.smartfine.android.networklayer.model;

/**
 * V��et, ud�vaj�c� mo�nosti, pro� do�lo k ne�sp�n�mu p�ihl�en�.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public enum LoginFailReason {
	/**
	 * Nezn�m� d�vod ne�sp�n�ho p�ihl�en�.
	 */
	UNKNOWN_REASON,
	/**
	 * Chybn� slu�ebn� ��slo nebo PIN.
	 */
	WRONG_BADGE_NUMBER_OR_PIN,
	/**
	 * Identifika�n� ��slo mobiln�ho za��zen� (IMEI) neodpov�d� slu�ebn�mu ��slu tj.
	 * policista nem��e pou��vat mobiln� za��zen� s p��slu�n�m IMEI (nen� s n�m
	 * sp�rov�n).
	 */
	IMEI_AND_BADGE_NUMBER_DONT_MATCH,
	/**
	 * Identifika�n� ��slo mobiln�ho za��zen� (IMEI) nen� v datab�zi registrovan�ch
	 * mobiln�ch za��zen�.
	 */
	UNKNOWN_IMEI,
	/**
	 * P�ihl�en� bylo ne�sp�n�, proto�e do�lo k ukon�en� spojen� ze strany serveru.
	 */
	CONNECTION_TERMINATED_FROM_SERVER
}