package cz.smartfine.networklayer.links;
import java.io.IOException;

import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Rozhraní pro transfery dat.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface ILink {

	/**
	 * Pøipojí se k serveru.
	 * @throws IOException 
	 */
	public void connect() throws IOException;
	
	/**
	 * Odpojí se od serveru.
	 */
	public void disconnect();
	
	/**
	 * Zjišuje, zda existuje spojení se serverem.
	 */
	public boolean isConnected();

	/**
	 * Odebírá posluchaèe pøíjmu dat.
	 * 
	 * @param networkInterface    Základní protokol pro pøenos dat.
	 */
	public void removeOnReceivedDataListener(INetworkInterface networkInterface);

	/**
	 * Odesílá data na server.
	 * 
	 * @param dataToSend    Data urèená pro odeslání na server.
	 * @throws IOException Chyba pøi odesílání dat.
	 */
	public void sendData(byte[] dataToSend) throws IOException;

	/**
	 * Nastavuje posluchaèe pøíjmu dat.
	 * 
	 * @param networkInterface    Základní protokol pro pøenos dat.
	 */
	public void setOnReceivedDataListener(INetworkInterface networkInterface);

}