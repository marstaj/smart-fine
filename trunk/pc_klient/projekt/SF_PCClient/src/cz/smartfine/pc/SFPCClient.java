package cz.smartfine.pc;

import cz.smartfine.pc.networklayer.ConnectionProvider;
import cz.smartfine.pc.preferences.PCClientPreferences;
import cz.smartfine.pc.view.MainFrame;
import java.io.*;
import java.util.Locale;
import java.util.Properties;
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) { }

        new SFPCClient(new Locale("cs", "CZ"));
    }
}
