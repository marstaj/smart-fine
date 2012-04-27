package cz.smartfine.server.business.client.mobile.providers.listeners;
import smartfine.networklayer.model.SMSParkingInfo;
import smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího zjištění stavu
 * parkování vozidla v zónách placeného stání.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:25
 */
public interface IServerSMSParkingProtocolListener extends IProtocolListener, IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

	/**
	 * Handler zpracovávající událost příchodu odpovědi o stavu parkování vozidla.
	 * 
	 * @param parkingInfo    Informace o parkování vozidla přijaté ze serveru.
	 */
	public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo);

}