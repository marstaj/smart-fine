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
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public SMSParkingProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
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
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchaè
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere posluchaèe událostí protokolu pro kontrolu èasu parkování vozidel.
	 * 
	 * @param smsParkingProtocolListener    Posluchaè událostí protokolu pro kontrolu
	 * parkování.
	 */
	public void removeSMSParkingProtocolListener(ISMSParkingProtocolListener smsParkingProtocolListener){
		this.smsParkingProtocolListener = null;
	}

	/**
	 * Pøidá posluchaèe událostí protokolu pro kontrolu èasu parkování vozidel.
	 * 
	 * @param smsParkingProtocolListener    Posluchaè událostí protokolu pro kontrolu
	 * parkování.
	 */
	public void setSMSParkingProtocolListener(ISMSParkingProtocolListener smsParkingProtocolListener){
		this.smsParkingProtocolListener = smsParkingProtocolListener;
	}
	
	//================================================== HANDLERY UDÁLOSTÍ ==================================================//
	
	/**
	 * Handler události ukonèení spojení.
	 */
	public void onConnectionTerminated(){
		if (smsParkingProtocolListener != null){
			smsParkingProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){
		if (smsParkingProtocolListener != null){
			smsParkingProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//pokud není žádný posluchaè není nutné zprávy zpracovávat//
		if (smsParkingProtocolListener != null){
			
			//kontrola typu zprávy//
			switch(receivedData[0]){
				case MessageIDs.ID_MSG_STATUS_PSMS: //úspìšné pøihlášení//
					Date parkingSince;
					Date parkingUntil;
					ParkingStatus parkingStatus;
					String vehicleRegistrationPlate;
					
					//zjištìní stavu parkování//
					switch (receivedData[1]){
						//parkování je povoleno//
						case ProtocolConstants.MSG_STATUS_PSMS_STATUS_ALLOWED:
							parkingStatus = ParkingStatus.ALLOWED;
							break;
						//parkování není povoleno//
						case ProtocolConstants.MSG_STATUS_PSMS_STATUS_NOT_ALLOWED:
							parkingStatus = ParkingStatus.NOT_ALLOWED;
							break;
						//stav se nepodaøilo urèit//
						case ProtocolConstants.MSG_STATUS_SPC_STATUS_UKNOWN:
							parkingStatus = ParkingStatus.UNKNOWN_PARKING_STATUS;
							break;
						//neznámá hodnota//
						default:
							parkingStatus = ParkingStatus.UNKNOWN_PARKING_STATUS;
					}
					
					parkingSince = new Date(Conventer.byteArrayToLong(receivedData, 2)); //naètení èasu od kdy smí vozidlo  parkovat
					parkingUntil = new Date(Conventer.byteArrayToLong(receivedData, 10)); //naètení èasu do kdy smí vozidlo  parkovat
					
					int rvpLength = Conventer.byteArrayToInt(receivedData, 18); //zjištìní délky pole s SPZ
					vehicleRegistrationPlate = new String(receivedData, 22, rvpLength); //pøevedení pole bytù na string
					
					smsParkingProtocolListener.onReceivedSMSParkingInfo(new SMSParkingInfo(parkingStatus, parkingSince, parkingUntil, vehicleRegistrationPlate));
					break;
			}
		}
	}
	
	//================================================== VÝKONNÉ METODY ==================================================//
	
	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){
		if(networkInterface != null){
			networkInterface.removeOnReceivedDataListener(this);
		}
	}

	/**
	 * Zjištìní stavu parkování vozidla podle SPZ.
	 * 
	 * @param vehicleRegistrationPlate    SPZ vozidla, u kterého je požadováno
	 * zjištìní stavu parkování.
	 */
	public void checkParking(String vehicleRegistrationPlate){
		if(networkInterface != null){
			networkInterface.sendData(createPSMSMessage(vehicleRegistrationPlate));
		}
	}

	//================================================== PRIVÁTNÍ METODY ==================================================//
	
	/**
	 * Vytváøí zprávu pro kontrolu èasu parkování.
	 * @param vehicleRegistrationPlate SPZ kontrolovaného vozidla.
	 * @return Zpráva pro odeslání na server.
	 */
	protected byte[] createPSMSMessage(String vehicleRegistrationPlate){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_CHECK_PSMS); //identifikátor zprávy
		msg.putArrayWithIntLength(vehicleRegistrationPlate.getBytes()); //vloží délku pole a pole se znaky spz
		
		return msg.getByteArray();
	}
	
}
