package cz.smartfine.android.model.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Pomocná třída, která poskytuje metody pro zobrazení toast upozornění
 * 
 * @author Martin Štajner
 * 
 */
public class Toaster {

	/**
	 * Kontext aplikace
	 */
	public static Context appContext;

	/**
	 * Globální hodnota reprezentující dlouhé zobrazení toastu
	 */
	public static final int LONG = 1;

	/**
	 * Globální hodnota reprezentující krátké zobrazení toastu
	 */
	public static final int SHORT = 0;

	/**
	 * Metoda vytvoří a odešle toast upozornění.
	 * 
	 * @param message
	 *            Zpráva
	 * @param length
	 *            Délka zobrazení upozornění
	 */
	public static void toast(String message, int length) {
		if (length == 1) {
			Toast.makeText(appContext, message, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Metoda vytvoří a odešle toast upozornění.
	 * 
	 * @param message
	 *            Zpráva
	 * @param length
	 *            Délka zobrazení upozornění
	 */
	public static void toast(int message, int length) {
		if (length == LONG) {
			Toast.makeText(appContext, message, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
		}
	}
}
