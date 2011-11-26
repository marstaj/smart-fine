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
	 * @param ticket PL urèený k uložení
	 */
	public abstract void saveTicket(Ticket ticket) throws Exception;

	//TODO: pøedìlat void loadTickets() na ArrayList<Ticket> loadTickets() a odstranit getLocals
	/**
	 * Naète PL z úložištì do pamìti
	 * @return Pole všech naètených PL
	 * @throws Exception
	 */
	//public abstract ArrayList<Ticket> loadTickets() throws Exception;

	/**
	 * Naète PL z úložištì do pamìti
	 * @throws Exception
	 */
	public abstract void loadTickets() throws Exception;
	
	public abstract ArrayList<Ticket> getLocals();

}