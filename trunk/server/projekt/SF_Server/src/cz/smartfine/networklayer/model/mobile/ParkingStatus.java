package cz.smartfine.networklayer.model.mobile;

/**
 * Výčet stavů parkování vozidla tj. za vozilo smí v zóně placeného stání parkovat
 * či ne.
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:45
 */
public enum ParkingStatus {

	/**
	 * Parkování vozidla je povolené.
	 */
	ALLOWED,
	/**
	 * Parkování vozidla není povolené.
	 */
	NOT_ALLOWED,
	/**
	 * Stav parkování se nepodařilo určit.
	 */
	UNKNOWN_PARKING_STATUS

}