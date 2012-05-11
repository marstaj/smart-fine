package cz.smartfine.server.business.client.mobile.providers.listeners;

import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího zjištění stavu parkování vozidla v zónách placeného stání.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:25
 */
public interface IServerSMSParkingProtocolListener extends IProtocolListener {

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
     * Handler reagující na událost příchodu dotazu, na povolený čas parkování vozidla.
     *
     * @param vehicleRegistrationPlate SPZ vozidla.
     */
    public void onSMSParkingCheckRequest(String vehicleRegistrationPlate);
}