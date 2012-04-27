package cz.smartfine.android.networklayer.dataprotocols;
import java.io.UnsupportedEncodingException;

import cz.smartfine.android.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.android.networklayer.model.LoginFailReason;
import cz.smartfine.android.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.android.networklayer.util.MessageBuilder;

/**
 * P�edstavuje t��du protokolu pro p�ihl�en� na server.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class LoginProtocol implements IDataProtocol {

	/**
	 * Rozhran� pro p��stup k odes�l�n� a p��j�m�n� dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� z t�to t��dy.
	 */
	private ILoginProtocolListener loginProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public LoginProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhran� pro p�enost dat.
	 * @param loginProtocolListener Poslucha� ud�lost� z t�to t��dy.
	 */
	public LoginProtocol(INetworkInterface networkInterface, ILoginProtocolListener loginProtocolListener){
		this.networkInterface = networkInterface;
		this.loginProtocolListener = loginProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrov�n� se jako poslucha�
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere poslucha�e ud�lost� protokolu pro p�ihl�en� na server.
	 * 
	 * @param loginProtocolListener    Poslucha� ud�lost� z p�ihla�ovac�ho protokolu.
	 */
	public void removeLoginProtocolListener(ILoginProtocolListener loginProtocolListener){
		this.loginProtocolListener = null;
	}

	/**
	 * P�id� poslucha�e ud�lost� protokolu pro p�ihl�en� na server.
	 * 
	 * @param loginProtocolListener    Poslucha� ud�lost� z p�ihla�ovac�ho protokolu.
	 */
	public void setLoginProtocolListener(ILoginProtocolListener loginProtocolListener){
		this.loginProtocolListener = loginProtocolListener;
	}
	
	//================================================== HANDLERY UD�LOST� ==================================================//
	
	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){
		if (loginProtocolListener != null){
			loginProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent(){
		if (loginProtocolListener != null){
			loginProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//pokud nen� ��dn� poslucha� nen� nutn� zpr�vy zpracov�vat//
		if (loginProtocolListener != null){
			
			//kontrola typu zpr�vy//
			switch(receivedData[0]){
				case MessageIDs.ID_MSG_SUC_AUTH: //�sp�n� p�ihl�en�//
					loginProtocolListener.onLoginConfirmed(); //zavol�n� handleru ud�losti
					break;
				case MessageIDs.ID_MSG_FAIL_AUTH: //ne�sp�n� autentizace//
					LoginFailReason reason; //d�vod ne�sp�n� autentizace
					
					//zji�t�n� d�vodu ne�sp�n� autentizace//
					switch (receivedData[1]){
						//nezn�m� chyba//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN:
							reason = LoginFailReason.UNKNOWN_REASON;
							break;
						//chybn� slu�ebn� ��slo nebo PIN//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN:
							reason = LoginFailReason.WRONG_BADGE_NUMBER_OR_PIN;
							break;
						//slu�ebn� ��slo se nep�ruje s IMEI//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_IMEI_AND_BN_DONT_MATCH:
							reason = LoginFailReason.IMEI_AND_BADGE_NUMBER_DONT_MATCH;
							break;
						//Nezn�m� IMEI//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN_IMEI:
							reason = LoginFailReason.UNKNOWN_IMEI;
							break;
						//nezn�m� chyba//
						default:
							reason = LoginFailReason.UNKNOWN_REASON;
					}

					loginProtocolListener.onLoginFailed(reason); //zavol�n� handleru ud�losti
					break;
				case MessageIDs.ID_MSG_TERM_CON: //ukon�en� spojen� ze strany serveru//
					loginProtocolListener.onLoginFailed(LoginFailReason.CONNECTION_TERMINATED_FROM_SERVER); //zavol�n� handleru ud�losti
					break;
			}
			
		}
	}
	
	//================================================== V�KONN� METODY ==================================================//
	
	/**
	 * Odpoj� datov� protokol od z�kladn�ho protokolu.
	 */
	public void disconnectProtocol(){
		if(networkInterface != null){
			networkInterface.removeOnReceivedDataListener(this);
		}
	}

	/**
	 * P�ihla�uje mobiln�ho klienta k serveru.
	 * 
	 * @param badgeNumber    Slu�ebn� ��slo p�ihla�ovan�ho policisty.
	 * @param pin    PIN p�ihla�ovan�ho policisty.
	 * @param imei    Identifika�n� ��slo mobiln�ho za��zen� (IMEI), ze kter�ho se
	 * p�ihla�uje.
	 */
	public void loginToServer(int badgeNumber, int pin, String imei){
		if(networkInterface != null){
			System.out.println("ANDROID: LOGIN PROTOCOL SEND LOGIN");
			networkInterface.sendData(createLoginMessage(badgeNumber, pin, imei));
		}
	}

	/**
	 * Odhla�uje mobiln�ho klienta ze serveru.
	 */
	public void logoutFromServer(){
		if(networkInterface != null){
			System.out.println("ANDROID: LOGIN PROTOCOL SEND LOGOUT");
			networkInterface.sendData(createLogoutMessage());
		}
	}

	//================================================== PRIV�TN� METODY ==================================================//

	/**
	 * Vytv��� p�ihla�ovac� zpr�vu.
	 * 
	 * @param badgeNumber    Slu�ebn� ��slo p�ihla�ovan�ho policisty.
	 * @param pin    PIN p�ihla�ovan�ho policisty.
	 * @param imei    Identifika�n� ��slo mobiln�ho za��zen� (IMEI), ze kter�ho se p�ihla�uje.
	 * @return Zpr�va pro odesl�n� na server.
	 */
	protected byte[] createLoginMessage(int badgeNumber, int pin, String imei){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_AUTHENTICATE); //identifik�tor zpr�vy
		msg.putByte(ProtocolConstants.MSG_AUTHENTICATE_REASON_LOGIN); //d�vod autentizace - p�ih�en�
		msg.putInt(badgeNumber); //slu�ebn� ��slo
		msg.putInt(pin); //PIN
		msg.putArray(imeiToByteArray(imei)); //IMEI p�eveden� na pole byt� s ASCI hodnotami
		
		return msg.getByteArray();
	}
	
	/**
	 * Vytv��� odhla�ovac� zpr�vu.
	 * @return Zpr�va pro odesl�n� na server.
	 */
	protected byte[] createLogoutMessage(){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_LOGOUT); //identifik�tor zpr�vy
		msg.putByte(ProtocolConstants.DUMMY_FIELD);
		
		return msg.getByteArray();
	}

	/**
	 * P�ev�d� IMEI na pole ASCI znak�.
	 * @param imei ��slo IMEI.
	 * @return Bytov� pole o patn�cti prvc�ch ve kter�ch jsou zak�dov�ny ASCI znaky ��sla IMEI.
	 */
	private byte[] imeiToByteArray(String imei){
		try {
			return imei.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			return new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		}
	}
	
}
