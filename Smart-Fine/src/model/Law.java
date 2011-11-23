package model;

/**
 * @author Martin Stajner
 * TODO
 */
public class Law {

	/**
	 * Sb�rka
	 */
	private int collection;
	/**
	 * Odstavec
	 */
	private int description;
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

	public int getDescription() {
		return description;
	}

	public void setDescription(int description) {
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

}