package model.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Martin Stajner
 * 
 */
public class Toaster {
	
	/**
	 * Kontext aplikace
	 */
	public static Context context;
	/**
	 * Dlouha delka zobrazeni
	 */
	public static final int LONG = 1;
	/**
	 * Kratka delka zobrazeni
	 */
	public static final int SHORT = 0;

	/**
	 * Vytvoreni a odeslani "toast" upozorneni
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
	 * Vytvoreni a odeslani "toast" upozorneni
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
