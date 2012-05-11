package cz.smartfine.android.model;

import java.io.Serializable;

/**
 * Třída reprezentující objekt geolokačního bodu, waypointu.
 * 
 * @author Martin Štajner
 * 
 */
public class Waypoint implements Serializable {

	/**
	 * UID verze serializace
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Zeměpisná délka
	 */
	private double longtitude;

	/**
	 * Zeměpisná šířka
	 */
	private double latitude;

	/**
	 * Čas pořízení geolokačního bodu
	 */
	private long time;

	/**
	 * Metoda vrátí zeměpisnou délku
	 * 
	 * @return Zeměpisná délka
	 */
	public double getLongtitude() {
		return longtitude;
	}

	/**
	 * Metoda nastaví zeměpisnou délku
	 * 
	 * @param longtitude
	 *            Zeměpisná délka
	 */
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	/**
	 * Metoda vrátí zeměpisnou šířku
	 * 
	 * @return Zeměpisná šířka
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Metoda nastaví zeměpisnou šířku
	 * 
	 * @param latitude
	 *            Zeměpisná šířka
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Metoda vrátí čas pořízení geolokačního bodu
	 * 
	 * @return Čas pořízení geolokačního bodu
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Metoda nastaví čas pořízení geolokačního bodu
	 * 
	 * @param time
	 *            Čas pořízení geolokačního bodu
	 */
	public void setTime(long time) {
		this.time = time;
	}
}
