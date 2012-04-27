package cz.smartfine.android.networklayer.dataprotocols.interfaces;

/**
 * Pøedstavuje rozhraní, deklarující posluchaèe na událost pøíjmu dat.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface IDataReceiverListener {

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData);

}