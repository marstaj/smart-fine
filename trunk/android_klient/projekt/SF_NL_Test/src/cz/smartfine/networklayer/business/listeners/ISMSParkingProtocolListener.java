package cz.smartfine.networklayer.business.listeners;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.model.SMSParkingInfo;

/**
 * Interface poslucha�e ud�lost� protokolu zpracov�vaj�c�ho zji�t�n� stavu
 * parkov�n� vozidla v z�n�ch placen�ho st�n�.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface ISMSParkingProtocolListener extends IProtocolListener {

	/**
	 * Handler zpracov�vaj�c� ud�lost ztr�ty spojen�.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reaguj�c� na ud�lost odesl�n� zpr�vy na server.
	 */
	public void onMessageSent();

	/**
	 * Handler zpracov�vaj�c� ud�lost p��chodu odpov�di o stavu parkov�n� vozidla.
	 * 
	 * @param parkingInfo    Informace o parkov�n� vozidla p�ijat� ze serveru.
	 */
	public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo);

}