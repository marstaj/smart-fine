package cz.smartfine.android.dao.interfaces;

/**
 * T��da pro pr�ci s DAO pro PL
 * 
 * @author Martin �tajner
 * 
 */
public interface IFreqValuesDAO {

	/**
	 * Na�te nej�ast�j�� hodnoty z �lo�i�t� do pam�ti
	 */
	public void loadValues();

	/**
	 * Metoda na��t� nej�ast�j�� hodnoty do pam�ti telefonu. Pou��vaj� se p�i
	 * vytv��en� nov�ch parkovac�ch l�stk�.
	 * 
	 * @throws Exception
	 *             V�jimka nastane v p��pad�, �e se z n�jak�ho d�vodu nepoda��
	 *             ulo�it seznamy nej�ast�j��ch hodnot do souboru.
	 */
	public void saveValues() throws Exception;

}