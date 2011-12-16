package model.validators;

import java.net.URI;

/**
 * Pomocn� t��da pro validaci
 * 
 * @author Pavel Bro�
 *
 */
public class URLValidator {

	/**
	 * Testuje validitu p�edan� URL
	 * @param url Reprezentuje testovanou URL, ulo�enou jako �et�zec znak�
	 * @return Vrac� true, pokud je URL z parametru validn�, jinak false
	 */
	public static boolean isURLValid(String url){
		//vyzkou��, zda je hodnota url v po��dku t�m, �e ji instancuje a po�k� zda dojde k v�jimce//
		try {
			new URI(url);
			return true;
		} catch (Exception ex) { //nov� hodnota url nen� v po��dku
			return false;
		}
	}
}