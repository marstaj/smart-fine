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
    	this.startActivity(new Intent(this, TicketEditActivity.class));
	}
    
    /**
     * Obsluha tlacitka - Lokalni zaznamy
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

}