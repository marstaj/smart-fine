package cz.smartfine.pc.query;

import cz.smartfine.model.MobileDeviceDB;
import cz.smartfine.networklayer.model.pc.QueryList;
import cz.smartfine.networklayer.model.pc.QueryState;
import cz.smartfine.pc.networklayer.business.listeners.IQueryProtocolListener;
import cz.smartfine.pc.networklayer.dataprotocols.QueryProtocol;
import java.util.List;

/**
 * Třída načítá ze serveru seznam zařízení na služebně.
 *
 * @author Pavel Brož
 */
public class DeviceListLoader implements IQueryProtocolListener {

    protected QueryProtocol qp;
    protected int idQuery;
    protected IDeviceListLoaderListener listener;

    public DeviceListLoader(QueryProtocol qp) {
        this.qp = qp;
    }

    /**
     * Načte seznam zařízení na služebně.
     *
     * @param listener Posluchač událostí.
     */
    public void loadDeviceList(IDeviceListLoaderListener listener) {
        this.listener = listener;
        qp.setQueryProtocolListener(this);
        idQuery = IdCreator.getID();

        qp.executeQuery(idQuery, QueryList.QUERY_GET_DEVICE_LIST, "");
    }

    public void disconnectProtocol() {
        qp.removeQueryProtocolListener(this);
    }

    @Override
    public void onConnectionTerminated() {
        listener.onErrorReceived("");
    }

    @Override
    public void onMessageSent() {
    }

    @Override
    public void onQueryResultReply(int id, QueryState state, Object result) {
        if (id == idQuery) {
            if (state == QueryState.QUERY_OK) {
                listener.onDeviceListReceived((List<MobileDeviceDB>) result);
            } else {
                if (result != null) {
                    listener.onErrorReceived((String) result);
                } else {
                    listener.onErrorReceived("");
                }
            }
        }
    }
}
