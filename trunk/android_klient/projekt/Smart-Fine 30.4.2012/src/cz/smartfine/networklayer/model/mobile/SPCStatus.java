package cz.smartfine.networklayer.model.mobile;

import java.io.Serializable;

/**
 * Výčet stavů přenosné parkovací karty tj. zda je nahlášena jako odcizená či ne.
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:46
 */
public enum SPCStatus implements Serializable {

	/**
	 * Přenosná parkovací karta (SPC) je hlášena jako odcizená.
	 */
	STOLEN_SPC,
	/**
	 * Přenosná parkovací karta (SPC) není hlášena jako odcizená.
	 */
	OK_SPC,
	/**
	 * Stav přenosné parkovací karty (SPC) není znám.
	 */
	UKNOWN_SPC_STATUS

}