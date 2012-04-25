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
public class FileFrequentValuesDAO {

	/**
	 * Kontext aplikace
	 */
	private Context context;

	/**
	 * DAO pro ukládání do souboru
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
	public FileFrequentValuesDAO(Context context) {
		super();
		this.context = context;
		this.dao = new FileDAO(context);		
	}

	//TODO Dodelat javadoc komentare vsude

	/**
	 * Naète nejèastìjší hodnoty z úložištì do pamìti
	 */
	public void loadFrequentValues() {
		// Zkusi nacist MPZ ze souboru a najit v nich oblibene polozky, v pripade chyby je nacte ze XML
		try {
			mpzValues = loadFrequentValuesFromFile(R.string.file_mpz);
			fav_mpzValues = findFavouriteValues(mpzValues);
		} catch (Exception e) {
			mpzValues = loadFrequentValuesFromXML(R.array.ticket_mpz);
			fav_mpzValues = new ArrayList<FrequentValue>();
		}

		// Barvy SPZ a typy vozidel nacte rovnou ze XML, protoze jich je malo
		spzColorValues = loadFrequentValuesFromXML(R.array.ticket_spzColor);
		vehicleTypeValues = loadFrequentValuesFromXML(R.array.ticket_vehicleType);

		// Zkusi nacist znacky aut ze souboru, v pripade chyby je nacte ze XML
		try {
			vehicleBrandValues = loadFrequentValuesFromFile(R.string.file_vehicleBrand);
			fav_vehicleBrandValues = findFavouriteValues(vehicleBrandValues);
		} catch (Exception e) {
			vehicleBrandValues = loadFrequentValuesFromXML(R.array.ticket_vehicleBrand);
			fav_vehicleBrandValues = new ArrayList<FrequentValue>();
		}
	}

	public void saveFrequentValues() throws Exception {
		dao.saveObjectToFile(mpzValues, context.getString(R.string.file_mpz));
		dao.saveObjectToFile(vehicleBrandValues, context.getString(R.string.file_vehicleBrand));
	}

	@SuppressWarnings("unchecked")
	private ArrayList<FrequentValue> loadFrequentValuesFromFile(int fileName) throws Exception {
		Object o = dao.loadObjectFromFile(context.getString(fileName));
		if (o instanceof ArrayList) {
			return (ArrayList<FrequentValue>) o;
		}
		return null;
	}

	private ArrayList<FrequentValue> loadFrequentValuesFromXML(int array) {
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