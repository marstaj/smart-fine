package cz.smartfine.android;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import cz.smartfine.android.R;
import cz.smartfine.android.dao.FileFreqValuesDAO;
import cz.smartfine.android.dao.FileLawDAO;
import cz.smartfine.android.model.FrequentValue;
import cz.smartfine.android.model.Law;
import cz.smartfine.android.model.PrefManager;
import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.model.util.Image;
import cz.smartfine.android.model.util.Phone;
import cz.smartfine.android.model.util.MyLocation;
import cz.smartfine.android.model.util.TicketSetter;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.model.util.MyLocation.LocationResult;
import cz.smartfine.android.networklayer.business.LoginProvider;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Třída představující objekt typu Activity, který slouží k vytvoření nového,
 * nebo úpravě stávajícího parkovacího lístku.
 * 
 * @author Martin Štajner
 * 
 */
public class TicketEditActivity extends Activity {

	/**
	 * Globální hodnota reprezentující vyfocení fotografie
	 */
	private static final int TAKE_PICTURE = 0;

	/**
	 * Globální hodnota reprezentující vybrání fotografie z galerie
	 */
	private static final int BROWSE_PICTURE = 1;

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/**
	 * Parkovací lístek určený k editaci
	 */
	private Ticket ticketForEdit;

	/**
	 * Sezam cest k fotografiím daného parkovacího lístku.
	 */
	private ArrayList<File> photos;

	/**
	 * Aktuální fotografie
	 */
	private File photo;

	/**
	 * Objekt pro získání aktuální adresy
	 */
	private MyLocation loc;

	/**
	 * Posluchač stisknutí tlačítka na přidání nové fotodokumentace
	 */
	private OnClickListener photoDocListener = new OnClickListener() {

		// Po kliknuti na tlacitko se objevi dialog s nabidkou
		public void onClick(View arg0) {
			final String[] items = {getText(R.string.view_ticket_dialogMenu_takeNewPicture).toString(), getText(R.string.view_ticket_dialogMenu_browsePicture).toString()};
			Builder builder = new Builder(TicketEditActivity.this);
			builder.setTitle(getText(R.string.view_ticket_dialogMenu_title).toString());
			builder.setItems(items, new DialogInterface.OnClickListener() {

				// Po kliku v dialogu se spusti prislusna aktivita
				public void onClick(DialogInterface dialog, int item) {

					// Volba 1 - Prime foceni
					if (item == 0) {
						Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
						photo = app.getPhotoDAO().newPhoto();
						i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
						startActivityForResult(i, TAKE_PICTURE);
					}
					// Volba 2 - vyber ze souboru
					if (item == 1) {
						Intent i = new Intent(Intent.ACTION_PICK);
						i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, BROWSE_PICTURE);
					}
				}

			});
			// Spusti dialog
			builder.show();
		}

	};

	/**
	 * Výsledek pokusu o nalezení aktuální adresy
	 */
	private LocationResult locationResult = new LocationResult() {
		@Override
		public void gotLocationAddress(Address address) {
			if (address != null) {
				// Vyplní formuláře
				EditText city = (EditText) findViewById(R.id.city);
				city.setText(address.getLocality());
				EditText street = (EditText) findViewById(R.id.street);
				street.setText(address.getThoroughfare());
				EditText number = (EditText) findViewById(R.id.number);
				number.setText(address.getSubThoroughfare());
			} else {
				Toaster.toast(R.string.gps_location_failure, Toaster.LONG);
			}
		}
	};

	// ==================================== INITIALIZATON ==================================== //

	/**
	 * Metoda inicializuje objekt TicketEditActivity a je volána při jeho
	 * vytváření
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket_edit);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		// Zjisteni zda se jedna o editaci ci nikoliv, a pripadne vyplneni
		// hodnot ve formulari
		Intent intent = this.getIntent();
		if (intent.hasExtra("Ticket")) {
			// získá index do DAO na lístek, který s má zobrazit, předaný ze spouštějící aktivity
			int ticketIndex = intent.getExtras().getInt("Ticket");

			ticketForEdit = app.getTicketDao().getTicket(ticketIndex);

			if (ticketForEdit != null) {
				TicketSetter.setTicketBasic(this, ticketForEdit);
			}

			// Nastavi fotografie pro zobrazeni z PL pro editaci
			photos = ticketForEdit.getPhotos();
		}

		if (savedInstanceState != null) {
			photos = (ArrayList<File>) savedInstanceState.get("photos");
			photo = (File) savedInstanceState.get("photo");
		}

		// Nastavi fotodokumentaci
		setPhotoDocumentation();

		// Nastavi Listenery na spinnery a obsah spinneru
		setSpinners();

		// Nastavi Mesto ze prefManager
		EditText cityEditText = (EditText) findViewById(R.id.city);
		if (cityEditText.getText().toString().equals("")) {
			cityEditText.setText(PrefManager.getInstance().getCity(getApplicationContext()));
		}
	}

	// ==================================== BUTTONS ==================================== //

	/**
	 * Obsluha tlačítka přidání fotodokumentace
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void photoDocumentationClick(View button) {
		photoDocListener.onClick(null);
	}

	/**
	 * Obsluha tlačítka automatického vyplnění adresy
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void gpsClick(View button) {

		// Kontrola pripojeni k internetu
		if (Phone.isConnectedToInternet(getApplicationContext())) {

			try {
				if (loc == null) {
					loc = new MyLocation(this);
				}
				loc.getLocation(locationResult);
			} catch (Exception e) {
				Toaster.toast(R.string.provider_missing, Toaster.LONG);
			}
		} else {
			Toaster.toast(R.string.internet_need_gpsaddress, Toaster.LONG);
		}
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
		inflater.inflate(R.menu.editmenu, menu);
		return true;
	}

	/**
	 * Metoda zavolá akci po návratu z Aktivity, která byla spuštěna z
	 * TicketEditActivity
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.fotodocumentationMenu : {
				photoDocListener.onClick(null);
				return true;
			}
			case R.id.saveTicketMenu : {
				saveTicketClick(null);
				return true;
			}
			case R.id.scanLocationMenu : {
				this.gpsClick(null);
				return true;
			}
			default :
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Obsluha tlačítka uložení parkovacího lístku
	 * 
	 * @param button
	 *            Stisknuté tlačítko
	 */
	public void saveTicketClick(View button) {
		Ticket ticket = setTicketData();
		String error = checkTicket(ticket);
		if (error != null) {
			Toaster.toast(error, Toaster.LONG);
		} else {
			try {
				// Ulozi PL
				app.getTicketDao().saveTicket(ticket);
				app.getTicketDao().saveAllTickets();
				Toaster.toast(R.string.val_ticket_success, Toaster.SHORT);
				// Nastavi result pro pripad editace, aby se mohl detail listku
				// refreshnout
				setResult(Activity.RESULT_OK, new Intent().putExtra("Ticket", app.getTicketDao().getAllTickets().indexOf(ticket)));
				finish();
			} catch (Exception e) {
				// Odstraneni posledniho pridaneho listku z arraylistu v pripade
				// vytvareni noveho listku, nikoliv editace
				if (ticketForEdit == null) {
					app.getTicketDao().deleteTicket(ticket);
				}
				Toaster.toast(R.string.val_ticket_failure, Toaster.LONG);
			}
		}
	}

	/**
	 * Metoda zavolá akci po návratu z Aktivity, která byla spuštěna z
	 * TicketEditActivity
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 *      android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Navrat po uspesnem vyfoceni fotografie
		if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
			photos.add(photo);
			photo = null;
		}

		// Navrat po uspesnem vybrani fotografie
		if (requestCode == BROWSE_PICTURE && resultCode == RESULT_OK) {

			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
			cursor.moveToFirst();
			String path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
			cursor.close();

			photos.add(new File(path));
			photo = null;
		}

		// Refreshne fotodokumentaci
		setPhotoDocumentation();
	}

	/**
	 * Metoda odstraní referenci na objekt pro získání polohy. Metoda je volána
	 * při ukončování aktivity.
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		if (loc != null) {
			loc.cancel();
		}
	}

	/**
	 * Metoda uloží data do bundlu při restartu aktivity
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putSerializable("photos", photos);
		savedInstanceState.putSerializable("photo", photo);
		super.onSaveInstanceState(savedInstanceState);
	}

	// ==================================== OTHER ==================================== //

	/**
	 * Metoda nastaví hodnoty do spinneru a zavolá nastavení příslušných
	 * posluchačů.
	 */
	public void setSpinners() {
		int ids[][] = {{R.id.mpzSpinner, R.id.mpz}, {R.id.spzColorSpinner, R.id.spzColor}, {R.id.vehicleTypeSpinner, R.id.vehicleType}, {R.id.vehicleBrandSpinner, R.id.vehicleBrand}};

		// Nastavi vsechny jednoduche spinnery
		for (int i = 0; i < ids.length; i++) {
			Spinner spinner = (Spinner) findViewById(ids[i][0]);

			ArrayList<FrequentValue> list = new ArrayList<FrequentValue>();

			// Ziska napln do adapteru. V pripade MPZ a VehicleBrand zkusi nejdrive oblibene hodnoty
			switch (i) {
				case 0 :
					if (!((FileFreqValuesDAO) app.getFreqValuesDAO()).getFav_mpzValues().isEmpty()) {
						list = ((FileFreqValuesDAO) app.getFreqValuesDAO()).getFav_mpzValues();
					} else {
						list = ((FileFreqValuesDAO) app.getFreqValuesDAO()).getMpzValues();
					}
					break;
				case 1 :
					list = ((FileFreqValuesDAO) app.getFreqValuesDAO()).getSpzColorValues();
					break;
				case 2 :
					list = ((FileFreqValuesDAO) app.getFreqValuesDAO()).getVehicleTypeValues();
					break;
				case 3 :
					if (!((FileFreqValuesDAO) app.getFreqValuesDAO()).getFav_vehicleBrandValues().isEmpty()) {
						list = ((FileFreqValuesDAO) app.getFreqValuesDAO()).getFav_vehicleBrandValues();
					} else {
						list = ((FileFreqValuesDAO) app.getFreqValuesDAO()).getVehicleBrandValues();
					}
					break;
			}

			// Nastavime spinner adapter
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
			// Vlozime jeste volbu - Vlastni - hned na zacatek listu
			adapter.add((String) this.getText(R.string.own_spinner));
			// Pak vlozime zbytek z listu cyklem
			for (int j = 0; j < list.size(); j++) {
				adapter.add(list.get(j).getValue());
			}
			adapter.setDropDownViewResource(R.layout.simple_spinner_list_item);
			spinner.setAdapter(adapter);

			setSimpleSpinnerListener(spinner, ids[i][1]);
		}

		// Nastavi jeste Law spinner
		setLawListener();
	}

	/**
	 * Metoda nastaví posluchače na spinner pro zákony.
	 */
	private void setLawListener() {
		final Spinner spinner = (Spinner) findViewById(R.id.lawSpinner);

		ArrayList<Law> list = ((FileLawDAO) app.getLawDAO()).getLaws();
		// Nastavime spinner adapter
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		// Vlozime jeste volbu - Vlastni - hned na zacatek listu
		adapter.add((String) this.getText(R.string.own_spinner));
		// Pak vlozime zbytek z listu cyklem
		for (int j = 0; j < list.size(); j++) {
			adapter.add(list.get(j).getEventDescription());
		}
		adapter.setDropDownViewResource(R.layout.simple_spinner_list_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			/**
			 * Zda se jedna o prvotni inicializaci, nebo akci uzivatele (spinner
			 * se totiz zavola jeste pred tim, nez uzivatel staci cokoliv
			 * udelat)
			 */
			boolean init = true;

			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (!init) {

					String text = spinner.getSelectedItem().toString();

					EditText ruleOfLaw = (EditText) findViewById(R.id.ruleOfLaw);
					EditText paragraph = (EditText) findViewById(R.id.paragraph);
					EditText letter = (EditText) findViewById(R.id.letter);
					EditText descriptionDZ = (EditText) findViewById(R.id.descriptionDZ);
					EditText eventDescription = (EditText) findViewById(R.id.eventDescription);
					EditText lawNumber = (EditText) findViewById(R.id.lawNumber);
					EditText collection = (EditText) findViewById(R.id.collection);

					ruleOfLaw.setText("");
					paragraph.setText("");
					letter.setText("");
					descriptionDZ.setText("");
					eventDescription.setText("");
					lawNumber.setText("");
					collection.setText("");

					// Kdyz je vybrano "Vlastni" tak vyprazni pole a odblokuje
					// je aby mohla byt editovalna
					if (text.equals(getString(R.string.own_spinner))) {
						ruleOfLaw.setEnabled(true);
						paragraph.setEnabled(true);
						letter.setEnabled(true);
						descriptionDZ.setEnabled(true);
						eventDescription.setEnabled(true);
						lawNumber.setEnabled(true);
						collection.setEnabled(true);
					} else {
						// Pokud je vybrana polozka, formulare se zablokuji a
						// vyplni daty automaticky
						Law law = ((FileLawDAO) app.getLawDAO()).getLaws().get(position - 1);

						ruleOfLaw.setEnabled(false);
						if (law.getRuleOfLaw() != 0) {
							ruleOfLaw.setText(String.valueOf(law.getRuleOfLaw()));
						}
						paragraph.setEnabled(false);
						if (law.getParagraph() != 0) {
							paragraph.setText(String.valueOf(law.getParagraph()));
						}
						lawNumber.setEnabled(false);
						if (law.getLawNumber() != 0) {
							lawNumber.setText(String.valueOf(law.getLawNumber()));
						}
						collection.setEnabled(false);
						if (law.getCollection() != 0) {
							collection.setText(String.valueOf(law.getCollection()));
						}

						letter.setEnabled(false);
						letter.setText(law.getLetter());
						descriptionDZ.setEnabled(false);
						descriptionDZ.setText(law.getDescriptionDZ());
						eventDescription.setEnabled(false);
						eventDescription.setText(law.getEventDescription());
					}

				} else {
					init = false;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	/**
	 * Metoda nastaví posluchače na vybraný spinner;
	 * 
	 * @param spinner
	 *            Listener, na který se nastavuje posluchač
	 * @param txt
	 *            ID EditText
	 */
	private void setSimpleSpinnerListener(final Spinner spinner, final int txt) {

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			/**
			 * Zda se jedna o prvotni inicializaci, nebo akci uzivatele (spinner
			 * se totiz zavola jeste pred tim, nez uzivatel staci cokoliv
			 * udelat)
			 */
			boolean init = true;

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (!init) {
					String text = spinner.getSelectedItem().toString();
					EditText editText = (EditText) findViewById(txt);
					// Kdyz je vybrano "Vlastni" tak vyprazni pole a odblokuje
					// ho aby mohlo byt editovalne
					if (text.equals(getString(R.string.own_spinner))) {
						text = "";
						editText.setEnabled(true);
					} else {
						// KDyz se jedna o automobilovou znacku, necha se pole i na dale editovaletne, jinak se zablokuje
						if (txt != R.id.vehicleBrand) {
							editText.setEnabled(false);
						}
					}
					editText.setText(text);

				} else {
					init = false;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	/**
	 * Metoda vytvoří nový parkovací lístek a přiřadí mu atributy, nebo změní
	 * údaje, které upravjeme při úpravě již existujícího parkovacího lístku.
	 * 
	 * @return Parkovací lístek
	 */
	private Ticket setTicketData() {
		Ticket ticket;
		// Pokud editujeme PL, tak zmenime atributy, pokud vyplnujeme novy PL,
		// vytvorime novy PL
		if (ticketForEdit != null) {
			ticket = ticketForEdit;
		} else {
			ticket = new Ticket();
			ticket.setLaw(new Law());
			ticket.setDate(new Date());

			// Nastaveni cisla odznaku
			ticket.setBadgeNumber(LoginProvider.getBadgeNumber(this));

		}

		// Pokud je formularik stringu prazdny, vyplni se automaticky hodnota
		// "";
		ticket.setSpz(((EditText) this.findViewById(R.id.spz)).getText().toString());

		String mpz = ((EditText) findViewById(R.id.mpz)).getText().toString();
		String[] ar = mpz.split(" ");
		ticket.setMpz(ar[0]);

		ticket.setSpzColor(((EditText) findViewById(R.id.spzColor)).getText().toString());
		ticket.setVehicleType(((EditText) findViewById(R.id.vehicleType)).getText().toString());
		ticket.setVehicleBrand(((EditText) findViewById(R.id.vehicleBrand)).getText().toString());
		ticket.setCity(((EditText) findViewById(R.id.city)).getText().toString());
		ticket.setStreet(((EditText) findViewById(R.id.street)).getText().toString());

		try {
			ticket.setNumber(Integer.valueOf(((EditText) findViewById(R.id.number)).getText().toString()));
		} catch (Exception e) {
			ticket.setNumber(0);
		}

		ticket.setLocation(((EditText) findViewById(R.id.location)).getText().toString());
		ticket.setTow(((CheckBox) findViewById(R.id.tow)).isChecked());
		ticket.setMoveableDZ(((CheckBox) findViewById(R.id.moveableDZ)).isChecked());

		// Nastavi law, pokud je policko prazdne, vyhodi to vyjimku a zustane tam defaultne 0 (coz je jako null u int)
		try {
			ticket.getLaw().setRuleOfLaw(Integer.valueOf(((EditText) findViewById(R.id.ruleOfLaw)).getText().toString()));
		} catch (Exception e) {
			ticket.getLaw().setRuleOfLaw(0);
		}
		try {
			ticket.getLaw().setParagraph(Integer.valueOf(((EditText) findViewById(R.id.paragraph)).getText().toString()));
		} catch (Exception e) {
			ticket.getLaw().setParagraph(0);
		}

		ticket.getLaw().setLetter(((EditText) findViewById(R.id.letter)).getText().toString());
		ticket.getLaw().setDescriptionDZ(((EditText) findViewById(R.id.descriptionDZ)).getText().toString());
		ticket.getLaw().setEventDescription(((EditText) findViewById(R.id.eventDescription)).getText().toString());

		try {
			ticket.getLaw().setLawNumber(Integer.valueOf(((EditText) findViewById(R.id.lawNumber)).getText().toString()));
		} catch (Exception e) {
			ticket.getLaw().setLawNumber(0);
		}
		try {
			ticket.getLaw().setCollection(Integer.valueOf(((EditText) findViewById(R.id.collection)).getText().toString()));
		} catch (Exception e) {
			ticket.getLaw().setCollection(0);
		}

		ticket.setPhotos(photos);

		return ticket;
	}

	/**
	 * Metoda kontroluje všechny povinné údaje a v případě chyby vrátí varování.
	 * 
	 * @param ticket
	 *            Parkovací lístek
	 * @return Chybové hlášení
	 */
	private String checkTicket(Ticket ticket) {
		String error = "";
		if (ticket.getSpz().length() < 5 || ticket.getSpz().length() > 10) {
			error += getString(R.string.val_ticket_err_spz) + "\n";
		}
		if (ticket.getMpz().length() < 1 || ticket.getMpz().length() > 3) {
			error += getString(R.string.val_ticket_err_mpz) + "\n";
		}
		if (ticket.getVehicleType().length() < 1 || ticket.getVehicleType().length() > 50) {
			error += getString(R.string.val_ticket_err_vehicleType) + "\n";
		}
		if (ticket.getVehicleBrand().length() < 1 || ticket.getVehicleBrand().length() > 50) {
			error += getString(R.string.val_ticket_err_vehicleBrand) + "\n";
		}
		if (ticket.getCity().length() < 1 || ticket.getCity().length() > 50) {
			error += getString(R.string.val_ticket_err_city) + "\n";
		}
		if (ticket.getStreet().length() < 1 || ticket.getStreet().length() > 255) {
			error += getString(R.string.val_ticket_err_street) + "\n";
		}
		if (ticket.getNumber() == 0) {
			error += getString(R.string.val_ticket_err_number) + "\n";
		}

		if (ticket.getLaw().getLawNumber() == 0) {
			error += getString(R.string.val_ticket_err_lawNumber) + "\n";
		}
		if (ticket.getLaw().getCollection() == 0) {
			error += getString(R.string.val_ticket_err_collection) + "\n";
		}

		if (error.length() != 0) {
			return error.substring(0, error.length() - 2);
		} else {
			return null;
		}
	}

	/**
	 * Metoda nastaví fotodokumentaci, zobrazí náhledy fotografií a přiřadí jim
	 * posluchače na zobrazení většího náhledu. Metoda také nastaví přidávání
	 * nových fotografií.
	 */
	private void setPhotoDocumentation() {
		// Zkontroluje, zda nahodou neni photos null
		if (photos == null) {
			photos = new ArrayList<File>();
		}

		// TODO Nastavovat lepe velikosti, nez pres testParams
		// to je neohrabane. Ale jako wtf.., kdyz neexistuje "setWidth" a "setHeight"
		// ani u "imagebutton", ani u "imageview" a u "button" ano..

		// Layout Parametry (velikost a marginy) pro tlacitko a obrazky ze xml
		// (xml obsahuje spravne nastavene tlacitko, ktere je skryte)
		LayoutParams testParams = ((ImageButton) findViewById(R.id.testButton)).getLayoutParams();

		// Do tohoto layoutu se budou pridavat nove layouty s obrazky a
		// tlacitkem. Pri kazdem zavolani se vyprazdni a naplni znova - refresh
		LinearLayout photosLayout = (LinearLayout) findViewById(R.id.photoDocumentationLayout);
		photosLayout.removeAllViews();

		// Jedotlive layouty do kterych se budou pridavat obrazky a tlacitko
		LinearLayout actualLayout = new LinearLayout(getApplicationContext());

		// Podle toho, kolik je ve photos fotek, tolik se vytvori imageView
		int i;
		for (i = 0; i < photos.size(); i++) {
			// Pri 2ti fotografiich se odradkuje
			if (i % 2 == 0) {
				actualLayout = new LinearLayout(getApplicationContext());
				photosLayout.addView(actualLayout);
			}
			ImageView imageView = new ImageView(getApplicationContext());
			imageView.setId(i);
			imageView.setOnClickListener(new OnClickListener() {

				// Po kliku na nahled pridate aktivity se pusti dialog s vetsim
				// nahledem obrazku
				public void onClick(View v) {

					// TODO Vytvorit lepší zobrazení náhledu obrázků
					Dialog dialog = new Dialog(TicketEditActivity.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.ticket_photo_dialog);
					ImageView image = (ImageView) dialog.findViewById(R.id.photoView);
					BitmapDrawable drawable = (BitmapDrawable) ((ImageView) v).getDrawable();
					image.setImageDrawable(drawable);
					// Spusti dialog
					dialog.show();
				}

			});
			imageView.setLayoutParams(testParams);
			// Zkusi nastavit obrazek
			try {
				Bitmap bitmap = Image.decodeFile(photos.get(i), 500);
				imageView.setImageBitmap(bitmap);
				actualLayout.addView(imageView);
			} catch (Exception e) {
				Toaster.toast(R.string.val_ticket_fotoDocumentation_failLoad, Toaster.SHORT);
				photos.remove(photos.get(i));
			}

		}

		// Jeste pridame tlacitko na pridani novych fotek. Pri 6ti fotografiich
		// se odradkuje
		if (i % 2 == 0) {
			actualLayout = new LinearLayout(getApplicationContext());
			photosLayout.addView(actualLayout);
		}
		ImageButton imageButton = new ImageButton(getApplicationContext());
		imageButton.setImageResource(android.R.drawable.ic_input_add);
		imageButton.setBackgroundResource(android.R.drawable.editbox_background_normal);
		imageButton.setLayoutParams(testParams);
		// Nastavi tlacitku listener
		imageButton.setOnClickListener(photoDocListener);
		// Prida tlacitko do layoutu
		actualLayout.addView(imageButton);
	}

}
