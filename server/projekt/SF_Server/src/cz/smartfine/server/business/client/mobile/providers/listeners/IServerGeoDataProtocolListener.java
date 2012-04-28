package cz.smartfine.server.business.client.mobile.providers.listeners;

import cz.smartfine.android.model.Waypoint;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import java.util.List;

/**
 * Interface posluchače událostí protokolu zpracovávajícího přenos geolokačních dat.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:25
 */
public interface IServerGeoDataProtocolListener extends IProtocolListener {

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
     * Handler události příchodu geolokačních dat.
     * @param geoData Geolokační data.
     */
    public void geoDataReceived(List<Waypoint> geoData);
}