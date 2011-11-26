package model;

import model.Validators.URLValidator;
import android.content.Context;
import android.preference.*;

/**
 * Tøída pro zajištìní pøístupu k nastavení aplikace skze kód
 * @author Pavel Brož
 */
public class Settings {

	/**
	 * Reprezentuje nastavení programu
	 */
	private static Settings settings;
	
	/**
	 * Privátní konstruktor
	 */
	private Settings(){	}

	/**
	 * Vrací instanci nastavení aplikace
	 * @return Vrátí instanci pro pøístup k nastavení aplikace
	 */
	public static Settings getInstance(){
		if (settings == null){
			settings = new Settings();
		}
		return settings;
	}
	
	public void finalize() throws Throwable {

	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze které je metoda volána
	 * @return Vrací služební èíslo policisty
	 */
	public String getBadgeNumber(Context context) {
		//získá pøístup k nastavení aplikace a poté získá hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_badgenumber", null);
	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze které je metoda volána
	 * @param badgeNumber Reprezentuje služební èíslo policisty
	 * @return Vrací true, pokud vložení nové hodnoty probìhlo v poøádku a false pokud došlo k chybì pøi ukládání
	 */
	public boolean setBadgeNumber(Context context, String badgeNumber) {
		//získá pøístup k nastavení aplikace, spustí editaci, vloží hodnotu a commituje
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_badgenumber", badgeNumber).commit();
	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze které je metoda volána
	 * @return Vrací mìsto, ve kterém se udìlují PL
	 */
	public String getCity(Context context) {
		//získá pøístup k nastavení aplikace a poté získá hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_city", null);
	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze které je metoda volána
	 * @param city Reprezentuje mìsto, ve kterém se udìlují PL
	 * @return Vrací true, pokud vložení nové hodnoty probìhlo v poøádku a false pokud došlo k chybì pøi ukládání
	 */
	public boolean setCity(Context context, String city) {
		//získá pøístup k nastavení aplikace, spustí editaci, vloží hodnotu a commituje
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_city", city).commit();
	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze které je metoda volána
	 * @return Vrací adresu serveru pro nahrávání PL
	 */
	public String getSyncUrl(Context context) {
		//získá pøístup k nastavení aplikace a poté získá hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_syncserverurl", null);
	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze které je metoda volána
	 * @param syncUrl Reprezentuje URL adresu serveru pro nahrávání PL
	 * @return Vrací true, pokud vložení nové hodnoty probìhlo v poøádku a false pokud došlo k chybì pøi ukládání nebo byla nová hodnota URL nevalidní
	 */
	public boolean setSyncUrl(Context context, String syncUrl) {
		//kontrola validity parametru//
		if(URLValidator.isURLValid(syncUrl)){
			//získá pøístup k nastavení aplikace, spustí editaci, vloží hodnotu a commituje
			return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_syncserverurl", syncUrl).commit();
		}else{ //nová hodnota url není v poøádku
			return false;
		}

	}

}