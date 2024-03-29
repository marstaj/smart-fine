package cz.smartfine.networklayer.links;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.*;

/**
 * Třída pro zabezpečenou komunikaci klienta.
 *
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:44
 */
public class SecuredClientLink extends SecuredLink {

    /**
     * SSL kontext pro šifrovanou komunikaci
     */
    protected SSLContext context;
    /**
     * Továrna na ssl sokety
     */
    protected SSLSocketFactory socketFactory;
    /**
     * Key Store důvěryhodných certifikačních autorit pro ověření serveru
     */
    protected KeyStore trustedKS;
    /**
     * Třída pro ověřování certifikátu serveru
     */
    protected TrustManagerFactory trstMngrFactory;
    /**
     * Adresa serveru
     */
    protected InetSocketAddress address;

    // ================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * Konstruktor.
     *
     * @param sslProtocol Protokol na jakém se bude komunikovat.
     * @param keyStoreType Typ key storu.
     * @param address Adresa serveru.
     * @param keyStoreStream InputStream obsahující KS.
     * @param keyStorePassword Heslo do KS.
     * @exception KeyManagementException Problém při inicializaci kontextu SSL.
     * @exception CertificateException Problém při načítání KS ze streamu.
     * @exception IOException Problém při načítání KS ze streamu.
     * @exception KeyStoreException Problém při vytváření KS.
     */
    public SecuredClientLink(String sslProtocol, String keyStoreType, InetSocketAddress address, InputStream keyStoreStream, String keyStorePassword) throws KeyStoreException, CertificateException, IOException, KeyManagementException {
        super();

        this.address = address;
        try {
            context = SSLContext.getInstance(sslProtocol);

            trustedKS = KeyStore.getInstance(keyStoreType);
            trustedKS.load(keyStoreStream, keyStorePassword.toCharArray());

            trstMngrFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            trstMngrFactory.init(trustedKS);

            context.init(null, trstMngrFactory.getTrustManagers(), new SecureRandom());
            socketFactory = context.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {}
    }

    // ================================================== VÝKONNÉ METODY ==================================================//
    /**
     * Připojí se k serveru.
     *
     * @exception IOException Problém při vytváření socketu.
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
        receiver = new LinkReceiver(this.in, this.networkInterface, this);
        receiverThread = new Thread(receiver, "linkReceiverThread");
        receiverThread.start();
    }

    /**
     * Vrátí adresu pro soket.
     *
     * @return Adresa serveru.
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     * Nastaví adresu pro soket.
     *
     * @param address Nová adresa serveru.
     */
    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }
}
