package cz.smartfine.server.business.client.mobile;
import cz.smartfine.server.business.client.IClientServer;
import cz.smartfine.server.business.client.ClientList;

/**
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:26
 */
public class MobileClientList extends ClientList {

	public MobileClientList(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Zjistí zda je připojeno mobilní zařízení s daným IMEI a vrátí jeho objekt
	 * serveru.
	 * 
	 * @param imei    IMEI mobilního zařízení.
	 */
	public IClientServer containIMEI(String imei){
		return null;
	}

}