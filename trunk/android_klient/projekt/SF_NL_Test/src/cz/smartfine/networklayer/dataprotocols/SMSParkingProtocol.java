package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.ISMSParkingProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Pøedstavuje protokol pro kontrolu èasu parkování pomocí SMS.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SMSParkingProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro pøístup k odesílání a pøíjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchaè událostí z této tøídy.
	 */
	private ISMSParkingProtocolListener smsParkingProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public SMSParkingProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro pøenos dat.
	 * @param smsParkingProtocolListener Posluchaè událostí z této tøídy.
	 */
	public SMSParkingProtocol(INetworkInterface networkInterface, ISMSParkingProtocolListener smsParkingProtocolListener){
		this.networkInterface = networkInterface;
		this.smsParkingProtocolListener = smsParkingProtocolListener;
	}

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Zjištìní stavu parkování vozidla podle SPZ.
	 * 
	 * @param vehicleRegistrationPlate    SPZ vozidla, u kterého je požadováno
	 * zjištìní stavu parkování.
	 */
	public void checkParking(String vehicleRegistrationPlate){

	}

	/**
	 * Handler události ukonèení spojení.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

	/**
	 * Odebere posluchaèe událostí protokolu pro kontrolu èasu parkování vozidel.
	 * 
	 * @param smsParkingProtocolListener    Posluchaè událostí protokolu pro kontrolu
	 * parkování.
	 */
	public void removeSMSParkingProtocolListener(ISMSParkingProtocolListener smsParkingProtocolListener){

	}

	/**
	 * Pøidá posluchaèe událostí protokolu pro kontrolu èasu parkování vozidel.
	 * 
	 * @param smsParkingProtocolListener    Posluchaè událostí protokolu pro kontrolu
	 * parkování.
	 */
	public void setSMSParkingProtocolListener(ISMSParkingProtocolListener smsParkingProtocolListener){

	}

}