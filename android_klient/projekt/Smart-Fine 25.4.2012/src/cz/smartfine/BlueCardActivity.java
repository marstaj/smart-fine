package cz.smartfine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * T��da aktivity kontroly parkovaci karty v modre zone
 * 
 * @author Martin �tajner
 * 
 */
public class BlueCardActivity extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blue_zone);

	}

	/**
	 * Obsluha tla��tka - Zkontrolovat kartu
	 * 
	 * @param button
	 */
	public void checkCardClick(View button) {

	}

}