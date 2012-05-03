package cz.smartfine.server.business.client.mobile.providers;

import cz.smartfine.networklayer.model.mobile.ParkingStatus;
import cz.smartfine.networklayer.model.mobile.SMSParkingInfo;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.HibernateUtil;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerSMSParkingProtocolListener;
import cz.smartfine.server.business.client.model.SMSParkingInfoDB;
import cz.smartfine.server.networklayer.dataprotocols.mobile.ServerSMSParkingProtocol;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

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
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //vybere záznamy SMS parkování, které mají SPZ na jakou se klient dotazuje a seřadí tyto záznamy sestupně (tj. nejnovější záznam bude první)//
            Query query = session.createQuery("FROM SMSParkingInfoDB smsp WHERE smsp.vehicleRegistrationPlate = :vrp ORDER BY smsp.parkingUntil DESC");
            query.setParameter("vrp", vehicleRegistrationPlate); //nastaví SPZ

            List smspList = query.list(); //spustí dotaz na DB
            session.getTransaction().commit();
            
            //dotaz musí vrátit nějaká data, jinak nesmí parkovat//
            if (smspList.size() > 0){
                SMSParkingInfoDB lastSmsParking = ((List<SMSParkingInfoDB>) smspList).get(0); //první záznam = poslední záznam, který byl do DB uložen
                //pokud je konečný čas parkování před nynějším časem (doba parkování uplynula) -> nesmí parkovat//
                //pokud není konečný čas parkování před nynějším časem (doba parkování stále běží) -> smí parkovat//
                if (lastSmsParking.getParkingUntil().before(new Date())){
                    //odešle zprávu, že řidič přesáhl polovený čas parkování
                    protocol.sendSMSParkingInfo(new SMSParkingInfo(ParkingStatus.NOT_ALLOWED, lastSmsParking.getParkingSince(), lastSmsParking.getParkingUntil(), lastSmsParking.getVehicleRegistrationPlate()));
                }else{
                    //odešle zprávu, že řidič smí parkovat
                    protocol.sendSMSParkingInfo(new SMSParkingInfo(ParkingStatus.ALLOWED, lastSmsParking.getParkingSince(), lastSmsParking.getParkingUntil(), lastSmsParking.getVehicleRegistrationPlate()));
                }
            }else{ //žádný záznam o parkování -> nesmí parkovat
                Date date = new Date();
                protocol.sendSMSParkingInfo(new SMSParkingInfo(ParkingStatus.NOT_ALLOWED, date, date, vehicleRegistrationPlate));
            }
            
        } catch (HibernateException e) {
            e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
            Date date = new Date();
            protocol.sendSMSParkingInfo(new SMSParkingInfo(ParkingStatus.UNKNOWN_PARKING_STATUS, date, date, vehicleRegistrationPlate));
        }
    }
}