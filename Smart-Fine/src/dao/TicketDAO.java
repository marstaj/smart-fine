package dao;

import java.util.ArrayList;

import model.Ticket;

/**
 * @author Pavel Bro�
 * 
 */
public interface TicketDAO {

	/**
	 * Ulo�� PL p�edan� v parametru
	 * @param ticket PL ur�en� k ulo�en�
	 */
	public abstract void saveTicket(Ticket ticket) throws Exception;

	//TODO: p�ed�lat void loadTickets() na ArrayList<Ticket> loadTickets() a odstranit getLocals
	/**
	 * Na�te PL z �lo�i�t� do pam�ti
	 * @return Pole v�ech na�ten�ch PL
	 * @throws Exception
	 */
	//public abstract ArrayList<Ticket> loadTickets() throws Exception;

	/**
	 * Na�te PL z �lo�i�t� do pam�ti
	 * @throws Exception
	 */
	public abstract void loadTickets() throws Exception;
	
	public abstract ArrayList<Ticket> getLocals();

}