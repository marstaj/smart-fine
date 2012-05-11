package cz.smartfine.android.networklayer.dataprotocols;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import cz.smartfine.android.model.Waypoint;
import cz.smartfine.android.networklayer.business.listeners.IGeoDataProtocolListener;
import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.MessageBuilder;

/**
 * Představuje třídu protokolu pro přenost geolokačních dat.
 * 
 * @author Pavel Brož
 * @version 1.0
 */
public class GeoDataProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro přístup k odesílání a příjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchač událostí z této třídy.
	 */
	private IGeoDataProtocolListener geoDataProtocolListener;

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
	public GeoDataProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface
	 *            Rozhraní pro přenos dat.
	 * @param geoDataProtocolListener
	 *            Posluchač událostí z této třídy.
	 */
	public GeoDataProtocol(INetworkInterface networkInterface,
			IGeoDataProtocolListener geoDataProtocolListener) {
		this.networkInterface = networkInterface;
		this.geoDataProtocolListener = geoDataProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); // zaregistrování
																// se jako
																// posluchač
	}

	// ================================================== GET/SET
	// ==================================================//

	/**
	 * Odebere posluchače událostí protokolu pro odesílání geolokačních dat.
	 * 
	 * @param geoDataProtocolListener
	 *            Posluchač událostí z protokolu pro odesílání geolokačních dat.
	 */
	public void removeGeoDataProtocolListener(
			IGeoDataProtocolListener geoDataProtocolListener) {
		this.geoDataProtocolListener = null;
	}

	/**
	 * Přidá posluchače událostí protokolu pro odesílání geolokačních dat.
	 * 
	 * @param geoDataProtocolListener
	 *            Posluchač událostí z protokolu pro odesílání geolokačních dat.
	 */
	public void setGeoDataProtocolListener(
			IGeoDataProtocolListener geoDataProtocolListener) {
		this.geoDataProtocolListener = geoDataProtocolListener;
	}

	// ================================================== HANDLERY UDÁLOSTÍ
	// ==================================================//

	/**
	 * Handler události ukončení spojení.
	 */
	public void onConnectionTerminated() {
		if (geoDataProtocolListener != null) {
			geoDataProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 * 
	 * @param sentData
	 *            Odeslaná data.
	 */
	public void onMessageSent(byte[] sentData) {
		if (geoDataProtocolListener != null) {
			geoDataProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler události příjmu dat.
	 * 
	 * @param receivedData
	 *            Přijmutá data uložená ve formě bytového pole.
	 */
	public void onReceivedData(byte[] receivedData) {
		// žádná data nepřijímá
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
	 * Odešle geolokační data na server.
	 * 
	 * @param geoData
	 *            Seznam geolokačních dat pro poslání na server.
	 * @throws IOException
	 */
	public void sendGeoData(List<Waypoint> geoData) throws IOException {
		if (networkInterface != null) {
			networkInterface.sendData(createGeoMessage(geoData), this);
		}
	}

	// ================================================== PRIVÁTNÍ METODY
	// ==================================================//

	protected byte[] createGeoMessage(List<Waypoint> geoData) throws IOException {
		MessageBuilder msg = new MessageBuilder();

		msg.putByte(MobileMessageIDs.ID_MSG_UPLOAD_GEO); // identifikátor zprávy

		ByteArrayOutputStream geoBytes = new ByteArrayOutputStream();
		ObjectOutputStream objOS = new ObjectOutputStream(geoBytes);
		objOS.writeObject(geoData); // serializuje geo data
		objOS.close();

		msg.putArrayWithIntLength(geoBytes.toByteArray()); // vložení pole serializované kolekce s geo daty
		
		geoBytes.close();
		return msg.getByteArray();
	}

}
