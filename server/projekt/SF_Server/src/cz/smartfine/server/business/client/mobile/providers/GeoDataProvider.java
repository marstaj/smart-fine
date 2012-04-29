package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.android.model.Waypoint;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerGeoDataProtocolListener;
import cz.smartfine.server.networklayer.dataprotocols.mobile.ServerGeoDataProtocol;
import java.util.List;

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
    
    public GeoDataProvider(INetworkInterface networkInterface) {
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
    }
}