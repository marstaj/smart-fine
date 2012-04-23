package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.ISPCCheckProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.SPCInfo;
import cz.smartfine.networklayer.model.SPCStatus;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.MessageBuilder;

/**
 * Pøedstavuje tøídu pro kontrolu odcizení pøenosné parkovací karty (PPK angl. SPC
 * - SUBSCRIBER PARKING CARD).
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SPCCheckProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro pøístup k odesílání a pøíjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchaè událostí z této tøídy.
	 */
	private ISPCCheckProtocolListener spcCheckProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public SPCCheckProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro pøenos dat.
	 * @param spcCheckProtocolListener Posluchaè událostí z této tøídy.
	 */
	public SPCCheckProtocol(INetworkInterface networkInterface, ISPCCheckProtocolListener spcCheckProtocolListener){
		this.networkInterface = networkInterface;
		this.spcCheckProtocolListener = spcCheckProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchaè
	}

	//================================================== GET/SET ==================================================//

	/**
	 * Odebere posluchaèe událostí protokolu pro kontrolu odcizení PPK.
	 * 
	 * @param spcCheckProtocolListener    Posluchaè událostí protokolu pro kontrolu
	 * odcizení PPK.
	 */
	public void removeSPCCheckProtocolListener(ISPCCheckProtocolListener spcCheckProtocolListener){
		this.spcCheckProtocolListener = null;
	}

	/**
	 * Pøidá posluchaèe událostí protokolu pro kontrolu odcizení PPK.
	 * 
	 * @param spcCheckProtocolListener    Posluchaè událostí protokolu pro kontrolu
	 * odcizení PPK.
	 */
	public void setSPCCheckProtocolListener(ISPCCheckProtocolListener spcCheckProtocolListener){
		this.spcCheckProtocolListener = spcCheckProtocolListener;
	}
	
	//================================================== HANDLERY UDÁLOSTÍ ==================================================//
	
	/**
	 * Handler události ukonèení spojení.
	 */
	public void onConnectionTerminated(){
		if (spcCheckProtocolListener != null){
			spcCheckProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){
		if (spcCheckProtocolListener != null){
			spcCheckProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//pokud není žádný posluchaè není nutné zprávy zpracovávat//
		if (spcCheckProtocolListener != null){
			
			//kontrola typu zprávy//
			switch(receivedData[0]){
				case MessageIDs.ID_MSG_STATUS_SPC: //úspìšné pøihlášení//
					SPCStatus spcStatus; //stav PPK
					String spcNumber; //èíslo PPK
					
					//zjištìní stavu PPK//
					switch (receivedData[1]){
						//PPK není hlášena jako odcizená//
						case ProtocolConstants.MSG_STATUS_SPC_STATUS_OK:
							spcStatus = SPCStatus.OK_SPC;
							break;
						//PPK je hlášena jako odcizená//
						case ProtocolConstants.MSG_STATUS_SPC_STATUS_STOLEN:
							spcStatus = SPCStatus.STOLEN_SPC;
							break;
						//nepodaøilo se zjistit stav PPK//
						case ProtocolConstants.MSG_STATUS_SPC_STATUS_UKNOWN:
							spcStatus = SPCStatus.UKNOWN_SPC_STATUS;
							break;
						//neznámá hodnota//
						default:
							spcStatus = SPCStatus.UKNOWN_SPC_STATUS;
					}
					
					int spcNumLength = Conventer.byteArrayToInt(receivedData, 2); //zjištìní délky pole s èíslem PPK
					spcNumber = new String(receivedData, 6, spcNumLength); //pøevedení pole bytù na string
					
					spcCheckProtocolListener.onReceivedSPCInfo(new SPCInfo(spcNumber, spcStatus));
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
	 * Zjistí stav pøenosné parkovací karty tj. jestli je hášena jako odcizená èi
	 * nikoliv.
	 * 
	 * @param spcNumber    Èíslo pøenosné parkovací karty.
	 */
	public void checkSPC(String spcNumber){
		if(networkInterface != null){
			networkInterface.sendData(createSPCMessage(spcNumber));
		}
	}

	//================================================== PRIVÁTNÍ METODY ==================================================//
	
	/**
	 * Vytváøí zprávu pro kontrolu odcizení PPK.
	 * @param spcNumber Èíslo pøenosné parkovací karty.
	 * @return Zpráva pro odeslání na server.
	 */
	protected byte[] createSPCMessage(String spcNumber){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_CHECK_SPC); //identifikátor zprávy
		msg.putArrayWithIntLength(spcNumber.getBytes()); //vloží délku pole a pole se znaky èísla PPK
		
		return msg.getByteArray();
	}
	
}
