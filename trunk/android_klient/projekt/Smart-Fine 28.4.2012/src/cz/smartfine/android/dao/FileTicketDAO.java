package cz.smartfine.android.dao;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.MyApp;
import cz.smartfine.android.dao.interfaces.ITicketDAO;
import cz.smartfine.android.model.Ticket;
import android.content.Context;

/**
 * T��da pro pr�ci s DAO pro PL
 * @author Martin �tajner
 * 
 */
public class FileTicketDAO implements ITicketDAO {

	/**
	 * Kontext aplikace
	 */
	private Context context;
	/**
	 * DAO pro ukl�d�n� do souboru
	 */
	private FileDAO dao;
	/**
	 * List v�ech lok�ln� ulo�en�ch PL
	 */
	private ArrayList<Ticket> tickets;
	/**
	 * instance aplikace
	 */
	private MyApp app;

	/**
	 * Konstruktor
	 * 
	 * @param context
	 * @throws Exception
	 */
	public FileTicketDAO(Context context) {
		super();
		this.context = context;
		this.dao = new FileDAO(context);
		this.tickets = new ArrayList<Ticket>();
		this.app = (MyApp) context.getApplicationContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.ITicketDAO#saveTicket(cz.smartfine.android.model.Ticket)
	 */
	public void saveTicket(Ticket ticket) throws Exception {
		if (!tickets.contains(ticket)) {
			tickets.add(ticket);
		}
		dao.saveObjectToFile(tickets, context.getString(R.string.file_tickets));
	}

	/**
	 * Na�te PL z �lo�i�t� do pam�ti
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void loadTickets() throws Exception {
		Object o = dao.loadObjectFromFile(context.getString(R.string.file_tickets));
		if (o instanceof ArrayList) {
			tickets = (ArrayList<Ticket>) o;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.ITicketDAO#getTickets()
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
		dao.saveObjectToFile(tickets, context.getString(R.string.file_tickets));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.ITicketDAO#deleteTicket(cz.smartfine.android.model.Ticket)
	 */
	public void deleteTicket(Ticket ticket) {
		tickets.remove(ticket);
	}
}