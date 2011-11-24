package dao;

import java.util.ArrayList;
import android.content.Context;
import model.Ticket;

/**
 * @author Martin Stajner
 * 
 */
public class TicketDao {

	/**
	 * Kontext aplikace - kvuli relativni ceste k ulozenym souborum aplikace
	 */
	Context context;
	/**
	 * DAO
	 */
	DAO dao;
	/**
	 * List vsech lokalne ulozenych listku
	 */
	ArrayList<Ticket> locals;
	/**
	 * Instance sama sebe - kvuli singletonu
	 */
	static TicketDao ticketDao;

	/**
	 * Singleton TicketDAO
	 * 
	 * @return
	 */
	public static TicketDao getInstance(Context context) {
		if (ticketDao == null) {
			ticketDao = new TicketDao(context);
		}
		return ticketDao;
	}
	
	/**
	 * Konstruktor
	 * 
	 * @param context
	 */
	public TicketDao(Context context) {
		super();
		this.context = context;
		this.dao = DAO.getInstance(context);
		this.locals = new ArrayList<Ticket>();
	}

	/**
	 * Ulozeni listu zaznamu privatne na disk
	 * 
	 * @param ticket
	 * @return
	 */
	public void saveTicket(Ticket ticket) throws Exception{
		locals.add(ticket);
		dao.saveObjectToFile(locals, "data.smf");
	}

	/**
	 * Nacteni listu zaznamu ze souboru z disku
	 */
	@SuppressWarnings("unchecked")
	public void loadTickets() throws Exception {
		Object o = dao.loadObjectFromFile("data.smf");
		if (o instanceof ArrayList) {
			locals = (ArrayList<Ticket>) o;
		}
	}

	public ArrayList<Ticket> getLocals() {
		return locals;
	}
}