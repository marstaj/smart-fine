package cz.smartfine.networklayer;
import java.io.IOException;

import cz.smartfine.networklayer.business.LoginProvider;
import cz.smartfine.networklayer.dataprotocols.LoginProtocol;
import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.networkinterface.SimpleNetworkInterface;

/**
 * Zprost�edkov�v� p��stup k z�kladn�m s�ov�m slu�b�m.
 * @author Pavel Bro�
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
	 * @param link    Objekt implementuj�c� ILink pro transfer dat.
	 * @param networkInterface    Objekt reprezentuj�c� rozhran�, se kter�m mohou
	 * komunikovat t��dy datov�ch protokol�.
	 */
	public ConnectionProvider(ILink link, INetworkInterface networkInterface){
		this.mLink = link;
		this.mNetworkInterface = networkInterface;
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