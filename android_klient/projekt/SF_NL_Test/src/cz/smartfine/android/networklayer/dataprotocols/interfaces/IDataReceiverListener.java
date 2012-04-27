package cz.smartfine.android.networklayer.dataprotocols.interfaces;

/**
 * P�edstavuje rozhran�, deklaruj�c� poslucha�e na ud�lost p��jmu dat.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface IDataReceiverListener {

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData);

}