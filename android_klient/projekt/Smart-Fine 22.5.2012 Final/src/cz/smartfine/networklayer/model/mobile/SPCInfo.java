package cz.smartfine.networklayer.model.mobile;

import java.io.Serializable;

/**
 * Obsahuje informace o stavu přenosné parkovací karty (PPK, angl. SPC).
 *
 * @author Pavel Brož
 * @version 1.0
 */
public class SPCInfo implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 1L;
    /**
     * Číslo přenosné parkovací karty, ke které se vztahuje informace o stavu.
     */
    private String spcNumber;
    /**
     * Stav přenosné parkovací karty tj. zda je hlášena jako kradená či nikoliv.
     */
    private SPCStatus spcStatus;

    public SPCInfo() {
        super();
    }

    /**
     * @param spcNumber
     * @param spcStatus
     */
    public SPCInfo(String spcNumber, SPCStatus spcStatus) {
        super();
        this.spcNumber = spcNumber;
        this.spcStatus = spcStatus;
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

    /**
     * @return the spcStatus
     */
    public SPCStatus getSpcStatus() {
        return spcStatus;
    }

    /**
     * @param spcStatus the spcStatus to set
     */
    public void setSpcStatus(SPCStatus spcStatus) {
        this.spcStatus = spcStatus;
    }
}