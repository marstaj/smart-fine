package cz.smartfine.networklayer.business;
import cz.smartfine.networklayer.ConnectionProvider;
import cz.smartfine.networklayer.business.listeners.ISPCCheckProtocolListener;
import cz.smartfine.networklayer.dataprotocols.SMSParkingProtocol;
import cz.smartfine.networklayer.model.SPCInfo;

/**
 * T��da zaji��uj�c� kontrolu odcizen� p�enosn� parkovac� karty.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SPCCheck implements ISPCCheckProtocolListener {

	public SPCCheck(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Handler reaguj�c� na ud�lost p��chodu odpov�di na zji�t�n� odcizen� PPK (SPC).
	 * 
	 * @param spcInfo    Informace o stavu p�enosn� parkovac� karty p�ijat� ze serveru.
	 */
	public void onReceivedSPCInfo(SPCInfo spcInfo){

	}

	/**
	 * Handler zpracov�vaj�c� ud�lost ztr�ty spojen�.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler, reaguj�c� na ud�lost odesl�n� zpr�vy na server.
	 */
	public void onMessageSent(){

	}

}