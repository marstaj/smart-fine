package cz.smartfine.android.dao.interfaces;

import java.util.ArrayList;

import cz.smartfine.android.model.Waypoint;

/**
 * Interface představující objekt, který ukládá, načítá a operuje se seznamem
 * geolokačních dat.
 * 
 * @author Martin Štajner
 * 
 */
public interface ILocationDAO {

	/**
	 * Metoda uloží waypoint do seznamu.
	 * 
	 * @param waypoint
	 *            Waypoint k uložení
	 */
	public abstract void saveWaypoint(Waypoint waypoint);

	/**
	 * Metoda uloží všechny lístky do uložiště.
	 * 
	 * @throws Exception
	 *             Výjimka vznikne, když vznikdne chyba při zapisování do
	 *             soubotu.
	 */
	public abstract void saveAllWaypoints() throws Exception;

	/**
	 * MEtoda vrátí seznam geolokačních dat.
	 * 
	 * @return List Seznam geolokačních dat
	 */
	public abstract ArrayList<Waypoint> getAllWaypoints();

	/**
	 * Metoda vrátí waypoind, podle daného indexu.
	 * 
	 * @param index
	 *            Index waypointu
	 * @return Vybraný waypoint
	 */
	public abstract Waypoint getWaypoint(int index);

	/**
	 * Metoda smaže všechna geolokační data.
	 * 
	 * @throws Exception
	 *             Výjimka vznikne, pokud nastane chyba při ukládání do
	 *             uložiště..
	 */
	public abstract void deleteAllWaypoints() throws Exception;

	/**
	 * Metoda smaže waypoint ze seznamu
	 * 
	 * @param waypoint
	 *            Waypoint, který se má smazat
	 */
	public abstract void deleteWaypoint(Waypoint waypoint);

	/**
	 * Metoda nahraje všechna geolokační data z uložiště do paměti.
	 * 
	 * @throws Exception
	 *             Výjimka vznikne, pokud nastane chyba při načítání z uložiště.
	 */
	public void loadAllWaypoints() throws Exception;

	/**
	 * Metoda vrátí poslední přidaný waypoint
	 * 
	 * @return Waypoint
	 */
	public Waypoint getLastWaypoint();
}