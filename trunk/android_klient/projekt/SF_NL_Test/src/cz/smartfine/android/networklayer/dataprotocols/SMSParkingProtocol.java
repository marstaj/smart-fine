package cz.smartfine.android.networklayer.dataprotocols;
import java.util.Date;

import cz.smartfine.android.networklayer.business.listeners.ISMSParkingProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.android.networklayer.model.ParkingStatus;
import cz.smartfine.android.networklayer.model.SMSParkingInfo;
import cz.smartfine.android.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.android.networklayer.util.Conventer;
import cz.smartfine.android.networklayer.util.MessageBuilder;

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
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public SMSParkingProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
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
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrov�n� se jako poslucha�
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere poslucha�e ud�lost� protokolu pro kontrolu �asu parkov�n� vozidel.
	 * 
	 * @param smsParkingProtocolListener    Poslucha� ud�lost� protokolu pro kontrolu
	 * parkov�n�.
	 */
	public void removeSMSParkingProtocolListener(ISMSParkingProtocolListener smsParkingProtocolListener){
		this.smsParkingProtocolListener = null;
	}

	/**
	 * P�id� poslucha�e ud�lost� protokolu pro kontrolu �asu parkov�n� vozidel.
	 * 
	 * @param smsParkingProtocolListener    Poslucha� ud�lost� protokolu pro kontrolu
	 * parkov�n�.
	 */
	public void setSMSParkingProtocolListener(ISMSParkingProtocolListener smsParkingProtocolListener){
		this.smsParkingProtocolListener = smsParkingProtocolListener;
	}
	
	//================================================== HANDLERY UD�LOST� ==================================================//
	
	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){
		if (smsParkingProtocolListener != null){
			smsParkingProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent(){
		if (smsParkingProtocolListener != null){
			smsParkingProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//pokud nen� ��dn� poslucha� nen� nutn� zpr�vy zpracov�vat//
		if (smsParkingProtocolListener != null){
			
			//kontrola typu zpr�vy//
			switch(receivedData[0]){
				case MessageIDs.ID_MSG_STATUS_PSMS: //�sp�n� p�ihl�en�//
					Date parkingSince;
					Date parkingUntil;
					ParkingStatus parkingStatus;
					String vehicleRegistrationPlate;
					
					//zji�t�n� stavu parkov�n�//
					switch (receivedData[1]){
						//parkov�n� je povoleno//
						case ProtocolConstants.MSG_STATUS_PSMS_STATUS_ALLOWED:
							parkingStatus = ParkingStatus.ALLOWED;
							break;
						//parkov�n� nen� povoleno//
						case ProtocolConstants.MSG_STATUS_PSMS_STATUS_NOT_ALLOWED:
							parkingStatus = ParkingStatus.NOT_ALLOWED;
							break;
						//stav se nepoda�ilo ur�it//
						case ProtocolConstants.MSG_STATUS_SPC_STATUS_UKNOWN:
							parkingStatus = ParkingStatus.UNKNOWN_PARKING_STATUS;
							break;
						//nezn�m� hodnota//
						default:
							parkingStatus = ParkingStatus.UNKNOWN_PARKING_STATUS;
					}
					
					parkingSince = new Date(Conventer.byteArrayToLong(receivedData, 2)); //na�ten� �asu od kdy sm� vozidlo  parkovat
					parkingUntil = new Date(Conventer.byteArrayToLong(receivedData, 10)); //na�ten� �asu do kdy sm� vozidlo  parkovat
					
					int rvpLength = Conventer.byteArrayToInt(receivedData, 18); //zji�t�n� d�lky pole s SPZ
					vehicleRegistrationPlate = new String(receivedData, 22, rvpLength); //p�eveden� pole byt� na string
					
					smsParkingProtocolListener.onReceivedSMSParkingInfo(new SMSParkingInfo(parkingStatus, parkingSince, parkingUntil, vehicleRegistrationPlate));
					break;
			}
		}
	}
	
	//================================================== V�KONN� METODY ==================================================//
	
	/**
	 * Odpoj� datov� protokol od z�kladn�ho protokolu.
	 */
	public void disconnectProtocol(){
		if(networkInterface != null){
			networkInterface.removeOnReceivedDataListener(this);
		}
	}

	/**
	 * Zji�t�n� stavu parkov�n� vozidla podle SPZ.
	 * 
	 * @param vehicleRegistrationPlate    SPZ vozidla, u kter�ho je po�adov�no
	 * zji�t�n� stavu parkov�n�.
	 */
	public void checkParking(String vehicleRegistrationPlate){
		if(networkInterface != null){
			networkInterface.sendData(createPSMSMessage(vehicleRegistrationPlate));
		}
	}

	//================================================== PRIV�TN� METODY ==================================================//
	
	/**
	 * Vytv��� zpr�vu pro kontrolu �asu parkov�n�.
	 * @param vehicleRegistrationPlate SPZ kontrolovan�ho vozidla.
	 * @return Zpr�va pro odesl�n� na server.
	 */
	protected byte[] createPSMSMessage(String vehicleRegistrationPlate){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_CHECK_PSMS); //identifik�tor zpr�vy
		msg.putArrayWithIntLength(vehicleRegistrationPlate.getBytes()); //vlo�� d�lku pole a pole se znaky spz
		
		return msg.getByteArray();
	}
	
}
