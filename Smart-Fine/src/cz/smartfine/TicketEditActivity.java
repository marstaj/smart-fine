package cz.smartfine;

import java.util.Date;

import model.Law;
import model.Settings;
import model.Ticket;
import model.util.TicketSetter;
import model.util.Toaster;
import cz.smartfine.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * @author Martin Stajner
 * 
 */
public class TicketEditActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;
	/**
	 * Tento vyvolany intent
	 */
	private Intent intent;
	/**
	 * PL urceny k editaci
	 */
	private Ticket ticketForEdit;
	

	@Override
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
			// získá index do DAO na lístek, který s má zobrazit, pøedaný ze
			// spouštìjící aktivity
			int ticketIndex = intent.getExtras().getInt("Ticket");

			ticketForEdit = app.getTicketDao().getTicket(ticketIndex);

			if (ticketForEdit != null) {
				TicketSetter.setTicketBasic(this, ticketForEdit);
			}
		}

		// Nastavi Listenery na spinnery
		setAllSpinnersListeners();
		
		// Nastavi Mesto ze settings
		EditText cityEditText = (EditText) findViewById(R.id.city);
		if (cityEditText.getText().toString().equals("")) {
			cityEditText.setText(Settings.getInstance().getCity(getApplicationContext()));
		}
	}

	/**
	 * Obsluha tlacitka - Ulozit listek
	 * 
	 * @param target
	 * @throws Exception
	 */
	public void saveTicket(View target) {
		Ticket ticket = setTicketData();
		String error = checkTicket(ticket);
		if (error != null) {
			Toaster.toast(error, Toaster.LONG);
		} else {
			try {
				// Ulozi PL
				app.getTicketDao().saveTicket(ticket);
				Toaster.toast(R.string.val_ticket_success, Toaster.LONG);
				// Nastavi result pro pripad editace, aby se mohl detail listku refreshnout
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
	 * Vytvoreni noveho listku a prirazeni jeho atributu NEBO zmena udaju PL,
	 * ktery editujeme
	 * 
	 * @return
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
			
			// TODO prozatimni random cislo odznaku
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
		
		return ticket;
	}

	/**
	 * Kontrola vsech povinnych udaju a vraceni varovani
	 * 
	 * @param ticket
	 * @return
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

	/**
	 * Obsluha nastaveni listeneru
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

			public void onNothingSelected(AdapterView<?> arg0) {
				//
			}
		});
	}
	
	/**
	 * Nastaveni listeneru na vybrany spinner
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

			public void onNothingSelected(AdapterView<?> arg0) {
				//
			}
		});
	}
}