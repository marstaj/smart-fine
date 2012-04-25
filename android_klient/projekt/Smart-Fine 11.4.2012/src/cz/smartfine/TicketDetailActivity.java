package cz.smartfine;

import model.Ticket;
import model.util.TicketSetter;
import model.util.Toaster;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

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
	@SuppressWarnings("unused")
	private static final int PRINT = 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketdetail);

		app = (MyApp) this.getApplication();

		// z�sk� index do DAO na l�stek, kter� s m� zobrazit, p�edan� ze
		// spou�t�j�c� aktivity a ziska dany listek
		ticketIndex = this.getIntent().getExtras().getInt("Ticket");
		Ticket ticket = app.getTicketDao().getTicket(ticketIndex);

		// ov���, zda se n�o vr�tilo a zavol� metodu pro napln�n� widget� daty//
		if (ticket != null) {
			setTicket(ticket);

			// TODO Tohle zmenit pri implementaci tisku - ted je to jenom dummy
			// kvuli PDA
			if (ticket.isPrinted()) {
				Button edit = (Button) findViewById(R.id.editTicketButton);
				edit.setEnabled(false);
			}
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
	public void printTicket(final View button) {
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
						Button edit = (Button) findViewById(R.id.editTicketButton);
						edit.setEnabled(false);
						button.setEnabled(false);
						setTicket(ticket);

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
	public void editTicket(View button) {
		// vytvo�� Intent, vlo�� do n�j index na zobrazen� PL a spust� aktivitu
		// pro editaci tohoto PL
		this.startActivityForResult(new Intent(this, TicketEditActivity.class).putExtra("Ticket", ticketIndex), EDIT);
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
			// ov���, zda se n�o vr�tilo a zavol� metodu pro napln�n� widget�
			// daty//
			if (ticket != null) {
				setTicket(ticket);
			}
		}
	}

}
