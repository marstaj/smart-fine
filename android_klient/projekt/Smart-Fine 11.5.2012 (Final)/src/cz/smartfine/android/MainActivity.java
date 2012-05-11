package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.model.util.LocalBinder;
import cz.smartfine.android.model.util.Toaster;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

// TODO CELA APLIKACE
// dodelat vsude javadoc a opravit otaznicky
// kdyz bude cas, tak poresit otaceni kdyz bezi vlakna, jinak zablokovat otaceni --
// do taskbaru dodelat pozadi pri stisknuti tlacitek
// nastavit nejake nejcastejsi hodnoty defaultne oblibene
// ocekovat � �

// Presun adapteru, zmena validatoru, zmena internet na phone, zmena Location checkovani provideru

/**
 * Třída představující objekt typu Activity, který slouží jako hlavní menu
 * aplikace.
 * 
 * @author Martin Štajner
 * 
 */
public class MainActivity extends Activity {

	/**
	 * Globální hodnota reprezentující Vytvoření nového parkovacího lístku
	 */
	private static final int NEW_TICKET = 0;

	/**
	 * Globální hodnota reprezentující přihlášení při vytváření nového
	 * parkovacího lístku
	 */
	private static final int TICKET_LOGIN = 1;

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Proměnná reprezentující úmysl LocationService
	 */
	private Intent locationServiceIntent;

	/**
	 * Proměnná značící, jestli je LocationService připoután k této aktivitě
	 * MainActivity
	 */
	private boolean mIsBound;

	/**
	 * Instance objektu LocationService
	 */
	private LocationService boundedLocationService;

	// ==================================== INITIALIZATON ==================================== //

	/**
	 * Metoda inicializuje objekt MainActivity a další součásti aplikace a je
	 * volána při jeho vytváření
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		locationServiceIntent = new Intent(this, LocationService.class);
		// Kontroluje, zda nebezi Location service. Pokud ne, spusti ho.
		if (!isLocationServiceRunning()) {
			// Zapne location service v jinem vlakne pro prijimani polohy
			Thread t = new Thread() {
				public void run() {
					MainActivity.this.startService(locationServiceIntent);
				}
			};
			t.start();
		}

		// Pripojí LocationService k aktivitě
		doBindService();

		//Nastaví view
		setContentView(R.layout.main);

		//Nastaví defaultní hodnoty nastavení tam, kde nejsou zatím žádné hodnoty
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}

	// ==================================== BUTTONS ==================================== //

	/**
	 * Obsluha tlačítka vytvoření nového parkovacího lístku
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void newTicketClick(View button) {
		startActivityForResult(new Intent(this, TicketLoginActivity.class), TICKET_LOGIN);
	}

	/**
	 * Obsluha tlačítka zobrazení listu uložených parkovacích lístků
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void ticketListClick(View button) {
		this.startActivity(new Intent(this, TicketListActivity.class));
	}

	/**
	 * Obsluha tlačítka kontroly parkovací karty
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void checkCardClick(View button) {
		this.startActivity(new Intent(this, BlueCardActivity.class));
	}

	/**
	 * Obsluha tlačítka odhlášení
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void logoutClick(View button) {

		// Když není připojení k serveru
		if (!app.getConnectionProvider().isConnected()) {

			final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle("Odhlašování");
			dialog.setMessage("Prosím počkejte...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();

			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					Bundle data = msg.getData();

					// Odpoved z vlakna, ktere se snazilo pripojit k serveru
					if (data.containsKey("connect")) {
						if (data.getBoolean("connect")) {
							dialog.dismiss();
							logout();
						} else {
							// TODO napsat do R
							dialog.dismiss();
							Toaster.toast("Nepodařilo se připojit a odhlásit se ze serveru.", Toaster.SHORT);
						}
					}
				}
			};

			// Pripojeni na server ve zvlastnim vlakne
			Runnable rable = new Runnable() {
				public void run() {
					boolean conected = app.getConnectionProvider().connectAndLogin();
					Message msg = Message.obtain();
					Bundle data = new Bundle();
					data.putBoolean("connect", conected);
					msg.setData(data);
					handler.sendMessage(msg);
				}
			};
			final Thread thread = new Thread(rable);
			thread.start();

		} else {
			logout();
		}
	}

	/**
	 * Obsluha tlačítka kontroly SMS parkování
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void checkSMSParkingClick(View button) {
		this.startActivity(new Intent(this, SMSParkingActivity.class));
	}

	/**
	 * Obsluha tlačítka nastavení aplikace
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void preferencesClick(View button) {
		this.startActivity(new Intent(this, PreferencesActivity.class));
	}

	// ==================================== OVERRIDE METHODS ==================================== //

	/**
	 * Metoda připraví menu
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	/**
	 * Metoda zavolá akci po stisku tlačítka v menu
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.newTicketMenu : {
				newTicketClick(null);
				return true;
			}
			case R.id.ticketListMenu : {
				ticketListClick(null);
				return true;
			}
			case R.id.preferencesMenu : {
				preferencesClick(null);
				return true;
			}
			case R.id.blueCardMenu : {
				checkCardClick(null);
				return true;
			}
			case R.id.smsMenu : {
				checkSMSParkingClick(null);
				return true;
			}
			default :
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Metoda odpoutá aktivitu od LocationService. Je volána při ukončení
	 * aktivity.
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		doUnbindService();
		super.finish();
	}

	/**
	 * Metoda zavolá akci po návratu z Aktivity, která byla spuštěna z
	 * MainActivity
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 *      android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Navrat z Ticket Login po uspesnem dokonceni prihlaseni
		if (requestCode == TICKET_LOGIN && resultCode == RESULT_OK) {
			this.startActivityForResult(new Intent(this, TicketEditActivity.class), NEW_TICKET);
		}

		// Navrat z EditActivity po dokonceni vytvareni PL a jeho vracenem indexu.
		if (requestCode == NEW_TICKET && resultCode == RESULT_OK && data.getExtras().containsKey("Ticket")) {
			startActivity(new Intent(getApplicationContext(), TicketDetailActivity.class).putExtra("Ticket", data.getExtras().getInt("Ticket")));
		}

	}

	/**
	 * Metoda odpoji aktivitu od LocationService. Je volána při ničení aktivity.
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		doUnbindService();
		super.onDestroy();
	}

	// ==================================== OTHER ==================================== //

	/**
	 * Medota zjistí, zda běží Location service.
	 * 
	 * @return True, když service běží, jinak false
	 */
	private boolean isLocationServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (LocationService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Připojení k LocationService
	 */
	private ServiceConnection locationServiceConnection = new ServiceConnection() {
		@SuppressWarnings("unchecked")
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// nastavi se instanci locationservice
			boundedLocationService = ((LocalBinder<LocationService>) service).getService();
		}
		@Override
		public void onServiceDisconnected(ComponentName className) {
		}
	};

	/**
	 * Metoda připojí LocationService k aktivitě
	 */
	private void doBindService() {
		// Pripoji service k aktivite
		bindService(locationServiceIntent, locationServiceConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	/**
	 * Metoda odpojí LocationService od aktivity
	 */
	private void doUnbindService() {
		if (mIsBound) {
			unbindService(locationServiceConnection);
			mIsBound = false;
		}
	}

	/**
	 * Metoda ukončí činnost LocationService, zavolá jeho zničení a ukončí
	 * MainActivity s příznakem odhlášení.
	 */
	private void logout() {
		// Stopne location service
		boundedLocationService.stop();
		stopService(locationServiceIntent);
		setResult(RESULT_OK);
		finish();
	}

}