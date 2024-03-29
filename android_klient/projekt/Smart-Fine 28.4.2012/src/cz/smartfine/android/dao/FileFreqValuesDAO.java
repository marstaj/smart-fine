package cz.smartfine.android.dao;

import java.util.ArrayList;


import cz.smartfine.android.R;
import cz.smartfine.android.dao.interfaces.IFreqValuesDAO;
import cz.smartfine.android.model.FrequentValue;
import android.content.Context;

/**
 * T��da pro pr�ci s DAO pro PL
 * 
 * @author Martin �tajner
 * 
 */
public class FileFreqValuesDAO implements IFreqValuesDAO {

	/**
	 * Kontext aplikace
	 */
	private Context context;

	/**
	 * DAO pro ukl�d�n� do souboru
	 */
	private FileDAO dao;

	private ArrayList<FrequentValue> mpzValues;
	private ArrayList<FrequentValue> spzColorValues;
	private ArrayList<FrequentValue> vehicleTypeValues;
	private ArrayList<FrequentValue> vehicleBrandValues;

	private ArrayList<FrequentValue> fav_mpzValues;
	private ArrayList<FrequentValue> fav_vehicleBrandValues;

	/**
	 * Konstruktor
	 * 
	 * @param context
	 * @throws Exception
	 */
	public FileFreqValuesDAO(Context context) {
		super();
		this.context = context;
		this.dao = new FileDAO(context);
	}

	//TODO Dodelat javadoc komentare vsude

	/**
	 * Na�te nej�ast�j�� hodnoty z �lo�i�t� do pam�ti
	 */
	public void loadValues() {
		// Zkusi nacist MPZ ze souboru a najit v nich oblibene polozky, v pripade chyby je nacte ze XML
		try {
			mpzValues = loadFreqValuesFromFile(R.string.file_mpz);
			fav_mpzValues = findFavouriteValues(mpzValues);
		} catch (Exception e) {
			mpzValues = loadFreqValuesFromXML(R.array.ticket_mpz);
			fav_mpzValues = new ArrayList<FrequentValue>();
		}

		// Barvy SPZ a typy vozidel nacte rovnou ze XML, protoze jich je malo
		spzColorValues = loadFreqValuesFromXML(R.array.ticket_spzColor);
		vehicleTypeValues = loadFreqValuesFromXML(R.array.ticket_vehicleType);

		// Zkusi nacist znacky aut ze souboru, v pripade chyby je nacte ze XML
		try {
			vehicleBrandValues = loadFreqValuesFromFile(R.string.file_vehicleBrand);
			fav_vehicleBrandValues = findFavouriteValues(vehicleBrandValues);
		} catch (Exception e) {
			vehicleBrandValues = loadFreqValuesFromXML(R.array.ticket_vehicleBrand);
			fav_vehicleBrandValues = new ArrayList<FrequentValue>();
		}
	}

	public void saveValues() throws Exception {
		dao.saveObjectToFile(mpzValues, context.getString(R.string.file_mpz));
		dao.saveObjectToFile(vehicleBrandValues, context.getString(R.string.file_vehicleBrand));
	}

	@SuppressWarnings("unchecked")
	private ArrayList<FrequentValue> loadFreqValuesFromFile(int fileName) throws Exception {
		Object o = dao.loadObjectFromFile(context.getString(fileName));
		if (o instanceof ArrayList) {
			return (ArrayList<FrequentValue>) o;
		}
		return null;
	}

	private ArrayList<FrequentValue> loadFreqValuesFromXML(int array) {
		ArrayList<FrequentValue> list = new ArrayList<FrequentValue>();
		String[] valuesArray = context.getResources().getStringArray(array);
		for (int i = 0; i < valuesArray.length; i++) {
			FrequentValue fv = new FrequentValue();
			fv.setValue(valuesArray[i]);
			list.add(fv);
		}
		return list;
	}

	private ArrayList<FrequentValue> findFavouriteValues(ArrayList<FrequentValue> list) {
		ArrayList<FrequentValue> newList = new ArrayList<FrequentValue>();
		for (FrequentValue t : list) {
			if (t.isFavourite()) {
				newList.add(t);
			}
		}
		return newList;
	}

	public ArrayList<FrequentValue> getMpzValues() {
		return mpzValues;
	}

	public ArrayList<FrequentValue> getSpzColorValues() {
		return spzColorValues;
	}

	public ArrayList<FrequentValue> getVehicleTypeValues() {
		return vehicleTypeValues;
	}

	public ArrayList<FrequentValue> getVehicleBrandValues() {
		return vehicleBrandValues;
	}

	public ArrayList<FrequentValue> getFav_mpzValues() {
		return fav_mpzValues;
	}

	public ArrayList<FrequentValue> getFav_vehicleBrandValues() {
		return fav_vehicleBrandValues;
	}

}