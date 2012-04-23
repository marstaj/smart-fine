package cz.smartfine.networklayer.dataprotocols;

public class ProtocolConstants {

	/**
	 * D�vod autentizace je p�ihl�en� na server.
	 */
	public static final byte MSG_AUTHENTICATE_REASON_LOGIN = 0x0;
	
	/**
	 * D�vod autentizace je autentizace policisty.
	 */
	public static final byte MSG_AUTHENTICATE_REASON_AUTHENTICATION = 0x1;

	/**
	 * Autentizace selhala z nezn�m�ho d�vodu.
	 */
	public static final byte MSG_FAIL_AUTH_ERR_UNKNOWN = 0x0;

	/**
	 * Autentizace selhala proto�e slu�ebn� ��slo nebo PIN byl chybn�.
	 */
	public static final byte MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN = 0x1;
	
	/**
	 * Autentizace selhala proto�e IMEI se nep�rovalo se slu�ebn�m ��slem.
	 */
	public static final byte MSG_FAIL_AUTH_ERR_IMEI_AND_BN_DONT_MATCH = 0x2;
	
	/**
	 * Autentizace selhala proto�e IMEI bylo nezn�m�.
	 */
	public static final byte MSG_FAIL_AUTH_ERR_UNKNOWN_IMEI = 0x3;
	
	/**
	 * P�enosn� parkovac� karta nen� hl�ena jako kraden�.
	 */
	public static final byte MSG_STATUS_SPC_STATUS_OK = 0x0;
	
	/**
	 * P�enosn� parkovac� karta je hl�ena jako kraden�.
	 */
	public static final byte MSG_STATUS_SPC_STATUS_STOLEN = 0x1;
	
	/**
	 * Stav p�enosn� parkovac� karty se nepoda�il ur�it.
	 */
	public static final byte MSG_STATUS_SPC_STATUS_UKNOWN = 0x2;

	/**
	 * Parkov�n� je povolen�.
	 */
	public static final byte MSG_STATUS_PSMS_STATUS_ALLOWED = 0x0;

	/**
	 * Parkov�n� nen� povolen�.
	 */
	public static final byte MSG_STATUS_PSMS_STATUS_NOT_ALLOWED = 0x1;

	/**
	 * Stav parkov�n� nen� zn�m.
	 */
	public static final byte MSG_STATUS_PSMS_STATUS_UNKNOWN = 0x2;
	
	/**
	 * Pole obsahuj�c� sam� bin�rn� jedni�ky.
	 */
	public static final byte DUMMY_FIELD = -1;
}
