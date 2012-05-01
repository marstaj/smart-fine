package cz.smartfine.networklayer.dataprotocols.interfaces;

/**
 * Představuje rozhraní, deklarující posluchače na událost příjmu dat.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:25
 */
public interface IDataReceiverListener {

	/**
	 * Handler události příjmu dat.
	 * 
	 * @param receivedData
	 *            Přijmutá data uložená ve formě bytového pole.
	 */
	public void onReceivedData(byte[] receivedData);

}