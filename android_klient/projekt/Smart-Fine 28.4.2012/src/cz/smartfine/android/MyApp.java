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
 * T��da reprezentuj�c� aplikaci
 * 
 * @author Martin �tajner
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
		
		// Vytvo�� FileTicketDAO pro pr�ci s parkovac�mi l�stky a na�te soubor s l�stky
		try {
			fileTicketDAO = new FileTicketDAO(this);
			fileTicketDAO.loadTickets();
		} catch (FileNotFoundException e) {
			Toaster.toast("Soubor nenalezen", Toaster.SHORT);
		} catch (Exception e) {
			Toaster.toast("Nepoda�ilo se na��st ulo�en� l�stky", Toaster.SHORT);
		}

		// Vytvori photoDAO pro praci s fotografiemi
		try {
			photoDAO = new PhotoDAO(this);
		} catch (Exception e) {
			Toaster.toast("Nepoda�ilo se vytvo�it slo�ku pro uklad�n� fotografi�", Toaster.SHORT);
		}

		// Vytvori FileFreqValuesDAO pro praci s nejcastejsimi hodnotami a nacte je
		fileFreqValuesDAO = new FileFreqValuesDAO(this);
		fileFreqValuesDAO.loadValues();

		// Vytvori FileLawDAO pro praci s nejcastejsimi hodnotami z�kon� p�estupk� a nacte je
		fileLawDAO = new FileLawDAO(this);
		fileLawDAO.loadValues();
	

	}

	// ==================================== GETTERY SETTERY ==================================== //

	/**
	 * Vr�t� p��stup k DAO
	 * 
	 * @return p��stup k DAO
	 */
	public ITicketDAO getTicketDao() {
		return fileTicketDAO;
	}

	/**
	 * Vr�t� p��stup k PhotoDAO
	 * 
	 * @return p��stup k PhotoDAO
	 */
	public IPhotoDAO getPhotoDAO() {
		return photoDAO;
	}

	/**
	 * Vr�t� p��stup k FileFreqValuesDAO
	 * 
	 * @return p��stup k FileFreqValuesDAO
	 */
	public IFreqValuesDAO getFreqValuesDAO() {
		return fileFreqValuesDAO;
	}

	/**
	 * Vr�t� p��stup k FileLawDAO
	 * 
	 * @return p��stup k FileLawDAO
	 */
	public IFreqValuesDAO getFileLawDAO() {
		return fileLawDAO;
	}

	/**
	 * Vr�t� p��stup k ConnectionProvider
	 * 
	 * @return p��stup k ConnectionProvider
	 */
	public ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

}
