package cz.smartfine.pc.networklayer.business.listeners;

import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;
import cz.smartfine.networklayer.model.pc.QueryState;

/**
 * Interface posluchače událostí protokolu zpracovávajícího změnu PINu.
 *
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:41
 */
public interface IQueryProtocolListener extends IProtocolListener {

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
     * Handler události příchodu odpovědi na dotaz.
     *
     * @param id Identifikátor zprávy.
     * @param state Stav dotazu.
     * @param result Výsledek dotazu.
     */
    public void onQueryResultReply(int id, QueryState state, Object result);
}