package cz.smartfine.android.networklayer.business;
import cz.smartfine.android.networklayer.ConnectionProvider;
import cz.smartfine.android.networklayer.business.listeners.ISMSParkingProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.SPCCheckProtocol;
import cz.smartfine.android.networklayer.model.SMSParkingInfo;

/**
 * T��da zaji��uj�c� zji�t�n� stavu parkov�n� vozidla.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SMSParking implements ISMSParkingProtocolListener {

	public SMSParking(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Handler zpracov�vaj�c� ud�lost p��chodu odpov�di o stavu parkov�n� vozidla.
	 * 
	 * @param parkingInfo    Informace o parkov�n� vozidla p�ijat� ze serveru.
	 */
	public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo){

	}

	/**
	 * Handler zpracov�vaj�c� ud�lost ztr�ty spojen�.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler, reaguj�c� na ud�lost odesl�n� zpr�vy na server.
	 */
	public void onMessageSent(){

	}

}