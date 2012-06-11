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
 * T��da aktivity pro p��stup k nastaven� aplikace u�ivatelem
 * 
 * @author Martin �tajner
 */
public class PreferencesActivity extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//p�i�ad� aktivit� preference a u�ivatelsk� rozhran�
		addPreferencesFromResource(R.xml.preferences);

		//p�i�azen� poslucha�e zm�n nastaven� na vybran� preference pro zaji�t�n� validace vstupu//
		findPreference("pref_syncserverurl").setOnPreferenceChangeListener(this);

		//p�i�azen� posluchacu na preference seznamu nejcastejsich hodnot
		findPreference("pref_frequentValues_mpz").setOnPreferenceClickListener(this);
		findPreference("pref_frequentValues_vehicleBrand").setOnPreferenceClickListener(this);
	}

	/**
	 * Handler reaguj�c� na ud�lost zm�ny nastaven� pro o�et�en� nevalidn�ch
	 * vstup�
	 * 
	 * @param preference
	 *            Objekt zm�n�n� preference
	 * @param newValue
	 *            Nov� hodnota preference, kter� byla zad�na
	 * @return Vrac� true, pokud je nov� hodnota validn�, jinak false
	 */
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey(); //z�sk� kl�� zm�n�n� preference

		//validuje URL adresu synchroniza�n�ho serveru//
		if (key.equals("pref_syncserverurl")) {
			//vyzkou��, zda je nov� hodnota url validn� za pomoci URL valid�toru//
			if (URLValidator.isURLValid((String) newValue)) {
				return true;
			} else { //nov� hodnota url nen� v po��dku
						//TODO: vlo�it text chyby do xml
				new AlertDialog.Builder(this).setTitle("chyba").setMessage("spatny format url").setNeutralButton("Zav��t", null).show();
				return false;
			}
		}

		return true; //pokud nen� preference explicitn� validov�na v��e, je nov� hodnota pova�ov�na za spr�vnou
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