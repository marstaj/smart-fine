package cz.smartfine.android;

import java.net.InetSocketAddress;

import cz.smartfine.android.R;
import cz.smartfine.android.model.util.StringValidator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

/**
 * Třída představující objekt typu PreferenceActivity, který slouží k přístupu
 * do preferencí, nastavení aplikace
 * 
 * @author Martin Štajner
 */
public class PreferencesActivity extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Metoda inicializuje objekt PreferencesActivity a je volána při jeho
	 * vytváření.
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// přiřadí aktivitě preference ze xml
		addPreferencesFromResource(R.xml.preferences);

		// přiřazení posluchačů  změn nastavení na vybrané preference pro zajištění validace vstupu
		findPreference("pref_syncserverurl").setOnPreferenceChangeListener(this);
		findPreference("pref_btprinter_macaddress").setOnPreferenceChangeListener(this);

		//přiřazení posluchacu na preference seznamu nejcastejsich hodnot
		findPreference("pref_frequentValues_mpz").setOnPreferenceClickListener(this);
		findPreference("pref_frequentValues_vehicleBrand").setOnPreferenceClickListener(this);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();
	}

	/**
	 * Metoda reagující na událost změny nastavení pro ošetření nevalidních
	 * vstupů
	 * 
	 * @see android.preference.Preference.OnPreferenceChangeListener#onPreferenceChange
	 *      (android.preference.Preference, java.lang.Object)
	 */
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		//získá klíč změněné preference
		String key = preference.getKey();

		//validuje adresu serveru
		if (key.equals("pref_syncserverurl")) {
			//vyzkouší, zda je nová hodnota adresy validní za pomoci validátoru
			String value = (String) newValue;
			if (StringValidator.isURLValid(value)) {
				// Vytvori novou inet adresu a ulozi ji do connection provideru
				InetSocketAddress inetAddress = new InetSocketAddress(value, 25000);
				app.getConnectionProvider().setNewAddress(inetAddress);
				app.getConnectionProvider().disconnect();
				return true;
			} else {
				//nová hodnota adresy serveru není v pořádku
				new AlertDialog.Builder(this).setTitle("Chyba").setMessage(app.getText(R.string.val_pref_syncserver_err).toString()).setNeutralButton("Zavřít", null).show();
				return false;
			}
		}

		//validuje MAC adresu mobilni tiskarny
		if (key.equals("pref_btprinter_macaddress")) {
			//vyzkouší, zda je nová hodnota adresy validní za pomoci validátoru
			if (StringValidator.isMACValid((String) newValue)) {
				return true;
			} else {
				new AlertDialog.Builder(this).setTitle("Chyba").setMessage(app.getText(R.string.val_pref_mac_err).toString()).setNeutralButton("Zavřít", null).show();
				return false;
			}
		}
		return true;
	}

	/**
	 * Metoda spouští akce podle stiknuté preference.
	 * 
	 * @see android.preference.Preference.OnPreferenceClickListener#onPreferenceClick
	 *      (android.preference.Preference)
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