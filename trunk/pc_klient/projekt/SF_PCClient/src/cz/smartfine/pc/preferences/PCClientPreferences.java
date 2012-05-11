package cz.smartfine.pc.preferences;

import java.util.prefs.Preferences;

/**
 * Třída umožňuje přistupovat k nastavením aplikace.
 * @author Pavel Brož
 */
public class PCClientPreferences {
    /**
     * Služební číslo přihlášeného policisty.
     */
    protected static int badgeNumber = -1;
    /**
     * PIN přihlášeného policisty.
     */
    protected static int pin = -1;
    
    protected static Preferences sysPrefs = Preferences.systemNodeForPackage(cz.smartfine.pc.SFPCClient.class);
    
    protected static String PREF_NAME_SERVER_ADDRESS = "server_address";
    protected static String PREF_NAME_SERVER_PORT = "server_port";
    protected static String PREF_NAME_KEY_STORE = "keystore";
    protected static String PREF_NAME_KEY_STORE_PASSWD = "keystore_passwd";
    
    public static String getServerAddress(){
        return sysPrefs.get(PREF_NAME_SERVER_ADDRESS, null);
    }
    
    public static void setServerAddress(String address){
        sysPrefs.put(PREF_NAME_SERVER_ADDRESS, address);
    }
    
    public static int getServerPort(){
        return sysPrefs.getInt(PREF_NAME_SERVER_PORT, 0);
    }
    
    public static void setServerPort(int port){
        sysPrefs.putInt(PREF_NAME_SERVER_PORT, port);
    }
    
    public static byte[] getKeyStore(){
        return sysPrefs.getByteArray(PREF_NAME_KEY_STORE, null);
    }
    
    public static void setKeyStore(byte[] keyStore){
        sysPrefs.putByteArray(PREF_NAME_KEY_STORE, keyStore);
    }
    
    public static String getKeyStorePassword(){
        return sysPrefs.get(PREF_NAME_KEY_STORE_PASSWD, null);
    }
    
    public static void setKeyStorePassword(String passwd){
        sysPrefs.put(PREF_NAME_KEY_STORE_PASSWD, passwd);
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
    
    public static boolean areLoginDataSaved(){
        if (badgeNumber != -1 && pin != -1){
            return true;
        }else{
            return false;
        }
    }
}
