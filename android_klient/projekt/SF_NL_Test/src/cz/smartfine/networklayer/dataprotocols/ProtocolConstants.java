package cz.smartfine.networklayer.dataprotocols;

public class ProtocolConstants {

	/**
	 * Dùvod autentizace je pøihlášení na server.
	 */
	public static final byte MSG_AUTHENTICATE_REASON_LOGIN = 0x0;
	
	/**
	 * Dùvod autentizace je autentizace policisty.
	 */
	public static final byte MSG_AUTHENTICATE_REASON_AUTHENTICATION = 0x1;

	/**
	 * Autentizace selhala z neznámého dùvodu.
	 */
	public static final byte MSG_FAIL_AUTH_ERR_UNKNOWN = 0x0;

	/**
	 * Autentizace selhala protože služební èíslo nebo PIN byl chybný.
	 */
	public static final byte MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN = 0x1;
	
	/**
	 * Autentizace selhala protože IMEI se nepárovalo se služebním èíslem.
	 */
	public static final byte MSG_FAIL_AUTH_ERR_IMEI_AND_BN_DONT_MATCH = 0x2;
	
	/**
	 * Autentizace selhala protože IMEI bylo neznámé.
	 */
	public static final byte MSG_FAIL_AUTH_ERR_UNKNOWN_IMEI = 0x3;
	
	/**
	 * Pøenosná parkovací karta není hlášena jako kradená.
	 */
	public static final byte MSG_STATUS_SPC_STATUS_OK = 0x0;
	
	/**
	 * Pøenosná parkovací karta je hlášena jako kradená.
	 */
	public static final byte MSG_STATUS_SPC_STATUS_STOLEN = 0x1;
	
	/**
	 * Stav pøenosné parkovací karty se nepodaøil urèit.
	 */
	public static final byte MSG_STATUS_SPC_STATUS_UKNOWN = 0x2;

	/**
	 * Parkování je povolené.
	 */
	public static final byte MSG_STATUS_PSMS_STATUS_ALLOWED = 0x0;

	/**
	 * Parkování není povolené.
	 */
	public static final byte MSG_STATUS_PSMS_STATUS_NOT_ALLOWED = 0x1;

	/**
	 * Stav parkování není znám.
	 */
	public static final byte MSG_STATUS_PSMS_STATUS_UNKNOWN = 0x2;
	
	/**
	 * Pole obsahující samé binární jednièky.
	 */
	public static final byte DUMMY_FIELD = -1;
}
