package cz.smartfine.android.dao;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.MyApp;
import cz.smartfine.android.dao.interfaces.ITicketDAO;
import cz.smartfine.android.model.Ticket;
import android.content.Context;

/**
 * Třída představující objekt, který ukládá parkovací lístky a operuje s nimi.
 * 
 * @author Martin Štajner
 * 
 */
public class FileTicketDAO implements ITicketDAO {

	/**
	 * Kontext aplikace
	 */
	private Context appContext;

	/**
	 * Instance objektu pro ukládání do souboru
	 */
	private FileDAO dao;

	/**
	 * Seznam všech pakovacích lístků
	 */
	private ArrayList<Ticket> tickets;

	/**
	 * instance aplikace
	 */
	private MyApp app;

	/**
	 * Konstruktor třídy
	 * 
	 * @param context
	 *            Kontext aplikace
	 */
	public FileTicketDAO(Context context) {
		super();
		this.appContext = context;
		this.dao = new FileDAO(context);
		this.tickets = new ArrayList<Ticket>();
		this.app = (MyApp) context.getApplicationContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.dao.interfaces.ITicketDAO#saveTicket(cz.smartfine
	 * .android.model.Ticket)
	 */
	public void saveTicket(Ticket ticket) {
		if (!tickets.contains(ticket)) {
			tickets.add(ticket);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.ITicketDAO#saveAllTickets()
	 */
	public void saveAllTickets() throws Exception {
		dao.saveObjectToFile(tickets, appContext.getString(R.string.file_tickets));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.ITicketDAO#loadAllTickets()
	 */
	@SuppressWarnings("unchecked")
	public void loadAllTickets() throws Exception {
		Object o = dao.loadObjectFromFile(appContext.getString(R.string.file_tickets));
		if (o instanceof ArrayList) {
			tickets = (ArrayList<Ticket>) o;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.ITicketDAO#getAllTickets()
	 */
	public ArrayList<Ticket> getAllTickets() {
		return tickets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.ITicketDAO#getTicket()
	 */
	public Ticket getTicket(int index) {
		return tickets.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.ITicketDAO#deleteAllTickets()
	 */
	public void deleteAllTickets() throws Exception {
		app.getPhotoDAO().deleteAllPhotosFromTickets(tickets);
		tickets.clear();
		dao.saveObjectToFile(tickets, appContext.getString(R.string.file_tickets));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.dao.interfaces.ITicketDAO#deleteTicket(cz.smartfine
	 * .android.model.Ticket)
	 */
	public void deleteTicket(Ticket ticket) {
		tickets.remove(ticket);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.dao.interfaces.ITicketDAO#saveTicket(cz.smartfine
	 * .android.model.Ticket, int)
	 */
	public void saveTicket(Ticket ticket, int index) {
		if (!tickets.contains(ticket)) {
			tickets.add(index, ticket);
		}
	}
}