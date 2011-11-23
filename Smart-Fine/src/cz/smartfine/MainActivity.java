package cz.smartfine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author Martin Stajner
 *
 */
public class MainActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Prirazeni instance aplikace - kvuli pristupu k datum z ruznych aktivit
    	app = (MyApp) this.getApplication();
    	
    	String strin = new String();//VOLE
    }
        
    /**
     * Obsluha tlacitka - Novy listek
     * @param target
     */
    public void newTicketClick(View target) {
    	this.startActivityForResult(new Intent(this, NewTicketActivity.class), 1);
	}
    
    /**
     * Obsluha tlacitka - Lokalni zaznamy
     * @param target
     */
    public void listClick(View target) {
    	this.startActivity(new Intent(this, LocalListActivity.class));
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);		
		if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
            	// Ulozeni vsech listku do souboru
            	app.getDao().saveTicket(app.getLocals());
            }
        }
	}

}