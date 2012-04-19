package cz.smartfine.networklayer.networkinterface;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataReceiverListener;

/**
 * Pøedstavuje rozhraní základního protokolu pro pøenost dat.
 * @author Pavel Brož
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
	 * @param dataProtocol    Datový protokol poslouchající událost pøíjmu dat.
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
	 * @param dataProtocol    Datový protokol poslouchající událost pøíjmu dat.
	 */
	public void setOnReceivedDataListener(IDataProtocol dataProtocol);

}