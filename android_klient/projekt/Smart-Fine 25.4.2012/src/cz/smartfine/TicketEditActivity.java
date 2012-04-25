package cz.smartfine;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import model.FrequentValue;
import model.Law;
import model.Settings;
import model.Ticket;
import model.util.Image;
import model.util.Internet;
import model.util.MyLocation;
import model.util.TicketSetter;
import model.util.Toaster;
import model.util.MyLocation.LocationResult;
import cz.smartfine.R;
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
 * Tøída aktivity vytváøení nového PL, nebo editace stávajícího PL
 * 
 * @author Martin Štajner
 * 
 */
public class TicketEditActivity extends Activity {

	/**
	 * Globální hodnota reprezentující Vyfocení fotografie
	 */
	private static final int TAKE_PICTURE = 0;
	/**
	 * Globální hodnota reprezentující vybrani fotografie z karty
	 */
	private static final int BROWSE_PICTURE = 1;
	/**
	 * Instance aplikace
	 */
	private MyApp app;
	/**
	 * Tento vyvolaný intent - this
	 */
	private Intent intent;
	/**
	 * PL urèený k editaci
	 */
	private Ticket ticketForEdit;
	/**
	 * List obsahující cesty k fotografiím daného PL
	 */
	private ArrayList<File> photos;
	/**
	 * Aktuální fotografie
	 */
	private File photo;
	/**
	 * Objekt pro ziskani polohy
	 */
	private MyLocation loc;

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

	/*
	 * (non-Javadoc)
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
		intent = this.getIntent();
		if (intent.hasExtra("Ticket")) {
			// získá index do DAO na lístek, který s má zobrazit, pøedaný ze spouštìjící aktivity
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

		// Nastavi Mesto ze settings
		EditText cityEditText = (EditText) findViewById(R.id.city);
		if (cityEditText.getText().toString().equals("")) {
			cityEditText.setText(Settings.getInstance().getCity(getApplicationContext()));
		}
	}

	/**
	 * Obsluha tlaèítka - Rozpoznání SPZ
	 * 
	 * @param button
	 */
	public void scanClick(View button) {
		// TODO Dummy!!
		EditText spz = (EditText) findViewById(R.id.spz);
		spz.setText("3U1-3491");
	}

	/**
	 * Obsluha tlaèítka - Pøidat fotodokumentaci *
	 * 
	 * @param button
	 */
	public void photoDocumentationClick(View button) {
		photoDocListener.onClick(null);
	}

	/**
	 * Obsluha tlaèítka - Vyplnìní adresy dle GPS
	 * 
	 * @param button
	 */
	public void gpsClick(View button) {

		if (Internet.isOnline(getApplicationContext())) {

			// Prirazeni location Manageru
			loc = new MyLocation(getApplicationContext());
			if (!loc.checkProviders()) {
				//Kdyz neni aktivni ani jeden provider
				// TODO dat do R.String a soupnout to nekam kam to patri
				Toaster.toast("Neni ani jeden provider.", Toaster.LONG);
			} else {
				// KDyz je aktivni alespon jeden provider
				loc.getLocation(locationResult);
			}
		} else {
			// TODO napsat do R.String
			Toaster.toast("Pro automatické urèení adresy je potøeba internetové pøipojení.", Toaster.LONG);
		}
	}

	private LocationResult locationResult = new LocationResult() {
		@Override
		public void getLocationAddress(Address address) {
			if (address != null) {
				EditText city = (EditText) findViewById(R.id.city);
				city.setText(address.getLocality());
				EditText street = (EditText) findViewById(R.id.street);
				street.setText(address.getThoroughfare());
				EditText number = (EditText) findViewById(R.id.number);
				number.setText(address.getSubThoroughfare());
			} else {
				// TODO napsat do R.String
				Toaster.toast("Nepodaøilo se získat polohu.", Toaster.LONG);
			}
		}
	};

	// ------------------------------- INICIALIZACE ---------------------------------------//

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.editmenu, menu);
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
	 * Nastaví hodnoty do spinneru a posluchace
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
					if (!app.getFrequentValuesDAO().getFav_mpzValues().isEmpty()) {
						list = app.getFrequentValuesDAO().getFav_mpzValues();
					} else {
						list = app.getFrequentValuesDAO().getMpzValues();
					}
					break;
				case 1 :
					list = app.getFrequentValuesDAO().getSpzColorValues();
					break;
				case 2 :
					list = app.getFrequentValuesDAO().getVehicleTypeValues();
					break;
				case 3 :
					if (!app.getFrequentValuesDAO().getFav_vehicleBrandValues().isEmpty()) {
						list = app.getFrequentValuesDAO().getFav_vehicleBrandValues();
					} else {
						list = app.getFrequentValuesDAO().getVehicleBrandValues();
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

			// Nastavi jeste Law spinner
			setLawListener();
		}
	}

	/**
	 * Nastaví posluchaè na spinner pro zákony
	 */
	private void setLawListener() {
		final Spinner spinner = (Spinner) findViewById(R.id.lawSpinner);
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
						Law law = app.getLaws().get(position - 1);

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
	 * Nastaví posluchaè na vybraný spinner
	 * 
	 * @param spinnerID
	 * @param txt
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

	// ------------------------------- PARKOVACI LISTEK -----------------------------------//

	/**
	 * Obsluha tlaèítka - Uložit lístek
	 * 
	 * @param button
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
	 * Vytvoøení nového PL a pøiøazení jeho atributù NEBO zmìna údajù PL, který
	 * editujeme
	 * 
	 * @return PL
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

			// TODO prozatimni random cislo odznaku, pak do settings asi
			ticket.setBadgeNumber(3403234);
		}

		// Pokud je formularik stringu prazdny, vyplni se automaticky hodnota
		// "";
		ticket.setSpz(((EditText) this.findViewById(R.id.spz)).getText().toString());
		ticket.setMpz(((EditText) findViewById(R.id.mpz)).getText().toString());
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
	 * Kontrola všech povinných údajù a vrácení varování
	 * 
	 * @param ticket
	 * @return chybové hlášení
	 */
	private String checkTicket(Ticket ticket) {
		String error = "";
		if (ticket.getSpz().equals("")) {
			error += getString(R.string.val_ticket_err_spz);
		}
		if (ticket.getMpz().equals("")) {
			error += getString(R.string.val_ticket_err_mpz);
		}
		if (ticket.getVehicleType().equals("")) {
			error += getString(R.string.val_ticket_err_vehicleType);
		}
		if (ticket.getVehicleBrand().equals("")) {
			error += getString(R.string.val_ticket_err_vehicleBrand);
		}
		if (ticket.getCity().equals("")) {
			error += getString(R.string.val_ticket_err_city);
		}
		if (ticket.getStreet().equals("")) {
			error += getString(R.string.val_ticket_err_street);
		}
		if (ticket.getNumber() == 0) {
			error += getString(R.string.val_ticket_err_number);
		}

		if (ticket.getLaw().getLawNumber() == 0) {
			error += getString(R.string.val_ticket_err_lawNumber);
		}
		if (ticket.getLaw().getCollection() == 0) {
			error += getString(R.string.val_ticket_err_collection);
		}

		if (error.length() != 0) {
			return error;
		} else {
			return null;
		}
	}

	// ------------------------------- FOTODOKUMENTACE ------------------------------------//
	// TODO: cast toho presunout do TicketSetter - duplikace kodu s TicketDetail

	/**
	 * Nastaví fotodokumentaci - zobrazí náhledy fotografií a pøiøadí jim
	 * posluchaèe na zobrazení vìtšího náhledu Nastaví pøidávání nových
	 * fotografií
	 */
	private void setPhotoDocumentation() {
		// Zkontroluje, zda nahodou neni photos null
		if (photos == null) {
			photos = new ArrayList<File>();
		}

		// TODO Nastavovat nejak lepe velikosti, nez pres testParams, to je
		// neohrabane. Ale jako wtf.., kdyz neexistuje "setWidth" a "setHeight"
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
					// TODO dodelat dialog tak, aby nebyly kolem obrazku
					// mezery.. custom dialog?
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
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

	/*
	 * (non-Javadoc)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putSerializable("photos", photos);
		savedInstanceState.putSerializable("photo", photo);
		super.onSaveInstanceState(savedInstanceState);
	}
}
