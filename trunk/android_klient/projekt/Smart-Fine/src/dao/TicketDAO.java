package dao;

import java.util.ArrayList;

import model.Ticket;

/**
 * Interface pro práci s DAO pro PL
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
	 * Vrátí list uložených PL v pamìti
	 * 
	 * @return List uložených PL v pamìti
	 */
	public abstract ArrayList<Ticket> getAllTickets();

	/**
	 * Vrátí PL dle indexu
	 * 
	 * @param index Index zadaneho PL
	 * @return Listek
	 */
	public abstract Ticket getTicket(int index);

	/**
	 * Smaže všechny lokálne uložené  PL
	 * 
	 * @throws Exception
	 */
	public abstract void deleteAllTickets() throws Exception;

	/**
	 * Smaže daný PL
	 */
	public abstract void deleteTicket(Ticket ticket);
	
	/**
	 * Nahraje všechny PL ze souboru
	 * @throws Exception
	 */
	public void loadTickets() throws Exception;
}