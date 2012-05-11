package cz.smartfine.android.networklayer.business.listeners;

import cz.smartfine.networklayer.model.mobile.SMSParkingInfo;
import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí protokolu zpracovávajícího zjištění stavu parkování vozidla v zónách placeného stání.
 *
 * @author Pavel Brož
 * @version 1.0
 */
public interface ISMSParkingProtocolListener extends IProtocolListener {

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
     * Handler zpracovávající událost příchodu odpovědi o stavu parkování vozidla.
     *
     * @param parkingInfo Informace o parkování vozidla přijaté ze serveru.
     */
    public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo);
}