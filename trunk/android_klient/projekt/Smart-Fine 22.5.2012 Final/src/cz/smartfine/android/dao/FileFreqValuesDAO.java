package cz.smartfine.android.dao;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.dao.interfaces.IFreqValuesDAO;
import cz.smartfine.android.model.FrequentValue;
import android.content.Context;

/**
 * Třída představující objekt, který ukládá seznamy nejčastějších hodnot, a
 * operuje s nimi.
 * 
 * @author Martin Štajner
 * 
 */
/**
 * @author Martin Štajner
 * 
 */
public class FileFreqValuesDAO implements IFreqValuesDAO {

	/**
	 * Kontext aplikace
	 */
	private Context appContext;

	/**
	 * Instance objektu pro ukládání do souboru
	 */
	private FileDAO dao;

	/**
	 * Seznam nejčastějších hodnot MPZ
	 */
	private ArrayList<FrequentValue> mpzValues;

	/**
	 * Seznam nejčastějších hodnot barvy SPZ
	 */
	private ArrayList<FrequentValue> spzColorValues;

	/**
	 * Seznam nejčastějších hodnot typu dopravního vozidla
	 */
	private ArrayList<FrequentValue> vehicleTypeValues;

	/**
	 * Seznam nejčastějších hodnot značky dopravního vozidla
	 */
	private ArrayList<FrequentValue> vehicleBrandValues;

	/**
	 * Seznam oblíbených SPZ
	 */
	private ArrayList<FrequentValue> fav_mpzValues;

	/**
	 * Seznam oblíbených značkek dopravního vozidla
	 */
	private ArrayList<FrequentValue> fav_vehicleBrandValues;

	/**
	 * Konstruktor třídy
	 * 
	 * @param context
	 *            Kontext aplikace
	 */
	public FileFreqValuesDAO(Context context) {
		super();
		this.appContext = context;
		this.dao = new FileDAO(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.IFreqValuesDAO#loadValues()
	 */
	public void loadValues() {
		// Zkusi nacist MPZ ze souboru a najit v nich oblibene polozky, v pripade chyby je nacte ze XML
		try {
			mpzValues = loadFreqValuesFromFile(R.string.file_mpz);
			fav_mpzValues = findFavouriteValues(mpzValues);
		} catch (Exception e) {
			mpzValues = loadFreqValuesFromXML(R.array.ticket_mpz_array);
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
			vehicleBrandValues = loadFreqValuesFromXML(R.array.ticket_vehicleBrand_array);
			fav_vehicleBrandValues = new ArrayList<FrequentValue>();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.IFreqValuesDAO#saveValues()
	 */
	public void saveValues() throws Exception {
		dao.saveObjectToFile(mpzValues, appContext.getString(R.string.file_mpz));
		dao.saveObjectToFile(vehicleBrandValues, appContext.getString(R.string.file_vehicleBrand));
	}

	/**
	 * Metoda načte nejčastější hodnoty ze souboru.
	 * 
	 * @param fileName
	 *            Soubor, ze kterého se má číst
	 * @return Seznam nejčastějších hodnot
	 * @throws Exception
	 *             Výjimka vznikne v případě chyby při nahrávání ze souboru.
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<FrequentValue> loadFreqValuesFromFile(int fileName) throws Exception {
		Object o = dao.loadObjectFromFile(appContext.getString(fileName));
		if (o instanceof ArrayList) {
			return (ArrayList<FrequentValue>) o;
		}
		return null;
	}

	/**
	 * Metoda načte nejčastější hodnoty ze XML.
	 * 
	 * @param array
	 *            Odkaz na seznam hodnot ve XML souboru
	 * @return Seznam nejčastějších hodnot
	 */
	private ArrayList<FrequentValue> loadFreqValuesFromXML(int array) {
		ArrayList<FrequentValue> list = new ArrayList<FrequentValue>();
		String[] valuesArray = appContext.getResources().getStringArray(array);
		for (int i = 0; i < valuesArray.length; i++) {
			FrequentValue fv = new FrequentValue();
			fv.setValue(valuesArray[i]);
			list.add(fv);
		}
		return list;
	}

	/**
	 * Metoda vyhledá oblíbené nejčastější hodnoty.
	 * 
	 * @param list
	 *            Seznam hodnot, ve kterém se má hledat.
	 * @return Seznam oblíbených hodnot
	 */
	private ArrayList<FrequentValue> findFavouriteValues(ArrayList<FrequentValue> list) {
		ArrayList<FrequentValue> newList = new ArrayList<FrequentValue>();
		for (FrequentValue t : list) {
			if (t.isFavourite()) {
				newList.add(t);
			}
		}
		return newList;
	}

	/**
	 * Metoda vrátí seznam nejčastějších hodnot MPZ
	 * 
	 * @return Seznam nejčastějších hodnot MPZ
	 */
	public ArrayList<FrequentValue> getMpzValues() {
		return mpzValues;
	}

	/**
	 * Metoda vrátí seznam nejčastějších hodnot barvy SPZ
	 * 
	 * @return Seznam nejčastějších hodnot barvy SPZ
	 */
	public ArrayList<FrequentValue> getSpzColorValues() {
		return spzColorValues;
	}

	/**
	 * Metoda vrátí seznam nejčastějších hodnot typu dopravního vozidla
	 * 
	 * @return Seznam nejčastějších hodnot typu dopravního vozidla
	 */
	public ArrayList<FrequentValue> getVehicleTypeValues() {
		return vehicleTypeValues;
	}

	/**
	 * Metoda vrátí seznam nejčastějších hodnot značky dopravního vozidla
	 * 
	 * @return Seznam nejčastějších hodnot značky dopravního vozidla
	 */
	public ArrayList<FrequentValue> getVehicleBrandValues() {
		return vehicleBrandValues;
	}

	/**
	 * Metoda vrátí seznam oblíbených SPZ
	 * 
	 * @return Seznam oblíbených SPZ
	 */
	public ArrayList<FrequentValue> getFav_mpzValues() {
		return fav_mpzValues;
	}

	/**
	 * Metoda vrátí seznam oblíbených značkek dopravního vozidla
	 * 
	 * @return Seznam oblíbených značkek dopravního vozidla
	 */
	public ArrayList<FrequentValue> getFav_vehicleBrandValues() {
		return fav_vehicleBrandValues;
	}

}