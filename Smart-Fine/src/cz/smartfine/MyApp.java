package cz.smartfine;

import model.Toaster;
import dao.TicketDao;
import android.app.Application;

/**
 * @author Martin Stajner
 *
 */
public class MyApp extends Application {

	/**
	 * Pristup k DAO
	 */
	TicketDao ticketDao;	
	
	@Override
	public void onCreate() {
		super.onCreate();
		Toaster.context = getApplicationContext();
		ticketDao = TicketDao.getInstance(getApplicationContext());
		try {
			ticketDao.loadTickets();
		} catch (Exception e) {
			Toaster.toast("Nepoda�ilo se na��st ulo�en� l�stky", Toaster.LONG);
			e.printStackTrace();
		}
	}

	public TicketDao getTicketDao() {
		return ticketDao;
	}

	public void setTicketDao(TicketDao dao) {
		this.ticketDao = dao;
	}
}
