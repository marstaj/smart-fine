package cz.smartfine.android.dao.interfaces;

import java.util.ArrayList;

import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.model.Waypoint;

/**
 * Interface pro práci s DAO pro polohu policisty
 * 
 * @author Martin Štajner
 * 
 */
public interface ILocationDAO {

	/**
	 * Uloží parkovací lístek předaný v parametru
	 * TODO zmenit
	 * 
	 * @param ticket
	 *            Parkovací lístek
	 * @throws Exception
	 */
	public abstract void saveWaypoint(Waypoint waypoint) throws Exception;

	/**
	 * Uloží všechny lístky do souboru
	 * TODO zmenit
	 * @return 
	 * 
	 * @throws Exception
	 */
	public abstract void saveAllWaypoints() throws Exception;

	/**
	 * Vr�t� list ulo�en�ch PL v pam�ti
	 * TODO zmenit
	 * 
	 * @return List ulo�en�ch PL v pam�ti
	 */
	public abstract ArrayList<Waypoint> getAllWaypoints();

	/**
	 * Vrátí parkovací lístek podle indexu
	 * TODO zmenit
	 * @param index
	 *            Index zadaneho parkovacíjo lístku
	 * @return Listek
	 */
	public abstract Waypoint getWaypoint(int index);

	/**
	 * Sma�e v�echny lok�lne ulo�en� PL
	 * TODO zmenit
	 * @throws Exception
	 */
	public abstract void deleteAllWaypoints() throws Exception;

	/**
	 * Sma�e dan� PL
	 * TODO zmenit
	 */
	public abstract void deleteWaypoint(Waypoint waypoint);

	/**
	 * Nahraje v�echny PL ze souboru
	 * TODO zmenit
	 * 
	 * @throws Exception
	 */
	public void loadAllWaypoints() throws Exception;
}