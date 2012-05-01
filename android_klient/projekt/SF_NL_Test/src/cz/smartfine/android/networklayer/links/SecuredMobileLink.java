package cz.smartfine.android.networklayer.links;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import cz.smartfine.networklayer.links.Receiver;
import cz.smartfine.networklayer.links.SecuredLink;

/**
 * Třída pro zabezpečenou komunikaci přes WiFi nebo mobilní síť.
 * 
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:44
 */
public class SecuredMobileLink extends SecuredLink {

	/**
	 * SSL kontext pro šifrovanou komunikaci
	 */
	private SSLContext context;
	/**
	 * Továrna na ssl sokety
	 */
	private SSLSocketFactory socketFactory;

	/**
	 * Key Store důvěryhodných certifikačních autorit pro ověření serveru
	 */
	private KeyStore trustedKS;
	/**
	 * Třída pro ověřování certifikátu serveru
	 */
	private TrustManagerFactory trstMngrFactory;

	/**
	 * Adresa serveru
	 */
	private InetSocketAddress address;

	// ================================================== KONSTRUKTORY &
	// DESTRUKTORY ==================================================//

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Konstruktor.
	 * 
	 * @param address
	 *            Adresa serveru.
	 * @param keyStoreStream
	 *            InputStream obsahující KS.
	 * @param keyStorePassword
	 *            Heslo do KS.
	 * @exception KeyManagementException
	 *                Problém při inicializaci kontextu SSL.
	 * @exception CertificateException
	 *                Problém při načítání KS ze streamu.
	 * @exception IOException
	 *                Problém při načítání KS ze streamu.
	 * @exception KeyStoreException
	 *                Problém při vytváření KS.
	 */
	public SecuredMobileLink(InetSocketAddress address,
			InputStream keyStoreStream, String keyStorePassword)
			throws KeyStoreException, CertificateException, IOException,
			KeyManagementException {
		super();

		this.address = address;
		try {
			context = SSLContext.getInstance("TLSv1");

			trustedKS = KeyStore.getInstance("bks");
			trustedKS.load(keyStoreStream, keyStorePassword.toCharArray());

			trstMngrFactory = TrustManagerFactory.getInstance(KeyManagerFactory
					.getDefaultAlgorithm());
			trstMngrFactory.init(trustedKS);

			context.init(null, trstMngrFactory.getTrustManagers(),
					new SecureRandom());
			socketFactory = context.getSocketFactory();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	// ================================================== VÝKONNÉ METODY
	// ==================================================//

	/**
	 * Připojí se k serveru.
	 * 
	 * @exception IOException
	 *                Problém při vytváření socketu.
	 */
	@Override
	public void connect() throws IOException {
		closeConnection();

		socket = (SSLSocket) socketFactory.createSocket();
		socket.connect(address);

		in = socket.getInputStream();
		out = socket.getOutputStream();

		listen(); // spuštění příjmu dat
	}

	/**
	 * Začne naslouchat na soketu.
	 */
	@Override
	public void listen() {
		receiver = new Receiver(this.in, this.networkInterface, this);
		receiverThread = new Thread(receiver, "linkReceiverThread");
		receiverThread.start();
	}

}
