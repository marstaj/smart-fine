package cz.smartfine.android.dao.interfaces;

/**
 * Tøída pro práci s DAO pro PL
 * 
 * @author Martin Štajner
 * 
 */
public interface IFreqValuesDAO {

	/**
	 * Naète nejèastìjší hodnoty z úložištì do pamìti
	 */
	public void loadValues();

	/**
	 * Metoda naèítá nejèastìjší hodnoty do pamìti telefonu. Používají se pøi
	 * vytváøení nových parkovacích lístkù.
	 * 
	 * @throws Exception
	 *             Výjimka nastane v pøípadì, že se z nìjakého dùvodu nepodaøí
	 *             uložit seznamy nejèastìjších hodnot do souboru.
	 */
	public void saveValues() throws Exception;

}