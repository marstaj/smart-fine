package cz.smartfine.networklayer.model;

import java.util.Date;

/**
 * Obsahuje informace o parkování vozidlav zónì placeného stání.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SMSParkingInfo {

	/**
	 * Datum a èas, od kterého mùže vozidlo parkovat v zónì placeného stání.
	 */
	private Date parkingSince;
	/**
	 * Udává, jaký je stav parkování vozidla tj. zda vozidlo smí v daném èase parkovat
	 * v zónì placeného stání èi ne.
	 */
	private ParkingStatus parkingStatus;
	/**
	 * Datum a èas, do kterého mùže vozidlo parkovat v zónì placeného stání.
	 */
	private Date parkingUntil;
	/**
	 * SPZ vozidla, na které se vztahují informace.
	 */
	private String vehicleRegistrationPlate;

	public SMSParkingInfo(){

	}

	
	/**
	 * Konstruktor.
	 * @param parkingStatus
	 * @param parkingSince
	 * @param parkingUntil
	 * @param vehicleRegistrationPlate
	 */
	public SMSParkingInfo(ParkingStatus parkingStatus, Date parkingSince, Date parkingUntil, String vehicleRegistrationPlate) {
		super();
		this.parkingSince = parkingSince;
		this.parkingStatus = parkingStatus;
		this.parkingUntil = parkingUntil;
		this.vehicleRegistrationPlate = vehicleRegistrationPlate;
	}

	/**
	 * @return the parkingSince
	 */
	public Date getParkingSince() {
		return parkingSince;
	}

	/**
	 * @param parkingSince the parkingSince to set
	 */
	public void setParkingSince(Date parkingSince) {
		this.parkingSince = parkingSince;
	}

	/**
	 * @return the parkingStatus
	 */
	public ParkingStatus getParkingStatus() {
		return parkingStatus;
	}

	/**
	 * @param parkingStatus the parkingStatus to set
	 */
	public void setParkingStatus(ParkingStatus parkingStatus) {
		this.parkingStatus = parkingStatus;
	}

	/**
	 * @return the parkingUntil
	 */
	public Date getParkingUntil() {
		return parkingUntil;
	}

	/**
	 * @param parkingUntil the parkingUntil to set
	 */
	public void setParkingUntil(Date parkingUntil) {
		this.parkingUntil = parkingUntil;
	}

	/**
	 * @return the vehicleRegistrationPlate
	 */
	public String getVehicleRegistrationPlate() {
		return vehicleRegistrationPlate;
	}

	/**
	 * @param vehicleRegistrationPlate the vehicleRegistrationPlate to set
	 */
	public void setVehicleRegistrationPlate(String vehicleRegistrationPlate) {
		this.vehicleRegistrationPlate = vehicleRegistrationPlate;
	}

	
}