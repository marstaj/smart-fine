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
 * T��da aktivity pro zobrazen� informac� z parkovac�ho l�stku
 * 
 * @author Pavel Bro�, Martin �tajner
 */
public class TicketDetailActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;
	/**
	 * Index zobrazen�ho PL do DAO
	 */
	private int ticketIndex;
	/**
	 * Glob�ln� hodnota reprezentuj�c� Editaci PL
	 */
	private static final int EDIT = 0;

	/**
	 * Glob�ln� hodnota reprezentuj�c� Tisk PL
	 */
	private static final int PRINT = 2;
	
	/**
	 * Glob�ln� hodnota reprezentuj�c� Tisk PL
	 */
	private static final int TICKET_LOGIN = 1;

	/**
	 * Zobrazen� parkovac� l�stek
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

		// z�sk� index do DAO na l�stek, kter� s m� zobrazit, p�edan� ze
		// spou�t�j�c� aktivity a zisk� dany listek
		ticketIndex = this.getIntent().getExtras().getInt("Ticket");
		showedTicket = app.getTicketDao().getTicket(ticketIndex);

		// ov���, zda se n�o vr�tilo a zavol� metodu pro napln�n� widget� daty//
		if (showedTicket != null) {
			setTicket(showedTicket);

			manageControls(showedTicket);
		}
	}

	/**
	 * Napln� widgety daty z instance PL p�edan� v parametru
	 * 
	 * @param ticket
	 */
	private void setTicket(Ticket ticket) {
		// napln� formul�� editovateln�mi daty
		TicketSetter.setTicketBasic(this, ticket);
		// napln� formul�� needitovateln�mi daty
		TicketSetter.setTicketExtra(this, ticket);
		// Nastavi fotodokumentaci
		TicketSetter.setTicketPhotoDocumentation(this, ticket.getPhotos());
	}

	/**
	 * Poslucha� stisknut� tla��tka tisku PL
	 * 
	 * @param button
	 *            Stisknut� tla��tko
	 */
	public void printTicketClick(final View button) {
		// TODO: podporu tisku implementovat a� ve f�zi 2 - HLAVNE odstranit
		// tuhle dummy hruzu co jsem tam napsal kvuli PDA :)
		// Pro print jsem nahore udelal promenou PRINT, pro
		// startActivityForResult jako je u editace nize. :)

		Builder builder = new Builder(this);
		builder.setMessage("Opravdu chcete parkovac� l�stek vytisknout?").setCancelable(false).setPositiveButton("Ano", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				final ProgressDialog dialog1 = ProgressDialog.show(TicketDetailActivity.this, "Tisk parkovac�ho l�stku", "Tisknu, pros�m po�kejte...", true);
				dialog1.show();

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						dialog1.dismiss();
						Toaster.toast("Parkovac� l�stek �sp�n� vyti�t�n.", Toaster.SHORT);
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
	 * Poslucha� stisknut� tla��tka editace PL
	 * 
	 * @param button
	 *            Stisknut� tla��tko
	 */
	public void editTicketClick(View button) {
		startActivityForResult(new Intent(this, TicketLoginActivity.class), TICKET_LOGIN);
	}

	/**
	 * Poslucha� stisknut� tla��tka odstran�n� l�stku
	 * 
	 * @param button
	 *            Stisknut� tla��tko
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

		// Navrat z Ticket Login po uspesnem dokonceni prihlaseni
		if (requestCode == TICKET_LOGIN && resultCode == RESULT_OK) {
			this.startActivityForResult(new Intent(this, TicketEditActivity.class).putExtra("Ticket", ticketIndex), EDIT);
		}
		// Navrat z EditActivity uspesnem po dokonceni editace. Refreshne data na obrazovce
		if (requestCode == EDIT && resultCode == RESULT_OK) {
			Ticket ticket = app.getTicketDao().getTicket(ticketIndex);
			// ovšří, zda se něo vrátilo a zavolá metodu pro naplnění widgetů daty
			if (ticket != null) {
				setTicket(ticket);
			}
		}
	}

	/**
	 * V p��pad�, �e je l�stek ji� vyti�t�n, znemo�n� dal�� editaci a maz�n�
	 * l�stku t�m, �e skryje ovl�d�n�.
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (showedTicket != null && showedTicket.isPrinted()) {
			// Zablokuje tla��tka editace a tisku parkovac�ho l�stkum pokud byl u� vyti�t�n
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
