package cz.smartfine.server.business.client;

/**
 * Třída pro práci s připojenými klienty.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:25
 */
public class ClientList {

	public ClientList(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Ukončí všechna spojení s klienty.
	 */
	public void closeAll(){

	}

	/**
	 * Zjistí zda, je připojen policista s daným služebním číslem a vrátí objekt
	 * serveru.
	 * 
	 * @param badgeNumber    Služební číslo policisty připojeného k serveru.
	 */
	public IClientServer containBadgeNumber(int badgeNumber){
		return null;
	}

	/**
	 * Vloží server pro klienta mezi připojené servery.
	 * 
	 * @param clientServer    Objekt serveru, který tvoří protistranu klientovy.
	 */
	public synchronized void put(IClientServer clientServer){

	}

	/**
	 * Odebere server pro klienta ze seznamu připojených serverů.
	 * 
	 * @param clientServer    Objekt serveru, který tvoří protistranu klientovy.
	 */
	public synchronized void remove(IClientServer clientServer){

	}

}