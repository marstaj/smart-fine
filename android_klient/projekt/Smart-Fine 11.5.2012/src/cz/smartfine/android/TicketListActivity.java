package cz.smartfine.android;

import java.util.ArrayList;
import cz.smartfine.android.R;
import cz.smartfine.android.adapters.TicketListAdapter;
import cz.smartfine.android.helpers.UploadTicketsActivityHelper;
import cz.smartfine.android.helpers.UploadWaypointsActivityHelper;
import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.model.Waypoint;
import cz.smartfine.android.model.util.Phone;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Třída představující objekt typu Activity, který slouží k zobrazení seznamu
 * všech uložených a neodeslaných parkovacích lístků. Objekt také zajišťuje
 * odeslání geolokačních dat a parkovacích lístků na server.
 * 
 * @author Martin Štajner
 * 
 */
public class TicketListActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Globální hodnota reprezentující přihlášení při nahrávání lístků na
	 * server.
	 */
	private static final int UPLOAD_LOGIN = 0;

	/**
	 * Globální hodnota reprezentující zobrazení detailu parkovacího lístku.
	 */
	private static final int DETAIL = 1;

	/**
	 * Dialog sloužící k informování uživatele o stavu komunikace se serverem
	 */
	private ProgressDialog progDialog;

	/**
	 * Instance listu s parkovacími lístky
	 */
	private ArrayList<Ticket> tickets;

	/**
	 * Instance listu s geolokačními daty
	 */
	private ArrayList<Waypoint> waypoints;

	/**
	 * Instance pomocného objektu pro odeslání parkovacích lístků
	 */
	private UploadTicketsActivityHelper helperTickets;

	/**
	 * Instance pomocného objektu pro odeslání geolokačních dat
	 */
	private UploadWaypointsActivityHelper helperWaypoints;

	/**
	 * Instance aktivity TicketListActivity
	 */
	private Activity activity;

	// ==================================== INITIALIZATON ==================================== //

	/**
	 * Metoda inicializuje objekt TicketListActivity a je volána při jeho
	 * vytváření.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Nastavení View
		setContentView(R.layout.ticket_list);

		// Přiřazení instance aktivity
		activity = this;

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		// V případě, že nejsou žádné lískty, odstraní tlačítko nahrání na server
		tickets = app.getTicketDao().getAllTickets();
		if (tickets.isEmpty()) {
			((ImageView) findViewById(R.id.titleUploadButton)).setVisibility(View.GONE);
			((TextView) findViewById(R.id.ticketListNoTicketsText)).setVisibility(View.VISIBLE);
		}

		// Nastaveni seznamu
		ListView lv = (ListView) this.findViewById(R.id.ticketList);
		lv.setTextFilterEnabled(true);
		lv.setAdapter(new TicketListAdapter(this, R.layout.ticket_list_item, tickets));

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Spusteni nove aktivity
				activity.startActivityForResult(new Intent(parent.getContext(), TicketDetailActivity.class).putExtra("Ticket", position), DETAIL);
			}
		});

	}

	// ==================================== BUTTONS ==================================== //

	/**
	 * Obsluha tlačítka nahrání parkovacích lístků na server
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void uploadToServerClick(View button) {

		// Dialog potvrzení nahrání lístků dat na server
		Builder builder = new Builder(this);
		//TODO do R.
		builder.setMessage("Oravdu chcete nahrát veškerá data na server?");
		builder.setCancelable(false);
		//TODO do R.
		builder.setPositiveButton("Ano", new OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {

				// Spusti se prihlaseni
				startActivityForResult(new Intent(TicketListActivity.this, UploadLoginActivity.class), UPLOAD_LOGIN);
			}
		});

		//TODO do R.
		builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.show();
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
		inflater.inflate(R.menu.ticketlistmenu, menu);
		return true;
	}

	/**
	 * Metoda v případě prázdného seznamu parkovacích lístků zablokuje tlačítko
	 * v menu sloužící k odeslání parkovacích lístků
	 * 
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (tickets.isEmpty()) {
			// Zablokuje tlačítko uploadu, kdyz je prazdny tickets
			menu.getItem(0).setEnabled(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Metoda zavolá akci po stisku tlačítka v menu
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.uploadTicketsMenu : {
				uploadToServerClick(null);
				return true;
			}
			default :
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Metoda zavolá akci po návratu z Aktivity, která byla spuštěna z
	 * TicketListActivity
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 *      android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Navrat z UploadLogin po uspesnem dokonceni prihlaseni
		if (requestCode == UPLOAD_LOGIN && resultCode == RESULT_OK && data.getExtras().containsKey("badgeNumber")) {
			continueWithUpload((String) data.getExtras().get("badgeNumber"));
		}

		// Navrat z TicketDetail po smazani listku
		if (requestCode == DETAIL && resultCode == RESULT_OK) {
			// Restartuje aktivitu
			reload();
		}

	}

	// ==================================== GET / SET ==================================== //

	/**
	 * Metoda vrání instanci aplikace
	 * 
	 * @return Instance aplikace
	 */
	public MyApp getApp() {
		return app;
	}

	/**
	 * Metoda vrátí list parkovacích lístků
	 * 
	 * @return List parkovacích lísků
	 */
	public ArrayList<Ticket> getList() {
		return tickets;
	}

	// ==================================== OTHER ==================================== //

	/**
	 * Metoda inicializje pomocné objekty pro nahrání dat na server a zavolá
	 * nahrání geolokačních dat.
	 * 
	 * @param badgeNumber
	 *            služební číslo policisty
	 */
	private void continueWithUpload(String badgeNumber) {
		// Nastavi helperTickets
		if (helperTickets == null) {
			helperTickets = new UploadTicketsActivityHelper(this);
		}
		// Nastavi helperWaypoints
		if (helperWaypoints == null) {
			helperWaypoints = new UploadWaypointsActivityHelper(this);
		}
		// Nastavi cislo prihlaseneho policisty
		helperTickets.setBadgeNumber(Integer.valueOf(badgeNumber));

		uploadWaypoints();
	}

	/**
	 * Metoda nahraje geolokační data na server a v případě úspěchu zavolá
	 * nahrání parkovacích lístků na server
	 */
	private void uploadWaypoints() {

		waypoints = app.getLocationDAO().getAllWaypoints();

		// Kdyz jsou k dispozici data o poloze, zkusi je odeslat. Jinak zavola odesilani listku
		if (!waypoints.isEmpty()) {

			// Nastavi geo data
			helperWaypoints.setWaypoints(waypoints);

			// Zkontroluje prihlaseni k internetu
			if (Phone.isConnectedToInternet(getApplicationContext())) {

				// Nastavení dialogu a LogIN
				// TODO napsat do R
				progDialog = new ProgressDialog(this);
				progDialog.setTitle("Nahrávání na server");
				progDialog.setMessage("Prosím počkejte...");
				progDialog.setIndeterminate(true);
				progDialog.setCancelable(false);
				progDialog.show();

				// Zablokuje otoceni obrazovky
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

				// Kontrola, zda uz nahodou nejsme pripojeni. Kdyz ano, tak se rovnou ocekovat SPZ, kdyz ne, tak se nejdrive zkusime pripojit a prihlasit.
				if (app.getConnectionProvider().isConnected()) {

					helperWaypoints.sendWaypoints(this, waypoints);
				} else {
					helperWaypoints.connectAndLogin();
				}

			} else {
				// TODO do R.
				Toaster.toast("K odeslání dat na server musíte být připojeni k internetu.", Toaster.SHORT);
			}
		} else {
			uploadTickets();
		}
	}

	/**
	 * Metoda zavolá smazání odeslaných geolokačních dat, odpojí komunikační
	 * protokol a nahraje parkovací lístky na server
	 */
	public void uploadTickets() {

		// Odpoji protokol
		if (helperWaypoints.getProtocol() != null) {
			helperWaypoints.getProtocol().disconnectProtocol();
		}

		try {
			app.getLocationDAO().deleteAllWaypoints();
		} catch (Exception e) {
			//TODO do R
			Toaster.toast("Nepodařilo smazat data o poloze.", Toaster.SHORT);
		}

		// Zkontroluje prihlaseni k internetu
		if (Phone.isConnectedToInternet(this)) {

			// Nastavení dialogu a LogIN
			// TODO napsat do R
			progDialog = new ProgressDialog(this);
			progDialog.setTitle("Nahrávání na server");
			progDialog.setMessage("Prosím počkejte...");
			progDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progDialog.setCancelable(false);
			progDialog.setProgress(0);
			progDialog.setMax(tickets.size());
			progDialog.show();

			// Kontrola, zda uz nahodou nejsme pripojeni. Kdyz ano, tak se rovnou ocekovat SPZ, kdyz ne, tak se nejdrive zkusime pripojit a prihlasit.
			if (app.getConnectionProvider().isConnected()) {

				helperTickets.sendTicket(this, helperTickets.getBadgeNumber());
			} else {
				helperTickets.connectAndLogin();
			}

		} else {
			// TODO do R.
			Toaster.toast("K odeslání dat na server musíte být připojeni k internetu.", Toaster.SHORT);
		}
	}

	/**
	 * Metoda upraví nahrávací dialog podle počtu již odeslaných parkovacích
	 * lístků.
	 */
	public void updateProggressBar() {
		int max = progDialog.getMax();
		int current = tickets.size();
		progDialog.setProgress(max - current);
	}

	/**
	 * Metoda odpojí komunikační protokol pomocné třídy helper a uloží seznam
	 * parkovacíh lístků. Ten je v ideálním případě prázdný.
	 */
	public void finishUpload() {
		// Odpoji protokol
		if (helperTickets.getProtocol() != null) {
			helperTickets.getProtocol().disconnectProtocol();
		}

		// Ulozi listky
		try {
			app.getTicketDao().saveAllTickets();
		} catch (Exception e) {
			//  TODO do R.
			Messenger.sendStringMessage(helperTickets, "Chyba při ukládání souboru s lístky.");
		}
		// Restartuje aktivitu
		reload();
	}

	/**
	 * Metoda restartuje aktivitu TicketListActivity
	 */
	private void reload() {
		// Restaruje aktivitu
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}

	/**
	 * Metoda zruší dialog.
	 */
	public void dismissDialog() {
		progDialog.dismiss();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}
}