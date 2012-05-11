package cz.smartfine.android.model.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Pomocná třída, která poskytuje metody kontroly stavu telefonu. Zjištuje
 * dostupnost internetového připojení, bluetooth a poskytovatelů polohy.
 * 
 * @author Martin Štajner
 * 
 */
public class Phone {

	/**
	 * Metoda zjistí, zda je telefon připojen k internetu.
	 * 
	 * @param context
	 *            Kontext aplikace
	 * @return Zda je telefon připojen k internetu
	 */
	public static boolean isConnectedToInternet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null) {
			return netInfo.isConnected();
		}
		return false;
	}

	/**
	 * Metoda vrátí, zda zařízení podporuje Bluetooth
	 * 
	 * @return Zda zařízení podporuje Bluetooth
	 */
	public static boolean isBluetoothSupported() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			return false;
		}
		return true;
	}

	/**
	 * MEtoda vrátí, zda je zapnutý Bluetooth
	 * 
	 * @return Zda je zapnutý Bluetooth
	 */
	public static boolean isBluetoothEnabled() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			return false;
		}
		return true;
	}

	/**
	 * Metoda zjišťuje, kteří poskytovatelé polohy, jsou aktivní.
	 * 
	 * @param context
	 *            Kontext aplikace
	 * @return Pole hodnot true/false, kde 1. je poskytovatel NET a druhý GPS
	 */
	public static boolean[] checkLocationProviders(Context context) {
		LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean[] providers = new boolean[2];
		providers[0] = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		providers[1] = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return providers;

	}

}
