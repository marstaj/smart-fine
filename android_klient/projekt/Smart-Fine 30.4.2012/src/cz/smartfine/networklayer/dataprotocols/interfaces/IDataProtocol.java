package cz.smartfine.networklayer.dataprotocols.interfaces;

/**
 * Představuje rozhraní třídy protokolu pro přenos dat na server.
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:47
 */
public interface IDataProtocol extends IDataReceiverListener {

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol();

	/**
	 * Handler události ukončení spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler na zpracování události odeslání zprávy.
	 * @param sentData Odeslaná data.
	 */
	public void onMessageSent(byte[] sentData);

	/**
	 * Handler události příjmu dat.
	 * 
	 * @param receivedData    Přijmutá data uložená ve formě bytového pole.
	 */
	public void onReceivedData(byte[] receivedData);

}