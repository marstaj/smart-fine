package cz.smartfine.pc.networklayer.dataprotocols;

import cz.smartfine.networklayer.dataprotocols.PCMessageIDs;
import cz.smartfine.networklayer.dataprotocols.PCProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.pc.PCClientPermission;
import cz.smartfine.networklayer.model.pc.PCLoginFailReason;
import cz.smartfine.networklayer.model.pc.QueryState;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.MessageBuilder;
import cz.smartfine.pc.networklayer.business.listeners.IQueryProtocolListener;
import java.io.*;

/**
 * Představuje třídu protokolu pro zpracování dotazů.
 *
 * @author Pavel Brož
 * @version 1.0 @created 14-4-2012 18:48:48
 */
public class QueryProtocol implements IDataProtocol {

    /**
     * Rozhraní pro přístup k odesílání a příjímání dat.
     */
    private INetworkInterface networkInterface;
    /**
     * Posluchač událostí z této třídy.
     */
    private IQueryProtocolListener queryProtocolListener;

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     */
    public QueryProtocol(INetworkInterface networkInterface) {
        this(networkInterface, null);
    }

    /**
     * Konstruktor.
     *
     * @param networkInterface Rozhraní pro přenost dat.
     * @param queryProtocolListener Posluchač událostí z této třídy.
     */
    public QueryProtocol(INetworkInterface networkInterface, IQueryProtocolListener queryProtocolListener) {
        this.networkInterface = networkInterface;
        this.queryProtocolListener = queryProtocolListener;
        this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač
    }

    //================================================== GET/SET ==================================================//
    /**
     * Odebere posluchače událostí protokolu pro přihlášení na server.
     *
     * @param queryProtocolListener Posluchač událostí z přihlašovacího protokolu.
     */
    public void removeQueryProtocolListener(IQueryProtocolListener queryProtocolListener) {
        this.queryProtocolListener = null;
    }

    /**
     * Přidá posluchače událostí protokolu pro přihlášení na server.
     *
     * @param queryProtocolListener Posluchač událostí z přihlašovacího protokolu.
     */
    public void setQueryProtocolListener(IQueryProtocolListener queryProtocolListener) {
        this.queryProtocolListener = queryProtocolListener;
    }

    //================================================== HANDLERY UDÁLOSTÍ ==================================================//
    /**
     * Handler události ukončení spojení.
     */
    @Override
    public void onConnectionTerminated() {
        if (queryProtocolListener != null) {
            queryProtocolListener.onConnectionTerminated();
        }
    }

    /**
     * Handler na zpracování události odeslání zprávy.
     *
     * @param sentData Odeslaná data.
     */
    @Override
    public void onMessageSent(byte[] sentData) {
        if (queryProtocolListener != null) {
            queryProtocolListener.onMessageSent();
        }
    }

    /**
     * Handler události příjmu dat.
     *
     * @param receivedData Přijmutá data uložená ve formě bytového pole.
     */
    @Override
    public void onReceivedData(byte[] receivedData) {
        //pokud není žádný posluchač není nutné zprávy zpracovávat//
        if (queryProtocolListener != null) {

            //kontrola typu zprávy//
            switch (receivedData[0]) {
                case PCMessageIDs.ID_MSG_QUERY_REPLY: //odpověď na dotaz//
                    
                    int id = Conventer.byteArrayToInt(receivedData, 1);
                    int replyLength = Conventer.byteArrayToInt(receivedData, 6); //zjištění délky pole
                    Object reply = null;
                    
                    //pokud zpráva není nulová, deserializuje se//
                    if (replyLength != 0) {
                        byte[] dataField = new byte[replyLength];
                        System.arraycopy(receivedData, 10, dataField, 0, replyLength);

                        reply = deserializeReplyData(dataField);
                    }
                    
                    //zjištění stavu dotazu//
                    switch (receivedData[5]) {
                        //dotaz proběhl v pořádku//
                        case PCProtocolConstants.MSG_QUERY_REPLY_STATUS_OK:
                            queryProtocolListener.onQueryResultReply(id, QueryState.QUERY_OK, reply);
                            break;
                        //při vykovávání dotazu došlo k chybě//
                        case PCProtocolConstants.MSG_QUERY_REPLY_STATUS_ERR:
                            queryProtocolListener.onQueryResultReply(id, QueryState.QUERY_ERROR, reply);
                            break;
                    }
                    break;
            }

        }
    }

    //================================================== VÝKONNÉ METODY ==================================================//
    /**
     * Odpojí datový protokol od základního protokolu.
     */
    @Override
    public void disconnectProtocol() {
        if (networkInterface != null) {
            networkInterface.removeOnReceivedDataListener(this);
        }
    }

    /**
     * Volá dotaz na serveru.
     * @param id Identifikátor konkrétního dotazu.
     * @param type Typ dotazu.
     * @param parameters Parametry dotazu.
     */
    public void executeQuery(int id, byte type, String parameters) {
        if (networkInterface != null) {
            //System.out.println("PC: QUERY PROTOCOL EXEC QUERY");
            networkInterface.sendData(createQueryMessage(id, type, parameters), this);
        }
    }

    //================================================== PRIVÁTNÍ METODY ==================================================//
    
    /**
     * Deserializuje odpověď na dotaz.
     *
     * @param data Příchozí data.
     * @return Objekt nebo null pokud došlo k chybě.
     */
    private Object deserializeReplyData(byte[] data) {
        ObjectInputStream objIS = null;
        try {
            ByteArrayInputStream objectBytes = new ByteArrayInputStream(data);
            objIS = new ObjectInputStream(objectBytes);

            Object reply = objIS.readObject();

            objIS.close();
            objectBytes.close();

            return reply;
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                objIS.close();
            } catch (IOException ex) {
            }
        }
    }
    
     /**
     * Vytváří zprávu s dotazem na server.
     *
     * @param id Identifikátor konkrétního dotazu.
     * @param type Typ dotazu.
     * @param parameters Parametry dotazu.
     * @return Zpráva pro odeslání na server.
     */
    protected byte[] createQueryMessage(int id, byte type, String parameters) {
        MessageBuilder msg = new MessageBuilder();

        msg.putByte(PCMessageIDs.ID_MSG_QUERY_REQUEST); //identifikátor zprávy
        msg.putInt(id); //id konkrétního dotazu
        msg.putByte(type); //typ dotazu
        
        msg.putArrayWithIntLength(parameters.getBytes()); //parametry dotazu
        
        return msg.getByteArray();
    }

}
