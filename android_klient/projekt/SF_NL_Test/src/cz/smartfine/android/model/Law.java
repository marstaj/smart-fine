package cz.smartfine.android.model;

import java.io.Serializable;

/**
 * T��da modelu z�kona
 * 
 * @author Martin �tajner
 */

public class Law implements Serializable {

	/**
	 * UID verze serializace
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Sb�rka z�kona
	 */
	private int collection;
	/**
	 * Popis z�kona
	 */
	private String description;
	/**
	 * ��slo z�kona
	 */
	private int lawNumber;
	/**
	 * P�smeno odstavce
	 */
	private String letter;
	/**
	 * Odstavec z�kona (Nikoliv �)
	 */
	private int paragraph;
	/**
	 * Parafgraf z�kona, �l�nek z�kona (�)
	 */
	private int ruleOfLaw;
	/**
	 * Popis jedn�n� DZ
	 */
	private String descriptionDZ;

	public String getDescriptionDZ() {
		return descriptionDZ;
	}

	public void setDescriptionDZ(String descriptionDZ) {
		this.descriptionDZ = descriptionDZ;
	}

	public int getCollection() {
		return collection;
	}

	public void setCollection(int collection) {
		this.collection = collection;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLawNumber() {
		return lawNumber;
	}

	public void setLawNumber(int lawNumber) {
		this.lawNumber = lawNumber;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public int getParagraph() {
		return paragraph;
	}

	public void setParagraph(int paragraph) {
		this.paragraph = paragraph;
	}

	public int getRuleOfLaw() {
		return ruleOfLaw;
	}

	public void setRuleOfLaw(int ruleOfLaw) {
		this.ruleOfLaw = ruleOfLaw;
	}

	@Override
	public String toString() {
		// TODO: dod�lat, aby metoda vracela spr�vn� form�t z�kona
		return super.toString();
	}
}