package cz.smartfine.android.model;

import java.io.Serializable;

/**
 * Tøída modelu zákona
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
	 * Èíslo zákona
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
	 * Parafgraf zákona, èlánek zákona (§)
	 */
	private int ruleOfLaw;
	/**
	 * Popis jednání DZ
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
	
	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	@Override
	public String toString() {
		// TODO: dodìlat, aby metoda vracela správný formát zákona
		return super.toString();
	}
}