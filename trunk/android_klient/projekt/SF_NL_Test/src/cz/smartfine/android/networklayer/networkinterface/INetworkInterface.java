package cz.smartfine.android.networklayer.networkinterface;
import cz.smartfine.android.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.android.networklayer.dataprotocols.interfaces.IDataReceiverListener;
import cz.smartfine.android.networklayer.links.ILink;

/**
 * Pøedstavuje rozhraní základního protokolu pro pøenost dat.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface INetworkInterface extends IDataReceiverListener {

	/**
	 * Handler pro zpracování události ukonèení spojení.
	 */
	public void onConnectionTerminated();

	/**
	 * Odebere posluchaèe události pøíjmu dat.
	 * 
	 * @param dataProtocol    Datovı protokol poslouchající událost pøíjmu dat.
	 */
	public void removeOnReceivedDataListener(IDataProtocol dataProtocol);

	/**
	 * Posílá data na server.
	 * 
	 * @param dataToSend    Data k odeslání.
	 */
	public void sendData(byte[] dataToSend);

	/**
	 * Pøidá posluchaèe události pøíjmu dat.
	 * 
	 * @param dataProtocol    Datovı protokol poslouchající událost pøíjmu dat.
	 */
	public void setOnReceivedDataListener(IDataProtocol dataProtocol);

	/**
	 * Nastaví link pro pøipojení na sí.
	 * 
	 * @param link    Síové rozhraní.
	 */
	public void setLink(ILink link);
}