package cz.smartfine.server.business.client.pc.providers.queryproviders;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Třída parsuje parametry dotazu.
 *
 * @author Pavel Brož
 */
public class QueryParameterParser {

    /**
     * Rozdělí řetězec parametrů na jednotlivé parametry.
     *
     * @param parameters Řetězec parametrů. Formát: jmenoa=hodnotaa[;jmenob=hodnotab;jmenoc=hodnotac...]
     * @return
     */
    public static Map<String, String> splitParameters(String parameters) {
        Map<String, String> params = new Hashtable<String, String>();

        if (!parameters.equals("")) {
            String[] strParams = parameters.split(";"); //oddělí jednotlivé parametry od sebe
            for (String param : strParams) {
                String[] nameValPair = param.split("="); //oddělí název a hodnotu
                if (nameValPair.length == 2) {
                    params.put(nameValPair[0], nameValPair[1]);
                } else if (nameValPair.length == 1) {
                    params.put(nameValPair[0], "");
                }
            }
        }
        return params;
    }

    /**
     * Rozdělí kolekci hodnot na jednotlivé hodnoty.
     *
     * @param collection Kolekce hodnot. Formát: {val1[,val2,val3...]}
     * @return
     */
    public static List<String> splitCollection(String collection) {
        ArrayList<String> list = new ArrayList<String>();

        if (!collection.equals("")) {
            String[] strValues = collection.replace('{', ' ').replace('}', ' ').trim().split(","); //odstraní závorky a rozdělí podle čárek
            //projde řežezce a vloží je do listu//
            for (String val : strValues) {
                if (val.length() > 0) {
                    list.add(val);
                }
            }
        }

        return list;
    }

    /**
     * Zjišťuje jestli je hodnota typu long.
     *
     * @param value Řetězcová hodnota.
     * @return true pokud je hodnota typu long, jinak false.
     */
    public static boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Zjišťuje jestli je hodnota typu .
     *
     * @param value Řetězcová hodnota.
     * @return true pokud je hodnota typu , jinak false.
     */
    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
