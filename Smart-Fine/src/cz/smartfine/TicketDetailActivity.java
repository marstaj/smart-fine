package cz.smartfine;

import model.Ticket;
import model.util.TicketSetter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * T��da aktivity pro zobrazen� informac� z parkovac�ho l�stku
 * 
 * @author Pavel Bro�
 */
public class TicketDetailActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;
	/**
	 * Reprezentuje index zobrazen�ho PL do DAO
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

		// z�sk� index do DAO na l�stek, kter� s m� zobrazit, p�edan� ze
		// spou�t�j�c� aktivity
		ticketIndex = this.getIntent().getExtras().getInt("Ticket");

		Ticket ticket = app.getTicketDao().getTicket(ticketIndex);

		// ov���, zda se n�o vr�tilo a zavol� metodu pro napln�n� widget� daty//
		if (ticket != null) {
			setTicket(ticket);
		}
	}

	/**
	 * Napln� widgety daty z instance PL p�edan� v parametru
	 * 
	 * @param ticket
	 */
	private void setTicket(Ticket ticket) {
		// napln� formul�� editovateln�mi daty
		TicketSetter.setTicketBasic(this, ticket);
		// napln� formul�� needitovateln�mi daty
		TicketSetter.setTicketExtra(this, ticket);
	}

	/**
	 * Poslucha� stisknut� tla��tka tisku PL
	 * 
	 * @param button Stisknut� tla��tko
	 */
	public void printTicket(View button) {
		// TODO: podporu tisku implementovat a� ve f�zi 2
		// Pro print jsem nahore udelal promenou PRINT, pro startActivityForResult jako je u editace nize. :)
	}

	/**
	 * Poslucha� stisknut� tla��tka editace PL
	 * 
	 * @param button Stisknut� tla��tko
	 */
	public void editTicket(View button) {
		// vytvo�� Intent, vlo�� do n�j index na zobrazen� PL a spust� aktivitu
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
			// ov���, zda se n�o vr�tilo a zavol� metodu pro napln�n� widget� daty//
			if (ticket != null) {
				setTicket(ticket);
			}
		}
	}	
	
}
