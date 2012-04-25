package model;

import java.io.Serializable;

public class FrequentValue implements Serializable{

	// TODO Komentare
	
	/**
	 * UID verze serializace
	 */
	private static final long serialVersionUID = 1L;
	private String value;
	private boolean favourite = false;
	
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isFavourite() {
		return favourite;
	}
	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}	
}
