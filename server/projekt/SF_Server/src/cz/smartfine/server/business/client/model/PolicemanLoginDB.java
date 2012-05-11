package cz.smartfine.server.business.client.model;

import cz.smartfine.model.PolicemanDB;

/**
 * Třída sloužící k načtení přihlašovacích údajů z DB.
 * @author Pavel Brož
 */
public class PolicemanLoginDB extends PolicemanDB{

    /**
     * PIN.
     */
    private int pin;
    /**
     * Oprávnění pro PC klienta.
     */
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
