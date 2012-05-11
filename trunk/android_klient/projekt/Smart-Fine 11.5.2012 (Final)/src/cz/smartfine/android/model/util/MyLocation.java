package cz.smartfine.android.model.util;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

/**
 * 
 * Třída reprezentující objekt, který slouží k určování adresy podle aktuální
 * polohy.
 * 
 * @author Martin Štajner
 * 
 */
public class MyLocation {

	/**
	 * Posluchač poskytovatele GPS
	 */
	private LocationListener gpsListener;

	/**
	 * Posluchač poskytovatele GPS
	 */
	private LocationListener netListener;

	/**
	 * Kontext aplikace
	 */
	private Context appContext;

	/**
	 * Location manažer
	 */
	private LocationManager locManager;

	/**
	 * Zda je povolen poskytovatel GPS
	 */
	private boolean gpsProviderEnabled = false;

	/**
	 * Zda je povolen poskytovatel NET
	 */
	private boolean netProviderEnabled = false;

	/**
	 * Objekt Handler pro samosatatné vlákno
	 */
	private Handler handler;

	/**
	 * Runnable běžící v samostatném vlákně
	 */
	private Runnable rAble;

	/**
	 * Konstruktor třídy
	 * 
	 * @param appContext
	 */
	public MyLocation(Context appContext) {
		super();
		this.appContext = appContext;
		locManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * Metoda získá aktuální polohu pomocí poskytovatelů polohy. Pokud ani jeden
	 * není k dispozici, vznikne výjimka. Když nalezne polohu jako první
	 * poskytovatel NET, poloha se uloží, ale pokračuje se v čekání na
	 * poskytovatele GPS. Pokud ten stihne polohu najít, před ukončením
	 * časovače, vyplní se tato poloha. Pokud ikoliv, zůstane poloha s*
	 * poskytovatele NEt.
	 * 
	 * @param result
	 *            Výsledek nalezení polohy
	 * @throws Exception
	 *             Výjimka vznikne, když není povolen ani jeden poskytovatel
	 *             polohy.
	 */
	public void getLocation(final LocationResult result) throws Exception {
		// Pres locationresult se pak aplikace ozve, ze nasla pozici

		// Kontrola dostupnosti provideru
		boolean providers[] = Phone.checkLocationProviders(appContext);
		if (providers[0] || providers[1]) {
			netProviderEnabled = providers[0];
			gpsProviderEnabled = providers[1];
		} else {
			throw new Exception();
		}

		// Casovac probehne kdyz ani jeden posluchac nedostane polohu za urceny cas
		handler = new Handler();
		rAble = new Runnable() {
			public void run() {
				// Toto probehne prakticky az po definovani posluchacu, ale kvuli nim to musi byt uvedeno tady..

				if (gpsListener != null) {
					locManager.removeUpdates(gpsListener);
				}
				if (netListener != null) {
					locManager.removeUpdates(netListener);
				}

				Location netLocation = null;
				Location gpsLocation = null;
				if (gpsProviderEnabled) {
					gpsLocation = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				}
				if (netProviderEnabled) {
					netLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}

				// Zjisti se poloha. Kdyz jsou 2 hodnoty, vybere se ta pozdejsi
				Location latestLocation;
				if (gpsLocation != null && netLocation != null) {
					if (gpsLocation.getTime() > netLocation.getTime()) {
						latestLocation = gpsLocation;
					} else {
						latestLocation = netLocation;
					}
				} else {
					// Vrati polohu z GPS kdyz je
					if (gpsLocation != null) {
						latestLocation = gpsLocation;
					} else {
						// Kdyz neni GPS, tak vrati polohu z NET, pokud je
						if (netLocation != null) {
							latestLocation = netLocation;
						} else {
							// Kdyz neni zadna poloha, vrati null
							latestLocation = null;
						}
					}
				}

				// Kdyz gps nevrati polohu, a netProvider je zablokovan, je pravdepodobne ze z nej zbyla nejaka stara hodnota, tu radeji eliminujeme
				if (gpsLocation != null && !netProviderEnabled) {
					latestLocation = null;
				}

				result.gotLocationAddress(getAddress(latestLocation));
			}
		};

		// Kdyz je NET provider aktivni, nastavi na nej posluchac
		if (netProviderEnabled) {
			netListener = new LocationListener() {
				public void onLocationChanged(Location location) {

					result.gotLocationAddress(getAddress(location));
				}
				public void onProviderDisabled(String provider) {
				}
				public void onProviderEnabled(String provider) {
				}
				public void onStatusChanged(String provider, int status, Bundle extras) {
				}
			};
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, netListener);
		}

		// Kdyz je GPS provider aktivni, nastavi na nej posluchac
		if (gpsProviderEnabled) {
			gpsListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					handler.removeCallbacks(rAble);
					locManager.removeUpdates(gpsListener);
					locManager.removeUpdates(netListener);
					result.gotLocationAddress(getAddress(location));
				}
				public void onProviderDisabled(String provider) {
				}
				public void onProviderEnabled(String provider) {
				}
				public void onStatusChanged(String provider, int status, Bundle extras) {
				}
			};
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
		}

		// Spusti se cekani na polohu
		handler.postDelayed(rAble, 30000);

	}

	/**
	 * Funkce zjistí adresdu z geolokačního bodu (souřadnic).
	 * 
	 * @param location
	 *            Souřadnice místa
	 * @return Adresu souřadnice. Pokud se ji nepodařilo najít, tak null.
	 */
	private Address getAddress(Location location) {

		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			int maxResults = 1;

			Geocoder gcgeocoder = new Geocoder(appContext, Locale.getDefault());
			List<Address> addresses;

			try {
				addresses = gcgeocoder.getFromLocation(latitude, longitude, maxResults);
			} catch (IOException e) {
				return null;
			}

			if (addresses.size() == 1) {
				return addresses.get(0);
			} else {
				return null;
			}

		} else {
			return null;
		}
	}

	/**
	 * Metoda zruší a odhlásí posluchače od poskytovatelů polohy, a odstraní
	 * spuštění zpožděného runnable (časovač).
	 */
	public void cancel() {
		if (gpsListener != null) {
			locManager.removeUpdates(gpsListener);
		}
		if (netListener != null) {
			locManager.removeUpdates(netListener);
		}
		if (handler != null) {
			handler.removeCallbacks(rAble);
		}
	}

	/**
	 * Specialní třída pro získavaní výsledku hledaní polohy, kvůli vláknu.
	 * 
	 * @author Martin Štajner
	 */
	public static abstract class LocationResult {
		/**
		 * Metoda se zavolá, když je nalezena poloha.
		 * 
		 * @param address
		 *            Adresa aktuální polohy
		 */
		public abstract void gotLocationAddress(Address address);
	}
}