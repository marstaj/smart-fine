package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.android.model.Ticket;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerTicketProtocolListener;
import cz.smartfine.server.networklayer.dataprotocols.mobile.ServerTicketSyncProtocol;
import java.io.File;

/**
 * Třída zajišťující nahrání PL na server.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:28
 */
public class TicketSyncProvider implements IServerTicketProtocolListener {

    /**
     * Datový protokol pro přenos dat.
     */
    private ServerTicketSyncProtocol protocol;

    public TicketSyncProvider(INetworkInterface networkInterface) {
        protocol = new ServerTicketSyncProtocol(networkInterface, this);
    }

    /**
     * Handler zpracovávající událost ztráty spojení.
     */
    @Override
    public void onConnectionTerminated() {
    }

    /**
     * Handler, reagující na událost odeslání zprávy na server.
     */
    @Override
    public void onMessageSent() {
    }

    @Override
    public void onTicketReceived(Ticket ticket, int badgeNumber) {
        System.out.println("SERVER: TICKET RECEIVED FROM: " + badgeNumber);
        System.out.println("SERVER: TICKET RECEIVED SPZ:" + ticket.getSpz() + " CITY: " +  ticket.getCity() + " STREET: " + ticket.getStreet()  + " TIME: " + ticket.getDate().toString()  );
    }
}