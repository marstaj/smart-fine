package cz.smartfine;

import cz.smartfine.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author Martin Stajner
 * 
 */
public class TicketListActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketlist);

		// Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
		app = (MyApp) this.getApplication();

		// Nastaveni listu
		ListView lv = (ListView) this.findViewById(R.id.listView1);
		lv.setTextFilterEnabled(true);
		lv.setAdapter(new TicketAdapter(this, R.layout.ticketlistitem, app.getTicketDao().getAllTickets()));

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Spusteni nove aktivity
				parent.getContext().startActivity(new Intent(parent.getContext(), TicketDetailActivity.class).putExtra("Ticket", position));
			}
		});
	}
}