package cz.smartfine.android.networklayer.model;

/**
 * V��et, ud�vaj�c� mo�nosti, pro� do�lo k ne�sp�n�mu ov��en� identity.
 * @author Pavel Bro�
 * @version 1.0
 */
public enum AuthenticationFailReason {
	/**
	 * Nezn�m� d�vod ne�sp�n�ho ov��en� identity.
	 */
	UNKNOWN_REASON,
	/**
	 * Chybn� slu�ebn� ��slo nebo PIN.
	 */
	WRONG_BADGE_NUMBER_OR_PIN,
}