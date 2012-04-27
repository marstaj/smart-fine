package cz.smartfine.android.networklayer.networkinterface;
import cz.smartfine.android.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.android.networklayer.dataprotocols.interfaces.IDataReceiverListener;
import cz.smartfine.android.networklayer.links.ILink;

/**
 * P�edstavuje rozhran� z�kladn�ho protokolu pro p�enost dat.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface INetworkInterface extends IDataReceiverListener {

	/**
	 * Handler pro zpracov�n� ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated();

	/**
	 * Odebere poslucha�e ud�losti p��jmu dat.
	 * 
	 * @param dataProtocol    Datov� protokol poslouchaj�c� ud�lost p��jmu dat.
	 */
	public void removeOnReceivedDataListener(IDataProtocol dataProtocol);

	/**
	 * Pos�l� data na server.
	 * 
	 * @param dataToSend    Data k odesl�n�.
	 */
	public void sendData(byte[] dataToSend);

	/**
	 * P�id� poslucha�e ud�losti p��jmu dat.
	 * 
	 * @param dataProtocol    Datov� protokol poslouchaj�c� ud�lost p��jmu dat.
	 */
	public void setOnReceivedDataListener(IDataProtocol dataProtocol);

	/**
	 * Nastav� link pro p�ipojen� na s�.
	 * 
	 * @param link    S�ov� rozhran�.
	 */
	public void setLink(ILink link);
}