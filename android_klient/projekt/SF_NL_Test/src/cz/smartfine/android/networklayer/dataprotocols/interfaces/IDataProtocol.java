package cz.smartfine.android.networklayer.dataprotocols.interfaces;

/**
 * P�edstavuje rozhran� t��dy protokolu pro p�enos dat na server.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface IDataProtocol extends IDataReceiverListener {

	/**
	 * Odpoj� datov� protokol od z�kladn�ho protokolu.
	 */
	public void disconnectProtocol();

	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent();

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData);

}