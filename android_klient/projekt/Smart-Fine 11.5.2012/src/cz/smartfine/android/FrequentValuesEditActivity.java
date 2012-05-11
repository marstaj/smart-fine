package cz.smartfine.android;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.adapters.FrequentValuesListAdapter;
import cz.smartfine.android.dao.FileFreqValuesDAO;
import cz.smartfine.android.model.FrequentValue;
import cz.smartfine.android.model.util.Toaster;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Třída představující objekt typu Activity, který slouží k vybrání oblíbených
 * nejčastějších hodnot, které jsou k dispozici jako nápověda při vytváření
 * patkovacího lístku.
 * 
 * @author Martin Štajner
 * 
 */
public class FrequentValuesEditActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Seznam všech nejčastějších hodnot
	 */
	private ArrayList<FrequentValue> list;
	/**
	 * Seznam oblíbených nejčastějších hodnot
	 */
	private ArrayList<FrequentValue> fav_list;

	/**
	 * Poskytovatel dat pro seznam nejčastějších hodnot
	 */
	private ArrayAdapter<FrequentValue> adapter;

	/**
	 * Metoda inicializuje objekt FrequentValuesEditActivity a je volána při
	 * jeho vytváření.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frequent_values_list);

		// Schova klavesnici
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		//Zjisti jakou skupinu nejcastejsich hodnot zobrazit
		int valuesGroup = getIntent().getExtras().getInt("valuesGroup");

		// Nastaveni listu
		ListView lv = (ListView) this.findViewById(R.id.frequentValuesList);
		lv.setItemsCanFocus(false);
		lv.setTextFilterEnabled(true);

		// Ziska napln do adapteru
		switch (valuesGroup) {
			case 1 :
				list = ((FileFreqValuesDAO) app.getFreqValuesDAO()).getMpzValues();
				fav_list = ((FileFreqValuesDAO) app.getFreqValuesDAO()).getFav_mpzValues();
				break;
			case 2 :
				list = ((FileFreqValuesDAO) app.getFreqValuesDAO()).getVehicleBrandValues();
				fav_list = ((FileFreqValuesDAO) app.getFreqValuesDAO()).getFav_vehicleBrandValues();
				break;
		}

		adapter = new FrequentValuesListAdapter(this, R.layout.frequent_values_list_item, list);
		lv.setAdapter(adapter);

		// Nastavi filtrovani podle hodnoty z formularoveho pole
		EditText filterText = (EditText) findViewById(R.id.addValueText);
		filterText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable arg0) {
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				adapter.getFilter().filter(arg0);
			}
		});

		// Posluchac kliknuti na item
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckBox chBox = (CheckBox) view.findViewById(R.id.isFavouriteCheckBox);
				if (chBox.isChecked()) {
					FrequentValue fv = ((FrequentValuesListAdapter) adapter).getCurrentItem(position);
					fv.setFavourite(false);
					if (fav_list.contains(fv)) {
						fav_list.remove(fv);
					}
				} else {
					FrequentValue fv = ((FrequentValuesListAdapter) adapter).getCurrentItem(position);
					fv.setFavourite(true);
					if (!fav_list.contains(fv)) {
						fav_list.add(fv);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	// ==================================== OVERRIDE METHODS ==================================== //

	/**
	 * Metoda uloží nejčastější hodnoty. Je volána při ukončení aktivity.
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		try {
			app.getFreqValuesDAO().saveValues();
		} catch (Exception e) {
			Toaster.toast("Chyba při ukládání seznamu nejčastejších hodnot.", Toaster.LONG);
		}
		super.finish();
	}
}