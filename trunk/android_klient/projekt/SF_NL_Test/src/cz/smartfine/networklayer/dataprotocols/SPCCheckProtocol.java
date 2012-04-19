package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.ISPCCheckProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Pøedstavuje tøídu pro kontrolu odcizení pøenosné parkovací karty (PPK angl. SPC
 * - SUBSCRIBER PARKING CARD).
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SPCCheckProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro pøístup k odesílání a pøíjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchaè událostí z této tøídy.
	 */
	private ISPCCheckProtocolListener spcCheckProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public SPCCheckProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro pøenos dat.
	 * @param spcCheckProtocolListener Posluchaè událostí z této tøídy.
	 */
	public SPCCheckProtocol(INetworkInterface networkInterface, ISPCCheckProtocolListener spcCheckProtocolListener){
		this.networkInterface = networkInterface;
		this.spcCheckProtocolListener = spcCheckProtocolListener;
	}

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Zjistí stav pøenosné parkovací karty tj. jestli je hášena jako odcizená èi
	 * nikoliv.
	 * 
	 * @param spcNumber    Èíslo pøenosné parkovací karty.
	 */
	public void checkSPC(String spcNumber){

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
	 * Odebere posluchaèe událostí protokolu pro kontrolu odcizení PPK.
	 * 
	 * @param spcCheckProtocolListener    Posluchaè událostí protokolu pro kontrolu
	 * odcizení PPK.
	 */
	public void removeSPCCheckProtocolListener(ISPCCheckProtocolListener spcCheckProtocolListener){

	}

	/**
	 * Pøidá posluchaèe událostí protokolu pro kontrolu odcizení PPK.
	 * 
	 * @param spcCheckProtocolListener    Posluchaè událostí protokolu pro kontrolu
	 * odcizení PPK.
	 */
	public void setSPCCheckProtocolListener(ISPCCheckProtocolListener spcCheckProtocolListener){

	}

}