/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.smartfine.pc;

import cz.smartfine.pc.networklayer.ConnectionProvider;
import cz.smartfine.pc.view.MainFrame;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Hlavní třída aplikace.
 *
 * @author Pavel Brož
 */
public class SFPCClient {

    private Localization localization;
    private static SFPCClient application;
    private MainFrame mainFrame;
    private ConnectionProvider connectionProvider;

    private SFPCClient(Locale localization) {
        this.localization = new Localization(localization);
        SFPCClient.application = this;
        this.mainFrame = new MainFrame();
    }

    public static SFPCClient getApplication() {
        return application;
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public void setConnectionProvider(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    /**
     * Získá objekt s lokalizovanými retězci
     *
     * @return lokalizační objekt
     */
    public Localization getLocalization() {
        return localization;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //        PCClientPreferences.setServerAddress("192.168.0.10");
        //        PCClientPreferences.setServerPort(25100);
        //        PCClientPreferences.setKeyStorePassword("ssltest");
        //        byte[] data = null;
        //        try {
        //            File f = new File("ssltestcert.jks");
        //            FileInputStream ip = new FileInputStream(f);
        //            data = new byte[(int)f.length()];
        //            try {
        //                ip.read(data);
        //            } catch (IOException ex) {
        //                System.out.println("SOUBOR chyba");
        //                Logger.getLogger(SFPCClient.class.getName()).log(Level.SEVERE, null, ex);
        //            }
        //            System.out.println("délka" + f.length());
        //        } catch (FileNotFoundException ex) {
        //            System.out.println("NENALEZEN SOUBOR");
        //            Logger.getLogger(SFPCClient.class.getName()).log(Level.SEVERE, null, ex);
        //        }
        //        if (data != null){
        //            PCClientPreferences.setKeyStore(data);
        //        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SFPCClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(SFPCClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SFPCClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SFPCClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        new SFPCClient(new Locale("cs", "CZ"));
    }
}
