package cz.smartfine.android.networklayer.dataprotocols.interfaces;

/**
 * Obecné rozhraní posluchaèe datového protokolu.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface IProtocolListener {

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent();

}