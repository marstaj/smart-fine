package cz.smartfine.pc;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Reprezentuje lokalizační balíček pro aplikaci.
 * @author Pavel Brož
 */
public class Localization {
    
    ResourceBundle resBundle;

    /**
     * Vytvoří lokalizační balíček
     * @param locale vybraný jazyk
     */
    public Localization(Locale locale) {
        resBundle = ResourceBundle.getBundle("cz.smartfine.pc.resource.localization.SFPCClient", locale); //načtení jazyka
    }
    
     /**
     * Získá lokalizovanou hodnotu
     * @param key klíč hodnoty
     * @return lokalizovaná hodnota
     */
    public String getString(String key){
        //return key;
        return resBundle.getString(key); //vrací string
    }
}
