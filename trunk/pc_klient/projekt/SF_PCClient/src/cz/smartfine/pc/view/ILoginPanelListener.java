/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.smartfine.pc.view;

import cz.smartfine.networklayer.model.pc.PCClientPermission;

/**
 *
 * @author Pavel Brož
 */
public interface ILoginPanelListener {

    /**
     * Handler události úspěšného přihlášení na server.
     * @param permissions 
     */
    public void onLoggedIn(PCClientPermission permissions);
}
