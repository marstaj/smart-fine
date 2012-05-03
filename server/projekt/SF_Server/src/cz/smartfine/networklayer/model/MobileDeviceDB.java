package cz.smartfine.networklayer.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Třída reprezentuje mobilní zařízení uložené v DB.
 * @author Pavel Brož
 */
public class MobileDeviceDB implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String imei;
    private String name;
    private String description;
    private int office;
    private Set<PolicemanDB> associations = new HashSet<PolicemanDB>();
    
    public MobileDeviceDB() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
}
