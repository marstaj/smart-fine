package cz.smartfine.pc.preferences;

import java.io.*;
import java.util.Properties;

/**
 * Třída umožňuje přistupovat k nastavením aplikace.
 *
 * @author Pavel Brož
 */
public class PCClientPreferences {

    protected static Properties systemPreferences = new Properties();
    
    /**
     * Proměnná slouží pro uchování načteného key store.
     */
    private static byte[] keyStoreData = null;
    
    /**
     * Název konfiguračního souboru.
     */
    protected static final String CONFIG_FILENAME = "config.properties";
    /**
     * Služební číslo přihlášeného policisty.
     */
    protected static int badgeNumber = -1;
    /**
     * PIN přihlášeného policisty.
     */
    protected static int pin = -1;
    /**
     * Klíč pro přístup k adrese serveru.
     */
    protected static String PREF_NAME_SERVER_ADDRESS = "server.address";
    /**
     * Klíč pro přístup k portu serveru.
     */
    protected static String PREF_NAME_SERVER_PORT = "server.port";
    /**
     * Klíč pro přístup k názvu souboru key store s certifikátem certifikační autority, která vydala certifikát serveru.
     */
    protected static String PREF_NAME_KEY_STORE_FILE = "keystore.filepath";
    /**
     * Klíč pro přístup k heslu keystore.
     */
    protected static String PREF_NAME_KEY_STORE_PASSWD = "keystore.password";

    static {
        readPreferences();
    }
    
    /**
     * Načte nastavení.
     */
    private static void readPreferences() {
        InputStream isPref = null;
        try {
            isPref = new FileInputStream(CONFIG_FILENAME);
            systemPreferences.load(isPref);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                isPref.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Uloží nastavení.
     */
    private static void writePreferences() {
        OutputStream osPref = null;
        try {
            osPref = new FileOutputStream(CONFIG_FILENAME);
            
            systemPreferences.store(osPref, "");
        } catch (Exception ex) {
        } finally {
            try {
                osPref.close();
            } catch (IOException ex) {}
        }
    }

    public static String getServerAddress() {
        return systemPreferences.getProperty(PREF_NAME_SERVER_ADDRESS, "");
    }

    public static void setServerAddress(String address) {
        systemPreferences.setProperty(PREF_NAME_SERVER_ADDRESS, address);
        writePreferences();
    }

    public static int getServerPort() {
        return Integer.parseInt(systemPreferences.getProperty(PREF_NAME_SERVER_PORT, "25100"));
    }

    public static void setServerPort(int port) {
        systemPreferences.setProperty(PREF_NAME_SERVER_PORT, String.valueOf(port));
        writePreferences();
    }

    public static byte[] getKeyStore() {
        if (keyStoreData != null) {
            return keyStoreData;
        }

        String ksFilePath = systemPreferences.getProperty(PREF_NAME_KEY_STORE_FILE, "");

        //načtení keystore//
        byte[] ksData;
        FileInputStream ip = null;
        try {
            File keyStoreFile = new File(ksFilePath);
            ip = new FileInputStream(keyStoreFile);
            ksData = new byte[(int) keyStoreFile.length()];

            ip.read(ksData);
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                ip.close();
            } catch (Exception ex) {}
        }

        keyStoreData = ksData;
        return ksData;
    }

    public static void setKeyStore(byte[] keyStore) {
        keyStoreData = keyStore;
        
        String ksFilePath = systemPreferences.getProperty(PREF_NAME_KEY_STORE_FILE, "");

        //uložení keystore//
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(ksFilePath);
            os.write(keyStore);
        } catch (Exception ex) {
        } finally {
            try {
                os.close();
            } catch (Exception ex) {}
        }
    }

    public static String getKeyStorePassword() {
        return systemPreferences.getProperty(PREF_NAME_KEY_STORE_PASSWD, "");
    }

    public static void setKeyStorePassword(String passwd) {
        systemPreferences.setProperty(PREF_NAME_KEY_STORE_PASSWD, passwd);
        writePreferences();
    }

    public static int getBadgeNumber() {
        return badgeNumber;
    }

    public static void setBadgeNumber(int badgeNumber) {
        PCClientPreferences.badgeNumber = badgeNumber;
    }

    public static int getPin() {
        return pin;
    }

    public static void setPin(int pin) {
        PCClientPreferences.pin = pin;
    }

    public static boolean areLoginDataSaved() {
        if (badgeNumber != -1 && pin != -1) {
            return true;
        } else {
            return false;
        }
    }
}
