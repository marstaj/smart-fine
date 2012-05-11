package cz.smartfine.android.networklayer.links;

import cz.smartfine.networklayer.links.SecuredClientLink;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;

/**
 * Třída pro zabezpečenou komunikaci přes WiFi nebo mobilní síť.
 *
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:44
 */
public class SecuredMobileClientLink extends SecuredClientLink {

    // ================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * Konstruktor.
     *
     * @param address Adresa serveru.
     * @param keyStoreStream InputStream obsahující KS.
     * @param keyStorePassword Heslo do KS.
     * @exception KeyManagementException Problém při inicializaci kontextu SSL.
     * @exception CertificateException Problém při načítání KS ze streamu.
     * @exception IOException Problém při načítání KS ze streamu.
     * @exception KeyStoreException Problém při vytváření KS.
     */
    public SecuredMobileClientLink(InetSocketAddress address, InputStream keyStoreStream, String keyStorePassword) throws KeyStoreException, CertificateException, IOException, KeyManagementException {
        super("TLSv1", "bks", address, keyStoreStream, keyStorePassword);
    }
}
