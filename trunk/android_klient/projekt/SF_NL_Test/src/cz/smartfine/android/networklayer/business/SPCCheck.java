package cz.smartfine.android.networklayer.business;
import cz.smartfine.android.networklayer.ConnectionProvider;
import cz.smartfine.android.networklayer.business.listeners.ISPCCheckProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.SMSParkingProtocol;
import cz.smartfine.android.networklayer.model.SPCInfo;

/**
 * Tøída zajišující kontrolu odcizení pøenosné parkovací karty.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SPCCheck implements ISPCCheckProtocolListener {

	public SPCCheck(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Handler reagující na událost pøíchodu odpovìdi na zjištìní odcizení PPK (SPC).
	 * 
	 * @param spcInfo    Informace o stavu pøenosné parkovací karty pøijaté ze serveru.
	 */
	public void onReceivedSPCInfo(SPCInfo spcInfo){

	}

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent(){

	}

}