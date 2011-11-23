package model;

/**
 * @author Martin Stajner
 * TODO
 */
public class Settings {

	/**
	 * Reprezentuje služební èíslo policisty, který aktuálnì používá aplikaci.
	 */
	private String badgeNumber;
	/**
	 * Reprezentuje název mìsta, který se pøedvyplòuje v parkovacím lístku.
	 */
	private String city;
	/**
	 * Reprezentuje URL adresu, na kterou jsou nahrávány lokální záznamy.
	 */
	private String syncUrl;

	public Settings(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * @return the badgeNumber
	 */
	public String getBadgeNumber() {
		return badgeNumber;
	}

	/**
	 * @param badgeNumber the badgeNumber to set
	 */
	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the syncUrl
	 */
	public String getSyncUrl() {
		return syncUrl;
	}

	/**
	 * @param syncUrl the syncUrl to set
	 */
	public void setSyncUrl(String syncUrl) {
		this.syncUrl = syncUrl;
	}

}