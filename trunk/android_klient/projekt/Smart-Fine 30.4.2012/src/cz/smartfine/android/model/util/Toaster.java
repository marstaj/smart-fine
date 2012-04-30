package cz.smartfine.android.model.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Pomocn� t��da pro zobrazovan� toast upozorn�n�
 * 
 * @author Martin �tajner
 * 
 */
public class Toaster {

	/**
	 * Kontext aplikace
	 */
	public static Context context;
	/**
	 * Glob�ln� hodnota reprezentuj�c� Dlouh� zobrazen� toastu
	 */
	public static final int LONG = 1;
	/**
	 * Glob�ln� hodnota reprezentuj�c� Kr�tk� zobrazen� toastu
	 */
	public static final int SHORT = 0;

	/**
	 * Vytvo�en� a odesl�n� "toast" upozorn�n�
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
	 * Vytvo�en� a odesl�n� "toast" upozorn�n�
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
