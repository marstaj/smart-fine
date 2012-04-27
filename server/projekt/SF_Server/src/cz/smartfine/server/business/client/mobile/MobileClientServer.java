package cz.smartfine.server.business.client.mobile;
import cz.smartfine.server.business.client.IClientServer;
import java.util.Date;

/**
 * Třída, která tvoří protistranu mobilnímu klientovy.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:26
 */
public class MobileClientServer implements IClientServer {

	public MobileClientServer(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Ukončí spojení s klientem.
	 */
	public void close(){

	}

	/**
	 * Vrací čas posledního kontaktu s klientem.
	 */
	public Date getLastContactTime(){
		return null;
	}

	/**
	 * Vrací služební číslo policisty, který je připojen k serveru.
	 */
	public int getBadgeNumber(){
		return 0;
	}

}