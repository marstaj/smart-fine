package cz.smartfine.networklayer.business;
import cz.smartfine.networklayer.ConnectionProvider;
import cz.smartfine.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.networklayer.dataprotocols.LoginProtocol;
import cz.smartfine.networklayer.model.LoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * T��da zaji��uj�c� ov��en� identity.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public class LoginProvider implements ILoginProtocolListener {

	public LoginProvider(){

	}

	public void finalize() throws Throwable {

	}
	
	/**
	 * P�ihla�uje mobiln�ho klienta k serveru.
	 * 
	 * @param badgeNumber    Slu�ebn� ��slo p�ihla�ovan�ho policisty.
	 * @param pin    PIN p�ihla�ovan�ho policisty.
	 * @param imei    Identifika�n� ��slo mobiln�ho za��zen� (IMEI), ze kter�ho se
	 * p�ihla�uje.
	 */
	public void login(INetworkInterface networkInterface, int badgeNumber, int pin, String imei){
		
	}
	
	/**
	 * Odhla�uje mobiln�ho klienta ze serveru
	 */
	public void logout(INetworkInterface networkInterface){
		
	}
	
	/**
	 * Zji��uje, zda jsou v preferenc�ch ulo�eny p�ihla�ovac� �daje.
	 * @return true, p�ihla�ovac� �daje jsou k dispozici, false p�ihla�ovac� �daje nejsou k dispozici
	 */
	public static boolean isAvaibleLoginInformation(){
		
	}
	
	/**
	 * Vrac� slu�ebn� ��slo z preferenc�.
	 * @return Slu�ebn� ��slo policisty nebo -1 pokud nen� ulo�eno.
	 */
	public static int getBadgeNumber(){
		
	}
	
	/**
	 * Vrac� PIN z preferenc�.
	 * @return PIN policisty nebo -1 pokud nen� ulo�en.
	 */
	public static int getPIN(){
		
	}
	
	/**
	 * Vrac� ��slo IMEI mobiln�ho za��zen�.
	 * @return IMEI ��slo.
	 */
	public static String getIMEI(){
		
	}
	
	/**
	 * Handler ud�losti spr�vn�ho p�ihl�en�.
	 */
	public void onLoginConfirmed(){

	}

	/**
	 * Handler ud�losti chybn�ho p�ihl�en�.
	 * 
	 * @param reason    D�vod selh�n� p�ihl�en�.
	 */
	public void onLoginFailed(LoginFailReason reason){

	}

	/**
	 * Handler zpracov�vaj�c� ud�lost ztr�ty spojen�.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler, reaguj�c� na ud�lost odesl�n� zpr�vy na server.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler ud�losti odhl�en� od serveru
	 */
	public void onLogout() {
		
		
	}

}