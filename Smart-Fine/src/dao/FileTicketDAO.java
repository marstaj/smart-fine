package dao;

import java.util.ArrayList;
import cz.smartfine.R;
import android.content.Context;
import model.Ticket;

/**
 * @author Martin Stajner
 * 
 */
public class FileTicketDAO implements TicketDAO {

	/**
	 * Kontext aplikace - kvuli relativni ceste k ulozenym souborum aplikace
	 */
	Context context;
	/**
	 * DAO
	 */
	FileDAO dao;
	/**
	 * List vsech lokalne ulozenych listku
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
		this.loadTickets();
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
	 * Naète PL z úložištì do pamìti
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void loadTickets() throws Exception {
		Object o = dao.loadObjectFromFile(context
				.getString(R.string.file_tickets));
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