package cz.smartfine.android;

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
import cz.smartfine.android.model.PrefManager;
import cz.smartfine.android.model.util.Toaster;
import cz.smartfine.android.networklayer.ConnectionProvider;
import cz.smartfine.android.networklayer.links.SecuredMobileClientLink;
import cz.smartfine.android.networklayer.networkinterface.SimpleNetworkInterface;
import cz.smartfine.networklayer.links.SecuredClientLink;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import android.app.Application;
import android.content.Context;

/**
 * Třída představující objekt typu Application, který slouží k uchovávání
 * přístupů k nejruznějším objektům potřebným při běhu aplikace. Objekt
 * představuje samotnou aplikaci, inicializuje také připojení k serveru.
 * 
 * @author Martin Štajner
 * 
 */
public class MyApp extends Application {

	/**
	 * Přístup k objektu, který uchovává parkovacím lístky
	 */
	private ITicketDAO fileTicketDAO;

	/**
	 * Přístup k objektu, který uchovává geolokační data
	 */
	private ILocationDAO fileLocationDAO;

	/**
	 * Přistup k objektu pracujícím s fotografiemi
	 */
	private IPhotoDAO photoDAO;

	/**
	 * Pristup k objektu, který uchovává nejčastější hodnoty
	 */
	private IFreqValuesDAO fileFreqValuesDAO;

	/**
	 * Pristup k objektu, který uchovává zákony
	 */
	private IFreqValuesDAO fileLawDAO;

	/**
	 * Instance poskytovatele připojení na server
	 */
	private ConnectionProvider connectionProvider;

	/**
	 * Kontext aplikace
	 */
	private Context appContext;

	// ==================================== INITIALIZATON ==================================== //

	/**
	 * Metoda inicializuje objekt MyApp a je volána při jeho vytváření.
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		appContext = getApplicationContext();

		// příznak, zda aplikace bezi poprve, ci nikoliv
		boolean firstRun = PrefManager.getInstance().isFirtstRun(appContext);

		// Nastavi kontext aplikace Toasteru, aby mohl toastovat
		Toaster.appContext = appContext;

		// Vytvoří FileTicketDAO pro práci s parkovacími lístky a načte soubor s lístky
		try {
			fileTicketDAO = new FileTicketDAO(appContext);
			fileTicketDAO.loadAllTickets();
		} catch (Exception e) {
			// Chybová hláška se zobrazí pouze tehdy, nejde li o první zpuštění.
			if (!firstRun) {
				Toaster.toast("Nepodařilo se načíst uložené lístky", Toaster.SHORT);
			}
		}

		// Vytvori photoDAO pro praci s fotografiemi
		try {
			photoDAO = new PhotoDAO(appContext);
		} catch (Exception e) {
			Toaster.toast("Nepodařilo se vytvořit složku pro ukladání fotografií", Toaster.SHORT);
		}

		// Vytvori FileFreqValuesDAO pro praci s nejcastejsimi hodnotami a nacte je
		fileFreqValuesDAO = new FileFreqValuesDAO(appContext);
		fileFreqValuesDAO.loadValues();

		// Vytvori FileLawDAO pro praci s nejcastejsimi hodnotami zákonů přestupků a nacte je
		fileLawDAO = new FileLawDAO(appContext);
		fileLawDAO.loadValues();

		// Vytvoří FileLocationDAO pro práci s polohou policistu
		try {
			fileLocationDAO = new FileLocationDAO(appContext);
			fileLocationDAO.loadAllWaypoints();
		} catch (Exception e) {
			// Chybová hláška se zobrazí pouze tehdy, nejde li o první zpuštění.
			if (!firstRun) {
				Toaster.toast("Nepodařilo se načíst informace o poloze.", Toaster.SHORT);
			}
		}
	}

	// ==================================== OTHER ==================================== //

	/**
	 * Metoda inicializuje poskytovatele připojení na server a poskytuje k němu
	 * přístup.
	 */
	public void inicilizeConnectionToServer() throws Exception {

		// Vytahne adresu z preferences
		String address = PrefManager.getInstance().getSyncUrl(this);
		InetSocketAddress inetAddress = new InetSocketAddress(address, 25000);

		// pristup k certifikatu
		InputStream in = this.getResources().openRawResource(R.raw.ssltestcert);

		SecuredClientLink ilink = new SecuredMobileClientLink(inetAddress, in, "ssltest");
		INetworkInterface ni = new SimpleNetworkInterface();

		connectionProvider = new ConnectionProvider(this, ilink, ni);
	}

	// ==================================== GETTERY SETTERY ==================================== //

	/**
	 * Vrátí přístup k objektu, který uchovává parkovací lístky
	 * 
	 * @return Přístup k TicketDAO
	 */
	public ITicketDAO getTicketDao() {
		return fileTicketDAO;
	}

	/**
	 * Vrátí přístup k objektu, který pracuje s fotografiemi
	 * 
	 * @return Přístup k PhotoDAO
	 */
	public IPhotoDAO getPhotoDAO() {
		return photoDAO;
	}

	/**
	 * Vrátí přístup k objektu, který uchovává nejčastější hodnoty
	 * 
	 * @return Přístup k FreqValuesDAO
	 */
	public IFreqValuesDAO getFreqValuesDAO() {
		return fileFreqValuesDAO;
	}

	/**
	 * Vrátí přístup k objektu, který uchovává zákony
	 * 
	 * @return Přístup k LawDAO
	 */
	public IFreqValuesDAO getLawDAO() {
		return fileLawDAO;
	}

	/**
	 * Vrátí přístup k objektu, který uchovává geolokační data
	 * 
	 * @return Přístup k LocationDAO
	 */
	public ILocationDAO getLocationDAO() {
		return fileLocationDAO;
	}

	/**
	 * Vrátí přístup k poskytovateli komunikaci se serverem
	 * 
	 * @return Přístup k ConnectionProvider
	 */
	public ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

}
