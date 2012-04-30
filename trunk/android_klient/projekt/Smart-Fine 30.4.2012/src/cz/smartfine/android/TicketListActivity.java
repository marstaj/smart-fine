package cz.smartfine.android;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.adapters.TicketListAdapter;
import cz.smartfine.android.helpers.UploadTicketsActivityHelper;
import cz.smartfine.android.helpers.UploadWaypointsActivityHelper;
import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.model.Waypoint;
import cz.smartfine.android.model.util.Internet;
import cz.smartfine.android.model.util.Messenger;
import cz.smartfine.android.model.util.Toaster;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
 * T��da aktivity zobrazen� listu v�ech ulo�en�ch PL
 * 
 * @author Martin �tajner
 * 
 */
public class TicketListActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Globální hodnota reprezentující přihlášení při uploadu lístků na server.
	 */
	private static final int UPLOAD_LOGIN = 0;

	private ProgressDialog progDialog;

	private ArrayList<Ticket> tickets;
	private ArrayList<Waypoint> waypoints;

	private UploadTicketsActivityHelper helperTickets;
	private UploadWaypointsActivityHelper helperWaypoints;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket_list);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		tickets = app.getTicketDao().getAllTickets();
		if (tickets.isEmpty()) {
			((ImageView) findViewById(R.id.titleUploadButton)).setVisibility(View.GONE);
			((TextView) findViewById(R.id.ticketListNoTicketsText)).setVisibility(View.VISIBLE);
		}

		// Nastaveni listu
		ListView lv = (ListView) this.findViewById(R.id.ticketList);
		lv.setTextFilterEnabled(true);
		lv.setAdapter(new TicketListAdapter(this, R.layout.ticket_list_item, tickets));

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Spusteni nove aktivity
				parent.getContext().startActivity(new Intent(parent.getContext(), TicketDetailActivity.class).putExtra("Ticket", position));
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ticketlistmenu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (tickets.isEmpty()) {
			// Zablokuje tlačítko uploadu, kdyz je prazdny tickets
			menu.getItem(0).setEnabled(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

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
	 * Obsluha tlačítka "Upload dat na server"
	 * 
	 * @param button
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Navrat z UploadLogin po uspesnem dokonceni prihlaseni
		if (requestCode == UPLOAD_LOGIN && resultCode == RESULT_OK && data.getExtras().containsKey("badgeNumber")) {
			continueWithUpload((String) data.getExtras().get("badgeNumber"));
		}
	}

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

	private void uploadWaypoints() {

		waypoints = app.getLocationDAO().getAllWaypoints();
		
		waypoints.add(new Waypoint());
		waypoints.add(new Waypoint());
		waypoints.add(new Waypoint());
		waypoints.add(new Waypoint());
		waypoints.add(new Waypoint());
		
		// Kdyz jsou k dispozici data o poloze, zkusi je odeslat. Jinak zavola odesilani listku
		if (!waypoints.isEmpty()) {
			
			// Nastavi geo data
			helperWaypoints.setWaypoints(waypoints);
			
			// Zkontroluje prihlaseni k internetu
			if (Internet.isOnline(this)) {

				// Nastavení dialogu a LogIN
				// TODO napsat do R
				progDialog = new ProgressDialog(this);
				progDialog.setTitle("Nahrávání na server");
				progDialog.setMessage("Prosím počkejte...");
				progDialog.setIndeterminate(true);
				progDialog.setCancelable(false);
				progDialog.show();

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
	 * Metoda se zavola po uspesnem prihlaseni policisty pri uploadu dat na
	 * server
	 */
	public void uploadTickets() {

		// Zkontroluje prihlaseni k internetu
		if (Internet.isOnline(this)) {

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

	// ==================================== GET / SET ==================================== //

	/**
	 * @return
	 */
	public Dialog getProggressDialog() {
		return progDialog;
	}

	/**
	 * @return
	 */
	public MyApp getApp() {
		return app;
	}

	/**
	 * @return
	 */
	public ArrayList<Ticket> getList() {
		return tickets;
	}

	// ==================================== OSTATNÍ ==================================== //

	public void updateProggressBar() {
		int max = progDialog.getMax();
		int current = tickets.size();
		progDialog.setProgress(max - current);
	}

	public void reload() {
		// Odpoji protokol
		if (helperTickets.getProtocol() != null) {
			helperTickets.getProtocol().disconnectProtocol();
		}

		// Ulozi listky
		try {
			app.getTicketDao().saveAllTickets();
		} catch (Exception e) {
			//  TODO do R.
			Messenger.sendStringMessage(helperTickets, "Chyba při ukládání souboru s lítky.");
		}

		// Restaruje aktivitu
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
}