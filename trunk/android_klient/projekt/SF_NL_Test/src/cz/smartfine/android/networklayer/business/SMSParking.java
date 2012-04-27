package cz.smartfine.android.networklayer.business;
import cz.smartfine.android.networklayer.ConnectionProvider;
import cz.smartfine.android.networklayer.business.listeners.ISMSParkingProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.SPCCheckProtocol;
import cz.smartfine.android.networklayer.model.SMSParkingInfo;

/**
 * Tøída zajišující zjištìní stavu parkování vozidla.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SMSParking implements ISMSParkingProtocolListener {

	public SMSParking(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Handler zpracovávající událost pøíchodu odpovìdi o stavu parkování vozidla.
	 * 
	 * @param parkingInfo    Informace o parkování vozidla pøijaté ze serveru.
	 */
	public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo){

	}

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent(){

	}

}