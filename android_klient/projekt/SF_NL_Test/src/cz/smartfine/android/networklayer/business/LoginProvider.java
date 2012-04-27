package cz.smartfine.android.networklayer.business;
import android.content.Context;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import cz.smartfine.android.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener;
import cz.smartfine.android.networklayer.dataprotocols.LoginProtocol;
import cz.smartfine.android.networklayer.model.LoginFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;


/**
 * Třída zajišťující ověření identity.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public class LoginProvider implements ILoginProtocolListener{

	/**
	 * Název klíče v preferencích, pro přístup ke služebnímu číslu přihlášeného policisty.
	 */
	protected static final String PREF_BADGENUMBER_KEY_NAME = "login_badgenumber";
	/**
	 * Název klíče v preferencích, pro přístup k PINu přihlášeného policisty.
	 */
	protected static final String PREF_PIN_KEY_NAME = "login_pin";
	
	/**
	 * Aplikační kontext.
	 */
	private Context appContext;
	
	/**
	 * Přihlašovací protokol.
	 */
	private LoginProtocol loginProtocol;
	/**
	 * Síťové rozhraní pro přihlašovací protokol.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchač událostí LoginProideru.
	 */
	private ILoginProviderListener loginProviderListener;
	
	/**
	 * Naposledy zadané služební číslo.
	 */
	private int lastBadgeNumber = -1;
	/**
	 * Naposledy zadaný PIN.
	 */
	private int lastPin = -1;
	
	/**
	 * Stavová proměnná určující zda právě probíhá odhlašování.
	 */
	private boolean logoutInProgress = false;
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	/**
	 * Konstruktor
	 * @param networkInterface Síťové rozhraní pro komunikaci datového protokolu.
	 * @param appContext Kontext aplikace.
	 */
	public LoginProvider(INetworkInterface networkInterface, Context appContext){
		this.networkInterface = networkInterface;
		this.appContext = appContext;
	}

	/**
	 * Konstruktor.
	 * @param networkInterface Síťové rozhraní pro komunikaci datového protokolu.
	 * @param appContext Kontext aplikace.
	 * @param loginProviderListener Posluchač událostí LoginProideru.
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
	 * @return Posluchač událostí LoginProideru.
	 */
	public ILoginProviderListener getLoginProviderListener() {
		return loginProviderListener;
	}

	/**
	 * @param loginProviderListener Posluchač událostí LoginProideru.
	 */
	public void setLoginProviderListener(ILoginProviderListener loginProviderListener) {
		this.loginProviderListener = loginProviderListener;
	}
	
	//================================================== VÝKONNÉ METODY ==================================================//
	
	/**
	 * Přihlašuje mobilního klienta k serveru.
	 * 
	 * @param badgeNumber    Služební číslo přihlašovaného policisty.
	 * @param pin    PIN přihlašovaného policisty.
	 * @param imei    Identifikační číslo mobilního zařízení (IMEI), ze kterého se
	 * přihlašuje.
	 */
	public void login(int badgeNumber, int pin, String imei){
		if (this.loginProtocol == null){
			this.loginProtocol = new LoginProtocol(this.networkInterface, this);
		}else{
			this.loginProtocol.setLoginProtocolListener(this);
		}
		
		//nastavení vnitřních proměnných pro následné zapamatování služebního čísla a pinu//
		this.lastBadgeNumber = badgeNumber;
		this.lastPin = pin;
		
		this.loginProtocol.loginToServer(badgeNumber, pin, imei); //odeslání přihlašovací zprávy
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
		
		logoutInProgress = true; //nastavení stavové proměnné určující, že probíhá odhlašování
		this.loginProtocol.logoutFromServer(); //poslání odhlašovací zprávy
		deleteLoginInformation(this.appContext); //odstranění přihlašovacích údajů z preferencí
	}
	
	//================================================== STATICKÉ METODY ==================================================//
	
	/**
	 * Zjišťuje, zda jsou v preferencích uloženy přihlašovací údaje.
	 * @param appContext Kontext aplikace pro přístup k preferencím.
	 * @return true, přihlašovací údaje jsou k dispozici, false přihlašovací údaje nejsou k dispozici
	 */
	public static boolean isAvaibleLoginInformation(Context appContext){
		//získá přístup k nastavení aplikace a poté získá hodnoty preferencí//
		int bn = PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_BADGENUMBER_KEY_NAME, -1);
		int pin = PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_PIN_KEY_NAME, -1);
		
		//zjištění, zda jsou dostupné přihlašovací údaje//
		if (bn == -1 || pin == -1){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Vrací služební číslo z preferencí.
	 * @param appContext Kontext aplikace pro přístup k preferencím.
	 * @return Služební číslo policisty nebo -1 pokud není uloženo.
	 */
	public static int getBadgeNumber(Context appContext){
		//získá přístup k nastavení aplikace a poté získá hodnotu služebního čísla//
		return PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_BADGENUMBER_KEY_NAME, -1);
	}
	
	/**
	 * Vrací PIN z preferencí.
	 * @param appContext Kontext aplikace pro přístup k preferencím.
	 * @return PIN policisty nebo -1 pokud není uložen.
	 */
	public static int getPIN(Context appContext){
		//získá přístup k nastavení aplikace a poté získá hodnotu služebního čísla//
		return PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_PIN_KEY_NAME, -1);
	}
	
	/**
	 * Vrací číslo IMEI mobilního zařízení.
	 * @param appContext Kontext aplikace.
	 * @return IMEI číslo.
	 */
	public static String getIMEI(Context appContext){
		TelephonyManager telManager = (TelephonyManager)appContext.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telManager.getDeviceId();
		//zjistí, jestli bylo nějaké číslo vráceno a případně vrátí nulové IMEI//
		if (imei != null){
			return imei;
		}else{
			return "000000000000000";
		}
	}
	
	//================================================== HANDLERY UDÁLOSTÍ ==================================================//
	
	/**
	 * Handler události správného přihlášení.
	 */
	public void onLoginConfirmed(){
		//po potvrzení přihlášení ze serveru, je možné přihlašovací údaje uložit//
		if (this.lastBadgeNumber != -1 && this.lastPin != -1){
			saveLoginInformation(this.appContext, this.lastBadgeNumber, this.lastPin); //uložení přihlašovacích údajů
			this.lastBadgeNumber = -1;
			this.lastPin = -1;
		}
		
		if(loginProviderListener != null){
			loginProviderListener.onLoginConfirmed();
		}
	}

	/**
	 * Handler události chybného přihlášení.
	 * 
	 * @param reason    Důvod selhání přihlášení.
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
		
		//informování posluchače o odhlášení//
		if(logoutInProgress && loginProviderListener != null){
			loginProviderListener.onLogout();
		}
	}
	
	//================================================== PRIVÁTNÍ METODY ==================================================//
	
	private void saveLoginInformation(Context appContext, int badgeNumber, int pin){
		//vloží do nastavení služební číslo a PIN//
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().putInt(LoginProvider.PREF_BADGENUMBER_KEY_NAME, badgeNumber).commit();
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().putInt(LoginProvider.PREF_PIN_KEY_NAME, pin).commit();
	}

	private void deleteLoginInformation(Context appContext){
		//odebere služební číslo a PIN z nastavení//
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().remove(LoginProvider.PREF_BADGENUMBER_KEY_NAME).commit();
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().remove(LoginProvider.PREF_PIN_KEY_NAME).commit();
	}
}