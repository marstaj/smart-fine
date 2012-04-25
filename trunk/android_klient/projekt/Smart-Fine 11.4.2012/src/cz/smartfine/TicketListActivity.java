package cz.smartfine;

import model.util.Toaster;
import cz.smartfine.R;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Tøída aktivity zobrazení listu všech uložených PL
 * @author Martin Štajner
 * 
 */
public class TicketListActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
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
	
	/**
     * Obsluha tlaèítka - Upload dat na server
     * @param button
     */
	public void uploadToServerClick (View button) {
		// TODO: odstranit tuhle dummy hruzu co jsem tam napsal kvuli PDA :)
		
		Builder builder = new Builder(this);
		builder.setMessage("Oravdu chcete nahrát veškerá data na server?")
		       .setCancelable(false)
		       .setPositiveButton("Ano", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					final ProgressDialog dialog1 = ProgressDialog.show(TicketListActivity.this, "Upload na server", "Uploaduju data na server, prosím poèkejte...", true);
					dialog1.show();

					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
					    public void run() {
					        dialog1.dismiss();
					        Toaster.toast("Data úspìšnì uploadována na server.", Toaster.SHORT);
					        
					        try {
								app.getTicketDao().deleteAllTickets();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					        onCreate(new Bundle());
					        		        
					    }}, 3000);  // 3000 milliseconds
				}
				
				
		       })
		       .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		builder.show();
				
	}
	
}