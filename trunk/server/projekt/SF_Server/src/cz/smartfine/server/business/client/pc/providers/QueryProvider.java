package cz.smartfine.server.business.client.pc.providers;

import cz.smartfine.networklayer.model.pc.QueryList;
import cz.smartfine.networklayer.model.pc.QueryResult;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.pc.PCClientServer;
import cz.smartfine.server.business.client.pc.providers.listeners.IServerQueryProtocolListener;
import cz.smartfine.server.business.client.pc.providers.queryproviders.Queries;
import cz.smartfine.server.networklayer.dataprotocols.pc.ServerQueryProtocol;
import java.io.IOException;

/**
 * Zprostředkovatel dotazů na server.
 *
 * @author Pavel Brož
 */
public class QueryProvider implements IServerQueryProtocolListener {

    /**
     * Odkaz na server.
     */
    private PCClientServer clientServer;
    /**
     * Datový protokol pro přenos dat.
     */
    private ServerQueryProtocol protocol;

    public QueryProvider(INetworkInterface networkInterface, PCClientServer clientServer) {
        this.clientServer = clientServer;
        this.protocol = new ServerQueryProtocol(networkInterface, this);
    }

    @Override
    public void onQueryRequest(int id, byte type, String parameters) {
        QueryResult result;
        //zjistí typ zprávy, ověří oprávnění, provede dotaz//
        switch (type) {
            case QueryList.QUERY_GET_OWN_TICKETS:
                if (clientServer.getPermissions().isPermShowOwnTickets()) {
                    result = Queries.getTickets(parameters, clientServer.getBadgeNumber());
                } else {
                    clientServer.close();
                    return;
                }
                break;
            case QueryList.QUERY_GET_TICKETS:
                if (clientServer.getPermissions().isPermShowTickets()) {
                    result = Queries.getTickets(parameters);
                } else {
                    clientServer.close();
                    return;
                }
                break;
            case QueryList.QUERY_GET_POLICEMEN_LIST:
                if (clientServer.getPermissions().isPermShowTickets() || clientServer.getPermissions().isPermAdminAssoc() || clientServer.getPermissions().isPermShowGeodata()) {
                    result = Queries.getPolicemanList(parameters, clientServer.getBadgeNumber());
                } else {
                    clientServer.close();
                    return;
                }
                break;
            case QueryList.QUERY_GET_GEOLOCATION_DATA:
                if (clientServer.getPermissions().isPermShowGeodata()) {
                    result = Queries.getGeolocationData(parameters);
                } else {
                    clientServer.close();
                    return;
                }
                break;
            case QueryList.QUERY_REGISTER_NEW_DEVICE:
                if (clientServer.getPermissions().isPermAdminDevices()) {
                    result = Queries.registerNewDevice(parameters, clientServer.getBadgeNumber());
                } else {
                    clientServer.close();
                    return;
                }
                break;
            case QueryList.QUERY_DELETE_DEVICE:
                if (clientServer.getPermissions().isPermAdminDevices()) {
                    result = Queries.deleteDevice(parameters);
                } else {
                    clientServer.close();
                    return;
                }
                break;
            case QueryList.QUERY_GET_DEVICE_LIST:
                if (clientServer.getPermissions().isPermAdminDevices() || clientServer.getPermissions().isPermAdminAssoc()) {
                    result = Queries.getDeviceList(parameters, clientServer.getBadgeNumber());
                } else {
                    clientServer.close();
                    return;
                }
                break;
            case QueryList.QUERY_EDIT_ASSOC:
                if (clientServer.getPermissions().isPermAdminAssoc()) {
                    result = Queries.editAssoc(parameters);
                } else {
                    clientServer.close();
                    return;
                }
                break;
            case QueryList.QUERY_DELETE_TICKET:
                if (clientServer.getPermissions().isPermDeleteTickets()) {
                    result = Queries.deleteTicket(parameters);
                } else {
                    clientServer.close();
                    return;
                }
                break;
            default:
                clientServer.close();
                return;
        }
        
        try {
            protocol.sendQueryResult(id, result.getState(), result.getResult());
        } catch (IOException ex) {
            //nic se nedělá
        }
    }

    @Override
    public void onConnectionTerminated() {
    }

    @Override
    public void onMessageSent() {
    }
}
