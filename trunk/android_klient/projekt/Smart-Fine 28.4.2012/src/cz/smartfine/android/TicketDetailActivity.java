package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.model.util.TicketSetter;
import cz.smartfine.android.model.util.Toaster;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Tøída aktivity pro zobrazení informací z parkovacího lístku
 * 
 * @author Pavel Brož, Martin Štajner
 */
public class TicketDetailActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;
	/**
	 * Index zobrazeného PL do DAO
	 */
	private int ticketIndex;
	/**
	 * Globální hodnota reprezentující Editaci PL
	 */
	private static final int EDIT = 0;

	/**
	 * Globální hodnota reprezentující Tisk PL
	 */
	@SuppressWarnings("unused")
	private static final int PRINT = 1;

	/**
	 * Zobrazení parkovací lístek
	 */
	private Ticket showedTicket;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket_detail);

		app = (MyApp) this.getApplication();

		// získá index do DAO na lístek, který s má zobrazit, pøedaný ze
		// spouštìjící aktivity a ziská dany listek
		ticketIndex = this.getIntent().getExtras().getInt("Ticket");
		showedTicket = app.getTicketDao().getTicket(ticketIndex);

		// ovìøí, zda se nìo vrátilo a zavolá metodu pro naplnìní widgetù daty//
		if (showedTicket != null) {
			setTicket(showedTicket);

			manageControls(showedTicket);
		}
	}

	/**
	 * Naplní widgety daty z instance PL pøedané v parametru
	 * 
	 * @param ticket
	 */
	private void setTicket(Ticket ticket) {
		// naplní formuláø editovatelnými daty
		TicketSetter.setTicketBasic(this, ticket);
		// naplní formuláø needitovatelnými daty
		TicketSetter.setTicketExtra(this, ticket);
		// Nastavi fotodokumentaci
		TicketSetter.setTicketPhotoDocumentation(this, ticket.getPhotos());
	}

	/**
	 * Posluchaè stisknutí tlaèítka tisku PL
	 * 
	 * @param button
	 *            Stisknuté tlaèítko
	 */
	public void printTicketClick(final View button) {
		// TODO: podporu tisku implementovat až ve fázi 2 - HLAVNE odstranit
		// tuhle dummy hruzu co jsem tam napsal kvuli PDA :)
		// Pro print jsem nahore udelal promenou PRINT, pro
		// startActivityForResult jako je u editace nize. :)

		Builder builder = new Builder(this);
		builder.setMessage("Opravdu chcete parkovací lístek vytisknout?").setCancelable(false).setPositiveButton("Ano", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				final ProgressDialog dialog1 = ProgressDialog.show(TicketDetailActivity.this, "Tisk parkovacího lístku", "Tisknu, prosím poèkejte...", true);
				dialog1.show();

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						dialog1.dismiss();
						Toaster.toast("Parkovací lístek úspìšnì vytištìn.", Toaster.SHORT);
						Ticket ticket = app.getTicketDao().getTicket(ticketIndex);
						ticket.setPrinted(true);
						setTicket(ticket);
						manageControls(ticket);
					}
				}, 3000); // 3000 milliseconds

			}
		}).setNegativeButton("Ne", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.show();

	}

	/**
	 * Posluchaè stisknutí tlaèítka editace PL
	 * 
	 * @param button
	 *            Stisknuté tlaèítko
	 */
	public void editTicketClick(View button) {
		// vytvoøí Intent, vloží do nìj index na zobrazený PL a spustí aktivitu
		// pro editaci tohoto PL
		this.startActivityForResult(new Intent(this, TicketEditActivity.class).putExtra("Ticket", ticketIndex), EDIT);
	}

	/**
	 * Posluchaè stisknutí tlaèítka odstranìní lístku
	 * 
	 * @param button
	 *            Stisknuté tlaèítko
	 */
	public void removeTicketClick(View button) {
		// TODO
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

		// Navrat z EditActivity uspesnem po dokonceni editace. Refreshne data
		// na obrazovce
		if (requestCode == EDIT && resultCode == RESULT_OK) {
			Ticket ticket = app.getTicketDao().getTicket(ticketIndex);
			// ovìøí, zda se nìo vrátilo a zavolá metodu pro naplnìní widgetù
			// daty//
			if (ticket != null) {
				setTicket(ticket);
			}
		}
	}

	/**
	 * V pøípadì, že je lístek již vytištìn, znemožní další editaci a mazání
	 * lístku tím, že skryje ovládání.
	 * 
	 * @param ticket
	 */
	private void manageControls(Ticket ticket) {
		if (ticket.isPrinted()) {
			ImageView titleEdit = (ImageView) findViewById(R.id.titleEditTicketButton);
			titleEdit.setVisibility(View.GONE);
			ImageView titleRemove = (ImageView) findViewById(R.id.titleRemoveTicketButton);
			titleRemove.setVisibility(View.GONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.detialmenu, menu);
		return true;
	}

	

	/* (non-Javadoc)
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (showedTicket != null && showedTicket.isPrinted()) {
			// Zablokuje tlaèítka editace a tisku parkovacího lístkum pokud byl už vytištìn
			menu.getItem(0).setEnabled(false);
			menu.getItem(1).setEnabled(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.editTicketMenu : {
				editTicketClick(null);
				return true;
			}
			case R.id.removeTicketMenu : {
				removeTicketClick(null);
				return true;
			}
			case R.id.printTicketMenu : {
				printTicketClick(null);
				return true;
			}
			default :
				return super.onOptionsItemSelected(item);
		}
	}
}
