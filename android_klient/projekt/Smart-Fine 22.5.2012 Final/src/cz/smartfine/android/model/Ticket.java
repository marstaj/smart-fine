package cz.smartfine.android.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Třída reprezentující objekt parkovacího lístku.
 * 
 * @author Martin Štajner
 * 
 */

public class Ticket implements Serializable {

	/**
	 * UID verze serializace
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Služební číslo policisty - číslo odznaku policisty
	 */
	private int badgeNumber;

	/**
	 * Město
	 */
	private String city;

	/**
	 * Datum a čas
	 */
	private Date date;

	/**
	 * Místo události (např.: číslo lampy)
	 */
	private String location;

	/**
	 * Zda je pohyblivé DZ
	 */
	private boolean moveableDZ;

	/**
	 * MPZ vozidla
	 */
	private String mpz;

	/**
	 * Číslo ulice
	 */
	private int number;

	/**
	 * Fotodokumentace události
	 */
	private ArrayList<File> photos;

	/**
	 * SPZ vozidla
	 */
	private String spz;

	/**
	 * Barva SPZ vozidla
	 */
	private String spzColor;

	/**
	 * Ulice
	 */
	private String street;

	/**
	 * Zda je vozidlo odtaženo
	 */
	private boolean tow;

	/**
	 * Značka automobilu
	 */
	private String vehicleBrand;

	/**
	 * Typ automobilu
	 */
	private String vehicleType;

	/**
	 * Zákon popisující přestupek
	 */
	private Law law;

	/**
	 * Zda byl už lístek vytištěn
	 */
	private boolean printed;

	/**
	 * Metoda vrátí služební číslo policisty
	 * 
	 * @return Služební číslo policisty
	 */
	public int getBadgeNumber() {
		return badgeNumber;
	}

	/**
	 * Metoda nastaví služební číslo policisty
	 * 
	 * @param badgeNumber
	 *            Služební číslo policisty
	 */
	public void setBadgeNumber(int badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	/**
	 * Metoda vrátí město
	 * 
	 * @return Město
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Metoda nastaví město
	 * 
	 * @param city Město
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Metoda vrátí datum a čas
	 * 
	 * @return Datum a čas
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Metoda nastaví datum a čas
	 * 
	 * @param date
	 *            Datum a čas
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Metoda vrátí místo události
	 * 
	 * @return Místo události
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Metoda vrátí místo události
	 * 
	 * @param location
	 *            Místo události
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Metoda vrátí zda je pohyblivé DZ
	 * 
	 * @return Zda je pohyblivé DZ
	 */
	public boolean isMoveableDZ() {
		return moveableDZ;
	}

	/**
	 * Metoda nastaví zda je pohyblivé DZ
	 * 
	 * @param moveableDZ
	 *            Zda je pohyblivé DZ
	 */
	public void setMoveableDZ(boolean moveableDZ) {
		this.moveableDZ = moveableDZ;
	}

	/**
	 * Metoda vrátí MPZ vozidla
	 * 
	 * @return MPZ vozidla
	 */
	public String getMpz() {
		return mpz;
	}

	/**
	 * Metoda nastaví MPZ vozidla
	 * 
	 * @param mpz
	 *            MPZ vozidla
	 */
	public void setMpz(String mpz) {
		this.mpz = mpz;
	}

	/**
	 * Metoda vrátí číslo ulice
	 * 
	 * @return Číslo ulice
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Metoda nastaví číslo ulice
	 * 
	 * @param number
	 *            Číslo ulice
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * Metoda vrátí fotodokumentace události
	 * 
	 * @return Fotodokumentace události
	 */
	public ArrayList<File> getPhotos() {
		return photos;
	}

	/**
	 * Metoda nastaví fotodokumentace události
	 * 
	 * @param photos
	 *            Fotodokumentace události
	 */
	public void setPhotos(ArrayList<File> photos) {
		this.photos = photos;
	}

	/**
	 * Metoda vrátí SPZ vozidla
	 * 
	 * @return SPZ vozidla
	 */
	public String getSpz() {
		return spz;
	}

	/**
	 * Metoda nastaví SPZ vozidla
	 * 
	 * @param spz
	 *            SPZ vozidla
	 */
	public void setSpz(String spz) {
		this.spz = spz;
	}

	/**
	 * Metoda vrátí barvu SPZ vozidla
	 * 
	 * @return Barva SPZ vozidla
	 */
	public String getSpzColor() {
		return spzColor;
	}

	/**
	 * Metoda nastaví barvu SPZ vozidla
	 * 
	 * @param spzColor
	 *            Barva SPZ vozidla
	 */
	public void setSpzColor(String spzColor) {
		this.spzColor = spzColor;
	}

	/**
	 * Metoda vrátí ulici
	 * 
	 * @return Ulice
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Metoda nastaví ulici
	 * 
	 * @param street
	 *            Ulice
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Metoda vrátí zda je vozidlo odtaženo
	 * 
	 * @return Zda je vozidlo odtaženo
	 */
	public boolean isTow() {
		return tow;
	}

	/**
	 * Metoda nastaví zda je vozidlo odtaženo
	 * 
	 * @param tow
	 *            Zda je vozidlo odtaženo
	 */
	public void setTow(boolean tow) {
		this.tow = tow;
	}

	/**
	 * Metoda vrátí značku automobilu
	 * 
	 * @return Značka automobilu
	 */
	public String getVehicleBrand() {
		return vehicleBrand;
	}

	/**
	 * Metoda nastaví značku automobilu
	 * 
	 * @param vehicleBrand
	 *            Značka automobilu
	 */
	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	/**
	 * Metoda vrátí typ automobilu
	 * 
	 * @return Typ automobilu
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * Metoda nastaví typ automobilu
	 * 
	 * @param vehicleType
	 *            Typ automobilu
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * Metoda vrátí zákon popisující přestupek
	 * 
	 * @return Zákon popisující přestupek
	 */
	public Law getLaw() {
		return law;
	}

	/**
	 * Metoda nastaví zákon popisující přestupek
	 * 
	 * @param law
	 *            Zákon popisující přestupek
	 */
	public void setLaw(Law law) {
		this.law = law;
	}

	/**
	 * Metoda vrátí zda byl už lístek vytištěn
	 * 
	 * @return Zda byl už lístek vytištěn
	 */
	public boolean isPrinted() {
		return printed;
	}

	/**
	 * Metoda nastaví zda byl už lístek vytištěn
	 * 
	 * @param printed
	 *            Zda byl už lístek vytištěn
	 */
	public void setPrinted(boolean printed) {
		this.printed = printed;
	}

}