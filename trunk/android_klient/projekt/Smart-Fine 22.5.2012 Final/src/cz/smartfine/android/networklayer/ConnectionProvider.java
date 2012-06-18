package cz.smartfine.android.networklayer;

import android.content.Context;
import cz.smartfine.android.networklayer.business.LoginProvider;
import cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener;
import cz.smartfine.networklayer.model.mobile.LoginFailReason;
import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.networklayer.links.SecuredClientLink;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.InterThreadType;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Zprostředkovává přístup k základním síťovým službám.
 *
 * @author Pavel Brož
 * @version 1.0
 */
public class ConnectionProvider {

    /**
     * Rozhraní pro NetworkInterface.
     */
    private SecuredClientLink mLink;
    /**
     * Síťové rozhraní pro datové protokoly.
     */
    private INetworkInterface mNetworkInterface;
    /**
     * Aplikační kontext.
     */
    private Context appContext;

    // ================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    public void finalize() throws Throwable {
    }

    /**
     * Konstruktor.
     *
     * @param appContext Kontext aplikace.
     * @param link Objekt implementující ILink pro transfer dat.
     * @param networkInterface Objekt reprezentující rozhraní, se kterým mohou komunikovat třídy datových protokolů.
     */
    public ConnectionProvider(Context appContext, SecuredClientLink link, INetworkInterface networkInterface) {
        this.appContext = appContext;
        this.mLink = link;
        this.mNetworkInterface = networkInterface;
        this.mNetworkInterface.setLink(link);
    }

    // ================================================== GET/SET ==================================================//
    /**
     * Vrací rozhraní pro transfer dat přes síť.
     */
    public ILink getLink() {
        return mLink;
    }

    /**
     * Vrací základní rozhraní pro komunikaci datových protokolů se serverem.
     */
    public INetworkInterface getNetworkInterface() {
        return mNetworkInterface;
    }

    // ================================================== VÝKONNÉ METODY ==================================================//
    /**
     * Ukončí spojení na server.
     */
    public void disconnect() {
        mLink.disconnect();
    }

    /**
     * Zjišťuje, zda je vytvořen a připojen socket.
     */
    public boolean isConnected() {
        return mLink.isConnected();
    }

    /**
     * Vytvoří spojení na server.
     */
    public boolean connect() {
        try {
            mLink.connect();
            return mLink.isConnected();
        } catch (IOException e) {
        	mLink.disconnect(); //
            return false;
        }
    }

    /**
     * Vytvoří spojení na server a přihlásí klienta.
     *
     * @return true, pokud se podařilo připojit a zároveň přihlásit, false pokud se nepodařilo připojit, nebo se nepodařilo přihlásit nebo nejsou k dispozici přihlašovací údaje.
     */
    public boolean connectAndLogin() {
        if (LoginProvider.isAvaibleLoginInformation(this.appContext)) {
            if (!connect()) {
                return false;
            }
            final InterThreadType<Boolean> loginResult = new InterThreadType<Boolean>(); // proměnná, která pozastaví běh vlákna, dokud nebude znám výsledek přihlášení
            // posluchač událostí z proměnné lp (LoginProvideru), který
            // nastavuje loginResult//
            ILoginProviderListener lpl = new ILoginProviderListener() {

                @Override
                public void onMessageSent() {
                }

                @Override
                public void onLogout() {
                }

                @Override
                public void onLoginFailed(LoginFailReason reason) {
                    try {
                        loginResult.put(false); // přihlášení neúspěšné
                    } catch (InterruptedException e) {
                    }
                }

                @Override
                public void onLoginConfirmed() {
                    try {
                        loginResult.put(true); // přihlášení úspěšné
                    } catch (InterruptedException e) {
                    }
                }

                @Override
                public void onConnectionTerminated() {
                    try {
                        loginResult.put(false); // přihlášení neúspěšné
                    } catch (InterruptedException e) {
                    }
                }
            };

            LoginProvider lp = new LoginProvider(this.getNetworkInterface(), this.appContext, lpl); // vytvoření login provideru
            // odeslání přihlašovací zprávy
            lp.login(LoginProvider.getBadgeNumber(this.appContext), LoginProvider.getPIN(this.appContext), LoginProvider.getIMEI(this.appContext));

            // zde dojde k pozastavení vlákna, dokud nebude znám výsledek přihlášení, poté se hodnota vrátí
            try {
                return loginResult.get();
            } catch (InterruptedException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Nastaví novou adresu serveru.
     *
     * @param address Nová adresa.
     */
    public void setNewAddress(InetSocketAddress address) {
        if (mLink != null) {
            mLink.setAddress(address);
        }
    }
}