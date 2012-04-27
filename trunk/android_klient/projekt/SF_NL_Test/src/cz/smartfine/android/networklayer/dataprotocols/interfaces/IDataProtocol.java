package cz.smartfine.android.networklayer.dataprotocols.interfaces;

/**
 * Pøedstavuje rozhraní tøídy protokolu pro pøenos dat na server.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface IDataProtocol extends IDataReceiverListener {

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol();

	/**
	 * Handler události ukonèení spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent();

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData);

}