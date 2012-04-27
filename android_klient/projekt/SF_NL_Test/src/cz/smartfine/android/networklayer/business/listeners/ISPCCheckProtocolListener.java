package cz.smartfine.android.networklayer.business.listeners;
import cz.smartfine.android.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.android.networklayer.model.SPCInfo;

/**
 * Interface poslucha�e ud�lost� protokolu zpracov�vaj�c�ho ov��ov�n� odcizen� PPK
 * (SPC).
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public interface ISPCCheckProtocolListener extends IProtocolListener {

	/**
	 * Handler zpracov�vaj�c� ud�lost ztr�ty spojen�.
	 */
	public void onConnectionTerminated();

	/**
	 * Handler, reaguj�c� na ud�lost odesl�n� zpr�vy na server.
	 */
	public void onMessageSent();

	/**
	 * Handler reaguj�c� na ud�lost p��chodu odpov�di na zji�t�n� odcizen� PPK (SPC).
	 * 
	 * @param spcInfo    Informace o stavu p�enosn� parkovac� karty p�ijat� ze serveru.
	 */
	public void onReceivedSPCInfo(SPCInfo spcInfo);

}