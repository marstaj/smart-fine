package cz.smartfine.android.model;

import cz.smartfine.android.model.validators.URLValidator;
import android.content.Context;
import android.preference.*;

/**
 * T��da pro zaji�t�n� p��stupu k nastaven� aplikace skze k�d
 * 
 * @author Martin �tajner, Pavel Bro�
 */
public class Settings {

	/**
	 * Reprezentuje nastaven� programu
	 */
	private static Settings settings;

	/**
	 * Priv�tn� konstruktor
	 */
	private Settings() {
	}

	/**
	 * Vrac� instanci nastaven� aplikace
	 * 
	 * @return Vr�t� instanci pro p��stup k nastaven� aplikace
	 */
	public static Settings getInstance() {
		if (settings == null) {
			settings = new Settings();
		}
		return settings;
	}

	/**
	 * Vrac� slu�ebn� ��slo policisty
	 * 
	 * @param context
	 *            Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @return Vrac� slu�ebn� ��slo policisty
	 */
	public String getBadgeNumber(Context context) {
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_badgenumber", null);
	}

	/**
	 * Vrac� true, pokud vlo�en� nov� hodnoty prob�hlo v po��dku a false pokud
	 * do�lo k chyb� p�i ukl�d�n�
	 * 
	 * @param context
	 *            Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @param badgeNumber
	 *            Reprezentuje slu�ebn� ��slo policisty
	 * @return true/false
	 */
	public boolean setBadgeNumber(Context context, String badgeNumber) {
		//z�sk� p��stup k nastaven� aplikace, spust� editaci, vlo�� hodnotu a commituje
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_badgenumber", badgeNumber).commit();
	}

	/**
	 * Vrac� m�sto, ve kter�m se ud�luj� PL
	 * 
	 * @param context
	 *            Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @return m�sto
	 */
	public String getCity(Context context) {
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_city", null);
	}

	/**
	 * Vrac� true, pokud vlo�en� nov� hodnoty prob�hlo v po��dku a false pokud
	 * do�lo k chyb� p�i ukl�d�n�
	 * 
	 * @param context
	 *            Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @param city
	 *            Reprezentuje m�sto, ve kter�m se ud�luj� PL
	 * @return true/false
	 */
	public boolean setCity(Context context, String city) {
		//z�sk� p��stup k nastaven� aplikace, spust� editaci, vlo�� hodnotu a commituje
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_city", city).commit();
	}

	/**
	 * Vrac� adresu serveru pro nahr�v�n� PL
	 * 
	 * @param context
	 *            Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @return adresa
	 */
	public String getSyncUrl(Context context) {
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_syncserverurl", null);
	}

	/**
	 * Vrac� true, pokud vlo�en� nov� hodnoty prob�hlo v po��dku a false pokud
	 * do�lo k chyb� p�i ukl�d�n� nebo byla nov� hodnota URL nevalidn�
	 * 
	 * @param context
	 *            Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @param syncUrl
	 *            Reprezentuje URL adresu serveru pro nahr�v�n� PL
	 * @return true/false
	 */
	public boolean setSyncUrl(Context context, String syncUrl) {
		//kontrola validity parametru//
		if (URLValidator.isURLValid(syncUrl)) {
			//z�sk� p��stup k nastaven� aplikace, spust� editaci, vlo�� hodnotu a commituje
			return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_syncserverurl", syncUrl).commit();
		} else { //nov� hodnota url nen� v po��dku
			return false;
		}

	}

	/**
	 * Metoda nahr�v� hodnotu "Slu�ebn� ��slo policisty" ze sd�len�ch preferenc�
	 * za��zen�.
	 * 
	 * @param context
	 *            Kontext aktivity, ze kter� je metoda vol�na
	 * @return Slu�ebn� ��slo policisty
	 */
	public String getLoginIdNumber(Context context) {
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("idNumber", null);
	}

	/**
	 * Metoda ukl�d� hodnotu "Slu�ebn� ��slo policisty" do sd�len�ch preferenc�
	 * za��zen�.
	 * 
	 * @param context
	 *            Kontext aktivity, ze kter� je metoda vol�na
	 * @param idNumber
	 *            Slu�ebn� ��slo policisty
	 * @return True - Kdy� byla hodnota ulo�ena, False - pokud nikoliv.
	 */
	public boolean setLoginIdNumber(Context context, String idNumber) {
		//z�sk� p��stup k nastaven� aplikace, spust� editaci, vlo�� hodnotu a commituje
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("idNumber", idNumber).commit();

	}

	/**
	 * Metoda nahr�v� hodnotu "Heslo policisty" ze sd�len�ch preferenc�
	 * za��zen�.
	 * 
	 * @param context
	 *            Kontext aktivity, ze kter� je metoda vol�na
	 * @return Heslo policisty
	 */
	public String getLoginPassword(Context context) {
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pin", null);
	}

	/**
	 * Metoda ukl�d� hodnotu "Heslo policisty" do sd�len�ch preferenc� za��zen�.
	 * 
	 * @param context
	 *            Kontext aktivity, ze kter� je metoda vol�na
	 * @param idNumber
	 *            Heslo policisty
	 * @return True - Kdy� byla hodnota ulo�ena, False - pokud nikoliv.
	 */
	public boolean setLoginPassword(Context context, String password) {
		//z�sk� p��stup k nastaven� aplikace, spust� editaci, vlo�� hodnotu a commituje
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pin", password).commit();

	}

	/**
	 * Metoda nahr�v� hodnotu, zda je u�ivatel p�ihl�en, ze sd�len�ch
	 * preferenc� za��zen�.
	 * 
	 * @param context
	 *            Kontext aktivity, ze kter� je metoda vol�na
	 * @return Zda je u�ivatel p�ihl�en.
	 */
	public boolean isLogged(Context context) {
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("logged", false);
	}

	/**
	 * Metoda ukl�d� hodnotu, zda je u�ivatel p�ihl�en, do sd�len�ch preferenc�
	 * za��zen�.
	 * 
	 * @param context
	 *            Kontext aktivity, ze kter� je metoda vol�na
	 * @param logged
	 *            Zda je u�ivatel p�ihl�en.
	 * @return True - Kdy� byla hodnota ulo�ena, False - pokud nikoliv.
	 */
	public boolean setLogged(Context context, boolean logged) {
		//z�sk� p��stup k nastaven� aplikace, spust� editaci, vlo�� hodnotu a commituje
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("logged", logged).commit();

	}

}