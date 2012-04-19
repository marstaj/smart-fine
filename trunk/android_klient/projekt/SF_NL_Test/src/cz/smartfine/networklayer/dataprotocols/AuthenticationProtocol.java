package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Pøedstavuje tøídu protokolu pro ovìøení identity.
 * @author Pavel Brož
 * @version 1.0
 */
public class AuthenticationProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro pøístup k odesílání a pøíjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchaè událostí z této tøídy.
	 */
	private IAuthenticationProtocolListener authenticationProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public AuthenticationProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro pøenost dat.
	 * @param authenticationProtocolListener Posluchaè událostí z této tøídy.
	 */
	public AuthenticationProtocol(INetworkInterface networkInterface, IAuthenticationProtocolListener authenticationProtocolListener){
		this.networkInterface = networkInterface;
		this.authenticationProtocolListener = authenticationProtocolListener;
	}

	
	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Ovìøuje identitu policisty.
	 * 
	 * @param badgeNumber    Služební èíslo policisty.
	 * @param pin    PIN policisty.
	 */
	public void authenticate(int badgeNumber, int pin){

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
	 * Odebere posluchaèe událostí protokolu pro ovìøení identity.
	 * 
	 * @param authenticationProtocolListener    Posluchaè událostí z autentizaèního protokolu.
	 */
	public void removeAuthenticationProtocolListener(IAuthenticationProtocolListener authenticationProtocolListener){

	}

	/**
	 * Pøidá posluchaèe událostí protokolu pro ovìøení identity.
	 * 
	 * @param authenticationProtocolListener    Posluchaè událostí z autentizaèního protokolu.
	 */
	public void setAuthenticationProtocolListener(IAuthenticationProtocolListener authenticationProtocolListener){

	}

}