package cz.smartfine.pc.view;

/**
 * Rozhraní posluchače události odhlášení.
 * @author Pavel Brož
 */
public interface ILogoutListener {
    
    /**
     * Habdler události odhlášení.
     */
    public void onLogout();
}
