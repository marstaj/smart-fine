package cz.smartfine.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Třída policisty pro uložení do DB.
 *
 * @author Pavel Brož
 */
public class PolicemanDB extends Policeman {

    /**
     * UID
     */
    private static final long serialVersionUID = 1L;
    private Integer office;
    private Set<MobileDeviceDB> associations = new HashSet<MobileDeviceDB>();

    public PolicemanDB() {
        super();
    }

    public Integer getOffice() {
        return office;
    }

    public void setOffice(Integer office) {
        this.office = office;
    }

    public Set<MobileDeviceDB> getAssociations() {
        return associations;
    }

    public void setAssociations(Set<MobileDeviceDB> associations) {
        this.associations = associations;
    }
}
