package cz.smartfine.android.networklayer.business;
import cz.smartfine.android.networklayer.ConnectionProvider;
import cz.smartfine.android.networklayer.business.listeners.ISMSParkingProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.SPCCheckProtocol;
import cz.smartfine.networklayer.model.mobile.SMSParkingInfo;

/**
 * Třída zajišťující zjištění stavu parkování vozidla.
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:40
 */
public class SMSParking implements ISMSParkingProtocolListener {

	public SMSParking(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Handler zpracovávající událost příchodu odpovědi o stavu parkování vozidla.
	 * 
	 * @param parkingInfo    Informace o parkování vozidla přijaté ze serveru.
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