package cz.smartfine.networklayer.model;

/**
 * Výèet stavù parkování vozidla tj. za vozilo smí v zónì placeného stání parkovat
 * èi ne.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public enum ParkingStatus {
	/**
	 * Parkování vozidla je povolené.
	 */
	ALLOWED,
	/**
	 * Parkování vozidla není povolené.
	 */
	NOT_ALLOWED
}