package cz.smartfine.networklayer.model;

import java.util.Date;

/**
 * Obsahuje informace o parkov�n� vozidlav z�n� placen�ho st�n�.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SMSParkingInfo {

	/**
	 * Datum a �as, od kter�ho m��e vozidlo parkovat v z�n� placen�ho st�n�.
	 */
	private Date parkingSince;
	/**
	 * Ud�v�, jak� je stav parkov�n� vozidla tj. zda vozidlo sm� v dan�m �ase parkovat
	 * v z�n� placen�ho st�n� �i ne.
	 */
	private ParkingStatus parkingStatus;
	/**
	 * Datum a �as, do kter�ho m��e vozidlo parkovat v z�n� placen�ho st�n�.
	 */
	private Date parkingUntil;
	/**
	 * SPZ vozidla, na kter� se vztahuj� informace.
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