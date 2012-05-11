package cz.smartfine.pc.networklayer;

import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.networklayer.model.pc.PCClientPermission;
import cz.smartfine.networklayer.model.pc.PCLoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.InterThreadType;
import cz.smartfine.pc.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.pc.networklayer.dataprotocols.LoginProtocol;
import cz.smartfine.pc.preferences.PCClientPreferences;
import java.io.IOException;

/**
 * Zprostředkovává přístup k základním síťovým službám.
 *
 * @author Pavel Brož
 * @version 1.0 @created 14-4-2012 18:48:46
 */
public class ConnectionProvider {

    /**
     * Rozhraní pro NetworkInterface.
     */
    private ILink mLink;
    /**
     * Síťové rozhraní pro datové protokoly.
     */
    private INetworkInterface mNetworkInterface;

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    /**
     * Konstruktor.
     *
     * @param link Objekt implementující ILink pro transfer dat.
     * @param networkInterface Objekt reprezentující rozhraní, se kterým mohou komunikovat třídy datových protokolů.
     */
    public ConnectionProvider(ILink link, INetworkInterface networkInterface) {
        this.mLink = link;
        this.mNetworkInterface = networkInterface;
        this.mNetworkInterface.setLink(link);
    }

    //================================================== GET/SET ==================================================//
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

    //================================================== VÝKONNÉ METODY ==================================================//
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
            return false;
        }
    }

    /**
     * Vytvoří spojení na server a přihlásí klienta.
     *
     * @return true, pokud se podařilo připojit a zároveň přihlásit, false pokud se nepodařilo připojit, nebo se nepodařilo přihlásit nebo nejsou k dispozici přihlašovací údaje.
     */
    public boolean connectAndLogin() {
        if (PCClientPreferences.areLoginDataSaved()) {
            if (!connect()) {
                return false;
            }
            final InterThreadType<Boolean> loginResult = new InterThreadType<Boolean>(); // proměnná,která pozastaví běh vlákna, dokud nebude znám výsledek přihlášení
            
            // posluchač událostí z proměnné lp (LoginProtocol), který
            // nastavuje loginResult//
            ILoginProtocolListener lpl = new ILoginProtocolListener() {

                @Override
                public void onMessageSent() {
                }

                @Override
                public void onLoginFailed(PCLoginFailReason reason) {
                    try {
                        loginResult.put(false); // přihlášení neúspěšné
                    } catch (InterruptedException e) {
                    }
                }

                @Override
                public void onLoginConfirmed(PCClientPermission permissions) {
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

            LoginProtocol lp = new LoginProtocol(this.getNetworkInterface(), lpl); // vytvoření login protocolu

            // odeslání přihlašovací zprávy
            lp.loginToServer(PCClientPreferences.getBadgeNumber(), PCClientPreferences.getPin());

            // zde dojde k pozastavení vlákna, dokud nebude znám výsledek
            // přihlášení, poté se hodnota vrátí
            try {
                return loginResult.get();
            } catch (InterruptedException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}