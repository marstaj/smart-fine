package dao;

import java.util.ArrayList;
import cz.smartfine.R;
import android.content.Context;
import model.Ticket;

/**
 * T��da pro pr�ci s DAO pro PL
 * @author Martin �tajner
 * 
 */
public class FileTicketDAO implements TicketDAO {

	/**
	 * Kontext aplikace
	 */
	Context context;
	/**
	 * DAO pro ukl�d�n� do souboru
	 */
	FileDAO dao;
	/**
	 * List v�ech lok�ln� ulo�en�ch PL
	 */
	ArrayList<Ticket> tickets;

	/**
	 * Konstruktor
	 * 
	 * @param context
	 * @throws Exception
	 */
	public FileTicketDAO(Context context) throws Exception {
		super();
		this.context = context;
		this.dao = new FileDAO(context);
		this.tickets = new ArrayList<Ticket>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.TicketDAO#saveTicket(model.Ticket)
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
	 * @see dao.TicketDAO#getTickets()
	 */
	public ArrayList<Ticket> getAllTickets() {
		return tickets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.TicketDAO#getTicket()
	 */
	public Ticket getTicket(int index) {
		return tickets.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.TicketDAO#deleteAllTickets()
	 */
	public void deleteAllTickets() throws Exception {
		tickets.clear();
		dao.saveObjectToFile(tickets, context.getString(R.string.file_tickets));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.TicketDAO#deleteTicket(model.Ticket)
	 */
	public void deleteTicket(Ticket ticket) {
		tickets.remove(ticket);
	}
}