package cz.smartfine.android.networklayer.dataprotocols;

public class MessageIDs {

	/**
	 * ID autentiza�n� zpr�vy.
	 */
	public static final byte ID_MSG_AUTHENTICATE = 0x1;

	/**
	 * ID zpr�vy s potvrzen�m �sp�n� autentizace.
	 */
	public static final byte ID_MSG_SUC_AUTH = 0x2;

	/**
	 * ID zpr�vy s informac� o ne�sp�n� autentizaci.
	 */
	public static final byte ID_MSG_FAIL_AUTH = 0x3;

	/**
	 * ID zpr�vy pro nahr�n� PL na server.
	 */
	public static final byte ID_MSG_UPLOAD_TICKET = 0x4;

	/**
	 * ID zpr�vy pro kontrolu odcizen� p�enosn� parkovac� karty (PPK, angl. SPC).
	 */
	public static final byte ID_MSG_CHECK_SPC = 0x6;

	/**
	 * ID zpr�vy se stavem o odcizen� p�enosn� parkovac� karty (PPK, angl. SPC).
	 */
	public static final byte ID_MSG_STATUS_SPC = 0x7;

	/**
	 * ID zpr�vy pro kontrolu �asu parkov�n� p�es SMS.
	 */
	public static final byte ID_MSG_CHECK_PSMS = 0x8;

	/**
	 * ID zpr�vy se stavem parkov�n� p�es SMS.
	 */
	public static final byte ID_MSG_STATUS_PSMS = 0x9;

	/**
	 * ID zpr�vy pro nahr�n� geoloka�n�ch dat na server.
	 */
	public static final byte ID_MSG_UPLOAD_GEO = 0xA;

	/**
	 * ID zpr�vy pro odhl�en� ze serveru.
	 */
	public static final byte ID_MSG_LOGOUT = 0xB;

	/**
	 * ID zpr�vy informuj�c� o ukon�en� spojen�.
	 */
	public static final byte ID_MSG_TERM_CON = 0xC;
	
}
