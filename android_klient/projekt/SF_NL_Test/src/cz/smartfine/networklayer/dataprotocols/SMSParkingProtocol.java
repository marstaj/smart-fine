package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.ISMSParkingProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * P�edstavuje protokol pro kontrolu �asu parkov�n� pomoc� SMS.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SMSParkingProtocol implements IDataProtocol {

	/**
	 * Rozhran� pro p��stup k odes�l�n� a p��j�m�n� dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� z t�to t��dy.
	 */
	private ISMSParkingProtocolListener smsParkingProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public SMSParkingProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhran� pro p�enos dat.
	 * @param smsParkingProtocolListener Poslucha� ud�lost� z t�to t��dy.
	 */
	public SMSParkingProtocol(INetworkInterface networkInterface, ISMSParkingProtocolListener smsParkingProtocolListener){
		this.networkInterface = networkInterface;
		this.smsParkingProtocolListener = smsParkingProtocolListener;
	}

	/**
	 * Odpoj� datov� protokol od z�kladn�ho protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Zji�t�n� stavu parkov�n� vozidla podle SPZ.
	 * 
	 * @param vehicleRegistrationPlate    SPZ vozidla, u kter�ho je po�adov�no
	 * zji�t�n� stavu parkov�n�.
	 */
	public void checkParking(String vehicleRegistrationPlate){

	}

	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

	/**
	 * Odebere poslucha�e ud�lost� protokolu pro kontrolu �asu parkov�n� vozidel.
	 * 
	 * @param smsParkingProtocolListener    Poslucha� ud�lost� protokolu pro kontrolu
	 * parkov�n�.
	 */
	public void removeSMSParkingProtocolListener(ISMSParkingProtocolListener smsParkingProtocolListener){

	}

	/**
	 * P�id� poslucha�e ud�lost� protokolu pro kontrolu �asu parkov�n� vozidel.
	 * 
	 * @param smsParkingProtocolListener    Poslucha� ud�lost� protokolu pro kontrolu
	 * parkov�n�.
	 */
	public void setSMSParkingProtocolListener(ISMSParkingProtocolListener smsParkingProtocolListener){

	}

}