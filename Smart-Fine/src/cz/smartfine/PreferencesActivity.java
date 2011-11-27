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
 * Tøída aktivity pro pøístup k nastavení aplikace uživatelem
 * @author Pavel Brož
 */
public class PreferencesActivity extends PreferenceActivity implements OnPreferenceChangeListener {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //pøiøadí aktivitì preference a uživatelské rozhraní
        addPreferencesFromResource(R.xml.preferences);
        
        //pøiøazení posluchaèe zmìn nastavení na vybrané preference pro zajištìní validace vstupu//
        this.getPreferenceScreen().findPreference("pref_syncserverurl").setOnPreferenceChangeListener(this);
		////////////////////////////////////////////////////
		// Zde je možné umístit další preference
		////////////////////////////////////////////////////
        
    }

	/**
	 * Handler reagující na událost zmìny nastavení pro ošetøení nevalidních vstupù
	 * @param preference Objekt zmìnìné preference
	 * @param newValue Nová hodnota preference, která byla zadána
	 * @return Vrací true, pokud je nová hodnota validní, jinak false
	 */
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey(); //získá klíè zmìnìné preference
		
		//validuje URL adresu synchronizaèního serveru//
		if(key.equals("pref_syncserverurl")){
			//vyzkouší, zda je nová hodnota url validní za pomoci URL validátoru//
			if(URLValidator.isURLValid((String)newValue)){
				return true;
			}else{ //nová hodnota url není v poøádku
				//TODO: vložit text chyby do xml
				new AlertDialog.Builder(this).setTitle("chyba").setMessage("spatny format url").setNeutralButton("Zavøít", null).show();
				return false;
			}
		}
		
		////////////////////////////////////////////////////
		// Zde je možné umístit další validátory nastavení
		////////////////////////////////////////////////////
		
		return true; //pokud není preference explicitnì validována výše, je nová hodnota považována za správnou
	}

}
