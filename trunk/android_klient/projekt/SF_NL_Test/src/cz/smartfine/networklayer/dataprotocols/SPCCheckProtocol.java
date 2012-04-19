package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.ISPCCheckProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * P�edstavuje t��du pro kontrolu odcizen� p�enosn� parkovac� karty (PPK angl. SPC
 * - SUBSCRIBER PARKING CARD).
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SPCCheckProtocol implements IDataProtocol {

	/**
	 * Rozhran� pro p��stup k odes�l�n� a p��j�m�n� dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� z t�to t��dy.
	 */
	private ISPCCheckProtocolListener spcCheckProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public SPCCheckProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhran� pro p�enos dat.
	 * @param spcCheckProtocolListener Poslucha� ud�lost� z t�to t��dy.
	 */
	public SPCCheckProtocol(INetworkInterface networkInterface, ISPCCheckProtocolListener spcCheckProtocolListener){
		this.networkInterface = networkInterface;
		this.spcCheckProtocolListener = spcCheckProtocolListener;
	}

	/**
	 * Odpoj� datov� protokol od z�kladn�ho protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Zjist� stav p�enosn� parkovac� karty tj. jestli je h�ena jako odcizen� �i
	 * nikoliv.
	 * 
	 * @param spcNumber    ��slo p�enosn� parkovac� karty.
	 */
	public void checkSPC(String spcNumber){

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
	 * Odebere poslucha�e ud�lost� protokolu pro kontrolu odcizen� PPK.
	 * 
	 * @param spcCheckProtocolListener    Poslucha� ud�lost� protokolu pro kontrolu
	 * odcizen� PPK.
	 */
	public void removeSPCCheckProtocolListener(ISPCCheckProtocolListener spcCheckProtocolListener){

	}

	/**
	 * P�id� poslucha�e ud�lost� protokolu pro kontrolu odcizen� PPK.
	 * 
	 * @param spcCheckProtocolListener    Poslucha� ud�lost� protokolu pro kontrolu
	 * odcizen� PPK.
	 */
	public void setSPCCheckProtocolListener(ISPCCheckProtocolListener spcCheckProtocolListener){

	}

}