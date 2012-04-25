package dao;

import java.util.ArrayList;

import model.FrequentValue;

import cz.smartfine.R;
import android.content.Context;

/**
 * Tøída pro práci s DAO pro PL
 * 
 * @author Martin Štajner
 * 
 */
public interface IFrequentValuesDAO {

	/**
	 * Naète nejèastìjší hodnoty z úložištì do pamìti
	 */
	public void loadFrequentValues();

	/**
	 * Metoda naèítá nejèastìjší hodnoty do pamìti telefonu. Používají se pøi
	 * vytváøení nových parkovacích lístkù.
	 * 
	 * @throws Exception
	 *             Výjimka nastane v pøípadì, že se z nìjakého dùvodu nepodaøí
	 *             uložit seznamy nejèastìjších hodnot do souboru.
	 */
	public void saveFrequentValues() throws Exception;

	public ArrayList<FrequentValue> getMpzValues();

	public ArrayList<FrequentValue> getSpzColorValues();

	public ArrayList<FrequentValue> getVehicleTypeValues();

	public ArrayList<FrequentValue> getVehicleBrandValues();

	public ArrayList<FrequentValue> getFav_mpzValues();

	public ArrayList<FrequentValue> getFav_vehicleBrandValues();

}