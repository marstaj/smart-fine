package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * P�edstavuje t��du protokolu pro ov��en� identity.
 * @author Pavel Bro�
 * @version 1.0
 */
public class AuthenticationProtocol implements IDataProtocol {

	/**
	 * Rozhran� pro p��stup k odes�l�n� a p��j�m�n� dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� z t�to t��dy.
	 */
	private IAuthenticationProtocolListener authenticationProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public AuthenticationProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhran� pro p�enost dat.
	 * @param authenticationProtocolListener Poslucha� ud�lost� z t�to t��dy.
	 */
	public AuthenticationProtocol(INetworkInterface networkInterface, IAuthenticationProtocolListener authenticationProtocolListener){
		this.networkInterface = networkInterface;
		this.authenticationProtocolListener = authenticationProtocolListener;
	}

	
	/**
	 * Odpoj� datov� protokol od z�kladn�ho protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Ov��uje identitu policisty.
	 * 
	 * @param badgeNumber    Slu�ebn� ��slo policisty.
	 * @param pin    PIN policisty.
	 */
	public void authenticate(int badgeNumber, int pin){

	}


	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

	/**
	 * Odebere poslucha�e ud�lost� protokolu pro ov��en� identity.
	 * 
	 * @param authenticationProtocolListener    Poslucha� ud�lost� z autentiza�n�ho protokolu.
	 */
	public void removeAuthenticationProtocolListener(IAuthenticationProtocolListener authenticationProtocolListener){

	}

	/**
	 * P�id� poslucha�e ud�lost� protokolu pro ov��en� identity.
	 * 
	 * @param authenticationProtocolListener    Poslucha� ud�lost� z autentiza�n�ho protokolu.
	 */
	public void setAuthenticationProtocolListener(IAuthenticationProtocolListener authenticationProtocolListener){

	}

}