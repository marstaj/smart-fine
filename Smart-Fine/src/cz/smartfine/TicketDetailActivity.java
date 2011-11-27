package cz.smartfine;

import dao.TicketDAO;
import model.Ticket;
import model.util.TicketSetter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Tøída aktivity pro zobrazení informací z parkovacího lístku
 * @author Pavel Brož
 */
public class TicketDetailActivity extends Activity{
	
	/**
	 * Reprezentuje index zobrazeného PL do DAO
	 */
	private int ticketIndex;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketdetail);
		
		//získá index do DAO na lístek, který s má zobrazit, pøedaný ze spouštìjící aktivity 
		ticketIndex = this.getIntent().getExtras().getInt("Ticket");
		//získá DAO
		TicketDAO dao =((MyApp)this.getApplication()).getTicketDao();
		
		//blok try/catch slouží k zachycení výjimky v pøípadì chybného indexu pole//
		//TODO: až bude implementován loadTicket() bude vracet null v pøípadì chybného indexu -> odstanit toto try/catch//
		//TODO: v pøípadì chyby zobrazit nìjakou hlášku
		try {
			//získá PL z DAO
			Ticket ticket = dao.getLocals().get(ticketIndex);
			
			//ovìøí, zda se nìo vrátilo a zavolá metodu pro naplnìní widgetù daty//
			if(ticket != null){
				setTicket(ticket);
			}
		} catch (Exception e) {}
	}
	
	/**
	 * Naplní widgety daty z instance PL pøedané v parametru
	 * @param ticket
	 */
	private void setTicket(Ticket ticket){
		//naplní formuláø editovatelnými daty
		TicketSetter.setTicketBasic(this, ticket);
		//naplní formuláø needitovatelnými daty
		TicketSetter.setTicketExtra(this, ticket);
	}
	
	/**
	 * Posluchaè stisknutí tlaèítka tisku PL
	 * @param button Stisknuté tlaèítko
	 */
	public void printTicket(View button){
		//TODO: podporu tisku implementovat až ve fázi 2
	}
	
	/**
	 * Posluchaè stisknutí tlaèítka editace PL
	 * @param button Stisknuté tlaèítko
	 */
	public void editTicket(View button){
		//vytvoøí Intent, vloží do nìj index na zobrazený PL a spustí aktivitu pro editaci tohoto PL
		this.startActivity(new Intent(this, TicketEditActivity.class).putExtra("Ticket", ticketIndex));
	}
}
