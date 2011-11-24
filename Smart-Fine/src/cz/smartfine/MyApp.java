package cz.smartfine;

import model.Toaster;
import dao.TicketDAO;
import android.app.Application;

/**
 * @author Martin Stajner
 *
 */
public class MyApp extends Application {

	/**
	 * Pristup k DAO
	 */
	TicketDAO ticketDAO;	
	
	@Override
	public void onCreate() {
		super.onCreate();
		Toaster.context = getApplicationContext();
		ticketDAO = TicketDAO.getInstance(getApplicationContext());
		try {
			ticketDAO.loadTickets();
		} catch (Exception e) {
			Toaster.toast("Nepoda�ilo se na��st ulo�en� l�stky", Toaster.LONG);
			e.printStackTrace();
		}
	}

	public TicketDAO getTicketDao() {
		return ticketDAO;
	}

	public void setTicketDao(TicketDAO dao) {
		this.ticketDAO = dao;
	}
}
