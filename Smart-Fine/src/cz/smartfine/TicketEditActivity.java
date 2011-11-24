package cz.smartfine;

import java.util.Date;
import model.Ticket;
import model.Toaster;
import cz.smartfine.R;
import android.app.Activity;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketedit);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych
		// aktivit
		app = (MyApp) this.getApplication();

		setAllSpinnerListeners();
	}

	/**
	 * Obsluha tlacitka - Ulozit listek
	 * 
	 * @param target
	 * @throws Exception
	 */
	public void saveTicket(View target) {
		Ticket ticket = createTicket();
		String error = checkTicket(ticket);
		if (error != null) {
			Toaster.toast(error, Toaster.LONG);
		} else {
			try {
				app.getTicketDao().saveTicket(ticket);
				Toaster.toast(R.string.val_ticket_success, Toaster.LONG);
				finish();
			} catch (Exception e) {
				// Odstraneni posledniho pridaneho listku z arraylistu
				app.getTicketDao().getLocals()
						.remove(app.getTicketDao().getLocals().size() - 1);
				Toaster.toast(R.string.val_ticket_failure, Toaster.LONG);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Vytvoreni noveho listku a prirazeni jeho atributu
	 * 
	 * @return
	 */
	private Ticket createTicket() {
		Ticket ticket = new Ticket();

		ticket.setDate(new Date());

		// TODO prozatimni random cislo odznaku
		ticket.setBadgeNumber(3403234);

		ticket.setSpz(((EditText) this.findViewById(R.id.spz)).getText()
				.toString());
		ticket.setMpz(((EditText) findViewById(R.id.mpz)).getText().toString());
		ticket.setSpzColor(((EditText) findViewById(R.id.spzColor)).getText()
				.toString());
		ticket.setVehicleType(((EditText) findViewById(R.id.vehicleType))
				.getText().toString());
		ticket.setVehicleBrand(((EditText) findViewById(R.id.vehicleBrand))
				.getText().toString());
		ticket.setCity(((EditText) findViewById(R.id.city)).getText()
				.toString());
		ticket.setStreet(((EditText) findViewById(R.id.street)).getText()
				.toString());
		if ((((EditText) findViewById(R.id.number)).getText().toString())
				.equals("")) {
			ticket.setNumber(0);
		} else {
			ticket.setNumber(Integer
					.valueOf(((EditText) findViewById(R.id.number)).getText()
							.toString()));
		}
		ticket.setLocation(((EditText) findViewById(R.id.location)).getText()
				.toString());
		ticket.setDescriptionDZ(((EditText) findViewById(R.id.descriptionDZ))
				.getText().toString());
		ticket.setTow(((CheckBox) findViewById(R.id.tow)).isChecked());
		ticket.setMoveableDZ(((CheckBox) findViewById(R.id.moveableDZ))
				.isChecked());
		ticket.setEventDescription(((EditText) findViewById(R.id.eventDescription))
				.getText().toString());

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

		if (error.length() != 0) {
			return error;
		} else {
			return null;
		}
	}

	/**
	 * Obsluha nastaveni listeneru
	 */
	private void setAllSpinnerListeners() {
		int ids[][] = { { R.id.mpzSpinner, R.id.mpz },
				{ R.id.spzColorSpinner, R.id.spzColor },
				{ R.id.vehicleTypeSpinner, R.id.vehicleType },
				{ R.id.vehicleBrandSpinner, R.id.vehicleBrand } };

		for (int i = 0; i < ids.length; i++) {
			setSimpleSpinnerListener(ids[i][0], ids[i][1]);
		}
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
					if (text.equals("- Vlastní -")) {
						text = "";
					}
					((EditText) findViewById(txt)).setText(text);
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