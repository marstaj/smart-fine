package cz.smartfine.android.dao;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.smartfine.android.R;
import cz.smartfine.android.dao.interfaces.IPhotoDAO;
import cz.smartfine.android.model.Ticket;
import android.content.Context;
import android.os.Environment;

/**
 * T��da pro pr�ci s fotografiemi
 * 
 * @author Martin Štajner
 * 
 */
public class PhotoDAO implements IPhotoDAO {

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
	public PhotoDAO(Context context) throws Exception{
		super();
		this.context = context;
		this.card = Environment.getExternalStorageDirectory();
		fileDestination();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.IPhotoDAO#deletePhoto(java.io.File)
	 */
	public void deletePhoto(File photo) throws Exception {
		if (!photo.delete()) {
			throw new Exception();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.IPhotoDAO#deleteAllPhotos(java.util.ArrayList)
	 */
	public void deleteAllPhotosFromTickets(ArrayList<Ticket> tickets) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.IPhotoDAO#newPhoto()
	 */
	public File newPhoto() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
		String fileName = df.format(new Date());
		return new File(directory, fileName + context.getText(R.string.fileType_jpg).toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.IPhotoDAO#fileDestination()
	 */
	public void fileDestination() throws Exception {
		directory = new File(card, context.getText(R.string.photoDirectory).toString());
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				throw new Exception();
			}
		}
	}

}