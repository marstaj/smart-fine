package cz.smartfine.android.networklayer.dataprotocols;
import java.util.Date;

import cz.smartfine.android.networklayer.business.listeners.ISMSParkingProtocolListener;
import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.MobileProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.mobile.ParkingStatus;
import cz.smartfine.networklayer.model.mobile.SMSParkingInfo;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.MessageBuilder;
/**
 * Představuje protokol pro kontrolu času parkování pomocí SMS.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SMSParkingProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro přístup k odesílání a příjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchač událostí z této třídy.
	 */
	private ISMSParkingProtocolListener smsParkingProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro přenost dat.
	 */
	public SMSParkingProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenos dat.
	 * @param smsParkingProtocolListener Posluchač událostí z této třídy.
	 */
	public SMSParkingProtocol(INetworkInterface networkInterface, ISMSParkingProtocolListener smsParkingProtocolListener){
		this.networkInterface = networkInterface;
		this.smsParkingProtocolListener = smsParkingProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere posluchače událostí protokolu pro kontrolu času parkování vozidel.
	 * 
	 * @param smsParkingProtocolListener    Posluchač událostí protokolu pro kontrolu
	 * parkování.
	 */
	public void removeSMSParkingProtocolListener(ISMSParkingProtocolListener smsParkingProtocolListener){
		this.smsParkingProtocolListener = null;
	}

	/**
	 * Přidá posluchače událostí protokolu pro kontrolu času parkování vozidel.
	 * 
	 * @param smsParkingProtocolListener    Posluchač událostí protokolu pro kontrolu
	 * parkování.
	 */
	public void setSMSParkingProtocolListener(ISMSParkingProtocolListener smsParkingProtocolListener){
		this.smsParkingProtocolListener = smsParkingProtocolListener;
	}
	
	//================================================== HANDLERY UDÁLOSTÍ ==================================================//
	
	/**
	 * Handler události ukončení spojení.
	 */
	public void onConnectionTerminated(){
		if (smsParkingProtocolListener != null){
			smsParkingProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 * @param sentData Odeslaná data.   
	 */
	public void onMessageSent(byte[] sentData){
		if (smsParkingProtocolListener != null){
			smsParkingProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler události příjmu dat.
	 * 
	 * @param receivedData    Přijmutá data uložená ve formě bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//pokud není žádný posluchač není nutné zprávy zpracovávat//
		if (smsParkingProtocolListener != null){
			
			//kontrola typu zprávy//
			switch(receivedData[0]){
				case MobileMessageIDs.ID_MSG_STATUS_PSMS: //úspěšné přihlášení//
					Date parkingSince;
					Date parkingUntil;
					ParkingStatus parkingStatus;
					String vehicleRegistrationPlate;
					
					//zjištění stavu parkování//
					switch (receivedData[1]){
						//parkování je povoleno//
						case MobileProtocolConstants.MSG_STATUS_PSMS_STATUS_ALLOWED:
							parkingStatus = ParkingStatus.ALLOWED;
							break;
						//parkování není povoleno//
						case MobileProtocolConstants.MSG_STATUS_PSMS_STATUS_NOT_ALLOWED:
							parkingStatus = ParkingStatus.NOT_ALLOWED;
							break;
						//stav se nepodařilo určit//
						case MobileProtocolConstants.MSG_STATUS_SPC_STATUS_UKNOWN:
							parkingStatus = ParkingStatus.UNKNOWN_PARKING_STATUS;
							break;
						//neznámá hodnota//
						default:
							parkingStatus = ParkingStatus.UNKNOWN_PARKING_STATUS;
					}
					
					parkingSince = new Date(Conventer.byteArrayToLong(receivedData, 2)); //načtení času od kdy smí vozidlo  parkovat
					parkingUntil = new Date(Conventer.byteArrayToLong(receivedData, 10)); //načtení času do kdy smí vozidlo  parkovat
					
					int rvpLength = Conventer.byteArrayToInt(receivedData, 18); //zjištění délky pole s SPZ
					vehicleRegistrationPlate = new String(receivedData, 22, rvpLength); //převedení pole bytů na string
					
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
	 * Zjištění stavu parkování vozidla podle SPZ.
	 * 
	 * @param vehicleRegistrationPlate    SPZ vozidla, u kterého je požadováno
	 * zjištění stavu parkování.
	 */
	public void checkParking(String vehicleRegistrationPlate){
		if(networkInterface != null){
			networkInterface.sendData(createPSMSMessage(vehicleRegistrationPlate), this);
		}
	}

	//================================================== PRIVÁTNÍ METODY ==================================================//
	
	/**
	 * Vytváří zprávu pro kontrolu času parkování.
	 * @param vehicleRegistrationPlate SPZ kontrolovaného vozidla.
	 * @return Zpráva pro odeslání na server.
	 */
	protected byte[] createPSMSMessage(String vehicleRegistrationPlate){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MobileMessageIDs.ID_MSG_CHECK_PSMS); //identifikátor zprávy
		msg.putArrayWithIntLength(vehicleRegistrationPlate.getBytes()); //vloží délku pole a pole se znaky spz
		
		return msg.getByteArray();
	}
	
}
