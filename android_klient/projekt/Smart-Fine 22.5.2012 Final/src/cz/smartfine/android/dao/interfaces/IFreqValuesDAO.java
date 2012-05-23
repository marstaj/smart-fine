package cz.smartfine.android.dao.interfaces;

/**
 * Interface představující objekt, který ukládá a načítá seznamy nejčastějších
 * hodnot.
 * 
 * @author Martin Štajner
 * 
 */
public interface IFreqValuesDAO {

	/**
	 * Metoda načte nejčastější hodnoty ze uložiště do paměti
	 */
	public void loadValues();

	/**
	 * Metoda uloží hodnoty z paměti do uložiště.
	 * 
	 * @throws Exception
	 *             Výjimka nastane v případě, že se z nějakého důvodu nepodaří
	 *             uložit hodnoty do uložiště.
	 */
	public void saveValues() throws Exception;

}