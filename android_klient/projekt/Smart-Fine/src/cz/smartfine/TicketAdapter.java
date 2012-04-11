package cz.smartfine;

import java.util.ArrayList;

import model.Ticket;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Tøída starajicí se o naplnìní listu uložených PL
 * @author Martin Štajner
 *
 */
public class TicketAdapter extends ArrayAdapter<Ticket> {

	/**
	 * List záznamù k zobrazení v listu
	 */
	private ArrayList<Ticket> items;
	
	/**
	 * Vytváøí instance uživatelských rozhraní
	 */
	private LayoutInflater mInflater;

	/**
	 * Ticket adapter
	 * @param context
	 * @param textViewResourceId
	 * @param items
	 */
	public TicketAdapter(Context context, int textViewResourceId, ArrayList<Ticket> items) {
		super(context, textViewResourceId, items);
		mInflater = LayoutInflater.from(context);
		this.items = items;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ticketlistitem, null);
		}
		Ticket ticket = items.get(position);
		((TextView) convertView.findViewById(R.id.showSpz)).setText("SPZ: "+ ticket.getSpz());
		((TextView) convertView.findViewById(R.id.showDate)).setText(ticket.getDate().toLocaleString());
		return convertView;
	}

}
