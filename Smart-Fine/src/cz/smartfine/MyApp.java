package cz.smartfine;

import java.io.FileNotFoundException;
import model.util.Toaster;
import dao.TicketDAO;
import dao.FileTicketDAO;
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
		try {
			ticketDAO = new FileTicketDAO(getApplicationContext());
		} catch (FileNotFoundException e) {
			Toaster.toast("Soubor nenalezen", Toaster.LONG);
		} catch (Exception e) {
			Toaster.toast("Nepodaøilo se naèíst uložené lístky", Toaster.LONG);
		}
	}

	public TicketDAO getTicketDao() {
		return ticketDAO;
	}

	public void setTicketDao(TicketDAO dao) {
		this.ticketDAO = dao;
	}

}
