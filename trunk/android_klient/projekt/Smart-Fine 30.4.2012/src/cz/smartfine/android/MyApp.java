package cz.smartfine.android;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import cz.smartfine.android.R;
import cz.smartfine.android.dao.FileFreqValuesDAO;
import cz.smartfine.android.dao.FileLawDAO;
import cz.smartfine.android.dao.FileLocationDAO;
import cz.smartfine.android.dao.FileTicketDAO;
import cz.smartfine.android.dao.PhotoDAO;
import cz.smartfine.android.dao.interfaces.IFreqValuesDAO;
import cz.smartfine.android.dao.interfaces.ILocationDAO;
import cz.smartfine.android.dao.interfaces.IPhotoDAO;
import cz.smartfine.android.dao.interfaces.ITicketDAO;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.ConnectionProvider;
import cz.smartfine.android.networklayer.links.SecuredMobileLink;
import cz.smartfine.android.networklayer.networkinterface.SimpleNetworkInterface;
import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.android.model.Settings;
import android.app.Application;

/**
 * Třída reprezentující aplikaci
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
	 * Pristup k fileLocationDAO
	 */
	private ILocationDAO fileLocationDAO;

	/**
	 * Přistup k photoDAO
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

		// Vytvoří FileTicketDAO pro práci s parkovacími lístky a načte soubor s lístky
		try {
			fileTicketDAO = new FileTicketDAO(this);
			fileTicketDAO.loadAllTickets();
		} catch (FileNotFoundException e) {
			Toaster.toast("Soubor nenalezen", Toaster.SHORT);
		} catch (Exception e) {
			Toaster.toast("Nepodařilo se načíst uložené lístky", Toaster.SHORT);
		}

		// Vytvori photoDAO pro praci s fotografiemi
		try {
			photoDAO = new PhotoDAO(this);
		} catch (Exception e) {
			Toaster.toast("Nepodařilo se vytvořit složku pro ukladání fotografií", Toaster.SHORT);
		}

		// Vytvori FileFreqValuesDAO pro praci s nejcastejsimi hodnotami a nacte je
		fileFreqValuesDAO = new FileFreqValuesDAO(this);
		fileFreqValuesDAO.loadValues();

		// Vytvori FileLawDAO pro praci s nejcastejsimi hodnotami zákonů přestupků a nacte je
		fileLawDAO = new FileLawDAO(this);
		fileLawDAO.loadValues();

		// Vytvoří FileLocationDAO pro práci s polohou policistu
		try {
			fileLocationDAO = new FileLocationDAO(this);
			fileLocationDAO.loadAllWaypoints();
		} catch (FileNotFoundException e) {
			Toaster.toast("Soubor location nenalezen", Toaster.SHORT);
		} catch (Exception e) {
			Toaster.toast("Nepodařilo se načíst informace o poloze.", Toaster.SHORT);
		}

	}

	/**
	 * Inicializuje ConnectionProvider
	 */
	public void inicilizeConnectionToServer() throws Exception {
		Settings settings;

		// Pak se bude vytahovat ze settings TODO
		//		settings.getSyncUrl(this);
		InetSocketAddress address = new InetSocketAddress("192.168.0.121", 25000);

		// TODO predelat RAW na ASSETS???
		InputStream in = this.getResources().openRawResource(R.raw.ssltestcert);

		ILink ilink = new SecuredMobileLink(address, in, "ssltest");
		SimpleNetworkInterface ni = new SimpleNetworkInterface();

		connectionProvider = new ConnectionProvider(this, ilink, ni);
	}

	// ==================================== GETTERY SETTERY ==================================== //

	/**
	 * Vrátí přístup k DAO
	 * 
	 * @return přístup k DAO
	 */
	public ITicketDAO getTicketDao() {
		return fileTicketDAO;
	}

	/**
	 * Vrátí přístup k PhotoDAO
	 * 
	 * @return přístup k PhotoDAO
	 */
	public IPhotoDAO getPhotoDAO() {
		return photoDAO;
	}

	/**
	 * Vrátí přístup k FileFreqValuesDAO
	 * 
	 * @return přístup k FileFreqValuesDAO
	 */
	public IFreqValuesDAO getFreqValuesDAO() {
		return fileFreqValuesDAO;
	}

	/**
	 * Vrátí přístup k FileLawDAO
	 * 
	 * @return přístup k FileLawDAO
	 */
	public IFreqValuesDAO getLawDAO() {
		return fileLawDAO;
	}
	
	/**
	 * Vrátí přístup k FileLocationDAO
	 * 
	 * @return přístup k FileLocationDAO
	 */
	public ILocationDAO getLocationDAO() {
		return fileLocationDAO;
	}

	/**
	 * Vrátí přístup k ConnectionProvider
	 * 
	 * @return přístup k ConnectionProvider
	 */
	public ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

}
