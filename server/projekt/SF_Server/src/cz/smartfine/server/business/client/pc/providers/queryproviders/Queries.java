package cz.smartfine.server.business.client.pc.providers.queryproviders;

import cz.smartfine.model.MobileDeviceDB;
import cz.smartfine.model.NetworkTicket;
import cz.smartfine.model.PolicemanDB;
import cz.smartfine.model.WaypointDB;
import cz.smartfine.networklayer.model.pc.QueryResult;
import cz.smartfine.networklayer.model.pc.QueryState;
import cz.smartfine.server.HibernateUtil;
import java.util.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Třída pro databázové dotazy. Statické metody vykonávají jednotlivé dotazy na DB.
 *
 * @author Pavel Brož
 */
public class Queries {

    /**
     * Spustí dotaz.
     *
     * @param parameters Parametry dotazu.
     * @param badgeNumber Služební číslo policisty, který vydal PL.
     * @return Výsledek dotazu.
     */
    public static QueryResult getTickets(String parameters, int badgeNumber) {
        Map<String, String> params = QueryParameterParser.splitParameters(parameters);

        if (params.containsKey("since") && QueryParameterParser.isLong(params.get("since"))
                && params.containsKey("until") && QueryParameterParser.isLong(params.get("until"))) {

            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
//System.out.println(new Date(Long.parseLong(params.get("since"))).toString());
//System.out.println(new Date(Long.parseLong(params.get("until"))).toString());
                //vybere záznamy//
                Query ticQuery = session.getNamedQuery("cz.smartfine.getalltickets.by.bn");
                ticQuery.setParameter("bn", badgeNumber); //nastaví služební číslo
                ticQuery.setTimestamp("since", new Date(Long.parseLong(params.get("since")))); //nastaví od
                ticQuery.setTimestamp("until", new Date(Long.parseLong(params.get("until")))); //nastaví do

                List<NetworkTicket> ticList = (List<NetworkTicket>) ticQuery.list(); //spustí dotaz na DB
                session.getTransaction().commit();

                for (NetworkTicket ticket : ticList) {
                    ticket.setPhotos(null);
                }

                return new QueryResult(QueryState.QUERY_OK, ticList);

            } catch (HibernateException e) {
                e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
                return new QueryResult(QueryState.QUERY_ERROR, e.getMessage());
            }
        } else {
            return new QueryResult(QueryState.QUERY_ERROR, null);
        }
    }

    /**
     * Spustí dotaz.
     *
     * @param parameters Parametry dotazu.
     * @return Výsledek dotazu.
     */
    public static QueryResult getTickets(String parameters) {
        Map<String, String> params = QueryParameterParser.splitParameters(parameters);
        if (params.containsKey("badgenumber") && QueryParameterParser.isInt(params.get("badgenumber"))) {
            return getTickets(parameters, Integer.parseInt(params.get("badgenumber")));
        } else {
            return new QueryResult(QueryState.QUERY_ERROR, null);
        }
    }

    /**
     * Spustí dotaz.
     *
     * @param parameters Parametry dotazu.
     * @param badgeNumber Služební číslo policisty, který žádá o zobrazení seznamu policistů.
     * @return Výsledek dotazu.
     */
    public static QueryResult getPolicemanList(String parameters, int badgeNumber) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //vybere záznamy//
            Query polQuery = session.getNamedQuery("cz.smartfine.getpolicemanlist.by.bn");
            polQuery.setParameter("bn", badgeNumber); //nastaví služební číslo

            List<PolicemanDB> polList = (List<PolicemanDB>) polQuery.list(); //spustí dotaz na DB
            session.getTransaction().commit();

            List<PolicemanDB> listToSend = new ArrayList<PolicemanDB>();
            for (PolicemanDB pol : polList) {
                PolicemanDB p = new PolicemanDB();
                p.setId(pol.getId());
                p.setBadgeNumber(pol.getBadgeNumber());
                p.setFirstName(pol.getFirstName());
                p.setLastName(pol.getLastName());
                listToSend.add(p);
            }
            return new QueryResult(QueryState.QUERY_OK, listToSend);

        } catch (HibernateException e) {
            e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
            return new QueryResult(QueryState.QUERY_ERROR, e.getMessage());
        }
    }

    /**
     * Spustí dotaz.
     *
     * @param parameters Parametry dotazu.
     * @return Výsledek dotazu.
     */
    public static QueryResult getGeolocationData(String parameters) {
        Map<String, String> params = QueryParameterParser.splitParameters(parameters);

        if (params.containsKey("since") && QueryParameterParser.isLong(params.get("since"))
                && params.containsKey("until") && QueryParameterParser.isLong(params.get("until"))
                && params.containsKey("badgenumber") && QueryParameterParser.isInt(params.get("badgenumber"))) {

            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();

                //vybere záznamy//
                Query wpQuery = session.getNamedQuery("cz.smartfine.getwaypoints.by.bn");
                wpQuery.setInteger("bn", Integer.parseInt(params.get("badgenumber"))); //nastaví služební číslo
                wpQuery.setTimestamp("since", new Date(Long.parseLong(params.get("since")))); //nastaví od
                wpQuery.setTimestamp("until", new Date(Long.parseLong(params.get("until")))); //nastaví do

                List<WaypointDB> wpList = (List<WaypointDB>) wpQuery.list(); //spustí dotaz na DB
                session.getTransaction().commit();

                return new QueryResult(QueryState.QUERY_OK, wpList);

            } catch (HibernateException e) {
                e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
                return new QueryResult(QueryState.QUERY_ERROR, e.getMessage());
            }
        } else {
            return new QueryResult(QueryState.QUERY_ERROR, null);
        }
    }

    /**
     * Spustí dotaz.
     *
     * @param parameters Parametry dotazu.
     * @param badgeNumber Služební číslo policisty, který ukládá nové zařízení.
     * @return Výsledek dotazu.
     */
    public static QueryResult registerNewDevice(String parameters, int badgeNumber) {
        Map<String, String> params = QueryParameterParser.splitParameters(parameters);

        if (params.containsKey("imei") && params.containsKey("name") && params.containsKey("description")) {
            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();

                //vybere záznamy o policistovy, který registruje zařízení//
                Query polQuery = session.getNamedQuery("cz.smartfine.getpoliceman.by.bn");
                polQuery.setInteger("bn", badgeNumber); //nastaví služební číslo
                polQuery.setMaxResults(1);

                PolicemanDB pol;
                pol = (PolicemanDB) polQuery.uniqueResult();

                //dotaz musí vrátit nějaká data, jinak policista neexistuje//
                if (pol == null) {
                    return new QueryResult(QueryState.QUERY_ERROR, null);
                }
                //vytvoří nové zařízení//
                MobileDeviceDB newDev = new MobileDeviceDB();
                newDev.setImei(params.get("imei"));
                newDev.setName(params.get("name"));
                newDev.setDescription(params.get("description"));
                newDev.setOffice(pol.getOffice());
                newDev.setBlocked(false);

                session.save(newDev); //uloží zařízení

                session.getTransaction().commit();
                return new QueryResult(QueryState.QUERY_OK, null);
            } catch (HibernateException e) {
                e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
                return new QueryResult(QueryState.QUERY_ERROR, e.getMessage());
            }
        } else {
            return new QueryResult(QueryState.QUERY_ERROR, null);
        }
    }

    /**
     * Spustí dotaz.
     *
     * @param parameters Parametry dotazu.
     * @return Výsledek dotazu.
     */
    public static QueryResult deleteDevice(String parameters) {
        Map<String, String> params = QueryParameterParser.splitParameters(parameters);

        if (params.containsKey("devid") && QueryParameterParser.isInt(params.get("devid"))) {
            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();

                //vybere zařízení//
                Query devQuery = session.getNamedQuery("cz.smartfine.getdevice.by.devid");
                devQuery.setInteger("devid", Integer.parseInt(params.get("devid"))); //nastaví id
                devQuery.setMaxResults(1); //maximálně jeden výsledek

                MobileDeviceDB dev = (MobileDeviceDB) devQuery.uniqueResult();
                //dotaz musí vrátit nějaká data, jinak zařízení neexistuje//
                if (dev == null) {
                    return new QueryResult(QueryState.QUERY_ERROR, null);
                }

                session.delete(dev);

                session.getTransaction().commit();
                return new QueryResult(QueryState.QUERY_OK, null);
            } catch (HibernateException e) {
                e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
                return new QueryResult(QueryState.QUERY_ERROR, e.getMessage());
            }
        } else {
            return new QueryResult(QueryState.QUERY_ERROR, null);
        }
    }

    /**
     * Spustí dotaz.
     *
     * @param parameters Parametry dotazu.
     * @param badgeNumber Služební číslo policisty, který žádá o zobrazení seznamu zařízení.
     * @return Výsledek dotazu.
     */
    public static QueryResult getDeviceList(String parameters, int badgeNumber) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //vybere záznamy o policistovy, který zobrazuje zařízení//
            Query polQuery = session.getNamedQuery("cz.smartfine.getpoliceman.by.bn");
            polQuery.setInteger("bn", badgeNumber); //nastaví služební číslo
            polQuery.setMaxResults(1);

            PolicemanDB pol;
            pol = (PolicemanDB) polQuery.uniqueResult();

            //dotaz musí vrátit nějaká data, jinak policista neexistuje//
            if (pol == null) {
                return new QueryResult(QueryState.QUERY_ERROR, null);
            }

            //vybere záznamy//
            Query devQuery = session.getNamedQuery("cz.smartfine.getdevice.by.office");
            devQuery.setParameter("of", pol.getOffice()); //nastaví služebnu

            List<MobileDeviceDB> hibDevList = (List<MobileDeviceDB>) devQuery.list(); //spustí dotaz na DB
            session.getTransaction().commit();

            List<MobileDeviceDB> devList = new ArrayList<>(); //kolekce na odeslání
            //upraví objekty, aby mohly být poslány na klienta//
            for (MobileDeviceDB hdev : hibDevList) {
                Set<PolicemanDB> pols = new HashSet<PolicemanDB>();

                for (PolicemanDB hpol : hdev.getAssociations()) {
                    hpol.setAssociations(null);
                    pols.add(hpol);
                }
                hdev.setAssociations(pols);
                devList.add(hdev);
            }

            return new QueryResult(QueryState.QUERY_OK, devList);

        } catch (HibernateException e) {
            e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
            return new QueryResult(QueryState.QUERY_ERROR, e.getMessage());
        }
    }

    /**
     * Spustí dotaz.
     *
     * @param parameters Parametry dotazu.
     * @return Výsledek dotazu.
     */
    public static QueryResult editAssoc(String parameters) {
        Map<String, String> params = QueryParameterParser.splitParameters(parameters);

        if (params.containsKey("devid") && QueryParameterParser.isInt(params.get("devid")) && params.containsKey("policemanids")) {
            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();

                List<String> policemen = QueryParameterParser.splitCollection(params.get("policemanids"));

                //vybere záznamy o policistech na seznamu//
                Query polQuery = session.getNamedQuery("cz.smartfine.getallpolicemen.by.ids");
                polQuery.setParameterList("polids", policemen.toArray());
                List<PolicemanDB> polList = (List<PolicemanDB>) polQuery.list(); //spustí dotaz na DB

                //vybere zařízení//
                Query devQuery = session.getNamedQuery("cz.smartfine.getdevice.by.devid");
                devQuery.setInteger("devid", Integer.parseInt(params.get("devid"))); //nastaví id
                devQuery.setMaxResults(1); //maximálně jeden výsledek

                MobileDeviceDB dev = (MobileDeviceDB) devQuery.uniqueResult();
                //dotaz musí vrátit nějaká data, jinak zařízení neexistuje//
                if (dev == null) {
                    return new QueryResult(QueryState.QUERY_ERROR, null);
                }

                dev.setAssociations(new HashSet<PolicemanDB>(polList));

                session.getTransaction().commit();
                return new QueryResult(QueryState.QUERY_OK, null);
            } catch (HibernateException e) {
                e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
                return new QueryResult(QueryState.QUERY_ERROR, e.getMessage());
            }
        } else {
            return new QueryResult(QueryState.QUERY_ERROR, null);
        }
    }

    /**
     * Spustí dotaz.
     *
     * @param parameters Parametry dotazu.
     * @return Výsledek dotazu.
     */
    public static QueryResult deleteTicket(String parameters) {
        Map<String, String> params = QueryParameterParser.splitParameters(parameters);
//System.out.println(params);
        if (params.containsKey("ticketid") && QueryParameterParser.isInt(params.get("ticketid"))) {

            Session session = null;
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();

                //vybere záznamy//
                Query ticQuery = session.getNamedQuery("cz.smartfine.getticket.by.id");
                ticQuery.setParameter("ticid", Integer.parseInt(params.get("ticketid"))); //nastaví id
                ticQuery.setMaxResults(1); //maximálně jeden výsledek

                NetworkTicket ticket = (NetworkTicket) ticQuery.uniqueResult();

                session.delete(ticket);
                session.getTransaction().commit();
                return new QueryResult(QueryState.QUERY_OK, null);

            } catch (HibernateException e) {
                e.printStackTrace(); //TODO: NĚCO S TÍM UDĚLAT
                return new QueryResult(QueryState.QUERY_ERROR, e.getMessage());
            }
        } else {
            return new QueryResult(QueryState.QUERY_ERROR, null);
        }
    }
}
