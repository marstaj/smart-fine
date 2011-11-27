package model.util;

import model.Ticket;
import android.app.Activity;
import android.widget.CheckBox;
import android.widget.EditText;
import cz.smartfine.R;

/**
 * Pomocn� t��da pro napln�n� formul��� PL, daty z instance PL
 * @author Pavel Bro�
 */
public class TicketSetter {
	
	/**
	 * Napln� widgety editovateln�mi daty z instance PL p�edan� v parametru
	 * @param activity Aktivita, ze kter� se metoda vol�
	 * @param ticket Instance PL, kter� se m� zobrazit
	 */
	public static void setTicketBasic(Activity activity, Ticket ticket){

		//nastav� jednotliv� widgety na hodnoty z instance PL//
		//v p��pad�, �e dojde k situaci, �e je n�kter� atribut null, try/catch sekce zajist�, �e se neovlivn� na�ten� ostatn�ch atribut�//
		//TODO: dod�lat zobrazen� obr�zk�
		try {
			((EditText) activity.findViewById(R.id.spz)).setText(ticket.getSpz());
		} catch (Exception e) {}
		try {
			((EditText) activity.findViewById(R.id.mpz)).setText(ticket.getMpz());
		} catch (Exception e) {}
		try {
			((EditText) activity.findViewById(R.id.spzColor)).setText(ticket.getSpzColor());
		} catch (Exception e) {}
		try {
			((EditText) activity.findViewById(R.id.vehicleType)).setText(ticket.getVehicleType());
		} catch (Exception e) {}
		try {
			((EditText) activity.findViewById(R.id.vehicleBrand)).setText(ticket.getVehicleBrand());
		} catch (Exception e) {}
		try {
			((EditText) activity.findViewById(R.id.city)).setText(ticket.getCity());
		} catch (Exception e) {}
		try {
			((EditText) activity.findViewById(R.id.street)).setText(ticket.getStreet());
		} catch (Exception e) {}

		((EditText) activity.findViewById(R.id.number)).setText(String.valueOf(ticket.getNumber()));

		try {
			((EditText) activity.findViewById(R.id.location)).setText(ticket.getLocation());
		} catch (Exception e) {}
		try {
			((EditText) activity.findViewById(R.id.descriptionDZ)).setText(ticket.getDescriptionDZ());
		} catch (Exception e) {}

		((CheckBox) activity.findViewById(R.id.tow)).setChecked(ticket.isTow());
		((CheckBox) activity.findViewById(R.id.moveableDZ)).setChecked(ticket.isMoveableDZ());

		try {
			((EditText) activity.findViewById(R.id.violation)).setText(ticket.getLaw().toString());
		} catch (Exception e) {}
		try {
			((EditText) activity.findViewById(R.id.eventDescription)).setText(ticket.getEventDescription());
		} catch (Exception e) {}
	}

	/**
	 * Napln� widgety needitovateln�mi daty z instance PL p�edan� v parametru
	 * @param activity Aktivita, ze kter� se metoda vol�
	 * @param ticket
	 */
	public static void setTicketExtra(Activity activity, Ticket ticket){
		//nastav� jednotliv� widgety na hodnoty z instance PL//
		//v p��pad�, �e dojde k situaci, �e je n�kter� atribut null, try/catch sekce zajist�, �e se neovlivn� na�ten� ostatn�ch atribut�//
		try {
			((EditText) activity.findViewById(R.id.dateTime)).setText(ticket.getDate().toLocaleString());
		} catch (Exception e) {}
		((EditText) activity.findViewById(R.id.badgeNumber)).setText(String.valueOf(ticket.getBadgeNumber()));
		((CheckBox) activity.findViewById(R.id.printed)).setChecked(ticket.isPrinted());
	}
}
