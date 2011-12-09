package cz.smartfine;

import java.io.File;
import java.util.ArrayList;
import model.Ticket;
import model.util.Image;
import model.util.TicketSetter;
import model.util.Toaster;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Tøída aktivity pro zobrazení informací z parkovacího lístku
 * 
 * @author Pavel Brož, Martin Štajner
 */
public class TicketDetailActivity extends Activity {

	/**
	 * Instance aplikace
	 */
	private MyApp app;
	/**
	 * Reprezentuje index zobrazeného PL do DAO
	 */
	private int ticketIndex;
	
	private static final int EDIT = 0;
	
	// TODO requestCode pro PRINT, az bude PRINT implementovan
	//private static final int PRINT = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticketdetail);

		app = (MyApp) this.getApplication();

		// získá index do DAO na lístek, který s má zobrazit, pøedaný ze
		// spouštìjící aktivity
		ticketIndex = this.getIntent().getExtras().getInt("Ticket");

		Ticket ticket = app.getTicketDao().getTicket(ticketIndex);

		// ovìøí, zda se nìo vrátilo a zavolá metodu pro naplnìní widgetù daty//
		if (ticket != null) {
			setTicket(ticket);
		}
	}

	/**
	 * Naplní widgety daty z instance PL pøedané v parametru
	 * 
	 * @param ticket
	 */
	private void setTicket(Ticket ticket) {
		// naplní formuláø editovatelnými daty
		TicketSetter.setTicketBasic(this, ticket);
		// naplní formuláø needitovatelnými daty
		TicketSetter.setTicketExtra(this, ticket);
		
		setPhotos(ticket);
	}

	/**
	 * Posluchaè stisknutí tlaèítka tisku PL
	 * 
	 * @param button Stisknuté tlaèítko
	 */
	public void printTicket(View button) {
		// TODO: podporu tisku implementovat až ve fázi 2
		// Pro print jsem nahore udelal promenou PRINT, pro startActivityForResult jako je u editace nize. :)
	}

	/**
	 * Posluchaè stisknutí tlaèítka editace PL
	 * 
	 * @param button Stisknuté tlaèítko
	 */
	public void editTicket(View button) {
		// vytvoøí Intent, vloží do nìj index na zobrazený PL a spustí aktivitu
		// pro editaci tohoto PL
		this.startActivityForResult(new Intent(this, TicketEditActivity.class).putExtra(
				"Ticket", ticketIndex), EDIT);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// Navrat z EditActivity uspesnem po dokonceni editace. Refreshne data na obrazovce
		if (requestCode == EDIT && resultCode == RESULT_OK) {
			Ticket ticket = app.getTicketDao().getTicket(ticketIndex);
			// ovìøí, zda se nìo vrátilo a zavolá metodu pro naplnìní widgetù daty//
			if (ticket != null) {
				setTicket(ticket);
			}
		}
	}	
	
	private void setPhotos(Ticket ticket) {
		ArrayList<File> photos = ticket.getPhotos();
		// Zkontroluje, zda nahodou neni photos null
		if (photos == null) {
			photos = new ArrayList<File>();
		}
		
		// TODO Nastavovat nejak lepe velikosti, nez pres testParams, to je neohrabane. Ale jako wtf.., kdyz neexistuje "setWidth" a "setHeight" ani u "imagebutton", ani u "imageview" a u "button" ano.. 
				// Layout Parametry (velikost a marginy) pro tlacitko a obrazky ze xml (xml obsahuje spravne nastavene tlacitko, ktere je skryte)
				LayoutParams testParams = ((ImageButton) findViewById(R.id.testButton)).getLayoutParams();
				
				// Do tohoto layoutu se budou pridavat nove layouty s obrazky a tlacitkem. Pri kazdem zavolani se vyprazdni a naplni znova - refresh
				LinearLayout photosLayout = (LinearLayout) findViewById(R.id.photoDocumentationLayout);
				photosLayout.removeAllViews();
				
				// Jedotlive layouty do kterych se budou pridavat obrazky a tlacitko
				LinearLayout actualLayout = new LinearLayout(getApplicationContext());
				
				// Podle toho, kolik je ve photos fotek, tolik se vytvori imageView
				int i;
				for(i = 0; i < photos.size(); i++) {
					// Pri 6ti fotografiich se odradkuje
					if (i % 2 == 0) {
						actualLayout = new LinearLayout(getApplicationContext());
						photosLayout.addView(actualLayout);
					}
					ImageView imageView = new ImageView(getApplicationContext());
					imageView.setId(i);
					imageView.setOnClickListener(new OnClickListener() {

						// Po kliku na nahled pridate aktivity se pusti dialog s vetsim nahledem obrazku
						public void onClick(View v) {
							// TODO dodelat dialog tak, aby nebyly kolem obrazku mezery.. custom dialog? 
							Dialog dialog = new Dialog (TicketDetailActivity.this);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.ticketphotodialog);					
							ImageView image = (ImageView) dialog.findViewById(R.id.photoView);
							BitmapDrawable drawable = (BitmapDrawable) ((ImageView) v).getDrawable();
							image.setImageDrawable(drawable);
							// Spusti dialog
							dialog.show();
						}
						
					});
					imageView.setLayoutParams(testParams);
					// Zkusi nastavit obrazek
					try {
			            Bitmap bitmap = Image.decodeFile(photos.get(i), 500);
						imageView.setImageBitmap(bitmap);
						actualLayout.addView(imageView);
					} catch (Exception e) {
						Toaster.toast(R.string.val_ticket_fotoDocumentation_failLoad, Toaster.SHORT);
						imageView.setImageResource(R.drawable.ic_launcher1);
						photos.remove(photos.get(i));
					}
					
				}
	}
}
