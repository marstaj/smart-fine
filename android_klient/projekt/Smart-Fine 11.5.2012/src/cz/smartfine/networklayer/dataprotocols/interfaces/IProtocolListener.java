package cz.smartfine.networklayer.dataprotocols.interfaces;

/**
 * Obecné rozhraní posluchače datového protokolu.
 *
 * @author Pavel Brož
 * @version 1.0
 */
public interface IProtocolListener {

    /**
     * Handler zpracovávající událost ztráty spojení.
     */
    public void onConnectionTerminated();

    /**
     * Handler, reagující na událost odeslání zprávy na server.
     */
    public void onMessageSent();
}