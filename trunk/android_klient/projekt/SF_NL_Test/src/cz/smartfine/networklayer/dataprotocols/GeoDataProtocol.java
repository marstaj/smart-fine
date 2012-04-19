package cz.smartfine.networklayer.dataprotocols;
import java.util.List;

import cz.smartfine.networklayer.business.listeners.IGeoDataProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;

/**
 * P�edstavuje t��du protokolu pro p�enost geoloka�n�ch dat.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:46
 */
public class GeoDataProtocol implements IDataProtocol {

	/**
	 * Rozhran� pro p��stup k odes�l�n� a p��j�m�n� dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� z t�to t��dy.
	 */
	private IGeoDataProtocolListener geoDataProtocolListener;
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public GeoDataProtocol(INetworkInterface networkInterface) {
		this.networkInterface = networkInterface;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhran� pro p�enos dat.
	 * @param geoDataProtocolListener Poslucha� ud�lost� z t�to t��dy.
	 */
	public GeoDataProtocol(INetworkInterface networkInterface, IGeoDataProtocolListener geoDataProtocolListener){
		this.networkInterface = networkInterface;
		this.geoDataProtocolListener = geoDataProtocolListener;
	}

	/**
	 * Odpoj� datov� protokol od z�kladn�ho protokolu.
	 */
	public void disconnectProtocol(){

	}

	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){

	}

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent(){

	}

	/**
	 * Odebere poslucha�e ud�lost� protokolu pro odes�l�n� geoloka�n�ch dat.
	 * 
	 * @param geoDataProtocolListener    Poslucha� ud�lost� z protokolu pro odes�l�n�
	 * geoloka�n�ch dat.
	 */
	public void removeGeoDataProtocolListener(IGeoDataProtocolListener geoDataProtocolListener){

	}

	/**
	 * Ode�le geoloka�n� data na server.
	 * 
	 * @param geodata    Seznam geoloka�n�ch dat pro posl�n� na server.
	 */
	public void sendGeoData(List geodata){

	}

	/**
	 * P�id� poslucha�e ud�lost� protokolu pro odes�l�n� geoloka�n�ch dat.
	 * 
	 * @param geoDataProtocolListener    Poslucha� ud�lost� z protokolu pro odes�l�n�
	 * geoloka�n�ch dat.
	 */
	public void setGeoDataProtocolListener(IGeoDataProtocolListener geoDataProtocolListener){

	}

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData){

	}

}