package cz.smartfine.android;

import cz.smartfine.android.R;
import cz.smartfine.android.model.PrefManager;
import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.model.util.Phone;
import cz.smartfine.android.model.util.TicketSetter;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.printlayer.PrintHelper;
import cz.smartfine.android.printlayer.StarT300BTPrinter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Třída představující objekt typu Activity, který slouží k zobrazení všech
 * informací obsažených v parkovacím lístku.
 * 
 * @author Martin Štajner
 */
public class TicketDetailActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Index zobrazeného parkovacího lístku
	 */
	private int ticketIndex;

	/**
	 * Globální hodnota reprezentující úpravu parkovacího lístku
	 */
	private static final int EDIT = 0;

	/**
	 * Globální hodnota reprezentující tisk parkovacího lístku
	 */
	private static final int TICKET_LOGIN = 1;

	/**
	 * Globální hodnota reprezentující tisk parkovacího lístku
	 */
	private static final int BLUETOOTH = 2;

	/**
	 * Zobrazený parkovací lístek
	 */
	private Ticket showedTicket;

	/**
	 * Dialog, který se zobrazí během komunikace s tiskárnou
	 */
	private ProgressDialog dialog;

	/**
	 * Instance pomocného objektu pro tisk parkovacích lístků
	 */
	private PrintHelper helper;

	/**
	 * Kontext aktivity
	 */
	private Context appContext = this;

	// ==================================== INITIALIZATON ==================================== //

	/**
	 * Metoda inicializuje objekt TicketDetailActivity a je volána při jeho
	 * spuštění.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket_detail);

		// Přiřazení instance aplikace
		app = (MyApp) this.getApplication();

		// zjisteni indexu parkovaciho listku
		ticketIndex = this.getIntent().getExtras().getInt("Ticket");

		// nastaveni zobrazeneho parkovaciho listku
		showedTicket = app.getTicketDao().getTicket(ticketIndex);

		// overi, zda parkovaci listek existuje a zavola metodu pro naplneni widgetu daty
		if (showedTicket != null) {
			setTicket(showedTicket);

			manageControls(showedTicket);
		}

		helper = new PrintHelper(this, new StarT300BTPrinter(getApplicationContext()));
	}

	// ==================================== BUTTONS ==================================== //

	/**
	 * Posluchač stisknutí tlačítka tiskparkovacího lístku
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void printTicketClick(final View button) {

		// Dialog potvrzení vytisknutí lístku
		Builder builder = new Builder(this);
		builder.setMessage(getText(R.string.view_alert_print));
		builder.setCancelable(false);
		builder.setPositiveButton(getText(R.string.yes), new OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {

				// kontrola zda telefon ma bluetooth a zda je zapnuty
				if (Phone.isBluetoothSupported()) {

					// Kontrola, zda je bluetooth povolen
					checkBluetoothEnabled();
				} else {
					// do R.
					Toaster.toast("Bluetooth není k dispozici. Parkovací lístek nelze vytisknout.", Toaster.SHORT);
				}
			}
		});

		builder.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	/**
	 * Posluchač stisknutí tlačítka editace PL
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void editTicketClick(View button) {

		// vytvoří Intent, vloží do něj index na zobrazení PL a spustí aktivitu
		// pro editaci tohoto PL
		this.startActivityForResult(new Intent(this, TicketEditActivity.class).putExtra("Ticket", ticketIndex), EDIT);

	}

	// ==================================== OVERRIDE METHODS ==================================== //

	/**
	 * Metoda zavolá akci po návratu z Aktivity, která byla spuštěna z
	 * TicketDetailActivity
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 *      android.content.Intent)
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

		// 
		if (requestCode == BLUETOOTH) {
			this.checkBluetoothEnabled();
		}
	}

	/**
	 * Metoda připraví menu
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.detialmenu, menu);
		return true;
	}

	/**
	 * Metoda v případě prázdného seznamu parkovacích lístků zablokuje tlačítka
	 * v menu sloužící k mazání a upravě parkovacího lístku.
	 * 
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (showedTicket != null && showedTicket.isPrinted()) {
			// Zablokuje tlačítka editace a tisku parkovacího lístkum pokud byl už vytištěn
			menu.getItem(0).setEnabled(false);
			menu.getItem(1).setEnabled(false);
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

	// ==================================== OTHER ==================================== //

	/**
	 * Naplní widgety daty z instance PL předané v parametru
	 * 
	 * @param ticket
	 */
	private void setTicket(Ticket ticket) {
		// naplní formulář editovatelnými daty
		TicketSetter.setTicketBasic(this, ticket);
		// naplní formulář needitovatelnými daty
		TicketSetter.setTicketExtra(this, ticket);
		// Nastavi fotodokumentaci
		TicketSetter.setTicketPhotoDocumentation(this, ticket.getPhotos());
	}

	/**
	 * Metoda kontroluje, zda je aktivni Bluetooth. Pokud ne, vyzve uživatele
	 * dialogem, aby ho zapnul.
	 */
	private void checkBluetoothEnabled() {

		if (Phone.isBluetoothEnabled()) {
			continutePrint();

		} else {
			Builder builder = new AlertDialog.Builder(TicketDetailActivity.this);
			builder.setMessage(getText(R.string.view_alert_bluetooth));
			builder.setCancelable(false);
			builder.setPositiveButton(getText(R.string.yes), new OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					startActivityForResult(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS), BLUETOOTH);
				}
			}).setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				}
			}).show();
		}
	}

	/**
	 * Metoda zobrazí dialog a zavolá tisk lístku.
	 */
	private void continutePrint() {
		// Nastavení dialogu a LogIN
		dialog = new ProgressDialog(appContext);
		dialog.setTitle(getText(R.string.view_dialog_print).toString());
		dialog.setMessage(getText(R.string.please_wait).toString());
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();

		helper.print(PrefManager.getInstance().getBTPrinterMacAddress(appContext), showedTicket);
	}

	/**
	 * Posluchač stisknutí tlačítka odstranění lístku
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void removeTicketClick(View button) {
		// Dialog potvrzení vytisknutí lístku
		Builder builder = new Builder(this);
		builder.setMessage(getText(R.string.view_alert_delete));
		builder.setCancelable(false);
		builder.setPositiveButton(getText(R.string.yes), new OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {

				app.getTicketDao().deleteTicket(showedTicket);
				try {
					// Ulozime listky do souboru
					app.getTicketDao().saveAllTickets();
					// Smazeme fotografie daneho listku
					app.getPhotoDAO().deleteAllPhotosFromTicket(showedTicket);
					setResult(RESULT_OK);
					finish();
				} catch (Exception e) {
					// V pripade, ze se nepodari ulozit listky do souboru, listek se zase prida do seznamu a zobrazi se chybova hlaska
					app.getTicketDao().saveTicket(showedTicket, ticketIndex);
					Toaster.toast(R.string.ticket_delete_failure, Toaster.SHORT);
				}

			}
		});

		builder.setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	/**
	 * Metoda v případě, že je lístek vytištěn, znemožní další úpravy a mazání
	 * parkovacího lístku a mazání skrytím ovládání.
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

	/**
	 * Metoda zruší dialog, případě změny parkovacího lístku ho uloží a
	 * restartuje aktivitu.
	 */
	public void onSuccessPrint() {
		if (dialog != null) {
			dialog.dismiss();
		}

		Toaster.toast(R.string.print_ticket_success , Toaster.SHORT);

		// Kdyz je listek tisknuty poprve, nastavi ze byl vytisknuty a reloadne aktivitu
		if (!showedTicket.isPrinted()) {
			showedTicket.setPrinted(true);

			// Reloadne aktivitu
			Intent intent = getIntent();
			finish();
			startActivity(intent);

			// ulozi parkovací listek
			try {
				((MyApp) this.getApplication()).getTicketDao().saveTicket(showedTicket);
			} catch (Exception e) {
				// TODO
			}
		}

	}

	/**
	 * Metoda zruší dialog a odblokuje otáčení obrazovky
	 */
	public void dismissDialog() {
		dialog.dismiss();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

}
