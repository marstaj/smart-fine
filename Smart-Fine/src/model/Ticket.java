package model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Martin Stajner
 * 
 */
@SuppressWarnings("serial")
public class Ticket implements Serializable {

	/**
	 * Slu�ebn� ��slo - ��slo odznaku policisty
	 */
	private int badgeNumber;
	/**
	 * M�sto
	 */
	private String city;
	/**
	 * Datum a �as
	 */
	private Date date;
	/**
	 * Popis DZ
	 */
	private String descriptionDZ;
	/**
	 * Popis ud�losti
	 */
	private String eventDescription;
	/**
	 * M�sto ud�losti (nap�.: na chodn�ku)
	 */
	private String location;
	/**
	 * Zda je pohybliv� DZ
	 */
	private boolean moveableDZ;
	/**
	 * Mezin�rodn� pozn�vac� zna�ka
	 */
	private String mpz;
	/**
	 * ��slo ulice
	 */
	private int number;
	/**
	 * Fotografie ud�losti
	 */
	private String[] photos;
	/**
	 * St�tn� pozn�vac� zna�ka
	 */
	private String spz;
	/**
	 * Barva SPZ
	 */
	private String spzColor;
	/**
	 * Ulice
	 */
	private String street;
	/**
	 * Zda odtah vozidla
	 */
	private boolean tow;
	/**
	 * Zna�ka automobilu
	 */
	private String vehicleBrand;
	/**
	 * Typ automobilu
	 */
	private String vehicleType;
	/**
	 * Z�kon popisuj�c� p�estupek
	 */
	private Law law;	
	/**
	 * Zda byl u� l�stek vyti�t�n
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

	public String getDescriptionDZ() {
		return descriptionDZ;
	}

	public void setDescriptionDZ(String descriptionDZ) {
		this.descriptionDZ = descriptionDZ;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
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

	public String[] getPhotos() {
		return photos;
	}

	public void setPhotos(String[] photos) {
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