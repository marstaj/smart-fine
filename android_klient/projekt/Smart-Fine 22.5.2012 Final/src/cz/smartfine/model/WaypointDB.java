package cz.smartfine.model;

import cz.smartfine.android.model.Waypoint;
import java.io.Serializable;
import java.util.Date;

public class WaypointDB implements Serializable {

    /**
     * UID verze serializace
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private int badgeNumber;
    private double longtitude;
    private double latitude;
    private Date time;

    public WaypointDB() {
    }

    public WaypointDB(Waypoint wp, int badgeNumber) {
        this(badgeNumber, wp.getLongtitude(), wp.getLatitude(), wp.getTime());
    }

    
    public WaypointDB(int badgeNumber, double longtitude, double latitude, long time) {
        this.badgeNumber = badgeNumber;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.time = new Date(time);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
