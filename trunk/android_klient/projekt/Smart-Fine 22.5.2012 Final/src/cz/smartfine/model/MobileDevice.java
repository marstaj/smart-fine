package cz.smartfine.model;

import java.io.Serializable;

/**
 * Třída reprezentující mobilní zařízení.
 *
 * @author Pavel Brož
 */
public abstract class MobileDevice implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String imei;
    private String name;
    private String description;

    public MobileDevice() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getImei() {
        return imei;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName() + " - IMEI: " + getImei();
    }
}
