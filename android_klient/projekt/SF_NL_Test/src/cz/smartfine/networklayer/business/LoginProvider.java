package cz.smartfine.networklayer.business;
import cz.smartfine.networklayer.ConnectionProvider;
import cz.smartfine.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.networklayer.dataprotocols.LoginProtocol;
import cz.smartfine.networklayer.model.LoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Tøída zajišující ovìøení identity.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public class LoginProvider implements ILoginProtocolListener {

	public LoginProvider(){

	}

	public void finalize() throws Throwable {

	}
	
	/**
	 * Pøihlašuje mobilního klienta k serveru.
	 * 
	 * @param badgeNumber    Sluební èíslo pøihlašovaného policisty.
	 * @param pin    PIN pøihlašovaného policisty.
	 * @param imei    Identifikaèní èíslo mobilního zaøízení (IMEI), ze kterého se
	 * pøihlašuje.
	 */
	public void login(INetworkInterface networkInterface, int badgeNumber, int pin, String imei){
		
	}
	
	/**
	 * Odhlašuje mobilního klienta ze serveru
	 */
	public void logout(INetworkInterface networkInterface){
		
	}
	
	/**
	 * Zjišuje, zda jsou v preferencích uloeny pøihlašovací údaje.
	 * @return true, pøihlašovací údaje jsou k dispozici, false pøihlašovací údaje nejsou k dispozici
	 */
	public static boolean isAvaibleLoginInformation(){
		
	}
	
	/**
	 * Vrací sluební èíslo z preferencí.
	 * @return Sluební èíslo policisty nebo -1 pokud není uloeno.
	 */
	public static int getBadgeNumber(){
		
	}
	
	/**
	 * Vrací PIN z preferencí.
	 * @return PIN policisty nebo -1 pokud není uloen.
	 */
	public static int getPIN(){
		
	}
	
	/**
	 * Vrací èíslo IMEI mobilního zaøízení.
	 * @return IMEI èíslo.
	 */
	public static String getIMEI(){
		
	}
	
	/**
	 * Handler události správného pøihlášení.
	 */
	public void onLoginConfirmed(){

	}

	/**
	 * Handler události chybného pøihlášení.
	 * 
	 * @param reason    Dùvod selhání pøihlášení.
	 */
	public void onLoginFailed(LoginFailReason reason){

	}

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent(){

	}

	/**
	 * Handler události odhlášení od serveru
	 */
	public void onLogout() {
		
		
	}

}