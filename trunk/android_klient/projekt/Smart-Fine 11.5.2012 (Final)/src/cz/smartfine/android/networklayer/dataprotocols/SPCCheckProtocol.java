package cz.smartfine.android.networklayer.dataprotocols;

import cz.smartfine.android.networklayer.business.listeners.ISPCCheckProtocolListener;
import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.MobileProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.mobile.SPCInfo;
import cz.smartfine.networklayer.model.mobile.SPCStatus;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.MessageBuilder;

/**
 * Představuje třídu pro kontrolu odcizení přenosné parkovací karty (PPK angl.
 * SPC - SUBSCRIBER PARKING CARD).
 * 
 * @author Pavel Brož
 * @version 1.0
 */
public class SPCCheckProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro přístup k odesílání a příjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchač událostí z této třídy.
	 */
	private ISPCCheckProtocolListener spcCheckProtocolListener;

	// ================================================== KONSTRUKTORY &
	// DESTRUKTORY ==================================================//

	public void finalize() throws Throwable {

	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface
	 *            Rozhraní pro přenost dat.
	 */
	public SPCCheckProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface
	 *            Rozhraní pro přenos dat.
	 * @param spcCheckProtocolListener
	 *            Posluchač událostí z této třídy.
	 */
	public SPCCheckProtocol(INetworkInterface networkInterface,
			ISPCCheckProtocolListener spcCheckProtocolListener) {
		this.networkInterface = networkInterface;
		this.spcCheckProtocolListener = spcCheckProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); // zaregistrování
																// se jako
																// posluchač
	}

	// ================================================== GET/SET
	// ==================================================//

	/**
	 * Odebere posluchače událostí protokolu pro kontrolu odcizení PPK.
	 * 
	 * @param spcCheckProtocolListener
	 *            Posluchač událostí protokolu pro kontrolu odcizení PPK.
	 */
	public void removeSPCCheckProtocolListener(
			ISPCCheckProtocolListener spcCheckProtocolListener) {
		this.spcCheckProtocolListener = null;
	}

	/**
	 * Přidá posluchače událostí protokolu pro kontrolu odcizení PPK.
	 * 
	 * @param spcCheckProtocolListener
	 *            Posluchač událostí protokolu pro kontrolu odcizení PPK.
	 */
	public void setSPCCheckProtocolListener(
			ISPCCheckProtocolListener spcCheckProtocolListener) {
		this.spcCheckProtocolListener = spcCheckProtocolListener;
	}

	// ================================================== HANDLERY UDÁLOSTÍ
	// ==================================================//

	/**
	 * Handler události ukončení spojení.
	 */
	public void onConnectionTerminated() {
		if (spcCheckProtocolListener != null) {
			spcCheckProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 * 
	 * @param sentData
	 *            Odeslaná data.
	 */
	public void onMessageSent(byte[] sentData) {
		if (spcCheckProtocolListener != null) {
			spcCheckProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler události příjmu dat.
	 * 
	 * @param receivedData
	 *            Přijmutá data uložená ve formě bytového pole.
	 */
	public void onReceivedData(byte[] receivedData) {
		// pokud není žádný posluchač není nutné zprávy zpracovávat//
		if (spcCheckProtocolListener != null) {

			// kontrola typu zprávy//
			switch (receivedData[0]) {
			case MobileMessageIDs.ID_MSG_STATUS_SPC: // úspěšné přihlášení//
				SPCStatus spcStatus; // stav PPK
				String spcNumber; // číslo PPK

				// zjištění stavu PPK//
				switch (receivedData[1]) {
				// PPK není hlášena jako odcizená//
				case MobileProtocolConstants.MSG_STATUS_SPC_STATUS_OK:
					spcStatus = SPCStatus.OK_SPC;
					break;
				// PPK je hlášena jako odcizená//
				case MobileProtocolConstants.MSG_STATUS_SPC_STATUS_STOLEN:
					spcStatus = SPCStatus.STOLEN_SPC;
					break;
				// nepodařilo se zjistit stav PPK//
				case MobileProtocolConstants.MSG_STATUS_SPC_STATUS_UKNOWN:
					spcStatus = SPCStatus.UKNOWN_SPC_STATUS;
					break;
				// neznámá hodnota//
				default:
					spcStatus = SPCStatus.UKNOWN_SPC_STATUS;
				}

				int spcNumLength = Conventer.byteArrayToInt(receivedData, 2); // zjištění
																				// délky
																				// pole
																				// s
																				// číslem
																				// PPK
				spcNumber = new String(receivedData, 6, spcNumLength); // převedení
																		// pole
																		// bytů
																		// na
																		// string

				spcCheckProtocolListener.onReceivedSPCInfo(new SPCInfo(
						spcNumber, spcStatus));
				break;
			}
		}
	}

	// ================================================== VÝKONNÉ METODY
	// ==================================================//

	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol() {
		if (networkInterface != null) {
			networkInterface.removeOnReceivedDataListener(this);
		}
	}

	/**
	 * Zjistí stav přenosné parkovací karty tj. jestli je hášena jako odcizená
	 * či nikoliv.
	 * 
	 * @param spcNumber
	 *            Číslo přenosné parkovací karty.
	 */
	public void checkSPC(String spcNumber) {
		if (networkInterface != null) {
			networkInterface.sendData(createSPCMessage(spcNumber), this);
		}
	}

	// ================================================== PRIVÁTNÍ METODY
	// ==================================================//

	/**
	 * Vytváří zprávu pro kontrolu odcizení PPK.
	 * 
	 * @param spcNumber
	 *            Číslo přenosné parkovací karty.
	 * @return Zpráva pro odeslání na server.
	 */
	protected byte[] createSPCMessage(String spcNumber) {
		MessageBuilder msg = new MessageBuilder();

		msg.putByte(MobileMessageIDs.ID_MSG_CHECK_SPC); // identifikátor zprávy
		msg.putArrayWithIntLength(spcNumber.getBytes()); // vloží délku pole a
															// pole se znaky
															// čísla PPK

		return msg.getByteArray();
	}

}
