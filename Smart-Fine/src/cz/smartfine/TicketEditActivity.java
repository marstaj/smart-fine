package cz.smartfine;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.Law;
import model.Settings;
import model.Ticket;
import model.util.Image;
import model.util.TicketSetter;
import model.util.Toaster;
import cz.smartfine.R;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * T��da aktivity vytv��en� nov�ho PL, nebo editace st�vaj�c�ho PL
 * @author Martin �tajner
 * 
 */
public class TicketEditActivity extends Activity {

	/**
	 * Glob�ln� hodnota reprezentuj�c� Vyfocen� fotografie
	 */
	private static final int TAKE_PICTURE = 0;
	/**
	 * Instance aplikace
	 */
	private MyApp app;
	/**
	 * Tento vyvolan� intent - this
	 */
	private Intent intent;
	/**
	 * PL ur�en� k editaci
	 */
	private Ticket ticketForEdit;	
	/**
	 * List obsahuj�c� cesty k fotografi�m dan�ho PL
	 */
	private ArrayList<File> photos;	
	/**
	 * Aktu�ln� fotografie
	 */
	private File photo;
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketedit);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych
		// aktivit
		app = (MyApp) this.getApplication();

		// Zjisteni zda se jedna o editaci ci nikoliv, a pripadne vyplneni
		// hodnot ve formulari
		intent = this.getIntent();
		if (intent.hasExtra("Ticket")) {
			// z�sk� index do DAO na l�stek, kter� s m� zobrazit, p�edan� ze
			// spou�t�j�c� aktivity
			int ticketIndex = intent.getExtras().getInt("Ticket");

			ticketForEdit = app.getTicketDao().getTicket(ticketIndex);

			if (ticketForEdit != null) {
				TicketSetter.setTicketBasic(this, ticketForEdit);
			}
			
			//Nastavi fotografie pro zobrazeni z PL pro editaci
			photos = ticketForEdit.getPhotos();
		} 
		
		if (savedInstanceState != null) {
            photos = (ArrayList<File>) savedInstanceState.get("photos");
            photo = (File) savedInstanceState.get("photo");
        }
		
		// Nastavi fotodokumentaci
		setPhotoDocumentation();		

		// Nastavi Listenery na spinnery
		setAllSpinnersListeners();
		
		// Nastavi Mesto ze settings
		EditText cityEditText = (EditText) findViewById(R.id.city);
		if (cityEditText.getText().toString().equals("")) {
			cityEditText.setText(Settings.getInstance().getCity(getApplicationContext()));
		}
	}
	
	/**
     * Obsluha tla��tka - Rozpozn�n� SPZ
     * @param button
     */
	public void scanClick(View button) {
		// TODO Dummy!!
		EditText spz = (EditText) findViewById(R.id.spz);
		spz.setText("3U1-3491");
	}
	
	/**
     * Obsluha tla��tka - Vypln�n� adresy dle GPS
     * @param button
     */
	public void gpsClick(View button) {
		// TODO Dummy!!
		EditText city = (EditText) findViewById(R.id.city);
		city.setText("Praha");
		EditText street = (EditText) findViewById(R.id.street);
		street.setText("Plze�sk�");
		EditText number = (EditText) findViewById(R.id.number);
		number.setText("41");
	}
	
	// ------------------------------- INICIALIZACE ---------------------------------------//

	/**
	 * Obsluha nastaveni poslucha�e pro spinnery
	 */
	private void setAllSpinnersListeners() {
		int ids[][] = { { R.id.mpzSpinner, R.id.mpz },
				{ R.id.spzColorSpinner, R.id.spzColor },
				{ R.id.vehicleTypeSpinner, R.id.vehicleType },
				{ R.id.vehicleBrandSpinner, R.id.vehicleBrand }};

		// Nastavi vsechny jednoduche spinnery
		for (int i = 0; i < ids.length; i++) {
			setSimpleSpinnerListener(ids[i][0], ids[i][1]);
		}		
		// Nastavi jeste Law spinner
		 setLawListener();
	}

	/**
	 * Nastav� poslucha� na spinner pro z�kony
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

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (!init) {
					
					String text = spinner.getSelectedItem().toString();
					
					EditText ruleOfLaw = (EditText) findViewById(R.id.ruleOfLaw);
					EditText paragraph = (EditText) findViewById(R.id.paragraph);
					EditText letter = (EditText) findViewById(R.id.letter);
					EditText lawNumber = (EditText) findViewById(R.id.lawNumber);
					EditText collection = (EditText) findViewById(R.id.collection);
					
					ruleOfLaw.setText("");
					paragraph.setText("");
					letter.setText("");
					lawNumber.setText("");
					collection.setText("");
					
					// Kdyz je vybrano "Vlastni" tak vyprazni pole a odblokuje je aby mohla byt editovalna
					if (text.equals(getString(R.string.own_spinner))) {
						ruleOfLaw.setEnabled(true);
						paragraph.setEnabled(true);
						letter.setEnabled(true);						
						lawNumber.setEnabled(true);						
						collection.setEnabled(true);
					} else {
						// Pokud je vybrana polozka, formulare se zablokuji a vyplni daty automaticky
						Law law = app.getLaws().get(position-1);
						
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
					}
					
				} else {
					init = false;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}
	
	/**
	 * Nastav� poslucha� na vybran� spinner
	 * 
	 * @param spin
	 * @param txt
	 */
	private void setSimpleSpinnerListener(int spin, final int txt) {

		final Spinner spinner = (Spinner) findViewById(spin);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			/**
			 * Zda se jedna o prvotni inicializaci, nebo akci uzivatele (spinner
			 * se totiz zavola jeste pred tim, nez uzivatel staci cokoliv
			 * udelat)
			 */
			boolean init = true;

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!init) {
					String text = spinner.getSelectedItem().toString();
					EditText editText = (EditText) findViewById(txt);
					// Kdyz je vybrano "Vlastni" tak vyprazni pole a odblokuje ho aby mohlo byt editovalne
					if (text.equals(getString(R.string.own_spinner))) {
						text = "";
						editText.setEnabled(true);
					} else {
						editText.setEnabled(false);
					}
					editText.setText(text);
					
				} else {
					init = false;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}

	
	// ------------------------------- PARKOVACI LISTEK -----------------------------------//
	
	/**
	 * Obsluha tla��tka - Ulo�it l�stek
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
				// Nastavi result pro pripad editace, aby se mohl detail listku refreshnout
				setResult(Activity.RESULT_OK, new Intent().putExtra("Ticket", app.getTicketDao().getAllTickets().indexOf(ticket)));
				finish();
			} catch (Exception e) {
				// Odstraneni posledniho pridaneho listku z arraylistu v pripade
				// vytvareni noveho listku, nikoliv editace
				if (ticketForEdit == null) {
					app.getTicketDao().deleteTicket(ticket);
				}
				Toaster.toast(R.string.val_ticket_failure, Toaster.SHORT);
			}
		}
	}
	
	/**
	 * Vytvo�en� nov�ho PL a p�i�azen� jeho atribut� NEBO zm�na �daj� PL, kter� editujeme
	 * 
	 * @return PL
	 */
	private Ticket setTicketData() {
		Ticket ticket;
		// Pokud editujeme PL, tak zmenime atributy, pokud vyplnujeme novy PL, vytvorime novy PL
		if (ticketForEdit != null) {
			ticket = ticketForEdit;
		} else {
			ticket = new Ticket();
			ticket.setLaw(new Law());
			ticket.setDate(new Date());
			
			// TODO prozatimni random cislo odznaku, pak do settings asi
			ticket.setBadgeNumber(3403234);
		}
		
		// Pokud je formularik stringu prazdny, vyplni se automaticky hodnota "";
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
		ticket.setDescriptionDZ(((EditText) findViewById(R.id.descriptionDZ)).getText().toString());
		ticket.setTow(((CheckBox) findViewById(R.id.tow)).isChecked());
		ticket.setMoveableDZ(((CheckBox) findViewById(R.id.moveableDZ)).isChecked());
		ticket.setEventDescription(((EditText) findViewById(R.id.eventDescription)).getText().toString());
		
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
	 * Kontrola v�ech povinn�ch �daj� a vr�cen� varov�n�
	 * 
	 * @param ticket
	 * @return chybov� hl�en�
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

	/**
	 * Nastav� fotodokumentaci - zobraz� n�hledy fotografi� a p�i�ad� jim poslucha�e na zobrazen� v�t��ho n�hledu
	 * Natav� p�id�v�n� nov�ch ftografi�
	 */
	private void setPhotoDocumentation() {
		// Zkontroluje, zda nahodou neni photos null
		if (photos == null) {
			photos = new ArrayList<File>();
		}
		
		// TODO Nastavovat nejak lepe velikosti, nez pres testParams, to je neohrabane. Ale jako wtf.., kdyz neexistuje "setWidth" a "setHeight" ani u "imagebutton", ani u "imageview" a u "button" ano.. 
		// Layout Parametry (velikost a marginy) pro tlacitko a obrazky ze xml (xml obsahuje spravne nastavene tlacitko, ktere je skryte)
		LayoutParams testParams = ((ImageButton) findViewById(R.id.testButton)).getLayoutParams();
		
		// Do tohoto layoutu se budou pridavat nove layouty s obrazky a tlacitkem. Pri kazdem zavolani se vyprazdni a naplni znova - refresh
		LinearLayout photosLayout = (LinearLayout) findViewById(R.id.photoDocumentationLayout);
		photosLayout.removeAllViews();
		
		// Jedotlive layouty do kterych se budou pridavat obrazky a tlacitko
		LinearLayout actualLayout = new LinearLayout(getApplicationContext());
		
		// Podle toho, kolik je ve photos fotek, tolik se vytvori imageView
		int i;
		for(i = 0; i < photos.size(); i++) {
			// Pri 6ti fotografiich se odradkuje
			if (i % 2 == 0) {
				actualLayout = new LinearLayout(getApplicationContext());
				photosLayout.addView(actualLayout);
			}
			ImageView imageView = new ImageView(getApplicationContext());
			imageView.setId(i);
			imageView.setOnClickListener(new OnClickListener() {

				// Po kliku na nahled pridate aktivity se pusti dialog s vetsim nahledem obrazku
				public void onClick(View v) {
					// TODO dodelat dialog tak, aby nebyly kolem obrazku mezery.. custom dialog? 
					Dialog dialog = new Dialog (TicketEditActivity.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.ticketphotodialog);					
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
				imageView.setImageResource(R.drawable.ic_launcher1);
				photos.remove(photos.get(i));
			}
			
		}
		
		// Jeste pridame tlacitko na pridani novych fotek. Pri 6ti fotografiich se odradkuje
		if (i % 2 == 0) {
			actualLayout = new LinearLayout(getApplicationContext());		
			photosLayout.addView(actualLayout);
		}
		ImageButton imageButton = new ImageButton(getApplicationContext());
		imageButton.setImageResource(android.R.drawable.ic_input_add);
		imageButton.setBackgroundResource(android.R.drawable.editbox_background_normal);
		imageButton.setLayoutParams(testParams);
		// Nastavi tlacitku listener
		imageButton.setOnClickListener(new OnClickListener() {
			
			// Po kliknuti na tlacitko se objevi dialog s nabidkou
			public void onClick(View arg0) {
				final String[] items = { getText(R.string.view_ticket_dialogMenu_takeNewPicture).toString(), getText(R.string.view_ticket_dialogMenu_browsePicture).toString()};
				Builder builder = new Builder(TicketEditActivity.this);
				builder.setTitle(getText(R.string.view_ticket_dialogMenu_title).toString());
				builder.setItems(items, new DialogInterface.OnClickListener() {
					
					//Po kliku v dialogu se spusti prislusna aktivita
				    public void onClick(DialogInterface dialog, int item) {
				    	
				    	// Volba 1 - Prime foceni
				    	if (item == 0) {
				    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
				    	    String fileName = df.format(new Date());				    	    
				    		Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
				    		// TODO dodelat ukladani do slozky
				    		photo = new File(Environment.getExternalStorageDirectory(), fileName + getText(R.string.fileType_jpg).toString());
				    		i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
				    		startActivityForResult(i, TAKE_PICTURE);
				    	}
				    	// Volba 2 - vyber ze souboru
				    	if (item == 1) {
				    		Toaster.toast(R.string.notImplementedYet, Toaster.SHORT);
				    	}
				    }
				    
				});
				// Spusti dialog
				builder.show();
			}	
			
		});
		// Prida tlacitko do layoutu
		actualLayout.addView(imageButton); 
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// Navrat po uspesnem vyfoceni fotografie
		if (requestCode == TAKE_PICTURE || resultCode == RESULT_OK) {
			photos.add(photo);
			photo = null;
		}		
		
		// Refreshne fotodokumentaci
		setPhotoDocumentation();
	}
		
	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putSerializable("photos", photos);
		savedInstanceState.putSerializable("photo", photo);	
		super.onSaveInstanceState(savedInstanceState);
	}
}

	