package cz.smartfine.android.networklayer;
import java.io.IOException;

import android.content.Context;

import cz.smartfine.android.networklayer.business.LoginProvider;
import cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener;
import cz.smartfine.android.networklayer.links.ILink;
import cz.smartfine.android.networklayer.model.LoginFailReason;
import cz.smartfine.android.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.android.networklayer.util.InterThreadType;

/**
 * Zprostøedkovává pøístup k základním síovım slubám.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:46
 */
public class ConnectionProvider {

	/**
	 * Rozhraní pro NetworkInterface.
	 */
	private ILink mLink;
	/**
	 * Síové rozhraní pro datové protokoly.
	 */
	private INetworkInterface mNetworkInterface;
	/**
	 * Aplikaèní kontext.
	 */
	private Context appContext;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//

	public void finalize() throws Throwable {

	}

	/**
	 * Konstruktor.
	 * @param appContext Kontext aplikace.
	 * @param link    Objekt implementující ILink pro transfer dat.
	 * @param networkInterface    Objekt reprezentující rozhraní, se kterım mohou
	 * komunikovat tøídy datovıch protokolù.
	 */
	public ConnectionProvider(Context appContext, ILink link, INetworkInterface networkInterface){
		this.appContext = appContext;
		this.mLink = link;
		this.mNetworkInterface = networkInterface;
		this.mNetworkInterface.setLink(link);
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Vrací rozhraní pro transfer dat pøes sí.
	 */
	public ILink getLink(){
		return mLink;
	}

	/**
	 * Vrací základní rozhraní pro komunikaci datovıch protokolù se serverem.
	 */
	public INetworkInterface getNetworkInterface(){
		return mNetworkInterface;
	}
	
	//================================================== VİKONNÉ METODY ==================================================//
	
	/**
	 * Ukonèí spojení na server.
	 */
	public void disconnect(){
		mLink.disconnect();
	}
	
	/**
	 * Zjišuje, zda je vytvoøen a pøipojen socket.
	 */
	public boolean isConnected(){
		return mLink.isConnected();
	}

	/**
	 * Vytvoøí spojení na server.
	 */
	public boolean connect(){
		try {
			mLink.connect();
			return mLink.isConnected();
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Vytvoøí spojení na server a pøihlásí klienta.
	 * @return true, pokud se podaøilo pøipojit a zároveò pøihlásit, false pokud se nepodaøilo pøipojit, nebo se nepodaøilo pøihlásit nebo nejsou k dispozici pøihlašovací údaje.
	 */
	public boolean connectAndLogin(){
		if (LoginProvider.isAvaibleLoginInformation(this.appContext)){
			if ( !connect()){
				System.out.println("ANDROID: CP CAN CANT CONNECT");
				return false;
			}
			final InterThreadType<Boolean> loginResult = new InterThreadType<Boolean>(); //promìnná, která pozastaví bìh vlákna, dokud nebude znám vısledek pøihlášení
			System.out.println("ANDROID: CP CAN LOGIN START");
			//posluchaè událostí z promìnné lp (LoginProvideru), kterı nastavuje loginResult//
			ILoginProviderListener lpl = new ILoginProviderListener() {
				
				public void onMessageSent() {}
				public void onLogout() {}
				
				public void onLoginFailed(LoginFailReason reason) {System.out.println("ANDROID: CP CAN LOGIN LISTENER FAILED");
					try {
						loginResult.put(false); //pøihlášení neúspìšné
					} catch (InterruptedException e) {} 
				}
				
				public void onLoginConfirmed() {System.out.println("ANDROID: CP CAN LOGIN LISTENER CONFIRMED");
					try {
						loginResult.put(true); //pøihlášení úspìšné
					} catch (InterruptedException e) {}
				}
				
				public void onConnectionTerminated() {System.out.println("ANDROID: CP CAN LOGIN LISTENER TERMINATED");
					try {
						loginResult.put(false); //pøihlášení neúspìšné
					} catch (InterruptedException e) {}
				}
			};
			
			LoginProvider lp = new LoginProvider(this.getNetworkInterface(), this.appContext, lpl); //vytvoøení login provideru
			System.out.println("ANDROID: CP CAN LOGIN SEND");
			//odeslání pøihlašovací zprávy
			lp.login(LoginProvider.getBadgeNumber(this.appContext), LoginProvider.getPIN(this.appContext), LoginProvider.getIMEI(this.appContext));
			
			//zde dojde k pozastavení vlákna, dokud nebude znám vısledek pøihlášení, poté se hodnota vrátí
			try {
				return loginResult.get();
			} catch (InterruptedException e) {
				return false;
			}
		}else{
			System.out.println("ANDROID: CP CAN NO LOGIN INFO");
			return false;
		}
	}
}