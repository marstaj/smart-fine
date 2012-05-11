package cz.smartfine.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Třída reprezentuje mobilní zařízení uložené v DB.
 *
 * @author Pavel Brož
 */
public class MobileDeviceDB extends MobileDevice {

    /**
     * UID
     */
    private static final long serialVersionUID = 1L;
    private int office;
    private boolean blocked;
    private Set<PolicemanDB> associations = new HashSet<PolicemanDB>();

    public MobileDeviceDB() {
        super();
    }

    public int getOffice() {
        return office;
    }

    public void setOffice(int office) {
        this.office = office;
    }

    public Set<PolicemanDB> getAssociations() {
        return associations;
    }

    public void setAssociations(Set<PolicemanDB> associations) {
        this.associations = associations;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
