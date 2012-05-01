package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.networklayer.model.NetworkTicket;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.HibernateUtil;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerTicketProtocolListener;
import cz.smartfine.server.networklayer.dataprotocols.mobile.ServerTicketSyncProtocol;
import org.hibernate.HibernateException;
import org.hibernate.Session;

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
    public void onTicketReceived(NetworkTicket ticket) {
        System.out.println("SERVER: TICKET RECEIVED FROM: " + ticket.getUploaderBadgeNumber());
        System.out.println("SERVER: TICKET MPZ: " + ticket.getMpz());
        System.out.println("SERVER: TICKET BG: " + ticket.getBadgeNumber());
        System.out.println("SERVER: TICKET photo: " + ticket.getPhotos().size());
        System.out.println("SERVER: TICKET RECEIVED SPZ:" + ticket.getSpz() + " CITY: " +  ticket.getCity() + " STREET: " + ticket.getStreet()  + " TIME: " + ticket.getDate().toString()  );
ticket.setMpz("AG");
ticket.setBadgeNumber(123456);

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(ticket);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
    }
}