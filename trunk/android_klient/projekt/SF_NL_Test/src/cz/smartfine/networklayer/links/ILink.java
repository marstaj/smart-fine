package cz.smartfine.networklayer.links;
import java.io.IOException;

import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Rozhran� pro transfery dat.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface ILink {

	/**
	 * P�ipoj� se k serveru.
	 * @throws IOException 
	 */
	public void connect() throws IOException;
	
	/**
	 * Odpoj� se od serveru.
	 */
	public void disconnect();
	
	/**
	 * Zji��uje, zda existuje spojen� se serverem.
	 */
	public boolean isConnected();

	/**
	 * Odeb�r� poslucha�e p��jmu dat.
	 * 
	 * @param networkInterface    Z�kladn� protokol pro p�enos dat.
	 */
	public void removeOnReceivedDataListener(INetworkInterface networkInterface);

	/**
	 * Odes�l� data na server.
	 * 
	 * @param dataToSend    Data ur�en� pro odesl�n� na server.
	 * @throws IOException Chyba p�i odes�l�n� dat.
	 */
	public void sendData(byte[] dataToSend) throws IOException;

	/**
	 * Nastavuje poslucha�e p��jmu dat.
	 * 
	 * @param networkInterface    Z�kladn� protokol pro p�enos dat.
	 */
	public void setOnReceivedDataListener(INetworkInterface networkInterface);

}