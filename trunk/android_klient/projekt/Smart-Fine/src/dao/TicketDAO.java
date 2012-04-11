package dao;

import java.util.ArrayList;

import model.Ticket;

/**
 * Interface pro pr�ci s DAO pro PL
 * @author Pavel Bro�
 * 
 */
public interface TicketDAO {

	/**
	 * Ulo�� PL p�edan� v parametru
	 * 
	 * @param ticket
	 *            PL ur�en� k ulo�en�
	 * @throws Exception
	 */
	public abstract void saveTicket(Ticket ticket) throws Exception;

	/**
	 * Vr�t� list ulo�en�ch PL v pam�ti
	 * 
	 * @return List ulo�en�ch PL v pam�ti
	 */
	public abstract ArrayList<Ticket> getAllTickets();

	/**
	 * Vr�t� PL dle indexu
	 * 
	 * @param index Index zadaneho PL
	 * @return Listek
	 */
	public abstract Ticket getTicket(int index);

	/**
	 * Sma�e v�echny lok�lne ulo�en�  PL
	 * 
	 * @throws Exception
	 */
	public abstract void deleteAllTickets() throws Exception;

	/**
	 * Sma�e dan� PL
	 */
	public abstract void deleteTicket(Ticket ticket);
	
	/**
	 * Nahraje v�echny PL ze souboru
	 * @throws Exception
	 */
	public void loadTickets() throws Exception;
}