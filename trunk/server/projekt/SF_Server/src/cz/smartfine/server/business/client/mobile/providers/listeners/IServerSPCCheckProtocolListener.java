package cz.smartfine.server.business.client.mobile.providers.listeners;
import smartfine.networklayer.model.SPCInfo;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího ověřování odcizení PPK
 * (SPC).
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:25
 */
public interface IServerSPCCheckProtocolListener extends IProtocolListener, IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

	/**
	 * Handler reagující na událost příchodu odpovědi na zjištění odcizení PPK (SPC).
	 * 
	 * @param spcInfo    Informace o stavu přenosné parkovací karty přijaté ze serveru.
	 */
	public void onReceivedSPCInfo(SPCInfo spcInfo);

}