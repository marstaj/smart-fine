package cz.smartfine.android.model;

import cz.smartfine.android.model.util.StringValidator;
import android.content.Context;
import android.preference.*;

/**
 * Třída představuje objekt, který zajišťuje přístup k hodnotám uloženým v
 * preferencích aplikace.
 * 
 * @author Martin Štajner, Pavel Brož
 */
public class PrefManager {

	/**
	 * Instance třídy nastavení aplikace
	 */
	private static PrefManager prefManager;

	/**
	 * Privátní konstruktor
	 */
	private PrefManager() {
	}

	/**
	 * Metoda vrátí instanci třídy nastavení aplikace
	 * 
	 * @return Instance třídy
	 */
	public static PrefManager getInstance() {
		if (prefManager == null) {
			prefManager = new PrefManager();
		}
		return prefManager;
	}

	/**
	 * Metoda získá hodnotu "Služební číslo policisty" z preferencí.
	 * 
	 * @param context
	 *            Kontext aktivity, ze které je metoda volána
	 * @return Služební číslo policisty
	 */
	public String getBadgeNumber(Context context) {
		//získá přístup k nastavení aplikace a poté záský hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_badgenumber", null);
	}

	/**
	 * Metoda vloží hodnotu "Služební číslo policisty" do preferencí.
	 * 
	 * @param context
	 *            Kontext aktivity, ze které je metoda volána
	 * @param badgeNumber
	 *            Služební číslo policisty
	 * @return True, když vložení proběhlo úspěšně, jinak false
	 */
	public boolean setBadgeNumber(Context context, String badgeNumber) {
		//získá přístup k nastavení aplikace, spustí editaci, a vloží hodnotu
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_badgenumber", badgeNumber).commit();
	}

	/**
	 * Metoda získá hodnotu "Město" z preferencí.
	 * 
	 * @param context
	 *            Kontext aktivity, ze které je metoda volána
	 * @return Město
	 */
	public String getCity(Context context) {
		//získá přístup k nastavení aplikace a poté záský hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_city", null);
	}

	/**
	 * Metoda vloží hodnotu "Město" do preferencí.
	 * 
	 * @param context
	 *            Kontext aktivity, ze které je metoda volána
	 * @param city
	 *            Město
	 * @return True, když vložení proběhlo úspěšně, jinak false
	 */
	public boolean setCity(Context context, String city) {
		//získá přístup k nastavení aplikace, spustí editaci, a vloží hodnotu
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_city", city).commit();
	}

	/**
	 * Metoda získá hodnotu "Adresu serveru" z preferencí.
	 * 
	 * @param context
	 *            Kontext aktivity, ze které je metoda volána
	 * @return Adresa serveru
	 */
	public String getSyncUrl(Context context) {
		//získá přístup k nastavení aplikace a poté záský hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_syncserverurl", null);
	}

	/**
	 * Metoda vloží hodnotu "Adresa serveru" do preferencí. Před vložením ze
	 * provede validace.
	 * 
	 * @param context
	 *            Kontext aktivity, ze které je metoda volána
	 * @param syncUrl
	 *            URL adresa serveru
	 * @return True, když je záznam validní a vložení proběhlo úspěšně, jinak
	 *         false
	 */
	public boolean setSyncUrl(Context context, String syncUrl) {
		//kontrola validity parametru
		if (StringValidator.isURLValid(syncUrl)) {
			//získá přístup k nastavení aplikace, spustí editaci, a vloží hodnotu
			return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_syncserverurl", syncUrl).commit();
		} else {
			return false;
		}
	}

	/**
	 * Metoda získá hodnotu "MAC adresa tiskárny" z preferencí.
	 * 
	 * @param context
	 *            Kontext aktivity, ze které je metoda volána
	 * @return MAC adresa tiskárny
	 */
	public String getBTPrinterMacAddress(Context context) {
		//získá přístup k nastavení aplikace a poté záský hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_btprinter_macaddress", null);
	}

	/**
	 * Metoda vloží hodnotu "MAC adresa tiskárny" do preferencí. Před vložením
	 * ze provede validace.
	 * 
	 * @param context
	 *            Kontext aktivity, ze které je metoda volána
	 * @param mac
	 *            MAC adresa tiskárny
	 * @return True, když je záznam validní a vložení proběhlo úspěšně, jinak
	 *         false
	 */
	public boolean setBTPrinterMacAddress(Context context, String mac) {
		//kontrola validity parametru
		if (StringValidator.isMACValid(mac)) {
			//získá přístup k nastavení aplikace, spustí editaci, a vloží hodnotu
			return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_btprinter_macaddress", mac).commit();
		} else {
			return false;
		}
	}

	/**
	 * Metoda získá hodnotu "První spuštění". Pokud je true, změní jí na false.
	 * 
	 * @param context
	 *            Kontext aktivity, ze které je metoda volána
	 * @return True, když v preferencích ještě hodnota nebyla nastavena, jinak
	 *         false.
	 */
	public boolean isFirtstRun(Context context) {
		//získá přístup k nastavení aplikace a poté záský hodnotu preference
		boolean firstRun = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("first_run", true);
		if (firstRun) {
			PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("first_run", false).commit();
		}
		return firstRun;
	}
}