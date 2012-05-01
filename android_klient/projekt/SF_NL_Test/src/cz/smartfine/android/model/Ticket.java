package cz.smartfine.android.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * T��da modelu PL
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
	 * Slu�ebn� ��slo - ��slo odzsnaku policisty
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
	 * M�sto ud�losti (nap�.: ��slo lampy)
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
	 * Fotodokumentace ud�losti
	 */
	private ArrayList<File> photos;
	/**
	 * St�tn� pozn�vac� zna�ka
	 */
	private String spz;
	/**
	 * Barva st�tn� pozn�vac� zna�ky
	 */
	private String spzColor;
	/**
	 * Ulice
	 */
	private String street;
	/**
	 * Zda je vozidlo odta�eno
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