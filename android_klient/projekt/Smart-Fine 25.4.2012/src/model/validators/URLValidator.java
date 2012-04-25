package model.validators;

import java.net.URI;

/**
 * Pomocná tøída pro validaci
 * 
 * @author Pavel Brož
 *
 */
public class URLValidator {

	/**
	 * Testuje validitu pøedané URL
	 * @param url Reprezentuje testovanou URL, uloženou jako øetìzec znakù
	 * @return Vrací true, pokud je URL z parametru validní, jinak false
	 */
	public static boolean isURLValid(String url){
		//vyzkouší, zda je hodnota url v poøádku tím, že ji instancuje a poèká zda dojde k výjimce//
		try {
			new URI(url);
			return true;
		} catch (Exception ex) { //nová hodnota url není v poøádku
			return false;
		}
	}
}