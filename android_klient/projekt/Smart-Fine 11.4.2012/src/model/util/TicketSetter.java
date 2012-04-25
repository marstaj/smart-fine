package model.util;

import java.io.File;
import java.util.ArrayList;
import model.Ticket;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cz.smartfine.R;

/**
 * Pomocná tøída pro naplnìní formuláøù PL, daty z instance PL
 * 
 * @author Pavel Brož, Martin Štajner
 */
public class TicketSetter {

	/**
	 * Naplní widgety editovatelnými daty z instance PL pøedané v parametru
	 * 
	 * @param activity
	 *            Aktivita, ze které se metoda volá
	 * @param ticket
	 *            Instance PL, která se má zobrazit
	 */
	public static void setTicketBasic(Activity activity, Ticket ticket) {

		// Nastaví jednotlivé widgety na hodnoty z instance PL.
		// Pokud bude nejaky stringovy atribut null, policko zustane prazdne
		// Pokud bude nejaky int atribut 0 (coz je jakoby null u int), policko
		// se rucne vyprazdni (jinak by to vypisovalo 0)

		((EditText) activity.findViewById(R.id.spz)).setText(ticket.getSpz());
		((EditText) activity.findViewById(R.id.mpz)).setText(ticket.getMpz());
		((EditText) activity.findViewById(R.id.spzColor)).setText(ticket
				.getSpzColor());
		((EditText) activity.findViewById(R.id.vehicleType)).setText(ticket
				.getVehicleType());
		((EditText) activity.findViewById(R.id.vehicleBrand)).setText(ticket
				.getVehicleBrand());
		((EditText) activity.findViewById(R.id.city)).setText(ticket.getCity());
		((EditText) activity.findViewById(R.id.street)).setText(ticket
				.getStreet());

		if (ticket.getNumber() == 0) {
			((EditText) activity.findViewById(R.id.number)).setText("");
		} else {
			((EditText) activity.findViewById(R.id.number)).setText(String
					.valueOf(ticket.getNumber()));
		}

		((EditText) activity.findViewById(R.id.location)).setText(ticket
				.getLocation());
		((CheckBox) activity.findViewById(R.id.tow)).setChecked(ticket.isTow());
		((CheckBox) activity.findViewById(R.id.moveableDZ)).setChecked(ticket
				.isMoveableDZ());
		((EditText) activity.findViewById(R.id.eventDescription))
				.setText(ticket.getEventDescription());

		if (ticket.getLaw() != null) {
			if (ticket.getLaw().getRuleOfLaw() != 0) {
				((EditText) activity.findViewById(R.id.ruleOfLaw))
						.setText(String.valueOf(ticket.getLaw().getRuleOfLaw()));
			} else {
				((EditText) activity.findViewById(R.id.ruleOfLaw)).setText("");
			}
			if (ticket.getLaw().getParagraph() != 0) {
				((EditText) activity.findViewById(R.id.paragraph))
						.setText(String.valueOf(ticket.getLaw().getParagraph()));
			} else {
				((EditText) activity.findViewById(R.id.paragraph)).setText("");
			}
			if (ticket.getLaw().getLawNumber() != 0) {
				((EditText) activity.findViewById(R.id.lawNumber))
						.setText(String.valueOf(ticket.getLaw().getLawNumber()));
			} else {
				((EditText) activity.findViewById(R.id.lawNumber)).setText("");
			}
			if (ticket.getLaw().getCollection() != 0) {
				((EditText) activity.findViewById(R.id.collection))
						.setText(String
								.valueOf(ticket.getLaw().getCollection()));
			} else {
				((EditText) activity.findViewById(R.id.collection)).setText("");
			}
			((EditText) activity.findViewById(R.id.letter)).setText(ticket
					.getLaw().getLetter());
			((EditText) activity.findViewById(R.id.descriptionDZ)).setText(ticket
					.getLaw().getDescriptionDZ());
		}
	}

	/**
	 * Naplní widgety needitovatelnými daty z instance PL pøedané v parametru
	 * 
	 * @param activity
	 *            Aktivita, ze které se metoda volá
	 * @param ticket
	 */
	public static void setTicketExtra(Activity activity, Ticket ticket) {
		// Nastaví jednotlivé widgety na hodnoty z instance PL.
		if (ticket.getDate() != null) {
			((EditText) activity.findViewById(R.id.dateTime)).setText(ticket
					.getDate().toLocaleString());
		}
		((EditText) activity.findViewById(R.id.badgeNumber)).setText(String
				.valueOf(ticket.getBadgeNumber()));
		((CheckBox) activity.findViewById(R.id.printed)).setChecked(ticket
				.isPrinted());
	}

	/**
	 * Nastaví fotodokumentaci - zobrazí náhledy fotografií a pøiøadí jim posluchaèe na zobrazení vìtšího náhledu
	 * @param activity
	 */
	public static void setTicketPhotoDocumentation(final Activity activity, ArrayList<File> photos) {

		// Zkontroluje, zda nahodou neni photos null
		if (photos == null) {
			photos = new ArrayList<File>();
		}

		// TODO Nastavovat nejak lepe velikosti, nez pres testParams, to je
		// neohrabane. Ale jako wtf.., kdyz neexistuje "setWidth" a "setHeight"
		// ani u "imagebutton", ani u "imageview" a u "button" ano..
		// Layout Parametry (velikost a marginy) pro tlacitko a obrazky ze xml
		// (xml obsahuje spravne nastavene tlacitko, ktere je skryte)
		LayoutParams testParams = ((ImageButton) activity.findViewById(R.id.testButton))
				.getLayoutParams();

		// Do tohoto layoutu se budou pridavat nove layouty s obrazky a
		// tlacitkem. Pri kazdem zavolani se vyprazdni a naplni znova - refresh
		LinearLayout photosLayout = (LinearLayout) activity.findViewById(R.id.photoDocumentationLayout);
		photosLayout.removeAllViews();

		// Jedotlive layouty do kterych se budou pridavat obrazky a tlacitko
		LinearLayout actualLayout = new LinearLayout(activity.getApplicationContext());

		// Podle toho, kolik je ve photos fotek, tolik se vytvori imageView
		int i;
		for (i = 0; i < photos.size(); i++) {
			// Pri 2ti fotografiich se odradkuje
			if (i % 2 == 0) {
				actualLayout = new LinearLayout(activity.getApplicationContext());
				photosLayout.addView(actualLayout);
			}
			ImageView imageView = new ImageView(activity.getApplicationContext());
			imageView.setId(i);
			imageView.setOnClickListener(new OnClickListener() {

				// Po kliku na nahled pridate aktivity se pusti dialog s vetsim
				// nahledem obrazku
				public void onClick(View v) {
					// TODO dodelat dialog tak, aby nebyly kolem obrazku
					// mezery.. custom dialog?
					Dialog dialog = new Dialog(activity);
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
				Toaster.toast(R.string.val_ticket_fotoDocumentation_failLoad,
						Toaster.SHORT);
				imageView.setImageResource(R.drawable.ic_launcher1);
				photos.remove(photos.get(i));
			}

		}
	}
}
