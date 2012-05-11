package cz.smartfine.networklayer.model.mobile;

import java.io.Serializable;

/**
 * Výčet stavů parkování vozidla tj. za vozilo smí v zóně placeného stání
 * parkovat či ne.
 * 
 * @author Pavel Brož
 * @version 1.0
 */
public enum ParkingStatus implements Serializable {

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