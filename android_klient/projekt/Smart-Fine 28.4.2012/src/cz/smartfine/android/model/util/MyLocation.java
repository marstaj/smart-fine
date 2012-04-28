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

public class MyLocation {

	/**
	 * Posluchac GPS providera
	 */
	private LocationListener gpsListener;

	/**
	 * Posluchac NET providera bezi dal, i kdyz najde pozici ukonci se az kdyz
	 * najde pozici GPS, nebo kdyz dobehne vlakno
	 */
	private LocationListener netListener;

	/**
	 * Kontext aplikace
	 */
	private Context context;

	/**
	 * Lokacni manazer
	 */
	private LocationManager locManager;

	/**
	 *  Zda je povolen GPS provider
	 */
	private boolean gpsProviderEnabled = false;
	
	/**
	 *  Zda je povolen NET provider
	 */
	private boolean netProviderEnabled = false;
	
	/**
	 *  Handler pro samostatne vlakno
	 */
	private Handler handler;
	
	/**
	 *  Runnable samotne vlakno
	 */
	private Runnable rAble;

	/**
	 * Konstruktor
	 * @param context
	 */
	public MyLocation(Context context) {
		super();
		this.context = context;
		locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * Kontrola GPS a NET provideru
	 */
	public boolean checkProviders() {
		gpsProviderEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		netProviderEnabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!gpsProviderEnabled && !netProviderEnabled) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Získání polohy pomocí NET a GPS
	 * @param result
	 */
	public void getLocation(final LocationResult result) {
		// Pres locationresult se pak aplikace ozve, ze nasla pozici
		
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
				
				result.getLocationAddress(getAddress(latestLocation));
			}
		};
		
		// Kdyz je NET provider aktivni, nastavi na nej posluchac
		if (netProviderEnabled) {
			netListener = new LocationListener() {
				public void onLocationChanged(Location location) {

					result.getLocationAddress(getAddress(location));
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
					result.getLocationAddress(getAddress(location));
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
		handler.postDelayed(rAble, 5000);

	}

	/**
	 * Zjisti adresdu z gps souradnic
	 * 
	 * @param location
	 * @return
	 * @throws Exception
	 */
	private Address getAddress(Location location) {

		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			int maxResults = 1;

			Geocoder gcgeocoder = new Geocoder(context, Locale.getDefault());
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
	 * Zruseni listeneru provideru a nacasovaneho vlakna
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
	 * @author Martin Stajner
	 * Specialni trida pro ziskavani vysledku hledani polohy, kvuli vlaknu
	 */
	public static abstract class LocationResult {
		// TODO predelat na nejakej jinej zpusob oznamovani obdrzeni polohy?
		public abstract void getLocationAddress(Address address);
	}
}