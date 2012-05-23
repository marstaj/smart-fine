package cz.smartfine.android.model.util;

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
		//vyzkouší, zda je hodnota url v pořádku tak, že ji instancuje, a počká, zda dojde k vyjimce
		// Nejdříve kontroluje IP adresu
		if (url.matches("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")) {
			return true;
		} else {
			// Kontroluje webovou adresu
			return url.matches("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
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