package model;

import java.io.Serializable;

/**
 * @author Martin Stajner
 */
@SuppressWarnings("serial")
public class Law implements Serializable {

	/**
	 * Sbírka
	 */
	private int collection;
	/**
	 * Popis
	 */
	private String description;
	/**
	 * Èíslo zákona
	 */
	private int lawNumber;
	/**
	 * Písmeno
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
		// TODO: dodìlat, aby metoda vracela správný formát zákona
		return super.toString();
	}
}