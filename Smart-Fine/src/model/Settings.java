package model;

/**
 * @author Martin Stajner
 * TODO
 */
public class Settings {

	/**
	 * Reprezentuje slu�ebn� ��slo policisty, kter� aktu�ln� pou��v� aplikaci.
	 */
	private String badgeNumber;
	/**
	 * Reprezentuje n�zev m�sta, kter� se p�edvypl�uje v parkovac�m l�stku.
	 */
	private String city;
	/**
	 * Reprezentuje URL adresu, na kterou jsou nahr�v�ny lok�ln� z�znamy.
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