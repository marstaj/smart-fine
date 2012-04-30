package cz.smartfine.android;

import java.io.Serializable;
import cz.smartfine.android.R;
import cz.smartfine.android.helpers.BlueCardActivityHelper;
import cz.smartfine.android.model.util.Internet;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.networklayer.model.mobile.SPCInfo;
import cz.smartfine.networklayer.model.mobile.SPCStatus;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * T��da aktivity kontroly parkovaci karty v modre zone
 * 
 * @author Martin �tajner
 * 
 */
public class BlueCardActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	ProgressDialog progDialog;

	BlueCardActivityHelper helper;

	Bundle bundle;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blue_zone);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		// Nastavi helper
		helper = new BlueCardActivityHelper(this);

		// Pokud jsou nejake informace v bundlu (kvuli pretoceni), tak je nastavi
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("SPCInfo")) {
				setInfo(savedInstanceState.getSerializable("SPCInfo"));
			}
		}
	}

	/**
	 * Obsluha tlačítka - Zkontrolovat kartu
	 * 
	 * @param button
	 */
	public void checkCardClick(View button) {

		// Zkontroluje prihlaseni k internetu
		if (Internet.isOnline(this)) {

			EditText cardNumber = (EditText) findViewById(R.id.cardNumber);
			helper.setSpcNumber(cardNumber.getText().toString());

			if (helper.getSpcNumber().length() != 0) {

				// Nastavení dialogu a LogIN
				// TODO napsat do R
				progDialog = new ProgressDialog(this);
				progDialog.setTitle("Kontrola parkovací karty");
				progDialog.setMessage("Prosím počkejte...");
				progDialog.setIndeterminate(true);
				progDialog.setCancelable(false);
				progDialog.show();

				// Kontrola, zda uz nahodou nejsme pripojeni. Kdyz ano, tak se rovnou ocekovat SPZ, kdyz ne, tak se nejdrive zkusime pripojit a prihlasit.
				if (app.getConnectionProvider().isConnected()) {

					helper.checkSPC(this, helper.getSpcNumber());
				} else {
					helper.connectAndLogin();
				}

			} else {
				Toaster.toast("Musíte zadat číslo parkovací karty.", Toaster.SHORT);
			}
		} else {
			// TODO do R.
			Toaster.toast("K ověření parkovací karty musíte být připojeni k internetu.", Toaster.SHORT);
		}
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
		if (bundle != null) {
			savedInstanceState.putAll(bundle);
		}
		super.onSaveInstanceState(savedInstanceState);
	}
	// ==================================== GET / SET ==================================== //

	/**
	 * @return
	 */
	public Dialog getProggressDialog() {
		return progDialog;
	}

	/**
	 * @return
	 */
	public MyApp getApp() {
		return app;
	}

	// ==================================== OSTATNÍ ==================================== //

	@Override
	public void finish() {

		// Odpoji protokol
		if (helper.getProtocol() != null) {
			helper.getProtocol().disconnectProtocol();
		}
		super.finish();
	}

	/**
	 * Z objektu SPCInfo přečte informace o parkovací kartě poslaná ze serveru a
	 * zobrazí je na obrazovce.
	 * 
	 * @param infoFromServer
	 *            Informace ze serveru o parkovací kartě.
	 */
	public void setInfo(Serializable infoFromServer) {
		SPCInfo info = (SPCInfo) infoFromServer;
		String cardNumber = info.getSpcNumber();
		SPCStatus status = info.getSpcStatus();

		((LinearLayout) findViewById(R.id.blueCardInfo)).setVisibility(View.VISIBLE);

		if (cardNumber != null) {
			((TextView) findViewById(R.id.blueCardNumber)).setText(cardNumber);
		}
		if (status != null) {
			if (status == SPCStatus.OK_SPC) {
				// TODO napsat do R
				((TextView) findViewById(R.id.blueCardStatus)).setText("Karta je v pořádku.");
			}
			if (status == SPCStatus.STOLEN_SPC) {
				// TODO napsat do R
				((TextView) findViewById(R.id.blueCardStatus)).setText("Karta byla ukradena.");
			}
			if (status == SPCStatus.UKNOWN_SPC_STATUS) {
				// TODO napsat do R
				((TextView) findViewById(R.id.blueCardStatus)).setText("Neznámý");
			}
		}

		if (bundle == null) {
			bundle = new Bundle();
		}
		bundle.putSerializable("SPCInfo", info);
	}

}