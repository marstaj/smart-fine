package cz.smartfine.networklayer.util;

import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;

/**
 * Třída zapouzdřuje zprávu z datového protokolu a referenci na datový protokol, který zprávu posílá.
 * @author Pavel Brož
 */
public class DataPackage {

	/**
	 * Zpráva z datového protokolu, která se má odeslat.
	 */
	private byte[] message;
	
	/**
	 * Datový protokol který zprávu odesílá.
	 */
	private IDataProtocol sender;
	
	/**
	 * Konstruktor.
	 * @param message Zpráva z datového protokolu.
	 * @param sender Datový protokol, který zprávu odesílá.
	 */
	public DataPackage(byte[] message, IDataProtocol sender) {
		super();
		this.message = message;
		this.sender = sender;
	}
	
	/**
	 * @return Zpráva.
	 */
	public byte[] getMessage() {
		return message;
	}
	
	/**
	 * @param Nastaví zprávu.
	 */
	public void setMessage(byte[] message) {
		this.message = message;
	}
	
	/**
	 * @return Odesílající datový protokol.
	 */
	public IDataProtocol getSender() {
		return sender;
	}
	
	/**
	 * @param sender Nastaví odesílající datový protokol.
	 */
	public void setSender(IDataProtocol sender) {
		this.sender = sender;
	}
	
	
}
