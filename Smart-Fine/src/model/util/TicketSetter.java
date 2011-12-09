package model.util;

import model.Ticket;
import android.app.Activity;
import android.widget.CheckBox;
import android.widget.EditText;
import cz.smartfine.R;

/**
 * Pomocn� t��da pro napln�n� formul��� PL, daty z instance PL
 * 
 * @author Pavel Bro�, Martin �tajner
 */
public class TicketSetter {	

	/**
	 * Napln� widgety editovateln�mi daty z instance PL p�edan� v parametru
	 * 
	 * @param activity Aktivita, ze kter� se metoda vol�
	 * @param ticket Instance PL, kter� se m� zobrazit
	 */
	public static void setTicketBasic(Activity activity, Ticket ticket) {

		// Nastav� jednotliv� widgety na hodnoty z instance PL.
		// Pokud bude nejaky stringovy atribut null, policko zustane prazdne
		// Pokud bude nejaky int atribut 0 (coz je jakoby null u int), policko se rucne vyprazdni (jinak by to vypisovalo 0)

		// TODO: dod�lat zobrazen� obr�zk�

		((EditText) activity.findViewById(R.id.spz)).setText(ticket.getSpz());
		((EditText) activity.findViewById(R.id.mpz)).setText(ticket.getMpz());
		((EditText) activity.findViewById(R.id.spzColor)).setText(ticket.getSpzColor());
		((EditText) activity.findViewById(R.id.vehicleType)).setText(ticket.getVehicleType());
		((EditText) activity.findViewById(R.id.vehicleBrand)).setText(ticket.getVehicleBrand());
		((EditText) activity.findViewById(R.id.city)).setText(ticket.getCity());
		((EditText) activity.findViewById(R.id.street)).setText(ticket.getStreet());

		if (ticket.getNumber() == 0) {
			((EditText) activity.findViewById(R.id.number)).setText("");
		} else {
			((EditText) activity.findViewById(R.id.number)).setText(String.valueOf(ticket.getNumber()));
		}

		((EditText) activity.findViewById(R.id.location)).setText(ticket.getLocation());
		((EditText) activity.findViewById(R.id.descriptionDZ)).setText(ticket.getDescriptionDZ());
		((CheckBox) activity.findViewById(R.id.tow)).setChecked(ticket.isTow());
		((CheckBox) activity.findViewById(R.id.moveableDZ)).setChecked(ticket.isMoveableDZ());
		((EditText) activity.findViewById(R.id.eventDescription)).setText(ticket.getEventDescription());

		if (ticket.getLaw() != null) {
			if (ticket.getLaw().getRuleOfLaw() != 0) {
				((EditText) activity.findViewById(R.id.ruleOfLaw)).setText(String.valueOf(ticket.getLaw().getRuleOfLaw()));
			} else {
				((EditText) activity.findViewById(R.id.ruleOfLaw)).setText("");
			}
			if (ticket.getLaw().getParagraph() != 0) {
				((EditText) activity.findViewById(R.id.paragraph)).setText(String.valueOf(ticket.getLaw().getParagraph()));
			} else {
				((EditText) activity.findViewById(R.id.paragraph)).setText("");
			}
			if (ticket.getLaw().getLawNumber() != 0) {
				((EditText) activity.findViewById(R.id.lawNumber)).setText(String.valueOf(ticket.getLaw().getLawNumber()));
			} else {
				((EditText) activity.findViewById(R.id.lawNumber)).setText("");
			}
			if (ticket.getLaw().getCollection() != 0) {
				((EditText) activity.findViewById(R.id.collection)).setText(String.valueOf(ticket.getLaw().getCollection()));
			} else {
				((EditText) activity.findViewById(R.id.collection)).setText("");
			}
			((EditText) activity.findViewById(R.id.letter)).setText(ticket.getLaw().getLetter());
		}			
	}

	/**
	 * Napln� widgety needitovateln�mi daty z instance PL p�edan� v parametru
	 * 
	 * @param activity Aktivita, ze kter� se metoda vol�
	 * @param ticket
	 */
	public static void setTicketExtra(Activity activity, Ticket ticket) {
		// Nastav� jednotliv� widgety na hodnoty z instance PL.
		if (ticket.getDate() != null) {
			((EditText) activity.findViewById(R.id.dateTime)).setText(ticket.getDate().toLocaleString());
		}
		((EditText) activity.findViewById(R.id.badgeNumber)).setText(String.valueOf(ticket.getBadgeNumber()));
		((CheckBox) activity.findViewById(R.id.printed)).setChecked(ticket.isPrinted());
	}


}
