package cz.smartfine.networklayer.business;
import android.content.Context;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import cz.smartfine.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.networklayer.business.listeners.ILoginProviderListener;
import cz.smartfine.networklayer.dataprotocols.LoginProtocol;
import cz.smartfine.networklayer.model.LoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * Tøída zajišující ovìøení identity.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public class LoginProvider implements ILoginProtocolListener{

	/**
	 * Název klíèe v preferencích, pro pøístup ke sluebnímu èíslu pøihlášeného policisty.
	 */
	protected static final String PREF_BADGENUMBER_KEY_NAME = "login_badgenumber";
	/**
	 * Název klíèe v preferencích, pro pøístup k PINu pøihlášeného policisty.
	 */
	protected static final String PREF_PIN_KEY_NAME = "login_pin";
	
	/**
	 * Aplikaèní kontext.
	 */
	private Context appContext;
	
	/**
	 * Pøihlašovací protokol.
	 */
	private LoginProtocol loginProtocol;
	/**
	 * Síové rozhraní pro pøihlašovací protokol.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchaè událostí LoginProideru.
	 */
	private ILoginProviderListener loginProviderListener;
	
	/**
	 * Naposledy zadané sluební èíslo.
	 */
	private int lastBadgeNumber = -1;
	/**
	 * Naposledy zadanı PIN.
	 */
	private int lastPin = -1;
	
	/**
	 * Stavová promìnná urèující zda právì probíhá odhlašování.
	 */
	private boolean logoutInProgress = false;
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	/**
	 * Konstruktor
	 * @param networkInterface Síové rozhraní pro komunikaci datového protokolu.
	 * @param appContext Kontext aplikace.
	 */
	public LoginProvider(INetworkInterface networkInterface, Context appContext){
		this.networkInterface = networkInterface;
		this.appContext = appContext;
	}

	/**
	 * Konstruktor.
	 * @param networkInterface Síové rozhraní pro komunikaci datového protokolu.
	 * @param appContext Kontext aplikace.
	 * @param loginProviderListener Posluchaè událostí LoginProideru.
	 */
	public LoginProvider(INetworkInterface networkInterface, Context appContext, ILoginProviderListener loginProviderListener){
		this.networkInterface = networkInterface;
		this.appContext = appContext;
		this.loginProviderListener = loginProviderListener;
	}
	
	public void finalize() throws Throwable {

	}
	
	//================================================== GET/SET ==================================================//
	
	/**
	 * @return Posluchaè událostí LoginProideru.
	 */
	public ILoginProviderListener getLoginProviderListener() {
		return loginProviderListener;
	}

	/**
	 * @param loginProviderListener Posluchaè událostí LoginProideru.
	 */
	public void setLoginProviderListener(ILoginProviderListener loginProviderListener) {
		this.loginProviderListener = loginProviderListener;
	}
	
	//================================================== VİKONNÉ METODY ==================================================//
	
	/**
	 * Pøihlašuje mobilního klienta k serveru.
	 * 
	 * @param badgeNumber    Sluební èíslo pøihlašovaného policisty.
	 * @param pin    PIN pøihlašovaného policisty.
	 * @param imei    Identifikaèní èíslo mobilního zaøízení (IMEI), ze kterého se
	 * pøihlašuje.
	 */
	public void login(int badgeNumber, int pin, String imei){
		if (this.loginProtocol == null){
			this.loginProtocol = new LoginProtocol(this.networkInterface, this);
		}else{
			this.loginProtocol.setLoginProtocolListener(this);
		}
		
		//nastavení vnitøních promìnnıch pro následné zapamatování sluebního èísla a pinu//
		this.lastBadgeNumber = badgeNumber;
		this.lastPin = pin;
		
		this.loginProtocol.loginToServer(badgeNumber, pin, imei); //odeslání pøihlašovací zprávy
	}
	

	/**
	 * Odhlašuje mobilního klienta ze serveru
	 */
	public void logout(){
		if (this.loginProtocol == null){
			this.loginProtocol = new LoginProtocol(this.networkInterface, this);
		}else{
			this.loginProtocol.setLoginProtocolListener(this);
		}
		
		logoutInProgress = true; //nastavení stavové promìnné urèující, e probíhá odhlašování
		this.loginProtocol.logoutFromServer(); //poslání odhlašovací zprávy
		deleteLoginInformation(this.appContext); //odstranìní pøihlašovacích údajù z preferencí
	}
	
	//================================================== STATICKÉ METODY ==================================================//
	
	/**
	 * Zjišuje, zda jsou v preferencích uloeny pøihlašovací údaje.
	 * @param appContext Kontext aplikace pro pøístup k preferencím.
	 * @return true, pøihlašovací údaje jsou k dispozici, false pøihlašovací údaje nejsou k dispozici
	 */
	public static boolean isAvaibleLoginInformation(Context appContext){
		//získá pøístup k nastavení aplikace a poté získá hodnoty preferencí//
		int bn = PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_BADGENUMBER_KEY_NAME, -1);
		int pin = PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_PIN_KEY_NAME, -1);
		
		//zjištìní, zda jsou dostupné pøihlašovací údaje//
		if (bn == -1 || pin == -1){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Vrací sluební èíslo z preferencí.
	 * @param appContext Kontext aplikace pro pøístup k preferencím.
	 * @return Sluební èíslo policisty nebo -1 pokud není uloeno.
	 */
	public static int getBadgeNumber(Context appContext){
		//získá pøístup k nastavení aplikace a poté získá hodnotu sluebního èísla//
		return PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_BADGENUMBER_KEY_NAME, -1);
	}
	
	/**
	 * Vrací PIN z preferencí.
	 * @param appContext Kontext aplikace pro pøístup k preferencím.
	 * @return PIN policisty nebo -1 pokud není uloen.
	 */
	public static int getPIN(Context appContext){
		//získá pøístup k nastavení aplikace a poté získá hodnotu sluebního èísla//
		return PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_PIN_KEY_NAME, -1);
	}
	
	/**
	 * Vrací èíslo IMEI mobilního zaøízení.
	 * @param appContext Kontext aplikace.
	 * @return IMEI èíslo.
	 */
	public static String getIMEI(Context appContext){
		TelephonyManager telManager = (TelephonyManager)appContext.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telManager.getDeviceId();
		//zjistí, jestli bylo nìjaké èíslo vráceno a pøípadnì vrátí nulové IMEI//
		if (imei != null){
			return imei;
		}else{
			return "000000000000000";
		}
	}
	
	//================================================== HANDLERY UDÁLOSTÍ ==================================================//
	
	/**
	 * Handler události správného pøihlášení.
	 */
	public void onLoginConfirmed(){
		//po potvrzení pøihlášení ze serveru, je moné pøihlašovací údaje uloit//
		if (this.lastBadgeNumber != -1 && this.lastPin != -1){
			saveLoginInformation(this.appContext, this.lastBadgeNumber, this.lastPin); //uloení pøihlašovacích údajù
			this.lastBadgeNumber = -1;
			this.lastPin = -1;
		}
		
		if(loginProviderListener != null){
			loginProviderListener.onLoginConfirmed();
		}
	}

	/**
	 * Handler události chybného pøihlášení.
	 * 
	 * @param reason    Dùvod selhání pøihlášení.
	 */
	public void onLoginFailed(LoginFailReason reason){
		this.lastBadgeNumber = -1;
		this.lastPin = -1;
		
		if(loginProviderListener != null){
			loginProviderListener.onLoginFailed(reason);
		}
	}

	/**
	 * Handler zpracovávající událost ztráty spojení.
	 */
	public void onConnectionTerminated(){
		if(loginProviderListener != null){
			loginProviderListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler, reagující na událost odeslání zprávy na server.
	 */
	public void onMessageSent(){
		if(loginProviderListener != null){
			loginProviderListener.onMessageSent();
		}
		
		//informování posluchaèe o odhlášení//
		if(logoutInProgress && loginProviderListener != null){
			loginProviderListener.onLogout();
		}
	}
	
	//================================================== PRIVÁTNÍ METODY ==================================================//
	
	private void saveLoginInformation(Context appContext, int badgeNumber, int pin){
		//vloí do nastavení sluební èíslo a PIN//
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().putInt(LoginProvider.PREF_BADGENUMBER_KEY_NAME, badgeNumber).commit();
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().putInt(LoginProvider.PREF_PIN_KEY_NAME, pin).commit();
	}

	private void deleteLoginInformation(Context appContext){
		//odebere sluební èíslo a PIN z nastavení//
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().remove(LoginProvider.PREF_BADGENUMBER_KEY_NAME).commit();
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().remove(LoginProvider.PREF_PIN_KEY_NAME).commit();
	}
}