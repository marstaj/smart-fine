package cz.smartfine;

import java.util.ArrayList;

import model.Ticket;
import model.Toaster;
import dao.TicketDao;
import android.app.Application;

/**
 * @author Martin Stajner
 *
 */
public class MyApp extends Application {

	/**
	 * List vsech lokalne ulozenych listku
	 */
	ArrayList<Ticket> locals;
	/**
	 * Pristup k DAO
	 */
	TicketDao dao;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		Toaster.context = getApplicationContext();
		dao = new TicketDao(getApplicationContext());
		// Nacteni lokalne ulozenych listku ze souboru
		locals = dao.loadTickets();
		if (locals == null) {
			locals = new ArrayList<Ticket>();
		}		
	}

	public ArrayList<Ticket> getLocals() {
		return locals;
	}

	public void setLocals(ArrayList<Ticket> locals) {
		this.locals = locals;
	}

	public TicketDao getDao() {
		return dao;
	}

	public void setDao(TicketDao dao) {
		this.dao = dao;
	}
}
