package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.model.util.Toaster;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * T��da aktivity hlavn� obrazovky aplikace.
 * 
 * @author Martin �tajner
 * 
 */
public class MainActivity extends Activity {

	/**
	 * Glob�ln� hodnota reprezentuj�c� Vytvo�en� nov�ho PL
	 */
	private static final int NEW_TICKET = 0;

	/**
	 * Glob�ln� hodnota reprezentuj�c� Prihlaseni pri vytvareni noveho
	 * parkovaciho listku
	 */
	private static final int TICKET_LOGIN = 1;

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//nastav� defaultn� hodnoty nastaven� tam, kde nejsou zat�m ��dn� hodnoty
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
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
	 * Obsluha tla��tka - Nov� PL
	 * 
	 * @param button
	 */
	public void newTicketClick(View button) {
		startActivityForResult(new Intent(this, TicketLoginActivity.class), TICKET_LOGIN);
	}

	/**
	 * Obsluha tla��tka - List ulo�en�ch PL
	 * 
	 * @param button
	 */
	public void ticketListClick(View button) {
		this.startActivity(new Intent(this, TicketListActivity.class));
	}

	/**
	 * Obsluha tlačítka - Kontrola parkovací karty
	 * 
	 * @param button
	 */
	public void checkCardClick(View button) {
		this.startActivity(new Intent(this, BlueCardActivity.class));
	}

	/**
	 * Obsluha tlačítka - Odhlásit
	 * 
	 * @param button
	 */
	public void logoutClick(View button) {
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
							setResult(RESULT_OK);
							finish();
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
			setResult(RESULT_OK);
			finish();
		}
	}

	/**
	 * Obsluha tlačítka - Kontrola SMS parkovani
	 * 
	 * @param button
	 */
	public void checkSMSParkingClick(View button) {
		this.startActivity(new Intent(this, SMSParkingActivity.class));
	}

	/**
	 * Obsluha tla��tka - Nastaven� aplikace
	 * 
	 * @param button
	 */
	public void preferencesClick(View button) {
		this.startActivity(new Intent(this, PreferencesActivity.class));
	}

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
}