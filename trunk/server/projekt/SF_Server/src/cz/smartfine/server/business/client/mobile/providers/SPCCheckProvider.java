package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.networklayer.model.mobile.SPCInfo;
import cz.smartfine.networklayer.model.mobile.SPCStatus;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.HibernateUtil;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerSPCCheckProtocolListener;
import cz.smartfine.server.business.client.model.SPCInfoDB;
import cz.smartfine.server.networklayer.dataprotocols.mobile.ServerSPCCheckProtocol;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

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
        //System.out.println("SERVER: PPK CHECK RECEIVED NUMBER:" + spcNumber);
        
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //vybere záznamy o PPK, které mají identifikační číslo na jaké se klient dotazuje//
            Query query = session.getNamedQuery("cz.smartfine.getspcinfo.by.spcnum");
            query.setParameter("spcn", spcNumber); //nastaví číslo PPK

            List spciList = query.list(); //spustí dotaz na DB
            session.getTransaction().commit();

            //dotaz musí vrátit nějaká data, jinak není PPK registrovaná jako kradená//
            if (spciList.size() > 0) {
                SPCInfoDB spcInfo = ((List<SPCInfoDB>) spciList).get(0); //první záznam
                protocol.sendSPCInfo(new SPCInfo(spcInfo.getSpcNumber(), SPCStatus.STOLEN_SPC));
            } else { //žádný záznam -> PPK není hlášena jako kradená
                protocol.sendSPCInfo(new SPCInfo(spcNumber, SPCStatus.OK_SPC));
            }
        } catch (HibernateException e) {
            //e.printStackTrace();
            protocol.sendSPCInfo(new SPCInfo(spcNumber, SPCStatus.UKNOWN_SPC_STATUS));
        }
    }
}