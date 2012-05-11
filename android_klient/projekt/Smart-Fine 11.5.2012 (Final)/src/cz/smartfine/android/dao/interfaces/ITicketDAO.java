package cz.smartfine.android.dao.interfaces;

import java.util.ArrayList;

import cz.smartfine.android.model.Ticket;

/**
 * Interface představující objekt, který ukládá, načítá a operuje se seznamem
 * parkovacích lístků.
 * 
 * @author Martin Štajner
 * 
 */
public interface ITicketDAO {

	/**
	 * Metoda uloží parkovací lístek do seznamu parkovacích lístků
	 * 
	 * @param ticket
	 *            Parkovací lístek
	 */
	public abstract void saveTicket(Ticket ticket);

	/**
	 * Uloží všechny lístky do uložiště
	 * 
	 * 
	 * @throws Exception
	 *             Výjimka vznikne pokud nastane chyba při ukládání do uložiště
	 */
	public abstract void saveAllTickets() throws Exception;

	/**
	 * Metoda vrátí seznam uložených parkovacích lístků
	 * 
	 * @return List Seznam parkovacích lístků
	 */
	public abstract ArrayList<Ticket> getAllTickets();

	/**
	 * MEtoda vrátí parkovací lístek podle indexu
	 * 
	 * @param index
	 *            Index zadaneho parkovacíjo lístku
	 * @return Listek Parkovací lístek
	 */
	public abstract Ticket getTicket(int index);

	/**
	 * Metoda smaže všechny uložené parkovací lístky
	 * 
	 * @throws Exception
	 *             Výjimka vznikne, když se nepodaří prázdný seznam uložit do
	 *             uložiště
	 */
	public abstract void deleteAllTickets() throws Exception;

	/**
	 * Metoda odstraní parkovací lístek ze seznamu parkovacích lístků.
	 */
	public abstract void deleteTicket(Ticket ticket);

	/**
	 * Metoda načte seznam parkovacích lístků z uložiště.
	 * 
	 * @throws Exception
	 *             Výjimka vznikne když dojde k chybě během nahrávání
	 */
	public void loadAllTickets() throws Exception;

	/**
	 * Metoda uloží parkovací lístek předaný v parametru na místo indexu
	 * 
	 * @param ticket
	 *            Parkovací lístek
	 * @param index
	 *            Index, kam se má uložit
	 */
	public abstract void saveTicket(Ticket ticket, int index);
}