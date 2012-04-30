package cz.smartfine.android.dao.interfaces;

import java.util.ArrayList;

import cz.smartfine.android.model.Ticket;

/**
 * Interface pro pr�ci s DAO pro PL
 * 
 * @author Pavel Bro�
 * 
 */
public interface ITicketDAO {

	/**
	 * Uloží parkovací lístek předaný v parametru
	 * 
	 * @param ticket
	 *            Parkovací lístek
	 * @throws Exception
	 */
	public abstract void saveTicket(Ticket ticket) throws Exception;

	/**
	 * Uloží všechny lístky do souboru
	 * @return 
	 * 
	 * @throws Exception
	 */
	public abstract void saveAllTickets() throws Exception;

	/**
	 * Vr�t� list ulo�en�ch PL v pam�ti
	 * 
	 * @return List ulo�en�ch PL v pam�ti
	 */
	public abstract ArrayList<Ticket> getAllTickets();

	/**
	 * Vrátí parkovací lístek podle indexu
	 * 
	 * @param index
	 *            Index zadaneho parkovacíjo lístku
	 * @return Listek
	 */
	public abstract Ticket getTicket(int index);

	/**
	 * Sma�e v�echny lok�lne ulo�en� PL
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
	 * 
	 * @throws Exception
	 */
	public void loadAllTickets() throws Exception;
}