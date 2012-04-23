package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.ISPCCheckProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.SPCInfo;
import cz.smartfine.networklayer.model.SPCStatus;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.MessageBuilder;

/**
 * P�edstavuje t��du pro kontrolu odcizen� p�enosn� parkovac� karty (PPK angl. SPC
 * - SUBSCRIBER PARKING CARD).
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SPCCheckProtocol implements IDataProtocol {

	/**
	 * Rozhran� pro p��stup k odes�l�n� a p��j�m�n� dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� z t�to t��dy.
	 */
	private ISPCCheckProtocolListener spcCheckProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public SPCCheckProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhran� pro p�enos dat.
	 * @param spcCheckProtocolListener Poslucha� ud�lost� z t�to t��dy.
	 */
	public SPCCheckProtocol(INetworkInterface networkInterface, ISPCCheckProtocolListener spcCheckProtocolListener){
		this.networkInterface = networkInterface;
		this.spcCheckProtocolListener = spcCheckProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrov�n� se jako poslucha�
	}

	//================================================== GET/SET ==================================================//

	/**
	 * Odebere poslucha�e ud�lost� protokolu pro kontrolu odcizen� PPK.
	 * 
	 * @param spcCheckProtocolListener    Poslucha� ud�lost� protokolu pro kontrolu
	 * odcizen� PPK.
	 */
	public void removeSPCCheckProtocolListener(ISPCCheckProtocolListener spcCheckProtocolListener){
		this.spcCheckProtocolListener = null;
	}

	/**
	 * P�id� poslucha�e ud�lost� protokolu pro kontrolu odcizen� PPK.
	 * 
	 * @param spcCheckProtocolListener    Poslucha� ud�lost� protokolu pro kontrolu
	 * odcizen� PPK.
	 */
	public void setSPCCheckProtocolListener(ISPCCheckProtocolListener spcCheckProtocolListener){
		this.spcCheckProtocolListener = spcCheckProtocolListener;
	}
	
	//================================================== HANDLERY UD�LOST� ==================================================//
	
	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){
		if (spcCheckProtocolListener != null){
			spcCheckProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent(){
		if (spcCheckProtocolListener != null){
			spcCheckProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//pokud nen� ��dn� poslucha� nen� nutn� zpr�vy zpracov�vat//
		if (spcCheckProtocolListener != null){
			
			//kontrola typu zpr�vy//
			switch(receivedData[0]){
				case MessageIDs.ID_MSG_STATUS_SPC: //�sp�n� p�ihl�en�//
					SPCStatus spcStatus; //stav PPK
					String spcNumber; //��slo PPK
					
					//zji�t�n� stavu PPK//
					switch (receivedData[1]){
						//PPK nen� hl�ena jako odcizen�//
						case ProtocolConstants.MSG_STATUS_SPC_STATUS_OK:
							spcStatus = SPCStatus.OK_SPC;
							break;
						//PPK je hl�ena jako odcizen�//
						case ProtocolConstants.MSG_STATUS_SPC_STATUS_STOLEN:
							spcStatus = SPCStatus.STOLEN_SPC;
							break;
						//nepoda�ilo se zjistit stav PPK//
						case ProtocolConstants.MSG_STATUS_SPC_STATUS_UKNOWN:
							spcStatus = SPCStatus.UKNOWN_SPC_STATUS;
							break;
						//nezn�m� hodnota//
						default:
							spcStatus = SPCStatus.UKNOWN_SPC_STATUS;
					}
					
					int spcNumLength = Conventer.byteArrayToInt(receivedData, 2); //zji�t�n� d�lky pole s ��slem PPK
					spcNumber = new String(receivedData, 6, spcNumLength); //p�eveden� pole byt� na string
					
					spcCheckProtocolListener.onReceivedSPCInfo(new SPCInfo(spcNumber, spcStatus));
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
	 * Zjist� stav p�enosn� parkovac� karty tj. jestli je h�ena jako odcizen� �i
	 * nikoliv.
	 * 
	 * @param spcNumber    ��slo p�enosn� parkovac� karty.
	 */
	public void checkSPC(String spcNumber){
		if(networkInterface != null){
			networkInterface.sendData(createSPCMessage(spcNumber));
		}
	}

	//================================================== PRIV�TN� METODY ==================================================//
	
	/**
	 * Vytv��� zpr�vu pro kontrolu odcizen� PPK.
	 * @param spcNumber ��slo p�enosn� parkovac� karty.
	 * @return Zpr�va pro odesl�n� na server.
	 */
	protected byte[] createSPCMessage(String spcNumber){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_CHECK_SPC); //identifik�tor zpr�vy
		msg.putArrayWithIntLength(spcNumber.getBytes()); //vlo�� d�lku pole a pole se znaky ��sla PPK
		
		return msg.getByteArray();
	}
	
}
