package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.android.model.Waypoint;
import cz.smartfine.networklayer.model.WaypointDB;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.HibernateUtil;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerGeoDataProtocolListener;
import cz.smartfine.server.networklayer.dataprotocols.mobile.ServerGeoDataProtocol;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Třída zajišťující nahrání geolokačních dat na server.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:25
 */
public class GeoDataProvider implements IServerGeoDataProtocolListener {

    /**
     * Datový protokol pro přenos dat.
     */
    private ServerGeoDataProtocol protocol;
    private int badgeNumber;
    /**
     * Udává, po kolika uložených objektech se má zavolat flush a clear na session.
     */
    private final int BATCH_SIZE_FLUSH = 30;
    
    public GeoDataProvider(INetworkInterface networkInterface, int badgeNumber) {
        this.badgeNumber = badgeNumber;
        protocol = new ServerGeoDataProtocol(networkInterface, this);
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
    public void geoDataReceived(List<Waypoint> geoData) {
        System.out.println("SERVER: GEO DATA RECEIVED POČET: " + geoData.size());
        
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            
            //projde geo data a uloží je do databáze//
            for (int i = 0; i < geoData.size(); i++) {
                session.save(new WaypointDB(geoData.get(i), badgeNumber)); //uloží waypoint do DB
                //po každých BATCH_SIZE_FLUSH vložení se vyčistí session//
                if (i % BATCH_SIZE_FLUSH == 0) {
                    session.flush();
                    session.clear();
                }
            }
        } catch (HibernateException e) {
            e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
        } finally {
            session.getTransaction().commit(); //potvrzení změn v DB
        }
    }
}