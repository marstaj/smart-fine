/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.smartfine.networklayer.model.pc;

/**
 * Stav dotazu.
 * @author Pavel Brož
 */
public enum QueryState {
    /**
     * Dotaz proběhl v pořádku.
     */
    QUERY_OK,
    /**
     * Při vykonávání dotazu došlo k chybě.
     */
    QUERY_ERROR
}
