package cz.smartfine.server.business.client.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Obsahuje informace o parkování vozidlav zóně placeného stání. Používá se pro přístup k DB.
 *
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:45
 */
public class SMSParkingInfoDB implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * ID záznamu.
     */
    private int id;
    /**
     * Datum a čas, od kterého může vozidlo parkovat v zóně placeného stání.
     */
    private Date parkingSince;
    /**
     * Datum a čas, do kterého může vozidlo parkovat v zóně placeného stání.
     */
    private Date parkingUntil;
    /**
     * SPZ vozidla, na které se vztahují informace.
     */
    private String vehicleRegistrationPlate;

    public SMSParkingInfoDB() {
        super();
    }

    /**
     * Konstruktor.
     *
     * @param parkingStatus
     * @param parkingSince
     * @param parkingUntil
     * @param vehicleRegistrationPlate
     */
    public SMSParkingInfoDB(Date parkingSince, Date parkingUntil, String vehicleRegistrationPlate) {
        super();
        this.parkingSince = parkingSince;
        this.parkingUntil = parkingUntil;
        this.vehicleRegistrationPlate = vehicleRegistrationPlate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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