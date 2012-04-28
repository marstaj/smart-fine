package cz.smartfine.android;

import java.io.FileNotFoundException;

import cz.smartfine.android.dao.FileFreqValuesDAO;
import cz.smartfine.android.dao.FileLawDAO;
import cz.smartfine.android.dao.FileTicketDAO;
import cz.smartfine.android.dao.PhotoDAO;
import cz.smartfine.android.dao.interfaces.IFreqValuesDAO;
import cz.smartfine.android.dao.interfaces.IPhotoDAO;
import cz.smartfine.android.dao.interfaces.ITicketDAO;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.ConnectionProvider;

import android.app.Application;

/**
 * Tøída reprezentující aplikaci
 * 
 * @author Martin Štajner
 * 
 */
public class MyApp extends Application {

	/**
	 * Pristup k fileTicketDAO
	 */
	private ITicketDAO fileTicketDAO;

	/**
	 * Pristup k photoDAO
	 */
	private IPhotoDAO photoDAO;

	/**
	 * Pristup k fileFreqValuesDAO
	 */
	private IFreqValuesDAO fileFreqValuesDAO;

	/**
	 * Pristup k fileLawDAO
	 */
	private IFreqValuesDAO fileLawDAO;

	/**
	 * Provider pripojeni
	 */
	private ConnectionProvider connectionProvider;

	// ==================================== INICIALIZACE ==================================== //
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Nastavi kontext aplikace Toasteru, aby mohl toastovat
		Toaster.context = getApplicationContext();
		
		// Vytvoøí FileTicketDAO pro práci s parkovacími lístky a naète soubor s lístky
		try {
			fileTicketDAO = new FileTicketDAO(this);
			fileTicketDAO.loadTickets();
		} catch (FileNotFoundException e) {
			Toaster.toast("Soubor nenalezen", Toaster.SHORT);
		} catch (Exception e) {
			Toaster.toast("Nepodaøilo se naèíst uložené lístky", Toaster.SHORT);
		}

		// Vytvori photoDAO pro praci s fotografiemi
		try {
			photoDAO = new PhotoDAO(this);
		} catch (Exception e) {
			Toaster.toast("Nepodaøilo se vytvoøit složku pro ukladání fotografií", Toaster.SHORT);
		}

		// Vytvori FileFreqValuesDAO pro praci s nejcastejsimi hodnotami a nacte je
		fileFreqValuesDAO = new FileFreqValuesDAO(this);
		fileFreqValuesDAO.loadValues();

		// Vytvori FileLawDAO pro praci s nejcastejsimi hodnotami zákonù pøestupkù a nacte je
		fileLawDAO = new FileLawDAO(this);
		fileLawDAO.loadValues();
	

	}

	// ==================================== GETTERY SETTERY ==================================== //

	/**
	 * Vrátí pøístup k DAO
	 * 
	 * @return pøístup k DAO
	 */
	public ITicketDAO getTicketDao() {
		return fileTicketDAO;
	}

	/**
	 * Vrátí pøístup k PhotoDAO
	 * 
	 * @return pøístup k PhotoDAO
	 */
	public IPhotoDAO getPhotoDAO() {
		return photoDAO;
	}

	/**
	 * Vrátí pøístup k FileFreqValuesDAO
	 * 
	 * @return pøístup k FileFreqValuesDAO
	 */
	public IFreqValuesDAO getFreqValuesDAO() {
		return fileFreqValuesDAO;
	}

	/**
	 * Vrátí pøístup k FileLawDAO
	 * 
	 * @return pøístup k FileLawDAO
	 */
	public IFreqValuesDAO getFileLawDAO() {
		return fileLawDAO;
	}

	/**
	 * Vrátí pøístup k ConnectionProvider
	 * 
	 * @return pøístup k ConnectionProvider
	 */
	public ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

}
