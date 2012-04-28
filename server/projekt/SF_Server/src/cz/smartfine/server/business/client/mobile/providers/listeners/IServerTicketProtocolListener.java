package cz.smartfine.server.business.client.mobile.providers.listeners;

import cz.smartfine.android.model.Ticket;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího přenos parkovacích lístků.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:26
 */
public interface IServerTicketProtocolListener extends IProtocolListener {

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
     * Handler reagující na událost příchodu parkovacího lístku.
     * @param ticket Parkovací lístek.
     * @param badgeNumber Služební číslo policisty, který nahrává PL.
     */
    public void onTicketReceived(Ticket ticket, int badgeNumber);
}