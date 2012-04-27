package cz.smartfine.android.networklayer.business.listeners;
import cz.smartfine.android.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.android.networklayer.model.SPCInfo;

/**
 * Interface posluchaèe událostí protokolu zpracovávajícího ovìøování odcizení PPK
 * (SPC).
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface ISPCCheckProtocolListener extends IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

	/**
	 * Handler reagující na událost pøíchodu odpovìdi na zjištìní odcizení PPK (SPC).
	 * 
	 * @param spcInfo    Informace o stavu pøenosné parkovací karty pøijaté ze serveru.
	 */
	public void onReceivedSPCInfo(SPCInfo spcInfo);

}