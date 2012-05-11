package cz.smartfine.android.model.util;

import java.lang.ref.WeakReference;
import android.os.Binder;

/**
 * Generická třída reprezentující Binder, který slouží k připojování aktivit k
 * sevice.
 * 
 * @author Martin Štajner
 * 
 * @param <T>
 *            Typ objektu
 */

public class LocalBinder<T> extends Binder {

	/**
	 * Slabá reference na objekt
	 */
	private WeakReference<T> newService;

	/**
	 * Metoda vytvoří slabou referenci na objekt
	 * 
	 * @param service
	 *            Objekt, ze kterého se má dělat slabá reference
	 */
	public LocalBinder(T service) {
		newService = new WeakReference<T>(service);
	}

	/**
	 * Metoda vrátí slabou referenci na objekt
	 * 
	 * @return Slabá reference na objekt
	 */
	public T getService() {
		return newService.get();
	}
}