package cz.smartfine.android.model;

import java.io.Serializable;

/**
 * Třída reprezentující objekt nejčastější hodnoty.
 * 
 * @author Martin Štajner
 * 
 */
public class FrequentValue implements Serializable {

	/**
	 * UID verze serializace
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Hodnota nejčastější hodnoty
	 */
	private String value;

	/**
	 * Příznak, zda nastavená jako oblíbená.
	 */
	private boolean favourite = false;

	/**
	 * Metoda vrátí hodnotu nejčastější hodnoty
	 * 
	 * @return Hodnota nejčastější hodnoty
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Metoda nastaví hodnotu nejčastější hodnoty
	 * 
	 * @param value
	 *            Hodnota nejčastější hodnoty
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Metoda vrátí, zda je hodnota nastaven jako oblíbená
	 * 
	 * @return True, když je oblíbená, jinak false.
	 */
	public boolean isFavourite() {
		return favourite;
	}

	/**
	 * Metoda nastaví, zda je hodnota nastaven jako oblíbená
	 * 
	 * @param favourite
	 *            Zda je hodnota oblíbená
	 */
	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}
}
