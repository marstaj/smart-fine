package cz.smartfine.server.business.client.mobile.providers;
import smartfine.networklayer.model.SPCInfo;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerSPCCheckProtocolListener;

/**
 * Třída zajišťující kontrolu odcizení přenosné parkovací karty.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:27
 */
public class SPCCheckProvider implements IServerSPCCheckProtocolListener {

	public SPCCheckProvider(){

	}

	/**
	 * 
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

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

	/**
	 * Handler reagující na událost příchodu odpovědi na zjištění odcizení PPK (SPC).
	 * 
	 * @param spcInfo    Informace o stavu přenosné parkovací karty přijaté ze serveru.
	 */
	public void onReceivedSPCInfo(SPCInfo spcInfo){

	}

}