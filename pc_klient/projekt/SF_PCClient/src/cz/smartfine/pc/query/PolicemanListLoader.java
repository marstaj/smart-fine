package cz.smartfine.pc.query;

import cz.smartfine.model.Policeman;
import cz.smartfine.model.PolicemanDB;
import cz.smartfine.networklayer.model.pc.QueryList;
import cz.smartfine.networklayer.model.pc.QueryState;
import cz.smartfine.pc.networklayer.business.listeners.IQueryProtocolListener;
import cz.smartfine.pc.networklayer.dataprotocols.QueryProtocol;
import java.util.List;

/**
 * Třída načítá ze serveru seznam policistů na služebně.
 *
 * @author Pavel Brož
 */
public class PolicemanListLoader implements IQueryProtocolListener {
    
    private QueryProtocol qp;
    private int idQuery;
    private IPolicemanListLoaderListener listener;
    
    public PolicemanListLoader(QueryProtocol qp) {
        this.qp = qp;
    }

    /**
     * Načte seznam policistů na služebně.
     *
     * @param listener Posluchač událostí.
     */
    public void loadPolicemanList(IPolicemanListLoaderListener listener) {
        this.listener = listener;
        qp.setQueryProtocolListener(this);
        idQuery = IdCreator.getID();
        
        qp.executeQuery(idQuery, QueryList.QUERY_GET_POLICEMEN_LIST, "");
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
                listener.onPolicemanListReceived((List<PolicemanDB>) result);
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
