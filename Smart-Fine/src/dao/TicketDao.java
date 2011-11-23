package dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import android.content.Context;
import model.Ticket;
import model.Toaster;

/**
 * @author Martin Stajner
 * 
 */
public class TicketDao {

	/**
	 * Kontext aplikace - kvuli relativni ceste k ulozenym souborum aplikace
	 */
	Context context;

	/**
	 * Konstruktor
	 * @param context
	 */
	public TicketDao(Context context) {
		super();
		this.context = context;
	}

	/**
	 * Ulozeni listu zaznamu privatne na disk
	 * @param list
	 * @return
	 */
	public boolean saveTicket(ArrayList<Ticket> list) {
		try {
			FileOutputStream fos = context.openFileOutput("data.smf", Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			Toaster.toast("FileNotFoundException output", Toaster.LONG);
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Toaster.toast("IOException output", Toaster.LONG);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Nacteni listu zaznamu ze souboru z disku
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Ticket> loadTickets() {		
		ArrayList<Ticket> list;
		try {			
			FileInputStream fis = context.openFileInput("data.smf");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			if (o instanceof ArrayList) {
				list = (ArrayList<Ticket>) o;
			} else {
				list = null;
			}			
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			Toaster.toast("FileNotFoundException input", Toaster.LONG);
			e.printStackTrace();
			return null;
		} catch (StreamCorruptedException e) {
			Toaster.toast("Stream corupted input", Toaster.LONG);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Toaster.toast("IOException input", Toaster.LONG);
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			Toaster.toast("ClassNotFoundException input", Toaster.LONG);
			e.printStackTrace();
			return null;
		}		
		return list;
	}
}