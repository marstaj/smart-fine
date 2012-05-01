package cz.smartfine.pc.networklayer.business.listeners;

import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.model.pc.PCClientPermission;
import cz.smartfine.networklayer.model.pc.PCLoginFailReason;

/**
 * Interface posluchače událostí protokolu zpracovávajícího přihlašování na server.
 *
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:41
 */
public interface ILoginProtocolListener extends IProtocolListener {

    /**
     * Handler zpracovávající událost ztráty spojení.
     */
    @Override
    public void onConnectionTerminated();

    /**
     * Handler události správného přihlášení.
     *
     * @param permissions Oprávnění pro přístup k serveru.
     */
    public void onLoginConfirmed(PCClientPermission permissions);

    /**
     * Handler, reagující na událost odeslání zprávy na server.
     */
    @Override
    public void onMessageSent();

    /**
     * Handler události chybného přihlášení.
     *
     * @param reason Důvod selhání přihlášení.
     */
    public void onLoginFailed(PCLoginFailReason reason);
}