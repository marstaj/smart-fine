package cz.smartfine.android.networklayer.business;

import cz.smartfine.android.networklayer.business.listeners.ISPCCheckProtocolListener;
import cz.smartfine.networklayer.model.mobile.SPCInfo;

/**
 * Třída zajišťující kontrolu odcizení přenosné parkovací karty.
 * 
 * @author Pavel Brož
 * @version 1.0
 */
public class SPCCheck implements ISPCCheckProtocolListener {

	public SPCCheck() {

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Handler reagující na událost příchodu odpovědi na zjištění odcizení PPK
	 * (SPC).
	 * 
	 * @param spcInfo
	 *            Informace o stavu přenosné parkovací karty přijaté ze serveru.
	 */
	public void onReceivedSPCInfo(SPCInfo spcInfo) {

	}

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated() {

	}

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent() {

	}

}