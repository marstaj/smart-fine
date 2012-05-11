package cz.smartfine.server.business.client.pc.providers.listeners;

import cz.smartfine.networklayer.dataprotocols.interfaces.IProtocolListener;

/**
 * Interface posluchače událostí z protokolu pro dotazování.
 * @author Pavel Brož
 */
public interface IServerQueryProtocolListener extends IProtocolListener {

    /**
     * Handler události příchodu zprávy s požadavkem na databázový dotaz.
     *
     * @param id Identifikátor konkrétního dotazu.
     * @param type Typ dotazu.
     * @param parameters Parametry dotazu.
     */
    public void onQueryRequest(int id, byte type, String parameters);
}
