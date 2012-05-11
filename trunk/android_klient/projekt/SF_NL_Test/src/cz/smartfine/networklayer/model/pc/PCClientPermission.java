package cz.smartfine.networklayer.model.pc;

import cz.smartfine.networklayer.dataprotocols.PCProtocolConstants;

/**
 * Ukládá oprávnění policisty pro práci s PC klientem.
 *
 * @author Pavel Brož
 */
public class PCClientPermission {

    /**
     * Oprávnění pro prohlížení vlastních PL.
     */
    private boolean permShowOwnTickets = false;
    /**
     * Oprávnění pro změnu PINu.
     */
    private boolean permChngPin = false;
    /**
     * Oprávnění pro správu mobilních zařízení.
     */
    private boolean permAdminDevices = false;
    /**
     * Oprávnění pro správu asociací mezi policisty a mobilnímy zařízeními.
     */
    private boolean permAdminAssoc = false;
    /**
     * Oprávnění pro prohlížení geolokačních záznamů podřízených.
     */
    private boolean permShowGeodata = false;
    /**
     * Oprávnění pro prohlížení PL podřízených.
     */
    private boolean permShowTickets = false;
    /**
     * Oprávnění pro mazání PL.
     */
    private boolean permDeleteTickets = false;

    public PCClientPermission() {
    }

    public PCClientPermission(int permissions) {
        if ((permissions & PCProtocolConstants.MSG_SUC_AUTH_PERM_SHOW_OWN_TICKETS) != 0) {
            this.permShowOwnTickets = true;
        }
        if ((permissions & PCProtocolConstants.MSG_SUC_AUTH_PERM_CHNG_PIN) != 0) {
            this.permChngPin = true;
        }
        if ((permissions & PCProtocolConstants.MSG_SUC_AUTH_PERM_ADMIN_DEVICES) != 0) {
            this.permAdminDevices = true;
        }
        if ((permissions & PCProtocolConstants.MSG_SUC_AUTH_PERM_ADMIN_ASSOC) != 0) {
            this.permAdminAssoc = true;
        }
        if ((permissions & PCProtocolConstants.MSG_SUC_AUTH_PERM_SHOW_GEODATA) != 0) {
            this.permShowGeodata = true;
        }
        if ((permissions & PCProtocolConstants.MSG_SUC_AUTH_PERM_SHOW_TICKETS) != 0) {
            this.permShowTickets = true;
        }
        if ((permissions & PCProtocolConstants.MSG_SUC_AUTH_PERM_DELETE_TICKETS) != 0) {
            this.permDeleteTickets = true;
        }
    }

    public boolean isPermAdminAssoc() {
        return permAdminAssoc;
    }

    public void setPermAdminAssoc(boolean permAdminAssoc) {
        this.permAdminAssoc = permAdminAssoc;
    }

    public boolean isPermAdminDevices() {
        return permAdminDevices;
    }

    public void setPermAdminDevices(boolean permAdminDevices) {
        this.permAdminDevices = permAdminDevices;
    }

    public boolean isPermChngPin() {
        return permChngPin;
    }

    public void setPermChngPin(boolean permChngPin) {
        this.permChngPin = permChngPin;
    }

    public boolean isPermDeleteTickets() {
        return permDeleteTickets;
    }

    public void setPermDeleteTickets(boolean permDeleteTickets) {
        this.permDeleteTickets = permDeleteTickets;
    }

    public boolean isPermShowGeodata() {
        return permShowGeodata;
    }

    public void setPermShowGeodata(boolean permShowGeodata) {
        this.permShowGeodata = permShowGeodata;
    }

    public boolean isPermShowOwnTickets() {
        return permShowOwnTickets;
    }

    public void setPermShowOwnTickets(boolean permShowOwnTickets) {
        this.permShowOwnTickets = permShowOwnTickets;
    }

    public boolean isPermShowTickets() {
        return permShowTickets;
    }

    public void setPermShowTickets(boolean permShowTickets) {
        this.permShowTickets = permShowTickets;
    }

    /**
     * Spočítá oprávnění do podoby čísla int.
     *
     * @return Oprávnění.
     */
    public int getPermissions() {

        int permissions = 0; //žádná oprávnění

        if (permShowOwnTickets) {
            permissions = permissions | PCProtocolConstants.MSG_SUC_AUTH_PERM_SHOW_OWN_TICKETS;
        }
        if (permChngPin) {
            permissions = permissions | PCProtocolConstants.MSG_SUC_AUTH_PERM_CHNG_PIN;
        }
        if (permAdminDevices) {
            permissions = permissions | PCProtocolConstants.MSG_SUC_AUTH_PERM_ADMIN_DEVICES;
        }
        if (permAdminAssoc) {
            permissions = permissions | PCProtocolConstants.MSG_SUC_AUTH_PERM_ADMIN_ASSOC;
        }
        if (permShowGeodata) {
            permissions = permissions | PCProtocolConstants.MSG_SUC_AUTH_PERM_SHOW_GEODATA;
        }
        if (permShowTickets) {
            permissions = permissions | PCProtocolConstants.MSG_SUC_AUTH_PERM_SHOW_TICKETS;
        }
        if (permDeleteTickets) {
            permissions = permissions | PCProtocolConstants.MSG_SUC_AUTH_PERM_DELETE_TICKETS;
        }

        return permissions;
    }
}
