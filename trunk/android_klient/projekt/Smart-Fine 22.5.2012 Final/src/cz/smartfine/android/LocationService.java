package cz.smartfine.android;

import cz.smartfine.android.dao.interfaces.ILocationDAO;
import cz.smartfine.android.model.Waypoint;
import cz.smartfine.android.model.util.LocalBinder;
import cz.smartfine.android.model.util.Phone;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

/**
 * Třída představující objekt typu Service, který běží na pozadí a stará se o
 * neustálé zjišťování aktuální polohy zařízení. Ta se zjišťuje vždy po určitém
 * časovém intervalu, z důvodu šetření baterie. Nová poloha se ukládá pouze
 * tehdy, když vzdálenost mezi jí a poslední uloženou polohou je větší, než
 * určitá vzdálenost. LocationService je spouštěn ve STICKY módu. To znamená, že
 * kdykoliv je LocationService zastaven systémem, opět se spustí. Po spuštění
 * provede zapnutí dostupných providerů.
 * 
 * @author Martin Štajner
 * 
 */
public class LocationService extends Service {

	/**
	 * Instance LocationDao
	 */
	private ILocationDAO locationDAO;

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Instance LocationManager
	 */
	private LocationManager locManager;

	/**
	 * Handler pro spouštění runnable se zpožděním
	 */
	private Handler handler;

	/**
	 * Proměnná představující časový interval mezi zjišťováním polohy
	 */
	private long milisec = 90000; // 90 vteřin = 1.5 minuty

	/**
	 * Proměnná představující časový interval zpoždění spuštění runnable
	 */
	private int sleep = 45000; // 45 vteřin

	/**
	 * Proměnná představující minimální vzdálenost mezi ukládanými body
	 */
	private int distance = 50; // 50 metrů

	/**
	 * Proměnná představující právě nastavený provider.
	 */
	private Provider setProvider = Provider.NONE;

	/**
	 * Enum výčet možností poviderů
	 * 
	 * @author Martin Štajner
	 */
	private enum Provider {
		NONE, NET, GPS
	};

	/**
	 * Posluchač NET providera
	 */
	private LocationListener netListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// Nalezení posledního přidaného bodu
			Waypoint wp = locationDAO.getLastWaypoint();
			// Když žádný není, nebo je moc daleko, přidá se nový bod.
			if (wp == null || calculateDistance(wp.getLatitude(), wp.getLongtitude(), location.getLatitude(), location.getLongitude()) > distance) {
				wp = new Waypoint();
				wp.setLatitude(location.getLatitude());
				wp.setLongtitude(location.getLongitude());
				wp.setTime(location.getTime());
				locationDAO.saveWaypoint(wp);
				//  TODO Code for DEBUG only
				//				Toaster.toast(String.valueOf(app.getLocationDAO().getAllWaypoints().size()), Toaster.SHORT);
				try {
					locationDAO.saveAllWaypoints();
				} catch (Exception e) {
					// TODO
				}
			} else {
				// TODO Code for DEBUG only
				//				Toaster.toast("Neukladam, moc blizko. " + String.valueOf(app.getLocationDAO().getAllWaypoints().size()), Toaster.SHORT);
			}
			// Znova se provede kontrola
			setLocationProvider();
		}
		public void onProviderDisabled(String provider) {
			setLocationProvider();
		}
		public void onProviderEnabled(String provider) {
			setLocationProvider();
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/**
	 * Posluchač GPS providera.
	 */
	private LocationListener gpsListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// Nalezení posledního přidaného bodu
			Waypoint wp = locationDAO.getLastWaypoint();
			// Když žádný není, nebo je moc daleko, přidá se nový bod.
			if (wp == null || calculateDistance(wp.getLatitude(), wp.getLongtitude(), location.getLatitude(), location.getLongitude()) > distance) {
				wp = new Waypoint();
				wp.setLatitude(location.getLatitude());
				wp.setLongtitude(location.getLongitude());
				wp.setTime(location.getTime());
				locationDAO.saveWaypoint(wp);
				// TODO Code for DEBUG only
				//				Toaster.toast(String.valueOf(app.getLocationDAO().getAllWaypoints().size()), Toaster.SHORT);
				try {
					locationDAO.saveAllWaypoints();
				} catch (Exception e) {
					// TODO 
				}
			} else {
				// TODO Code for DEBUG only
				//				Toaster.toast("Neukladam, moc blizko. " + String.valueOf(app.getLocationDAO().getAllWaypoints().size()), Toaster.SHORT);
			}
			// Znova se provede kontrola
			setLocationProvider();
		}
		public void onProviderDisabled(String provider) {
			setLocationProvider();
		}
		public void onProviderEnabled(String provider) {
			setLocationProvider();
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/**
	 * Runnable spuštění kontroly provderů.
	 */
	private Runnable r = new Runnable() {
		public void run() {
			//  TODO Code for DEBUG only
			//			Toaster.toast("Probouzim se", Toaster.SHORT);
			setLocationProvider();
		}
	};

	// ==================================== INITIALIZATON ==================================== //

	/**
	 * Metoda inicializuje objekt LocationService a je volána při jeho
	 * vytváření.
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		// Přiřazení instance aplikace - kvůli přístupu k datům z různych aktivit
		app = (MyApp) this.getApplication();

		// Přiřazení instance locationDAO pro snadější přístup
		locationDAO = app.getLocationDAO();

		// Přiřazení instance LocationManager pro přístup k funkcím zjišťování aktuální polohy
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * Metoda spustí LocationService ve STICKY módu.
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 * 
	 * @return Způsob spuštění LocationService
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Nastaví providery
		setLocationProvider();
		// Service bezi dokut neni explicitne zastaven
		return START_STICKY;
	}

	/**
	 * Metoda vytvoří nový Binder k LocationService a vrátí jej. Binder je
	 * použit pro komunikaci z aktivity, která o Binder zažádala.
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 * 
	 * @return Binder k LocationService
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// Vrati binder pro komunikaci z aktivity
		return new LocalBinder<LocationService>(this);
	}

	// ==================================== OVERRIDE METHODS ==================================== //

	/**
	 * Metoda provede "úklid", před samotným zničením LocationService
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// Uklidí
		stop();
		super.onDestroy();
	}

	// ==================================== OTHER ==================================== //

	/**
	 * Metoda ověří, zda je k dispozici NET provider. Pokud ano, zažádá o
	 * aktuální polohu. Pokud ne, zkontroluje GPS provider. Při nedostupnosti
	 * internetového připojení automaticky přejde rovnou ke kontrole GPS
	 * providera.
	 */
	private void setLocationProvider() {

		if (Phone.isConnectedToInternet(getApplicationContext())) {
			if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				if (setProvider == Provider.GPS || setProvider == Provider.NONE) {
					locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, milisec, 0, netListener);
					locManager.removeUpdates(gpsListener);
					setProvider = Provider.NET;
					//  TODO Code for DEBUG only
					//					Toaster.toast("Zacinam NET", Toaster.SHORT);
				} else {
					//  TODO Code for DEBUG only
					//					Toaster.toast("NET", Toaster.SHORT);
				}
			} else {
				checkGPS();
			}
		} else {
			checkGPS();
		}
	};

	/**
	 * Procedůra ověří, zda je k dispozici GPS provider. Pokud ano, zažádá o
	 * aktuální polohu. Zároveň spustí kontrolu provdedů se zpožděním.
	 */
	private void checkGPS() {
		if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			if (setProvider == Provider.NET || setProvider == Provider.NONE) {
				locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, milisec, 0, gpsListener);
				locManager.removeUpdates(netListener);
				setProvider = Provider.GPS;
				//  TODO Code for DEBUG only
				//				Toaster.toast("Zacinam GPS", Toaster.SHORT);
			} else {
				//  TODO Code for DEBUG only
				//				Toaster.toast("GPS", Toaster.SHORT);
			}
		} else {
			stop();
		}
		checkAgain();
	}

	/**
	 * Procedůra spustí runnable kód se zpožděním.
	 */
	private synchronized void checkAgain() {
		//  TODO Code for DEBUG only 
		//		Toaster.toast("Spím", Toaster.SHORT);
		if (handler == null) {
			handler = new Handler();
		}
		handler.postDelayed(r, sleep);
	}

	/**
	 * Fuknce zpočítá vzdálenost dvou součadnic. Výsledek je v metrech. Na větší
	 * vzdálenosti vrací nepřesný výsledek, způsobený tím, že země není přesná
	 * koule.
	 * 
	 * @param lat1
	 *            Zeměpisná šířka 1. souřadnice
	 * @param lng1
	 *            Zeměpisná délka 1. souřadnice
	 * @param lat2
	 *            Zeměpisná šířka 2. souřadnice
	 * @param lng2
	 *            Zeměpisná délka 2. souřadnice
	 * @return Vzdálenost dvou souřadnic v meterech.
	 */
	private static float calculateDistance(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6378;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;
		int meterConversion = 1000;
		return (float) (dist * meterConversion);
	}

	/**
	 * Metoda odstraní čekající Runnable, pokud takový je, a odstraní žádosti o
	 * aktualizace aktuální pozice, pokud nějaké jsou. Metoda vlastně kompletně
	 * stopne činnost LocationService.
	 */
	public void stop() {
		// Odstraní čekající Runnable
		if (handler != null) {
			handler.removeCallbacks(r);
		}
		// Odstraní žádosti o aktualizace pozice
		locManager.removeUpdates(netListener);
		locManager.removeUpdates(gpsListener);
	}

}
