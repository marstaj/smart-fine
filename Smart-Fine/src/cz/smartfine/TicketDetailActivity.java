package cz.smartfine;

import model.Ticket;
import model.util.TicketSetter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Tøída aktivity pro zobrazení informací z parkovacího lístku
 * 
 * @author Pavel Brož
 */
public class TicketDetailActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;
	/**
	 * Reprezentuje index zobrazeného PL do DAO
	 */
	private int ticketIndex;
	
	private int EDIT = 1;
	
	// TODO requestCode pro PRINT, az bude PRINT implementovan
	//private int PRINT = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketdetail);

		app = (MyApp) this.getApplication();

		// získá index do DAO na lístek, který s má zobrazit, pøedaný ze
		// spouštìjící aktivity
		ticketIndex = this.getIntent().getExtras().getInt("Ticket");

		Ticket ticket = app.getTicketDao().getTicket(ticketIndex);

		// ovìøí, zda se nìo vrátilo a zavolá metodu pro naplnìní widgetù daty//
		if (ticket != null) {
			setTicket(ticket);
		}
	}

	/**
	 * Naplní widgety daty z instance PL pøedané v parametru
	 * 
	 * @param ticket
	 */
	private void setTicket(Ticket ticket) {
		// naplní formuláø editovatelnými daty
		TicketSetter.setTicketBasic(this, ticket);
		// naplní formuláø needitovatelnými daty
		TicketSetter.setTicketExtra(this, ticket);
	}

	/**
	 * Posluchaè stisknutí tlaèítka tisku PL
	 * 
	 * @param button Stisknuté tlaèítko
	 */
	public void printTicket(View button) {
		// TODO: podporu tisku implementovat až ve fázi 2
		// Pro print jsem nahore udelal promenou PRINT, pro startActivityForResult jako je u editace nize. :)
	}

	/**
	 * Posluchaè stisknutí tlaèítka editace PL
	 * 
	 * @param button Stisknuté tlaèítko
	 */
	public void editTicket(View button) {
		// vytvoøí Intent, vloží do nìj index na zobrazený PL a spustí aktivitu
		// pro editaci tohoto PL
		this.startActivityForResult(new Intent(this, TicketEditActivity.class).putExtra(
				"Ticket", ticketIndex), EDIT);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// Navrat z EditActivity uspesnem po dokonceni editace. Refreshne data na obrazovce
		if (requestCode == EDIT && resultCode == RESULT_OK) {
			Ticket ticket = app.getTicketDao().getTicket(ticketIndex);
			// ovìøí, zda se nìo vrátilo a zavolá metodu pro naplnìní widgetù daty//
			if (ticket != null) {
				setTicket(ticket);
			}
		}
	}	
	
}
