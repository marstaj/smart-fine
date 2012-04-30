package cz.smartfine.android.model.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Pomocná tøída pro zobrazovaní toast upozornìní
 * 
 * @author Martin Štajner
 * 
 */
public class Toaster {

	/**
	 * Kontext aplikace
	 */
	public static Context context;
	/**
	 * Globální hodnota reprezentující Dlouhé zobrazení toastu
	 */
	public static final int LONG = 1;
	/**
	 * Globální hodnota reprezentující Krátké zobrazení toastu
	 */
	public static final int SHORT = 0;

	/**
	 * Vytvoøení a odeslání "toast" upozornìní
	 * 
	 * @param message
	 * @param length
	 */
	public static void toast(String message, int length) {
		if (length == 1) {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Vytvoøení a odeslání "toast" upozornìní
	 * 
	 * @param message
	 * @param length
	 */
	public static void toast(int message, int length) {
		if (length == LONG) {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}
}
