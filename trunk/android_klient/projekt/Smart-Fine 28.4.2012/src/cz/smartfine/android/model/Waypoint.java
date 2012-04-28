package cz.smartfine.android.model;

import java.io.Serializable;

public class Waypoint implements Serializable{

	// TODO Komentare
	
	/**
	 * UID verze serializace
	 */
	private static final long serialVersionUID = 1L;
	private double longtitude;
	private double latitude;
	private long time;	
	
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}	
}
