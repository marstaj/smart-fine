package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Tøída modelu PL
 * 
 * @author Martin Stajner
 * 
 */

public class Ticket implements Serializable {

	/**
	 * UID verze serializace
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Služební èíslo - èíslo odzsnaku policisty
	 */
	private int badgeNumber;
	/**
	 * Mìsto
	 */
	private String city;
	/**
	 * Datum a èas
	 */
	private Date date;
	
	/**
	 * Místo události (napø.: èíslo lampy)
	 */
	private String location;
	/**
	 * Zda je pohyblivé DZ
	 */
	private boolean moveableDZ;
	/**
	 * Mezinárodní poznávací znaèka
	 */
	private String mpz;
	/**
	 * Èíslo ulice
	 */
	private int number;
	/**
	 * Fotodokumentace události
	 */
	private ArrayList<File> photos;
	/**
	 * Státní poznávací znaèka
	 */
	private String spz;
	/**
	 * Barva státní poznávací znaèky
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
	 * Znaèka automobilu
	 */
	private String vehicleBrand;
	/**
	 * Typ automobilu
	 */
	private String vehicleType;
	/**
	 * Zákon popisující pøestupek
	 */
	private Law law;
	/**
	 * Zda byl už lístek vytištìn
	 */
	private boolean printed;

	public int getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(int badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isMoveableDZ() {
		return moveableDZ;
	}

	public void setMoveableDZ(boolean moveableDZ) {
		this.moveableDZ = moveableDZ;
	}

	public String getMpz() {
		return mpz;
	}

	public void setMpz(String mpz) {
		this.mpz = mpz;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ArrayList<File> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<File> photos) {
		this.photos = photos;
	}

	public String getSpz() {
		return spz;
	}

	public void setSpz(String spz) {
		this.spz = spz;
	}

	public String getSpzColor() {
		return spzColor;
	}

	public void setSpzColor(String spzColor) {
		this.spzColor = spzColor;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public boolean isTow() {
		return tow;
	}

	public void setTow(boolean tow) {
		this.tow = tow;
	}

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Law getLaw() {
		return law;
	}

	public void setLaw(Law law) {
		this.law = law;
	}

	public boolean isPrinted() {
		return printed;
	}

	public void setPrinted(boolean printed) {
		this.printed = printed;
	}

}