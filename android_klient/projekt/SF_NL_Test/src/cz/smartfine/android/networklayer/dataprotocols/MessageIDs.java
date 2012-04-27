package cz.smartfine.android.networklayer.dataprotocols;

public class MessageIDs {

	/**
	 * ID autentizaèní zprávy.
	 */
	public static final byte ID_MSG_AUTHENTICATE = 0x1;

	/**
	 * ID zprávy s potvrzením úspìšné autentizace.
	 */
	public static final byte ID_MSG_SUC_AUTH = 0x2;

	/**
	 * ID zprávy s informací o neúspìšné autentizaci.
	 */
	public static final byte ID_MSG_FAIL_AUTH = 0x3;

	/**
	 * ID zprávy pro nahrání PL na server.
	 */
	public static final byte ID_MSG_UPLOAD_TICKET = 0x4;

	/**
	 * ID zprávy pro kontrolu odcizení pøenosné parkovací karty (PPK, angl. SPC).
	 */
	public static final byte ID_MSG_CHECK_SPC = 0x6;

	/**
	 * ID zprávy se stavem o odcizení pøenosné parkovací karty (PPK, angl. SPC).
	 */
	public static final byte ID_MSG_STATUS_SPC = 0x7;

	/**
	 * ID zprávy pro kontrolu èasu parkování pøes SMS.
	 */
	public static final byte ID_MSG_CHECK_PSMS = 0x8;

	/**
	 * ID zprávy se stavem parkování pøes SMS.
	 */
	public static final byte ID_MSG_STATUS_PSMS = 0x9;

	/**
	 * ID zprávy pro nahrání geolokaèních dat na server.
	 */
	public static final byte ID_MSG_UPLOAD_GEO = 0xA;

	/**
	 * ID zprávy pro odhlášení ze serveru.
	 */
	public static final byte ID_MSG_LOGOUT = 0xB;

	/**
	 * ID zprávy informující o ukonèení spojení.
	 */
	public static final byte ID_MSG_TERM_CON = 0xC;
	
}
