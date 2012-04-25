package cz.smartfine;

import model.validators.URLValidator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

/**
 * Tøída aktivity pro pøístup k nastavení aplikace uživatelem
 * 
 * @author Martin Štajner
 */
public class PreferencesActivity extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//pøiøadí aktivitì preference a uživatelské rozhraní
		addPreferencesFromResource(R.xml.preferences);

		//pøiøazení posluchaèe zmìn nastavení na vybrané preference pro zajištìní validace vstupu//
		findPreference("pref_syncserverurl").setOnPreferenceChangeListener(this);

		//pøiøazení posluchacu na preference seznamu nejcastejsich hodnot
		findPreference("pref_frequentValues_mpz").setOnPreferenceClickListener(this);
		findPreference("pref_frequentValues_vehicleBrand").setOnPreferenceClickListener(this);
	}

	/**
	 * Handler reagující na událost zmìny nastavení pro ošetøení nevalidních
	 * vstupù
	 * 
	 * @param preference
	 *            Objekt zmìnìné preference
	 * @param newValue
	 *            Nová hodnota preference, která byla zadána
	 * @return Vrací true, pokud je nová hodnota validní, jinak false
	 */
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey(); //získá klíè zmìnìné preference

		//validuje URL adresu synchronizaèního serveru//
		if (key.equals("pref_syncserverurl")) {
			//vyzkouší, zda je nová hodnota url validní za pomoci URL validátoru//
			if (URLValidator.isURLValid((String) newValue)) {
				return true;
			} else { //nová hodnota url není v poøádku
						//TODO: vložit text chyby do xml
				new AlertDialog.Builder(this).setTitle("chyba").setMessage("spatny format url").setNeutralButton("Zavøít", null).show();
				return false;
			}
		}

		return true; //pokud není preference explicitnì validována výše, je nová hodnota považována za správnou
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.preference.Preference.OnPreferenceClickListener#onPreferenceClick
	 * (android.preference.Preference)
	 */
	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals("pref_frequentValues_mpz")) {
			startActivity(new Intent(this, FrequentValuesEditActivity.class).putExtra("valuesGroup", 1));
			return true;
		}
		if (preference.getKey().equals("pref_frequentValues_vehicleBrand")) {
			startActivity(new Intent(this, FrequentValuesEditActivity.class).putExtra("valuesGroup", 2));
			return true;
		}
		return false;
	}

}