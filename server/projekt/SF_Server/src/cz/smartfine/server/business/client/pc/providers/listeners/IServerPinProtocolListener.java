package cz.smartfine.server.business.client.pc.providers.listeners;

import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí z protokolu na změnu PINu.
 *
 * @author Pavel Brož
 */
public interface IServerPinProtocolListener extends IProtocolListener {

    /**
     * Handler události příjmu požadavku na změnu PINu.
     *
     * @param badgeNumber Služební číslo policisty.
     * @param pin Aktuální PIN policisty.
     */
    public void onNewPinRequest(int badgeNumber, int pin);
}
