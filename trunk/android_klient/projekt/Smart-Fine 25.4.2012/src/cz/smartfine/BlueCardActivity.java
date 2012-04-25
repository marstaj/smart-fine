package cz.smartfine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Tøída aktivity kontroly parkovaci karty v modre zone
 * 
 * @author Martin Štajner
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
	 * Obsluha tlaèítka - Zkontrolovat kartu
	 * 
	 * @param button
	 */
	public void checkCardClick(View button) {

	}

}