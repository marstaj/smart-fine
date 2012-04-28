package cz.smartfine.android;

import cz.smartfine.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Tøída aktivity hlavní obrazovky aplikace.
 * 
 * @author Martin Štajner
 * 
 */
public class MainActivity extends Activity {

	/**
	 * Globální hodnota reprezentující Vytvoøení nového PL
	 */
	private static final int NEW_TICKET = 0;

	/**
	 * Globální hodnota reprezentující Prihlaseni pri vytvareni noveho
	 * parkovaciho listku
	 */
	private static final int TICKET_LOGIN = 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//nastaví defaultní hodnoty nastavení tam, kde nejsou zatím žádné hodnoty
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
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
	 * Obsluha tlaèítka - Nový PL
	 * 
	 * @param button
	 */
	public void newTicketClick(View button) {
		startActivityForResult(new Intent(this, TicketLoginActivity.class), TICKET_LOGIN);
	}

	/**
	 * Obsluha tlaèítka - List uložených PL
	 * 
	 * @param button
	 */
	public void ticketListClick(View button) {
		this.startActivity(new Intent(this, TicketListActivity.class));
	}

	/**
	 * Obsluha tlaèítka - Kontrola parkovací karty
	 * 
	 * @param button
	 */
	public void checkCardClick(View button) {
		this.startActivity(new Intent(this, BlueCardActivity.class));
	}

	/**
	 * Obsluha tlaèítka - Kontrola SMS parkovani
	 * 
	 * @param button
	 */
	public void checkSMSParkingClick(View button) {
		this.startActivity(new Intent(this, SMSParkingActivity.class));
	}

	/**
	 * Obsluha tlaèítka - Nastavení aplikace
	 * 
	 * @param button
	 */
	public void preferencesClick(View button) {
		this.startActivity(new Intent(this, PreferencesActivity.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
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
}