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
 * Zprost�edkov�v� p��stup k z�kladn�m s�ov�m slu�b�m.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:46
 */
public class ConnectionProvider {

	/**
	 * Rozhran� pro NetworkInterface.
	 */
	private ILink mLink;
	/**
	 * S�ov� rozhran� pro datov� protokoly.
	 */
	private INetworkInterface mNetworkInterface;
	/**
	 * Aplika�n� kontext.
	 */
	private Context appContext;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//

	public void finalize() throws Throwable {

	}

	/**
	 * Konstruktor.
	 * @param appContext Kontext aplikace.
	 * @param link    Objekt implementuj�c� ILink pro transfer dat.
	 * @param networkInterface    Objekt reprezentuj�c� rozhran�, se kter�m mohou
	 * komunikovat t��dy datov�ch protokol�.
	 */
	public ConnectionProvider(Context appContext, ILink link, INetworkInterface networkInterface){
		this.appContext = appContext;
		this.mLink = link;
		this.mNetworkInterface = networkInterface;
		this.mNetworkInterface.setLink(link);
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Vrac� rozhran� pro transfer dat p�es s�.
	 */
	public ILink getLink(){
		return mLink;
	}

	/**
	 * Vrac� z�kladn� rozhran� pro komunikaci datov�ch protokol� se serverem.
	 */
	public INetworkInterface getNetworkInterface(){
		return mNetworkInterface;
	}
	
	//================================================== V�KONN� METODY ==================================================//
	
	/**
	 * Ukon�� spojen� na server.
	 */
	public void disconnect(){
		mLink.disconnect();
	}
	
	/**
	 * Zji��uje, zda je vytvo�en a p�ipojen socket.
	 */
	public boolean isConnected(){
		return mLink.isConnected();
	}

	/**
	 * Vytvo�� spojen� na server.
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
	 * Vytvo�� spojen� na server a p�ihl�s� klienta.
	 * @return true, pokud se poda�ilo p�ipojit a z�rove� p�ihl�sit, false pokud se nepoda�ilo p�ipojit, nebo se nepoda�ilo p�ihl�sit nebo nejsou k dispozici p�ihla�ovac� �daje.
	 */
	public boolean connectAndLogin(){
		if (LoginProvider.isAvaibleLoginInformation(this.appContext)){
			if ( !connect()){
				System.out.println("ANDROID: CP CAN CANT CONNECT");
				return false;
			}
			final InterThreadType<Boolean> loginResult = new InterThreadType<Boolean>(); //prom�nn�, kter� pozastav� b�h vl�kna, dokud nebude zn�m v�sledek p�ihl�en�
			System.out.println("ANDROID: CP CAN LOGIN START");
			//poslucha� ud�lost� z prom�nn� lp (LoginProvideru), kter� nastavuje loginResult//
			ILoginProviderListener lpl = new ILoginProviderListener() {
				
				public void onMessageSent() {}
				public void onLogout() {}
				
				public void onLoginFailed(LoginFailReason reason) {System.out.println("ANDROID: CP CAN LOGIN LISTENER FAILED");
					try {
						loginResult.put(false); //p�ihl�en� ne�sp�n�
					} catch (InterruptedException e) {} 
				}
				
				public void onLoginConfirmed() {System.out.println("ANDROID: CP CAN LOGIN LISTENER CONFIRMED");
					try {
						loginResult.put(true); //p�ihl�en� �sp�n�
					} catch (InterruptedException e) {}
				}
				
				public void onConnectionTerminated() {System.out.println("ANDROID: CP CAN LOGIN LISTENER TERMINATED");
					try {
						loginResult.put(false); //p�ihl�en� ne�sp�n�
					} catch (InterruptedException e) {}
				}
			};
			
			LoginProvider lp = new LoginProvider(this.getNetworkInterface(), this.appContext, lpl); //vytvo�en� login provideru
			System.out.println("ANDROID: CP CAN LOGIN SEND");
			//odesl�n� p�ihla�ovac� zpr�vy
			lp.login(LoginProvider.getBadgeNumber(this.appContext), LoginProvider.getPIN(this.appContext), LoginProvider.getIMEI(this.appContext));
			
			//zde dojde k pozastaven� vl�kna, dokud nebude zn�m v�sledek p�ihl�en�, pot� se hodnota vr�t�
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