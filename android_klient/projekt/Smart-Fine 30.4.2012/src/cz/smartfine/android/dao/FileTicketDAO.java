package cz.smartfine.android.dao;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.MyApp;
import cz.smartfine.android.dao.interfaces.ITicketDAO;
import cz.smartfine.android.model.Ticket;
import android.content.Context;

/**
 * Třída pro práci s DAO pro parkovací lískty
 * @author Martin Štajner
 * 
 */
public class FileTicketDAO implements ITicketDAO {

	/**
	 * Kontext aplikace
	 */
	private Context context;
	/**
	 * DAO pro ukládání do souboru
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
		saveAllTickets();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.ITicketDAO#saveTicket(cz.smartfine.android.model.Ticket)
	 */
	public void saveAllTickets() throws Exception {
		dao.saveObjectToFile(tickets, context.getString(R.string.file_tickets));
	}

	/**
	 * Načte PL z uložiště do paměti
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void loadAllTickets() throws Exception {
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