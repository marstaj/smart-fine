package cz.smartfine.server.business.client.model;

import cz.smartfine.networklayer.model.PolicemanDB;

/**
 *
 * @author Pavel Brož
 */
public class PolicemanLoginDB extends PolicemanDB{

    private int pin;
    private int permissions;

    public PolicemanLoginDB() {
        super();
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
    
}
