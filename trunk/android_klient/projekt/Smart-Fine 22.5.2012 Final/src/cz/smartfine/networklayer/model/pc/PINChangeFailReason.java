/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.smartfine.networklayer.model.pc;

/**
 * Výčet možností při selhání změny PINu.
 * @author Pavel Brož
 */
public enum PINChangeFailReason {

    /**
     * Neznámý důvod.
     */
    UNKNOWN_REASON,
    /**
     * Chybné služební číslo nebo PIN.
     */
    WRONG_BADGE_NUMBER_OR_PIN
}
