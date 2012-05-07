package cz.smartfine.networklayer.links;

import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.net.ssl.SSLSocket;

/**
 * Třída pro zabezpečenou komunikacimezi serverem a klientem.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:44
 */
abstract public class SecuredLink implements ILink {

	/**
	 * Soket, který je používán pro přenos dat
	 */
	protected SSLSocket socket;
	/**
	 * Třída typu INetworkInterface, která využívá služeb této třídy
	 */
	protected INetworkInterface networkInterface;
	/**
	 * Stream vstupních dat
	 */
	protected InputStream in;
	/**
	 * Stream výstupních dat
	 */
	protected OutputStream out;
	/**
	 * Třída, která asynchroně přijímá data.
	 */
	protected Receiver receiver;
	/**
	 * Vlákno pro příjem dat
	 */
	protected Thread receiverThread;

	// ================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}
		if (socket != null) {
			socket.close();
		}
		if (receiverThread != null && receiverThread.isAlive()) {
			receiverThread.interrupt();
		}
	}

	/**
	 * Konstruktor.
	 */
	public SecuredLink() {
		this.socket = null;
	}

	/**
	 * Konstruktor.
	 */
	public SecuredLink(SSLSocket socket) {
		try {
			this.socket = socket;
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException ex) {
		}
	}

	// ================================================== GET/SET ==================================================//
	/**
	 * Nastavuje posluchače příjmu dat.
	 * 
	 * @param networkInterface
	 *            Základní rozhraní pro přenos dat.
	 */
	@Override
	public void setOnReceivedDataListener(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
		if (this.receiver != null) {
			this.receiver.setNetworkInterface(networkInterface);
		}
	}

	/**
	 * Odebírá posluchače příjmu dat.
	 * 
	 * @param networkInterface
	 *            Základní rozhraní pro přenos dat.
	 */
	@Override
	public void removeOnReceivedDataListener(INetworkInterface networkInterface) {
		this.networkInterface = null;
		if (this.receiver != null) {
			this.receiver.setNetworkInterface(null);
		}
	}

	// ================================================== VÝKONNÉ METODY ==================================================//
	/**
	 * Odesílá data na síť.
	 * 
	 * @param dataToSend
	 *            Data určená pro odeslání.
	 * @exception IOException
	 *                Selhání spojení během odesílání dat.
	 */
	@Override
	public void sendData(byte[] dataToSend) throws IOException {
		try {
			System.out.println("ANDROID: link.sendData()");
			out.write(dataToSend);
			out.flush();
		} catch (IOException e) {
			System.out.println("ANDROID:  link.sendData() EXCEPTION: "
					+ e.getMessage());
			closeConnection();
			throw e;
		}
	}

	/**
	 * Připojí se k serveru.
	 * 
	 * @exception IOException
	 *                Problém při vytváření socketu.
	 */
	@Override
	abstract public void connect() throws IOException;

	/**
	 * Začne naslouchat na soketu.
	 */
	@Override
	abstract public void listen();

	/**
	 * Odpojí se od serveru.
	 */
	@Override
	public void disconnect() {
		closeConnection();
	}

	/**
	 * Zjišťuje, zda existuje spojení se serverem.
	 */
	@Override
	public boolean isConnected() {
		if (socket != null) {
			return socket.isConnected();
		}
		return false;
	}

	// ================================================== PRIVÁTNÍ METODY ==================================================//
	/**
	 * Uzavře síťové spojení
	 */
	protected void closeConnection() {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception e) {
		}
		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception e) {
		}
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
		}
	}
}
