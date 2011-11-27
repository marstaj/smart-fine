package cz.smartfine;

import dao.TicketDAO;
import model.Ticket;
import model.util.TicketSetter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * T��da aktivity pro zobrazen� informac� z parkovac�ho l�stku
 * @author Pavel Bro�
 */
public class TicketDetailActivity extends Activity{
	
	/**
	 * Reprezentuje index zobrazen�ho PL do DAO
	 */
	private int ticketIndex;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketdetail);
		
		//z�sk� index do DAO na l�stek, kter� s m� zobrazit, p�edan� ze spou�t�j�c� aktivity 
		ticketIndex = this.getIntent().getExtras().getInt("Ticket");
		//z�sk� DAO
		TicketDAO dao =((MyApp)this.getApplication()).getTicketDao();
		
		//blok try/catch slou�� k zachycen� v�jimky v p��pad� chybn�ho indexu pole//
		//TODO: a� bude implementov�n loadTicket() bude vracet null v p��pad� chybn�ho indexu -> odstanit toto try/catch//
		//TODO: v p��pad� chyby zobrazit n�jakou hl�ku
		try {
			//z�sk� PL z DAO
			Ticket ticket = dao.getLocals().get(ticketIndex);
			
			//ov���, zda se n�o vr�tilo a zavol� metodu pro napln�n� widget� daty//
			if(ticket != null){
				setTicket(ticket);
			}
		} catch (Exception e) {}
	}
	
	/**
	 * Napln� widgety daty z instance PL p�edan� v parametru
	 * @param ticket
	 */
	private void setTicket(Ticket ticket){
		//napln� formul�� editovateln�mi daty
		TicketSetter.setTicketBasic(this, ticket);
		//napln� formul�� needitovateln�mi daty
		TicketSetter.setTicketExtra(this, ticket);
	}
	
	/**
	 * Poslucha� stisknut� tla��tka tisku PL
	 * @param button Stisknut� tla��tko
	 */
	public void printTicket(View button){
		//TODO: podporu tisku implementovat a� ve f�zi 2
	}
	
	/**
	 * Poslucha� stisknut� tla��tka editace PL
	 * @param button Stisknut� tla��tko
	 */
	public void editTicket(View button){
		//vytvo�� Intent, vlo�� do n�j index na zobrazen� PL a spust� aktivitu pro editaci tohoto PL
		this.startActivity(new Intent(this, TicketEditActivity.class).putExtra("Ticket", ticketIndex));
	}
}
