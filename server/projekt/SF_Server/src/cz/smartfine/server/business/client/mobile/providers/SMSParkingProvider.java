package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.networklayer.model.mobile.ParkingStatus;
import cz.smartfine.networklayer.model.mobile.SMSParkingInfo;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerSMSParkingProtocolListener;
import cz.smartfine.server.networklayer.dataprotocols.mobile.ServerSMSParkingProtocol;
import java.util.Date;

/**
 * Třída zajišťující zjištění stavu parkování vozidla.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:27
 */
public class SMSParkingProvider implements IServerSMSParkingProtocolListener {

    /**
     * Datový protokol pro přenos dat.
     */
    private ServerSMSParkingProtocol protocol;

    public SMSParkingProvider(INetworkInterface networkInterface) {
        protocol = new ServerSMSParkingProtocol(networkInterface, this);
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
    public void onSMSParkingCheckRequest(String vehicleRegistrationPlate) {
        System.out.println("SERVER: SMS PARKING RECEIVED  SPZ:" + vehicleRegistrationPlate);
        if (vehicleRegistrationPlate.equals("ABCDEF")){
            protocol.sendSMSParkingInfo(new SMSParkingInfo(ParkingStatus.ALLOWED, new Date(((new Date()).getTime() - 3600000)), new Date(((new Date()).getTime() + 3600000)), vehicleRegistrationPlate));
        }else{
            protocol.sendSMSParkingInfo(new SMSParkingInfo(ParkingStatus.NOT_ALLOWED, new Date(((new Date()).getTime() - 3600000)), new Date(((new Date()).getTime() - 600000)), vehicleRegistrationPlate));
        }
    }
}