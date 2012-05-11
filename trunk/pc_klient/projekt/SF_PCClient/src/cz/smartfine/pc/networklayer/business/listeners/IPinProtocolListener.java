package cz.smartfine.pc.networklayer.business.listeners;

import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.model.pc.PINChangeFailReason;

/**
 * Interface posluchače událostí protokolu zpracovávajícího změnu PINu.
 *
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:41
 */
public interface IPinProtocolListener extends IProtocolListener {

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
     * Handler události změny PINu.
     *
     * @param newPin Nový PIN.
     */
    public void onPinChanged(int newPin);
    
    /**
     * Handler události chyby při změně PINu.
     *
     * @param reason Důvod selhání.
     */
    public void onPinChangeError(PINChangeFailReason reason);
}