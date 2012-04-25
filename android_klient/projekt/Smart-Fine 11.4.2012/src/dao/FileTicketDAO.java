package dao;

import java.util.ArrayList;

import cz.smartfine.MyApp;
import cz.smartfine.R;
import android.content.Context;
import model.Ticket;

/**
 * Tøída pro práci s DAO pro PL
 * @author Martin Štajner
 * 
 */
public class FileTicketDAO implements TicketDAO {

	/**
	 * Kontext aplikace
	 */
	private Context context;
	/**
	 * DAO pro ukládání do souboru
	 */
	private FileDAO dao;
	/**
	 * List všech lokálnì uložených PL
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
	public FileTicketDAO(Context context) throws Exception {
		super();
		this.context = context;
		this.dao = new FileDAO(context);
		this.tickets = new ArrayList<Ticket>();
		this.app = (MyApp) context.getApplicationContext();
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
		app.getPhotoDAO().deleteAllPhotos(tickets);
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