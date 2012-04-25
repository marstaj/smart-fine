package cz.smartfine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Tøída aktivity kontroly zaplaceneho parkovaní pomoci SMS
 * 
 * @author Martin Štajner
 * 
 */
public class SMSParkingActivity extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_parking);

	}

	/**
	 * Obsluha tlaèítka - Zkontrolovat kartu
	 * 
	 * @param button
	 */
	public void checkSMSParkingClick(View button) {

	}

}