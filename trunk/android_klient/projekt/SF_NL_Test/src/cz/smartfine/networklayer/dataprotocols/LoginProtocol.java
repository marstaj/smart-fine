package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.IGeoDataProtocolListener;
import cz.smartfine.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * P�edstavuje t��du protokolu pro p�ihl�en� na server.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class LoginProtocol implements IDataProtocol {

	/**
	 * Rozhran� pro p��stup k odes�l�n� a p��j�m�n� dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� z t�to t��dy.
	 */
	private ILoginProtocolListener loginProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public LoginProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhran� pro p�enost dat.
	 * @param loginProtocolListener Poslucha� ud�lost� z t�to t��dy.
	 */
	public LoginProtocol(INetworkInterface networkInterface, ILoginProtocolListener loginProtocolListener){
		this.networkInterface = networkInterface;
		this.loginProtocolListener = loginProtocolListener;
	}

	/**
	 * Odpoj� datov� protokol od z�kladn�ho protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * P�ihla�uje mobiln�ho klienta k serveru.
	 * 
	 * @param badgeNumber    Slu�ebn� ��slo p�ihla�ovan�ho policisty.
	 * @param pin    PIN p�ihla�ovan�ho policisty.
	 * @param imei    Identifika�n� ��slo mobiln�ho za��zen� (IMEI), ze kter�ho se
	 * p�ihla�uje.
	 */
	public void loginToServer(int badgeNumber, int pin, String imei){

	}

	/**
	 * Odhla�uje mobiln�ho klienta ze serveru.
	 */
	public void logoutFromServer(){

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
	 * Odebere poslucha�e ud�lost� protokolu pro p�ihl�en� na server.
	 * 
	 * @param loginProtocolListener    Poslucha� ud�lost� z p�ihla�ovac�ho protokolu.
	 */
	public void removeLoginProtocolListener(ILoginProtocolListener loginProtocolListener){

	}

	/**
	 * P�id� poslucha�e ud�lost� protokolu pro p�ihl�en� na server.
	 * 
	 * @param loginProtocolListener    Poslucha� ud�lost� z p�ihla�ovac�ho protokolu.
	 */
	public void setLoginProtocolListener(ILoginProtocolListener loginProtocolListener){

	}

}