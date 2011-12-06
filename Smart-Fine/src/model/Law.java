package model;

import java.io.Serializable;

/**
 * @author Martin Stajner
 */
@SuppressWarnings("serial")
public class Law implements Serializable {

	/**
	 * Sb�rka
	 */
	private int collection;
	/**
	 * Popis
	 */
	private String description;
	/**
	 * ��slo z�kona
	 */
	private int lawNumber;
	/**
	 * P�smeno
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