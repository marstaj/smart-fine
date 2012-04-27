package cz.smartfine.server.networklayer.mobile.dataprotocols;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.server.business.client.mobile.providers.listeners.IServerSPCCheckProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import smartfine.networklayer.business.listeners.ISPCCheckProtocolListener;
import smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Představuje třídu pro kontrolu odcizení přenosné parkovací karty (PPK angl. SPC
 * - SUBSCRIBER PARKING CARD).
 * @author Pavel Brož
 * @version 1.0
 * @created 27-4-2012 17:00:27
 */
public class ServerSPCCheckProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro přístup k odesílání a příjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchač událostí z této třídy.
	 */
	private IServerSPCCheckProtocolListener spcCheckProtocolListener;

	public ServerSPCCheckProtocol(){

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenost dat.
	 */
	public ServerSPCCheckProtocol(INetworkInterface networkInterface){

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenos dat.
	 * @param spcCheckProtocolListener    Posluchač událostí z této třídy.
	 */
	public ServerSPCCheckProtocol(INetworkInterface networkInterface, IServerSPCCheckProtocolListener spcCheckProtocolListener){

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
	 * Vytváří zprávu pro kontrolu odcizení PPK.
	 * @return Zpráva pro odeslání na server.
	 * 
	 * @param spcNumber    Číslo přenosné parkovací karty.
	 */
	protected byte[] createSPCMessage(String spcNumber){
		return 0;
	}

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Zjistí stav přenosné parkovací karty tj. jestli je hášena jako odcizená či
	 * nikoliv.
	 * 
	 * @param spcNumber    Číslo přenosné parkovací karty.
	 */
	public void checkSPC(String spcNumber){

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
	 * Odebere posluchače událostí protokolu pro kontrolu odcizení PPK.
	 * 
	 * @param spcCheckProtocolListener    Posluchač událostí protokolu pro kontrolu
	 * odcizení PPK.
	 */
	public void removeSPCCheckProtocolListener(IServerSPCCheckProtocolListener spcCheckProtocolListener){

	}

	/**
	 * Přidá posluchače událostí protokolu pro kontrolu odcizení PPK.
	 * 
	 * @param spcCheckProtocolListener    Posluchač událostí protokolu pro kontrolu
	 * odcizení PPK.
	 */
	public void setSPCCheckProtocolListener(IServerSPCCheckProtocolListener spcCheckProtocolListener){

	}

}