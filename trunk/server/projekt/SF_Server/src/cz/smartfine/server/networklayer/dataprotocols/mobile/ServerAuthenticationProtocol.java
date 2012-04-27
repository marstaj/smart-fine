package cz.smartfine.server.networklayer.mobile.dataprotocols;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerAuthenticationProtocolListener;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import smartfine.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Představuje třídu protokolu pro ověření identity.
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:24
 */
public class ServerAuthenticationProtocol implements IDataProtocol {

	/**
	 * Posluchač událostí z této třídy.
	 */
	private IServerAuthenticationProtocolListener authenticationProtocolListener;
	/**
	 * Rozhraní pro přístup k odesílání a příjímání dat.
	 */
	private INetworkInterface networkInterface;

	public ServerAuthenticationProtocol(){

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenost dat.
	 */
	public ServerAuthenticationProtocol(INetworkInterface networkInterface){

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenost dat.
	 * @param authenticationProtocolListener    Posluchač událostí z této třídy.
	 */
	public ServerAuthenticationProtocol(INetworkInterface networkInterface, IServerAuthenticationProtocolListener authenticationProtocolListener){

	}

	/**
	 * ================================================== KONSTRUKTORY & DESTRUKTORY
	 * ==================================================
	 * @exception Throwable
	 */
	public void finalize()
	  throws Throwable{

	}

	/**
	 * Ověřuje identitu policisty.
	 * 
	 * @param badgeNumber    Služební číslo policisty.
	 * @param pin    PIN policisty.
	 */
	public void authenticate(int badgeNumber, int pin){

	}

	/**
	 * Vytváří autentizační zprávu.
	 * @return Zpráva pro odeslání na server
	 * 
	 * @param badgeNumber    Služební číslo policisty.
	 * @param pin    PIN policisty.
	 */
	private byte[] createAuthenticationMessage(int badgeNumber, int pin){
		return 0;
	}

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Handler události ukončení spojení.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler události příjmu dat.
	 * 
	 * @param receivedData    Přijmutá data uložená ve formě bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

	/**
	 * Odebere posluchače událostí protokolu pro ověření identity.
	 * 
	 * @param authenticationProtocolListener    Posluchač událostí z autentizačního
	 * protokolu.
	 */
	public void removeAuthenticationProtocolListener(IServerAuthenticationProtocolListener authenticationProtocolListener){

	}

	/**
	 * Přidá posluchače událostí protokolu pro ověření identity.
	 * 
	 * @param authenticationProtocolListener    Posluchač událostí z autentizačního
	 * protokolu.
	 */
	public void setAuthenticationProtocolListener(IServerAuthenticationProtocolListener authenticationProtocolListener){

	}

}