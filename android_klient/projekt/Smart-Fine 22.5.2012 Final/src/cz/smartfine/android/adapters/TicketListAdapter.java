package cz.smartfine.android.adapters;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.model.Ticket;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Třída představující objekt typu ArrayAdapter, který slouží k naplnění listu
 * parkovacích lístků.
 * 
 * @author Martin Štajner
 * 
 */
public class TicketListAdapter extends ArrayAdapter<Ticket> {

	/**
	 * Seznam parkovacích lístků k zobrazení v listu.
	 */
	private ArrayList<Ticket> items;

	/**
	 * Instance objektu, který vytváří instance uživatelských rozhraní.
	 */
	private LayoutInflater mInflater;

	/**
	 * Odkaz na tvar položky seznamu parkovacích lístku definovaný ve XML
	 */
	private int textViewResourceId;

	/**
	 * Konstruktor adaptéru
	 * 
	 * @param context
	 *            Kontext aktivity
	 * @param textViewResourceId
	 *            Odkaz na tvar položky seznamu nejčastějších hodnot
	 * @param items
	 *            Seznam hodnot vstupující do adaptéru
	 */
	public TicketListAdapter(Context context, int textViewResourceId, ArrayList<Ticket> items) {
		super(context, textViewResourceId, items);
		mInflater = LayoutInflater.from(context);
		this.textViewResourceId = textViewResourceId;
		this.items = items;
	}

	/**
	 * Metoda nastavuje informace do View položky seznamu.
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(textViewResourceId, null);
		}
		Ticket ticket = items.get(position);
		((TextView) convertView.findViewById(R.id.showDescription)).setText(ticket.getLaw().getEventDescription());
		((TextView) convertView.findViewById(R.id.showDate)).setText(ticket.getDate().toLocaleString());
		((TextView) convertView.findViewById(R.id.showSpz)).setText("SPZ: " + ticket.getSpz());
		return convertView;
	}

}
