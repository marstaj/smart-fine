package dao;

import java.util.ArrayList;

import model.Ticket;

/**
 * @author Pavel Brož
 * 
 */
public interface TicketDAO {

	/**
	 * Uloží PL pøedaný v parametru
	 * 
	 * @param ticket
	 *            PL urèený k uložení
	 * @throws Exception
	 */
	public abstract void saveTicket(Ticket ticket) throws Exception;

	/**
	 * Vrati list ulozenych PL v pameti
	 * 
	 * @return List ulozenych PL v pameti
	 */
	public abstract ArrayList<Ticket> getAllTickets();

	/**
	 * Vrati PL dle indexu
	 * 
	 * @param index Index zadaneho PL
	 * @return Listek
	 */
	public abstract Ticket getTicket(int index);

	/**
	 * Smaze vsechny lokalne ulozene PL
	 * 
	 * @throws Exception
	 */
	public abstract void deleteAllTickets() throws Exception;

	/**
	 * Smaze PL
	 */
	public abstract void deleteTicket(Ticket ticket);

}