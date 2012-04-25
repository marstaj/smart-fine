package dao;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Ticket;
import cz.smartfine.R;
import android.content.Context;
import android.os.Environment;

/**
 * Tøída pro práci s fotografiemi
 * @author Martin Štajner
 * 
 */
public class PhotoDAO {

	/**
	 * Kontext aplikace
	 */
	private Context context;
	/**
	 * Cesta na pametovou kartu
	 */
	private File card;
	/**
	 * Slozka pro fotografie
	 */
	private File directory;
	
	
	/**
	 * Konstruktor
	 * 
	 * @param context
	 * @throws Exception
	 */
	public PhotoDAO(Context context) throws Exception {
		super();
		this.context = context;
		this.card = Environment.getExternalStorageDirectory();
		fileDestination();
	}
	
	/**
	 * Smaze fotografii
	 * @param photo
	 * @throws Exception
	 */
	public void deletePhoto(File photo) throws Exception {
		if (!photo.delete()) {
			throw new Exception();
		}
	}
	
	/**
	 *  Smaze vsechny fotografie
	 */
	public void deleteAllPhotos(ArrayList<Ticket> tickets) {
		// TODO dodelat nejaky checkovani, zda se to opravdu smazalo??
		// Projede vsechny tickety a jejich fotky a postupne je smaze
		for (int i = 0; i < tickets.size(); i++) {
			if (tickets.get(i).getPhotos() != null) {
				for (int j = 0; j < tickets.get(i).getPhotos().size(); j++) {
					// Pouze pokud je ve slozce smartfine - jenom fotky vyfocene zkrze smartfine
					File f = tickets.get(i).getPhotos().get(j);
					if (f.getParent().endsWith(context.getString(R.string.photoDirectory))) {
						f.delete();
					}
	        	}
			}
        }            
	}
	
	/**
	 * Vytvori a vrati odkaz na novou fotku
	 * @return
	 */
	public File newPhoto() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
	    String fileName = df.format(new Date());
	    return new File(directory, fileName + context.getText(R.string.fileType_jpg).toString());
	}
	
	
	/**
	 * Kontroluje, zda existuje na karte slozka, kam se nahravaji fotografie.. pokud ne, tak ji vytvori
	 * @throws Exception
	 */
	public void fileDestination() throws Exception {
		directory = new File(card, context.getText(R.string.photoDirectory).toString());
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				throw new Exception();
			}
		}
	}

	public File getDirectory() {
		return directory;
	}
	

}