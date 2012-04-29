package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.networklayer.model.mobile.SPCInfo;
import cz.smartfine.networklayer.model.mobile.SPCStatus;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerSPCCheckProtocolListener;
import cz.smartfine.server.networklayer.dataprotocols.mobile.ServerSPCCheckProtocol;

/**
 * Třída zajišťující kontrolu odcizení přenosné parkovací karty.
 *
 * @author Pavel Brož
 * @version 1.0 @created 27-4-2012 17:00:27
 */
public class SPCCheckProvider implements IServerSPCCheckProtocolListener {

    /**
     * Datový protokol pro přenos dat.
     */
    private ServerSPCCheckProtocol protocol;

    public SPCCheckProvider(INetworkInterface networkInterface) {
        protocol = new ServerSPCCheckProtocol(networkInterface, this);
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
    public void onSPCCheckRequest(String spcNumber) {
        System.out.println("SERVER: PPK CHECK RECEIVED  NUMBER:" + spcNumber);
        if (spcNumber.equals("0123456789")){
            protocol.sendSPCInfo(new SPCInfo(spcNumber, SPCStatus.OK_SPC));
        }else{
            protocol.sendSPCInfo(new SPCInfo(spcNumber, SPCStatus.STOLEN_SPC));
        }
        
    }
}