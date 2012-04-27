package cz.smartfine.android.networklayer.model;

/**
 * V��et stav� parkov�n� vozidla tj. za vozilo sm� v z�n� placen�ho st�n� parkovat
 * �i ne.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public enum ParkingStatus {
	/**
	 * Parkov�n� vozidla je povolen�.
	 */
	ALLOWED,
	/**
	 * Parkov�n� vozidla nen� povolen�.
	 */
	NOT_ALLOWED,
	/**
	 * Stav parkov�n� se nepoda�ilo ur�it.
	 */
	UNKNOWN_PARKING_STATUS
	
}