package cz.smartfine;

import model.util.Toaster;
import cz.smartfine.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
		
		if (app.getTicketDao().getAllTickets().isEmpty()) {
			Button upload = (Button) findViewById(R.id.uploadButton);
			upload.setEnabled(false);
		}
		
	}
	
	public void uploadToServerClick (View button) {
		// TODO: odstranit tuhle dummy hruzu co jsem tam napsal kvuli PDA :)// Pro print jsem nahore udelal promenou PRINT, pro startActivityForResult jako je u editace nize. :)
		
		
		final ProgressDialog dialog = ProgressDialog.show(this, "Upload na server", "Uploaduju data na server, prosím poèkejte...", true);
		dialog.show();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		    public void run() {
		        dialog.dismiss();
		        Toaster.toast("Data úspìšnì uploadována na server.", Toaster.SHORT);
		        
		        app.getTicketDao().getAllTickets().clear();
		        onCreate(new Bundle());
		        		        
		    }}, 3000);  // 3000 milliseconds
	}
	
}