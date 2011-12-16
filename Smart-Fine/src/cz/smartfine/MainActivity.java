package cz.smartfine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * T��da aktivity hlavn� obrazovky aplikace.
 * @author Martin �tajner
 *
 */
public class MainActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	@SuppressWarnings("unused")
	private MyApp app;
	
	/**
	 * Glob�ln� hodnota reprezentuj�c� Vytvo�en� nov�ho PL
	 */
	private static final int NEW_TICKET = 0;
	
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //nastav� defaultn� hodnoty nastaven� tam, kde nejsou zat�m ��dn� hodnoty
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        // Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
    	app = (MyApp) this.getApplication();
    }
        
    /**
     * Obsluha tla��tka - Nov� PL
     * @param button
     */
    public void newTicketClick(View button) {
    	this.startActivityForResult(new Intent(this, TicketEditActivity.class), NEW_TICKET);
	}
    
    /**
     * Obsluha tla��tka - List ulo�en�ch PL
     * @param button
     */
    public void ticketListClick(View button) {
    	this.startActivity(new Intent(this, TicketListActivity.class));
	}
    
    /**
     * Obsluha tla��tka - Nastaven� aplikace
     * @param button
     */
    public void preferencesClick(View button) {
    	this.startActivity(new Intent(this, PreferencesActivity.class));
	}
    
    /* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// Navrat z EditActivity po dokonceni vytvareni PL a jeho vracenem indexu.
		if (requestCode == NEW_TICKET && resultCode == RESULT_OK && data.getExtras().containsKey("Ticket")) {
			startActivity(new Intent(getApplicationContext(), TicketDetailActivity.class).putExtra("Ticket", data.getExtras().getInt("Ticket")));
		}
	}	

}