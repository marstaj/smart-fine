package model;

import model.Validators.URLValidator;
import android.content.Context;
import android.preference.*;

/**
 * T��da pro zaji�t�n� p��stupu k nastaven� aplikace skze k�d
 * @author Pavel Bro�
 */
public class Settings {

	/**
	 * Reprezentuje nastaven� programu
	 */
	private static Settings settings;
	
	/**
	 * Priv�tn� konstruktor
	 */
	private Settings(){	}

	/**
	 * Vrac� instanci nastaven� aplikace
	 * @return Vr�t� instanci pro p��stup k nastaven� aplikace
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
	 * @param context Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @return Vrac� slu�ebn� ��slo policisty
	 */
	public String getBadgeNumber(Context context) {
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_badgenumber", null);
	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @param badgeNumber Reprezentuje slu�ebn� ��slo policisty
	 * @return Vrac� true, pokud vlo�en� nov� hodnoty prob�hlo v po��dku a false pokud do�lo k chyb� p�i ukl�d�n�
	 */
	public boolean setBadgeNumber(Context context, String badgeNumber) {
		//z�sk� p��stup k nastaven� aplikace, spust� editaci, vlo�� hodnotu a commituje
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_badgenumber", badgeNumber).commit();
	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @return Vrac� m�sto, ve kter�m se ud�luj� PL
	 */
	public String getCity(Context context) {
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_city", null);
	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @param city Reprezentuje m�sto, ve kter�m se ud�luj� PL
	 * @return Vrac� true, pokud vlo�en� nov� hodnoty prob�hlo v po��dku a false pokud do�lo k chyb� p�i ukl�d�n�
	 */
	public boolean setCity(Context context, String city) {
		//z�sk� p��stup k nastaven� aplikace, spust� editaci, vlo�� hodnotu a commituje
		return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_city", city).commit();
	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @return Vrac� adresu serveru pro nahr�v�n� PL
	 */
	public String getSyncUrl(Context context) {
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu preference
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_syncserverurl", null);
	}

	/**
	 * @param context Reprezentuje kontext aktivity, ze kter� je metoda vol�na
	 * @param syncUrl Reprezentuje URL adresu serveru pro nahr�v�n� PL
	 * @return Vrac� true, pokud vlo�en� nov� hodnoty prob�hlo v po��dku a false pokud do�lo k chyb� p�i ukl�d�n� nebo byla nov� hodnota URL nevalidn�
	 */
	public boolean setSyncUrl(Context context, String syncUrl) {
		//kontrola validity parametru//
		if(URLValidator.isURLValid(syncUrl)){
			//z�sk� p��stup k nastaven� aplikace, spust� editaci, vlo�� hodnotu a commituje
			return PreferenceManager.getDefaultSharedPreferences(context).edit().putString("pref_syncserverurl", syncUrl).commit();
		}else{ //nov� hodnota url nen� v po��dku
			return false;
		}

	}

}