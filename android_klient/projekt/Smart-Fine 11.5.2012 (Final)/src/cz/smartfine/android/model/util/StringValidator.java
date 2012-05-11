package cz.smartfine.android.model.util;

import java.net.URI;

/**
 * Pomocná třída, která poskytuje metody validace adresy serveru a mac adresy
 * mobilní tiskárny.
 * 
 * @author Martin Štajner
 * 
 */
public class StringValidator {

	/**
	 * Metoda kontroluje, zda je hodnota "Adresa serveru" ve tvaru adresy
	 * serveru.
	 * 
	 * @param url
	 *            Adresa serveru
	 * @return Zda je adresa serveru validní
	 */
	public static boolean isURLValid(String url) {
		//TODO tohle se mi nejak nezda... 
		//vyzkouší, zda je hodnota url v pořádku tak, že ji instancuje, a počká, zda dojde k vyjimce
		try {
			new URI(url);
			return true;
		} catch (Exception ex) { //nová hodnota url není v pořádku
			return false;
		}
	}

	/**
	 * Metoda kontroluje, zda je hodnota "MAC adresa tiskárny" ve tvaru mac
	 * adresy.
	 * 
	 * @param macAddress
	 *            MAC adresa tiskárny
	 * @return Zda je mac adresa validní
	 */
	public static boolean isMACValid(String macAddress) {
		// Regular expression Mac Adresy
		return macAddress.matches("([0-9a-fA-F][0-9a-fA-F]-){5}([0-9a-fA-F][0-9a-fA-F])");
	}
}