package cz.smartfine;

import java.util.ArrayList;

import model.FrequentValue;
import cz.smartfine.adapters.FrequentValuesListAdapter;
import android.app.Activity;
import android.content.Intent;
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
 * Tøída aktivity upravy seznamu nejcastejsch hodnot
 * 
 * @author Martin Štajner
 * 
 */
public class FrequentValuesFavouriteActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * List hodnot, se kterým aktivita zrovna pracuje
	 */
	private ArrayList<FrequentValue> list;
	/**
	 * List oblíbených hodnot.
	 */
	private ArrayList<FrequentValue> fav_list;

	/**
	 * Adapter listu
	 */
	private ArrayAdapter<FrequentValue> adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frequent_values_list);
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
				list = app.getFrequentValuesDAO().getMpzValues();
				//				if (app.getFrequentValuesDAO().getFav_mpzValues() != null) {
				//					list = app.getFrequentValuesDAO().getFav_mpzValues();
				//				}				
				break;
			case 2 :
				list = app.getFrequentValuesDAO().getVehicleBrandValues();
				//				if (app.getFrequentValuesDAO().getFav_vehicleBrandValues() != null) {
				//					list = app.getFrequentValuesDAO().getFav_vehicleBrandValues();
				//				}
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
	
	

	//	/**
	//	 * Obsluha tlaèítka - Pridat hodnotu
	//	 * 
	//	 * @param button
	//	 */
	//	public void addValueClick(View button) {
	//		EditText text = (EditText) findViewById(R.id.addValueText);
	//		if (!text.getText().toString().equals("")) {
	//			list.add(text.getText().toString());
	//			adapter.notifyDataSetChanged();
	//			text.setText("");
	//		}
	//	}
	//	

}