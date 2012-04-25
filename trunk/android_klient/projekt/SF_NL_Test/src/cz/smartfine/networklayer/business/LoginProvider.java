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
 * T��da zaji��uj�c� ov��en� identity.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:47
 */
public class LoginProvider implements ILoginProtocolListener{

	/**
	 * N�zev kl��e v preferenc�ch, pro p��stup ke slu�ebn�mu ��slu p�ihl�en�ho policisty.
	 */
	protected static final String PREF_BADGENUMBER_KEY_NAME = "login_badgenumber";
	/**
	 * N�zev kl��e v preferenc�ch, pro p��stup k PINu p�ihl�en�ho policisty.
	 */
	protected static final String PREF_PIN_KEY_NAME = "login_pin";
	
	/**
	 * Aplika�n� kontext.
	 */
	private Context appContext;
	
	/**
	 * P�ihla�ovac� protokol.
	 */
	private LoginProtocol loginProtocol;
	/**
	 * S�ov� rozhran� pro p�ihla�ovac� protokol.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� LoginProideru.
	 */
	private ILoginProviderListener loginProviderListener;
	
	/**
	 * Naposledy zadan� slu�ebn� ��slo.
	 */
	private int lastBadgeNumber = -1;
	/**
	 * Naposledy zadan� PIN.
	 */
	private int lastPin = -1;
	
	/**
	 * Stavov� prom�nn� ur�uj�c� zda pr�v� prob�h� odhla�ov�n�.
	 */
	private boolean logoutInProgress = false;
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	/**
	 * Konstruktor
	 * @param networkInterface S�ov� rozhran� pro komunikaci datov�ho protokolu.
	 * @param appContext Kontext aplikace.
	 */
	public LoginProvider(INetworkInterface networkInterface, Context appContext){
		this.networkInterface = networkInterface;
		this.appContext = appContext;
	}

	/**
	 * Konstruktor.
	 * @param networkInterface S�ov� rozhran� pro komunikaci datov�ho protokolu.
	 * @param appContext Kontext aplikace.
	 * @param loginProviderListener Poslucha� ud�lost� LoginProideru.
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
	 * @return Poslucha� ud�lost� LoginProideru.
	 */
	public ILoginProviderListener getLoginProviderListener() {
		return loginProviderListener;
	}

	/**
	 * @param loginProviderListener Poslucha� ud�lost� LoginProideru.
	 */
	public void setLoginProviderListener(ILoginProviderListener loginProviderListener) {
		this.loginProviderListener = loginProviderListener;
	}
	
	//================================================== V�KONN� METODY ==================================================//
	
	/**
	 * P�ihla�uje mobiln�ho klienta k serveru.
	 * 
	 * @param badgeNumber    Slu�ebn� ��slo p�ihla�ovan�ho policisty.
	 * @param pin    PIN p�ihla�ovan�ho policisty.
	 * @param imei    Identifika�n� ��slo mobiln�ho za��zen� (IMEI), ze kter�ho se
	 * p�ihla�uje.
	 */
	public void login(int badgeNumber, int pin, String imei){
		if (this.loginProtocol == null){
			this.loginProtocol = new LoginProtocol(this.networkInterface, this);
		}else{
			this.loginProtocol.setLoginProtocolListener(this);
		}
		
		//nastaven� vnit�n�ch prom�nn�ch pro n�sledn� zapamatov�n� slu�ebn�ho ��sla a pinu//
		this.lastBadgeNumber = badgeNumber;
		this.lastPin = pin;
		
		this.loginProtocol.loginToServer(badgeNumber, pin, imei); //odesl�n� p�ihla�ovac� zpr�vy
	}
	

	/**
	 * Odhla�uje mobiln�ho klienta ze serveru
	 */
	public void logout(){
		if (this.loginProtocol == null){
			this.loginProtocol = new LoginProtocol(this.networkInterface, this);
		}else{
			this.loginProtocol.setLoginProtocolListener(this);
		}
		
		logoutInProgress = true; //nastaven� stavov� prom�nn� ur�uj�c�, �e prob�h� odhla�ov�n�
		this.loginProtocol.logoutFromServer(); //posl�n� odhla�ovac� zpr�vy
		deleteLoginInformation(this.appContext); //odstran�n� p�ihla�ovac�ch �daj� z preferenc�
	}
	
	//================================================== STATICK� METODY ==================================================//
	
	/**
	 * Zji��uje, zda jsou v preferenc�ch ulo�eny p�ihla�ovac� �daje.
	 * @param appContext Kontext aplikace pro p��stup k preferenc�m.
	 * @return true, p�ihla�ovac� �daje jsou k dispozici, false p�ihla�ovac� �daje nejsou k dispozici
	 */
	public static boolean isAvaibleLoginInformation(Context appContext){
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnoty preferenc�//
		int bn = PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_BADGENUMBER_KEY_NAME, -1);
		int pin = PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_PIN_KEY_NAME, -1);
		
		//zji�t�n�, zda jsou dostupn� p�ihla�ovac� �daje//
		if (bn == -1 || pin == -1){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Vrac� slu�ebn� ��slo z preferenc�.
	 * @param appContext Kontext aplikace pro p��stup k preferenc�m.
	 * @return Slu�ebn� ��slo policisty nebo -1 pokud nen� ulo�eno.
	 */
	public static int getBadgeNumber(Context appContext){
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu slu�ebn�ho ��sla//
		return PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_BADGENUMBER_KEY_NAME, -1);
	}
	
	/**
	 * Vrac� PIN z preferenc�.
	 * @param appContext Kontext aplikace pro p��stup k preferenc�m.
	 * @return PIN policisty nebo -1 pokud nen� ulo�en.
	 */
	public static int getPIN(Context appContext){
		//z�sk� p��stup k nastaven� aplikace a pot� z�sk� hodnotu slu�ebn�ho ��sla//
		return PreferenceManager.getDefaultSharedPreferences(appContext).getInt(LoginProvider.PREF_PIN_KEY_NAME, -1);
	}
	
	/**
	 * Vrac� ��slo IMEI mobiln�ho za��zen�.
	 * @param appContext Kontext aplikace.
	 * @return IMEI ��slo.
	 */
	public static String getIMEI(Context appContext){
		TelephonyManager telManager = (TelephonyManager)appContext.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telManager.getDeviceId();
		//zjist�, jestli bylo n�jak� ��slo vr�ceno a p��padn� vr�t� nulov� IMEI//
		if (imei != null){
			return imei;
		}else{
			return "000000000000000";
		}
	}
	
	//================================================== HANDLERY UD�LOST� ==================================================//
	
	/**
	 * Handler ud�losti spr�vn�ho p�ihl�en�.
	 */
	public void onLoginConfirmed(){
		//po potvrzen� p�ihl�en� ze serveru, je mo�n� p�ihla�ovac� �daje ulo�it//
		if (this.lastBadgeNumber != -1 && this.lastPin != -1){
			saveLoginInformation(this.appContext, this.lastBadgeNumber, this.lastPin); //ulo�en� p�ihla�ovac�ch �daj�
			this.lastBadgeNumber = -1;
			this.lastPin = -1;
		}
		
		if(loginProviderListener != null){
			loginProviderListener.onLoginConfirmed();
		}
	}

	/**
	 * Handler ud�losti chybn�ho p�ihl�en�.
	 * 
	 * @param reason    D�vod selh�n� p�ihl�en�.
	 */
	public void onLoginFailed(LoginFailReason reason){
		this.lastBadgeNumber = -1;
		this.lastPin = -1;
		
		if(loginProviderListener != null){
			loginProviderListener.onLoginFailed(reason);
		}
	}

	/**
	 * Handler zpracov�vaj�c� ud�lost ztr�ty spojen�.
	 */
	public void onConnectionTerminated(){
		if(loginProviderListener != null){
			loginProviderListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler, reaguj�c� na ud�lost odesl�n� zpr�vy na server.
	 */
	public void onMessageSent(){
		if(loginProviderListener != null){
			loginProviderListener.onMessageSent();
		}
		
		//informov�n� poslucha�e o odhl�en�//
		if(logoutInProgress && loginProviderListener != null){
			loginProviderListener.onLogout();
		}
	}
	
	//================================================== PRIV�TN� METODY ==================================================//
	
	private void saveLoginInformation(Context appContext, int badgeNumber, int pin){
		//vlo�� do nastaven� slu�ebn� ��slo a PIN//
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().putInt(LoginProvider.PREF_BADGENUMBER_KEY_NAME, badgeNumber).commit();
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().putInt(LoginProvider.PREF_PIN_KEY_NAME, pin).commit();
	}

	private void deleteLoginInformation(Context appContext){
		//odebere slu�ebn� ��slo a PIN z nastaven�//
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().remove(LoginProvider.PREF_BADGENUMBER_KEY_NAME).commit();
		PreferenceManager.getDefaultSharedPreferences(appContext).edit().remove(LoginProvider.PREF_PIN_KEY_NAME).commit();
	}
}