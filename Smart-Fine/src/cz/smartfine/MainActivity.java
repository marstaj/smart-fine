package cz.smartfine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * @author Martin Stajner
 *
 */
public class MainActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	@SuppressWarnings("unused")
	private MyApp app;
	
	/**
	 * Priznak, ze se jedna o vytváreni noveho PL
	 */
	private static final int NEW_TICKET = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //nastaví defaultní hodnoty nastavení tam, kde nejsou zatím žádné hodnoty
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        // Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
    	app = (MyApp) this.getApplication();
    }
        
    /**
     * Obsluha tlacitka - Novy listek
     * @param target
     */
    public void newTicketClick(View target) {
    	this.startActivityForResult(new Intent(this, TicketEditActivity.class), NEW_TICKET);
	}
    
    /**
     * Obsluha tlacitka - List listku
     * @param target
     */
    public void ticketListClick(View target) {
    	this.startActivity(new Intent(this, TicketListActivity.class));
	}
    
    /**
     * Obsluha tlaèítka - nastavení aplikace
     * @param target
     */
    public void preferencesClick(View target) {
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