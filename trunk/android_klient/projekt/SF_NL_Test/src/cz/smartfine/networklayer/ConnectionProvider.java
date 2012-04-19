package cz.smartfine.networklayer;
import java.io.IOException;

import cz.smartfine.networklayer.business.LoginProvider;
import cz.smartfine.networklayer.dataprotocols.LoginProtocol;
import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.networkinterface.SimpleNetworkInterface;

/**
 * Zprostøedkovává pøístup k základním síovım slubám.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:46
 */
public class ConnectionProvider {

	private ILink mLink;
	private INetworkInterface mNetworkInterface;
	
	public ConnectionProvider(){

	}

	public void finalize() throws Throwable {

	}

	/**
	 * Konstruktor.
	 * @param link    Objekt implementující ILink pro transfer dat.
	 * @param networkInterface    Objekt reprezentující rozhraní, se kterım mohou
	 * komunikovat tøídy datovıch protokolù.
	 */
	public ConnectionProvider(ILink link, INetworkInterface networkInterface){
		this.mLink = link;
		this.mNetworkInterface = networkInterface;
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
		if (LoginProvider.isAvaibleLoginInformation()){
			if ( !connect()){
				return false;
			}
			//TODO
		}else{
			return false;
		}
	}
}