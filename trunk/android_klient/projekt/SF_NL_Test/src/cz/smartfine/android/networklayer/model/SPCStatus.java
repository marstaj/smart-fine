package cz.smartfine.android.networklayer.model;

/**
 * Výèet stavù pøenosné parkovací karty tj. zda je nahlášena jako odcizená èi ne.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:49
 */
public enum SPCStatus {
	/**
	 * Pøenosná parkovací karta (SPC) je hlášena jako odcizená.
	 */
	STOLEN_SPC,
	/**
	 * Pøenosná parkovací karta (SPC) není hlášena jako odcizená.
	 */
	OK_SPC,
	/**
	 * Stav pøenosné parkovací karty (SPC) není znám.
	 */
	UKNOWN_SPC_STATUS
}