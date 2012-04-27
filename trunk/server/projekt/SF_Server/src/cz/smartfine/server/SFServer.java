package cz.smartfine.server;
import cz.smartfine.server.business.MobileClientAcceptor;
import cz.smartfine.server.business.client.ClientList;
import cz.smartfine.server.business.PCClientAcceptor;
import cz.smartfine.server.business.TimeoutCloser;

/**
 * Hlavní třída aplikace Smart-Fine serveru.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:27
 */
public class SFServer {

	/**
	 * Třída přijímající síťová připojení na server od mobilních klientů.
	 */
	private static MobileClientAcceptor mobileClientAcceptor;
	/**
	 * Třída se seznamem připojených mobilních klientů.
	 */
	private static ClientList mobileClientList;
	/**
	 * Třída přijímající síťová připojení na server od PC klientů.
	 */
	private static PCClientAcceptor pcClientAcceptor;
	/**
	 * Třída se seznamem připojených PC klientů.
	 */
	private static ClientList pcClientList;
	/**
	 * Třída zajišťující zavření neaktivních síťových spojení.
	 */
	private static TimeoutCloser timeoutCloser;

	public SFServer(){

	}

	public void finalize() throws Throwable {

	}

	public static void main(String[] args){

	}

}