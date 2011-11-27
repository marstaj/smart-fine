/**
 * 
 */
package cz.smartfine;

import model.validators.URLValidator;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

/**
 * T��da aktivity pro p��stup k nastaven� aplikace u�ivatelem
 * @author Pavel Bro�
 */
public class PreferencesActivity extends PreferenceActivity implements OnPreferenceChangeListener {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //p�i�ad� aktivit� preference a u�ivatelsk� rozhran�
        addPreferencesFromResource(R.xml.preferences);
        
        //p�i�azen� poslucha�e zm�n nastaven� na vybran� preference pro zaji�t�n� validace vstupu//
        this.getPreferenceScreen().findPreference("pref_syncserverurl").setOnPreferenceChangeListener(this);
		////////////////////////////////////////////////////
		// Zde je mo�n� um�stit dal�� preference
		////////////////////////////////////////////////////
        
    }

	/**
	 * Handler reaguj�c� na ud�lost zm�ny nastaven� pro o�et�en� nevalidn�ch vstup�
	 * @param preference Objekt zm�n�n� preference
	 * @param newValue Nov� hodnota preference, kter� byla zad�na
	 * @return Vrac� true, pokud je nov� hodnota validn�, jinak false
	 */
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey(); //z�sk� kl�� zm�n�n� preference
		
		//validuje URL adresu synchroniza�n�ho serveru//
		if(key.equals("pref_syncserverurl")){
			//vyzkou��, zda je nov� hodnota url validn� za pomoci URL valid�toru//
			if(URLValidator.isURLValid((String)newValue)){
				return true;
			}else{ //nov� hodnota url nen� v po��dku
				//TODO: vlo�it text chyby do xml
				new AlertDialog.Builder(this).setTitle("chyba").setMessage("spatny format url").setNeutralButton("Zav��t", null).show();
				return false;
			}
		}
		
		////////////////////////////////////////////////////
		// Zde je mo�n� um�stit dal�� valid�tory nastaven�
		////////////////////////////////////////////////////
		
		return true; //pokud nen� preference explicitn� validov�na v��e, je nov� hodnota pova�ov�na za spr�vnou
	}

}
