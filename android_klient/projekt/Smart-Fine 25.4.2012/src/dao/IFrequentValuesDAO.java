package dao;

import java.util.ArrayList;

import model.FrequentValue;

import cz.smartfine.R;
import android.content.Context;

/**
 * T��da pro pr�ci s DAO pro PL
 * 
 * @author Martin �tajner
 * 
 */
public interface IFrequentValuesDAO {

	/**
	 * Na�te nej�ast�j�� hodnoty z �lo�i�t� do pam�ti
	 */
	public void loadFrequentValues();

	/**
	 * Metoda na��t� nej�ast�j�� hodnoty do pam�ti telefonu. Pou��vaj� se p�i
	 * vytv��en� nov�ch parkovac�ch l�stk�.
	 * 
	 * @throws Exception
	 *             V�jimka nastane v p��pad�, �e se z n�jak�ho d�vodu nepoda��
	 *             ulo�it seznamy nej�ast�j��ch hodnot do souboru.
	 */
	public void saveFrequentValues() throws Exception;

	public ArrayList<FrequentValue> getMpzValues();

	public ArrayList<FrequentValue> getSpzColorValues();

	public ArrayList<FrequentValue> getVehicleTypeValues();

	public ArrayList<FrequentValue> getVehicleBrandValues();

	public ArrayList<FrequentValue> getFav_mpzValues();

	public ArrayList<FrequentValue> getFav_vehicleBrandValues();

}