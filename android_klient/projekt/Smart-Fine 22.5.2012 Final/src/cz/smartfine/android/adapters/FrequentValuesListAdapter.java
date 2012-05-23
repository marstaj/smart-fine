package cz.smartfine.android.adapters;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.model.FrequentValue;
import cz.smartfine.android.model.util.Text;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * Třída představující objekt typu ArrayAdapter, který slouží k naplnění listu
 * nejčastějších hodnot. List se dá filtrovat podle jmen hodnot.
 * 
 * @author Martin Štajner
 * 
 */
public class FrequentValuesListAdapter extends ArrayAdapter<FrequentValue> implements Filterable {

	/**
	 * Seznam nějčastějších hodnot vstupující do adaptéru.
	 */
	private ArrayList<FrequentValue> originalItems;

	/**
	 * Seznam nějčastějších hodnot k zobrazení v listu. List se při filtrování
	 * mění!
	 */
	private ArrayList<FrequentValue> items;

	/**
	 * Instance objektu, který vytváří instance uživatelských rozhraní.
	 */
	private LayoutInflater mInflater;

	/**
	 * Odkaz na tvar položky seznamu nejčastějších hodnot definovaný ve XML
	 */
	private int textViewResourceId;

	/**
	 * Filter pro vyhledávání v seznamu hodnot
	 */
	private Filter filter;

	/**
	 * Konstruktor adaptéru
	 * 
	 * @param context
	 *            Kontext aktivity
	 * @param textViewResourceId
	 *            Odkaz na tvar položky seznamu nejčastějších hodnot
	 * @param list
	 *            Seznam hodnot vstupující do adaptéru
	 */
	public FrequentValuesListAdapter(Context context, int textViewResourceId, ArrayList<FrequentValue> list) {
		super(context, textViewResourceId, list);
		mInflater = LayoutInflater.from(context);
		this.textViewResourceId = textViewResourceId;
		this.originalItems = list;
		this.items = new ArrayList<FrequentValue>(originalItems);
	}

	/**
	 * Metoda nastavuje informace do View položky seznamu.
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(textViewResourceId, null);
		}
		String value = items.get(position).getValue();
		((TextView) convertView.findViewById(R.id.showValue)).setText(value);

		// Zaskrtne checkbox v pripade, ze patri mezi oblibene
		CheckBox chBox = (CheckBox) convertView.findViewById(R.id.isFavouriteCheckBox);
		if (items.get(position).isFavourite()) {
			chBox.setChecked(true);
		} else {
			chBox.setChecked(false);
		}

		return convertView;
	}

	public FrequentValue getCurrentItem(int position) {
		return items.get(position);
	}

	/**
	 * Metoda vytváří a vrací filr pro vyhledávání v seznamu podle jména.
	 * 
	 * @see android.widget.ArrayAdapter#getFilter()
	 */
	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new Filter() {

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults results = new FilterResults();

					// Kdyz filtrovaci pole neobsahuje zadne znaky
					if (constraint.length() == 0 || constraint == null) {
						results.count = originalItems.size();
						results.values = originalItems;

						// Kdyz filtrovaci pole obsahuje nejake znaky
					} else {
						String filterText = constraint.toString().toLowerCase();

						ArrayList<FrequentValue> filtredItems = new ArrayList<FrequentValue>();

						for (int i = 0; i < originalItems.size(); i++) {

							String textToCheck = originalItems.get(i).getValue().toString().toLowerCase();
							textToCheck = Text.removeCzechLowerCaseDiacritics(textToCheck);

							// Nejdřísve zkusí najít shodu bez rozdělování
							if (textToCheck.startsWith(filterText)) {
								filtredItems.add(originalItems.get(i));

							} else {
								// Rozdeli slova podle mezer
								String[] words = textToCheck.split(" ");

								// Start at index 0, in case valueText starts with space(s)
								for (int k = 0; k < words.length; k++) {
									if (words[k].startsWith(filterText)) {
										filtredItems.add(originalItems.get(i));
										break;
									}
								}
							}
						}

						results.values = filtredItems;
						results.count = filtredItems.size();
					}
					return results;
				}

				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					items = (ArrayList<FrequentValue>) results.values;
					if (results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}

				}
			};
		}
		return filter;
	}

	/**
	 * MEtoda vrací délku seznamu
	 * 
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return items.size();
	}

}
