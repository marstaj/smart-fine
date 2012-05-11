package cz.smartfine.server.business.client.model;

import java.io.Serializable;

/**
 * Obsahuje informace o přenosné parkovací kartě (PPK, angl. SPC). Používá se pro přístup k DB.
 *
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:45
 */
public class SPCInfoDB implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * ID záznamu v DB.
     */
    private int id;
    /**
     * Číslo přenosné parkovací karty, ke které se vztahuje informace o stavu.
     */
    private String spcNumber;

    public SPCInfoDB() {
        this(null);
    }

    /**
     * @param spcNumber
     */
    public SPCInfoDB(String spcNumber) {
        super();
        this.spcNumber = spcNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the spcNumber
     */
    public String getSpcNumber() {
        return spcNumber;
    }

    /**
     * @param spcNumber the spcNumber to set
     */
    public void setSpcNumber(String spcNumber) {
        this.spcNumber = spcNumber;
    }
}