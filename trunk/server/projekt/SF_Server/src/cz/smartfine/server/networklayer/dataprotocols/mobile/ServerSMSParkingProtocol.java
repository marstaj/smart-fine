package cz.smartfine.server.networklayer.mobile.dataprotocols;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerSMSParkingProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import smartfine.networklayer.business.listeners.ISMSParkingProtocolListener;
import smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Představuje protokol pro kontrolu času parkování pomocí SMS.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:27
 */
public class ServerSMSParkingProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro přístup k odesílání a příjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchač událostí z této třídy.
	 */
	private IServerSMSParkingProtocolListener smsParkingProtocolListener;

	public ServerSMSParkingProtocol(){

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenost dat.
	 */
	public ServerSMSParkingProtocol(INetworkInterface networkInterface){

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenos dat.
	 * @param smsParkingProtocolListener    Posluchač událostí z této třídy.
	 */
	public ServerSMSParkingProtocol(INetworkInterface networkInterface, IServerSMSParkingProtocolListener smsParkingProtocolListener){

	}

	/**
	 * ================================================== KONSTRUKTORY & DESTRUKTORY
	 * ==================================================
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

	}

	/**
	 * Vytváří zprávu pro kontrolu času parkování.
	 * @return Zpráva pro odeslání na server.
	 * 
	 * @param vehicleRegistrationPlate    SPZ kontrolovaného vozidla.
	 */
	protected byte[] createPSMSMessage(String vehicleRegistrationPlate){
		return 0;
	}

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Zjištění stavu parkování vozidla podle SPZ.
	 * 
	 * @param vehicleRegistrationPlate    SPZ vozidla, u kterého je požadováno
	 * zjištění stavu parkování.
	 */
	public void checkParking(String vehicleRegistrationPlate){

	}

	/**
	 * Handler události ukončení spojení.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler události příjmu dat.
	 * 
	 * @param receivedData    Přijmutá data uložená ve formě bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

	/**
	 * Odebere posluchače událostí protokolu pro kontrolu času parkování vozidel.
	 * 
	 * @param smsParkingProtocolListener    Posluchač událostí protokolu pro kontrolu
	 * parkování.
	 */
	public void removeSMSParkingProtocolListener(IServerSMSParkingProtocolListener smsParkingProtocolListener){

	}

	/**
	 * Přidá posluchače událostí protokolu pro kontrolu času parkování vozidel.
	 * 
	 * @param smsParkingProtocolListener    Posluchač událostí protokolu pro kontrolu
	 * parkování.
	 */
	public void setSMSParkingProtocolListener(IServerSMSParkingProtocolListener smsParkingProtocolListener){

	}

}