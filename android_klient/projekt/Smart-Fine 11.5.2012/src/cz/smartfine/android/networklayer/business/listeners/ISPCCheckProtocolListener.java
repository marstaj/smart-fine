package cz.smartfine.android.networklayer.business.listeners;

import cz.smartfine.networklayer.model.mobile.SPCInfo;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího ověřování odcizení PPK (SPC).
 *
 * @author Pavel Brož
 * @version 1.0
 */
public interface ISPCCheckProtocolListener extends IProtocolListener {

    /**
     * Handler zpracovávající událost ztráty spojení.
     */
    @Override
    public void onConnectionTerminated();

    /**
     * Handler, reagující na událost odeslání zprávy na server.
     */
    @Override
    public void onMessageSent();

    /**
     * Handler reagující na událost příchodu odpovědi na zjištění odcizení PPK (SPC).
     *
     * @param spcInfo Informace o stavu přenosné parkovací karty přijaté ze serveru.
     */
    public void onReceivedSPCInfo(SPCInfo spcInfo);
}