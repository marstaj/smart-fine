package cz.smartfine;

import model.Toaster;
import cz.smartfine.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Martin Stajner
 *
 */
public class LocalListActivity extends Activity {
	
	/**
	 * Instance aplikace
	 */
	private MyApp app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locallist);
		
		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		// Nastaveni listu
		ListView lv = (ListView) this.findViewById(R.id.listView1);
		lv.setTextFilterEnabled(true);
		lv.setAdapter(new TicketAdapter(this, R.layout.listitem, app.getLocals()));
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// When clicked, show a toast with the TextView text
			      Toaster.toast((String) ((TextView) arg1).getText(), Toaster.SHORT);
			}
		  });
	}
}