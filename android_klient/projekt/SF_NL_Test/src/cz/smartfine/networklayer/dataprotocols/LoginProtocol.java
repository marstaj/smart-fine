package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.IGeoDataProtocolListener;
import cz.smartfine.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Pøedstavuje tøídu protokolu pro pøihlášení na server.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class LoginProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro pøístup k odesílání a pøíjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchaè událostí z této tøídy.
	 */
	private ILoginProtocolListener loginProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public LoginProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro pøenost dat.
	 * @param loginProtocolListener Posluchaè událostí z této tøídy.
	 */
	public LoginProtocol(INetworkInterface networkInterface, ILoginProtocolListener loginProtocolListener){
		this.networkInterface = networkInterface;
		this.loginProtocolListener = loginProtocolListener;
	}

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Pøihlašuje mobilního klienta k serveru.
	 * 
	 * @param badgeNumber    Služební èíslo pøihlašovaného policisty.
	 * @param pin    PIN pøihlašovaného policisty.
	 * @param imei    Identifikaèní èíslo mobilního zaøízení (IMEI), ze kterého se
	 * pøihlašuje.
	 */
	public void loginToServer(int badgeNumber, int pin, String imei){

	}

	/**
	 * Odhlašuje mobilního klienta ze serveru.
	 */
	public void logoutFromServer(){

	}

	/**
	 * Handler události ukonèení spojení.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

	/**
	 * Odebere posluchaèe událostí protokolu pro pøihlášení na server.
	 * 
	 * @param loginProtocolListener    Posluchaè událostí z pøihlašovacího protokolu.
	 */
	public void removeLoginProtocolListener(ILoginProtocolListener loginProtocolListener){

	}

	/**
	 * Pøidá posluchaèe událostí protokolu pro pøihlášení na server.
	 * 
	 * @param loginProtocolListener    Posluchaè událostí z pøihlašovacího protokolu.
	 */
	public void setLoginProtocolListener(ILoginProtocolListener loginProtocolListener){

	}

}