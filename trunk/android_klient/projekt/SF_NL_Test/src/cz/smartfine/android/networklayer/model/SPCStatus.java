package cz.smartfine.android.networklayer.model;

/**
 * V��et stav� p�enosn� parkovac� karty tj. zda je nahl�ena jako odcizen� �i ne.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:49
 */
public enum SPCStatus {
	/**
	 * P�enosn� parkovac� karta (SPC) je hl�ena jako odcizen�.
	 */
	STOLEN_SPC,
	/**
	 * P�enosn� parkovac� karta (SPC) nen� hl�ena jako odcizen�.
	 */
	OK_SPC,
	/**
	 * Stav p�enosn� parkovac� karty (SPC) nen� zn�m.
	 */
	UKNOWN_SPC_STATUS
}