package cz.smartfine.networklayer.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Pavel Brož
 */
public class PolicemanDB implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String firstName;
    private String lastName;
    private int badgeNumber;
    private Integer office;
    private Set<MobileDeviceDB> associations = new HashSet<MobileDeviceDB>();
    
    public PolicemanDB() {
        super();
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
