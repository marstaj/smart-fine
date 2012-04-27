package cz.smartfine.server.business.client.mobile.providers;
import smartfine.networklayer.model.SMSParkingInfo;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerSMSParkingProtocolListener;

/**
 * Třída zajišťující zjištění stavu parkování vozidla.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:27
 */
public class SMSParkingProvider implements IServerSMSParkingProtocolListener {

	public SMSParkingProvider(){

	}

	/**
	 * 
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

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

	/**
	 * Handler zpracovávající událost příchodu odpovědi o stavu parkování vozidla.
	 * 
	 * @param parkingInfo    Informace o parkování vozidla přijaté ze serveru.
	 */
	public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo){

	}

}