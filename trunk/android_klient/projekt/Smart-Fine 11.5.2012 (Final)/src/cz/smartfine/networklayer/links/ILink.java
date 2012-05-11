package cz.smartfine.networklayer.links;

import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import java.io.IOException;

/**
 * Rozhraní link pro transfery dat.
 * 
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:44
 */
public interface ILink {

	/**
	 * Připojí se k protějšímu linku.
	 * 
	 * @exception IOException
	 *                Problém při vytváření socketu.
	 */
	public void connect() throws IOException;

	/**
	 * Začne naslouchat na soketu.
	 */
	public void listen();

	/**
	 * Ukončí síťové spojení.
	 */
	public void disconnect();

	/**
	 * Zjišťuje, zda existuje vytvořené síťové spojení.
	 */
	public boolean isConnected();

	/**
	 * Odebírá posluchače příjmu dat.
	 * 
	 * @param networkInterface
	 *            Základní rozhraní pro přenos dat.
	 */
	public void removeOnReceivedDataListener(INetworkInterface networkInterface);

	/**
	 * Odesílá data na síť.
	 * 
	 * @param dataToSend
	 *            Data určená pro odeslání.
	 * @exception IOException
	 *                Chyba při odesílání dat.
	 */
	public void sendData(byte[] dataToSend) throws IOException;

	/**
	 * Nastavuje posluchače příjmu dat.
	 * 
	 * @param networkInterface
	 *            Základní rozhraní pro přenos dat.
	 */
	public void setOnReceivedDataListener(INetworkInterface networkInterface);
}