package cz.smartfine.networklayer.business.listeners;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.model.SMSParkingInfo;

/**
 * Interface posluchaèe událostí protokolu zpracovávajícího zjištìní stavu
 * parkování vozidla v zónách placeného stání.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface ISMSParkingProtocolListener extends IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

	/**
	 * Handler zpracovávající událost pøíchodu odpovìdi o stavu parkování vozidla.
	 * 
	 * @param parkingInfo    Informace o parkování vozidla pøijaté ze serveru.
	 */
	public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo);

}