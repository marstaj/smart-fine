package cz.smartfine.android.model;

import java.io.Serializable;

/**
 * Třída reprezentující objekt zákona.
 * 
 * @author Martin Štajner
 */

public class Law implements Serializable {

	/**
	 * UID verze serializace
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Sbírka zákona
	 */
	private int collection;

	/**
	 * Popis události
	 */
	private String eventDescription;

	/**
	 * Číslo zákona
	 */
	private int lawNumber;

	/**
	 * Písmeno odstavce
	 */
	private String letter;

	/**
	 * Odstavec zákona (Nikoliv §)
	 */
	private int paragraph;

	/**
	 * Paragraf zákona, článek zákona (§)
	 */
	private int ruleOfLaw;

	/**
	 * Popis jednání DZ
	 */
	private String descriptionDZ;

	/**
	 * Metoda vrátí popis jednání DZ
	 * 
	 * @return Popis jednání DZ
	 */
	public String getDescriptionDZ() {
		return descriptionDZ;
	}

	/**
	 * Metoda nastaví popis jednání DZ
	 * 
	 * @param descriptionDZ
	 *            Popis jednání DZ
	 */
	public void setDescriptionDZ(String descriptionDZ) {
		this.descriptionDZ = descriptionDZ;
	}

	/**
	 * Metoda vrátí sbírku zákona
	 * 
	 * @return Sbírka zákona
	 */
	public int getCollection() {
		return collection;
	}

	/**
	 * Metoda nastaví sbírku zákona
	 * 
	 * @param collection
	 *            Ssbírka zákona
	 */
	public void setCollection(int collection) {
		this.collection = collection;
	}

	/**
	 * Metoda vrátí číslo zákona
	 * 
	 * @return Číslo zákona
	 */
	public int getLawNumber() {
		return lawNumber;
	}

	/**
	 * Metoda nastaví číslo zákona
	 * 
	 * @param lawNumber
	 *            Číslo zákona
	 */
	public void setLawNumber(int lawNumber) {
		this.lawNumber = lawNumber;
	}

	/**
	 * Metoda vrátí písmeno odstavce
	 * 
	 * @return Písmeno odstavce
	 */
	public String getLetter() {
		return letter;
	}

	/**
	 * Metoda nastaví písmeno odstavce
	 * 
	 * @param letter
	 *            Písmeno odstavce
	 */
	public void setLetter(String letter) {
		this.letter = letter;
	}

	/**
	 * Metoda vrátí odstavec zákona
	 * 
	 * @return Odstavec zákona
	 */
	public int getParagraph() {
		return paragraph;
	}

	/**
	 * Metoda nastaví odstavec zákona
	 * 
	 * @param paragraph
	 *            Odstavec zákona
	 */
	public void setParagraph(int paragraph) {
		this.paragraph = paragraph;
	}

	/**
	 * Metoda vrátí paragraf zákona
	 * 
	 * @return Paragraf zákona
	 */
	public int getRuleOfLaw() {
		return ruleOfLaw;
	}

	/**
	 * Metoda nastaví paragraf zákona
	 * 
	 * @param ruleOfLaw
	 *            Paragraf zákona
	 */
	public void setRuleOfLaw(int ruleOfLaw) {
		this.ruleOfLaw = ruleOfLaw;
	}

	/**
	 * Metoda vrátí popis události
	 * 
	 * @return Popis události
	 */
	public String getEventDescription() {
		return eventDescription;
	}

	/**
	 * Metoda nastaví popis události
	 * 
	 * @param eventDescription
	 *            Popis události
	 */
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

}