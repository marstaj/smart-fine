package cz.smartfine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * T��da aktivity kontroly zaplaceneho parkovan� pomoci SMS
 * 
 * @author Martin �tajner
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
	 * Obsluha tla��tka - Zkontrolovat kartu
	 * 
	 * @param button
	 */
	public void checkSMSParkingClick(View button) {

	}

}