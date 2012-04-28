package cz.smartfine.android.networklayer.dataprotocols;
import java.io.UnsupportedEncodingException;

import cz.smartfine.android.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.android.networklayer.model.LoginFailReason;
import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.MobileProtocolConstants;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.MessageBuilder;
/**
 * Představuje třídu protokolu pro přihlášení na server.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class LoginProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro přístup k odesílání a příjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchač událostí z této třídy.
	 */
	private ILoginProtocolListener loginProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro přenost dat.
	 */
	public LoginProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro přenost dat.
	 * @param loginProtocolListener Posluchač událostí z této třídy.
	 */
	public LoginProtocol(INetworkInterface networkInterface, ILoginProtocolListener loginProtocolListener){
		this.networkInterface = networkInterface;
		this.loginProtocolListener = loginProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchač
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere posluchače událostí protokolu pro přihlášení na server.
	 * 
	 * @param loginProtocolListener    Posluchač událostí z přihlašovacího protokolu.
	 */
	public void removeLoginProtocolListener(ILoginProtocolListener loginProtocolListener){
		this.loginProtocolListener = null;
	}

	/**
	 * Přidá posluchače událostí protokolu pro přihlášení na server.
	 * 
	 * @param loginProtocolListener    Posluchač událostí z přihlašovacího protokolu.
	 */
	public void setLoginProtocolListener(ILoginProtocolListener loginProtocolListener){
		this.loginProtocolListener = loginProtocolListener;
	}
	
	//================================================== HANDLERY UDÁLOSTÍ ==================================================//
	
	/**
	 * Handler události ukončení spojení.
	 */
	public void onConnectionTerminated(){
		if (loginProtocolListener != null){
			loginProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 * @param sentData Odeslaná data.   
	 */
	public void onMessageSent(byte[] sentData){
		if (loginProtocolListener != null){
			loginProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler události příjmu dat.
	 * 
	 * @param receivedData    Přijmutá data uložená ve formě bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//pokud není žádný posluchač není nutné zprávy zpracovávat//
		if (loginProtocolListener != null){
			
			//kontrola typu zprávy//
			switch(receivedData[0]){
				case MobileMessageIDs.ID_MSG_SUC_AUTH: //úspěšné přihlášení//
					loginProtocolListener.onLoginConfirmed(); //zavolání handleru události
					break;
				case MobileMessageIDs.ID_MSG_FAIL_AUTH: //neúspěšná autentizace//
					LoginFailReason reason; //důvod neúspěšné autentizace
					
					//zjištění důvodu neúspěšné autentizace//
					switch (receivedData[1]){
						//neznámá chyba//
						case MobileProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN:
							reason = LoginFailReason.UNKNOWN_REASON;
							break;
						//chybné služební číslo nebo PIN//
						case MobileProtocolConstants.MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN:
							reason = LoginFailReason.WRONG_BADGE_NUMBER_OR_PIN;
							break;
						//služební číslo se nepáruje s IMEI//
						case MobileProtocolConstants.MSG_FAIL_AUTH_ERR_IMEI_AND_BN_DONT_MATCH:
							reason = LoginFailReason.IMEI_AND_BADGE_NUMBER_DONT_MATCH;
							break;
						//Neznámé IMEI//
						case MobileProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN_IMEI:
							reason = LoginFailReason.UNKNOWN_IMEI;
							break;
						//neznámá chyba//
						default:
							reason = LoginFailReason.UNKNOWN_REASON;
					}

					loginProtocolListener.onLoginFailed(reason); //zavolání handleru události
					break;
				case MobileMessageIDs.ID_MSG_TERM_CON: //ukončení spojení ze strany serveru//
					loginProtocolListener.onLoginFailed(LoginFailReason.CONNECTION_TERMINATED_FROM_SERVER); //zavolání handleru události
					break;
			}
			
		}
	}
	
	//================================================== VÝKONNÉ METODY ==================================================//
	
	/**
	 * Odpojí datový protokol od základního protokolu.
	 */
	public void disconnectProtocol(){
		if(networkInterface != null){
			networkInterface.removeOnReceivedDataListener(this);
		}
	}

	/**
	 * Přihlašuje mobilního klienta k serveru.
	 * 
	 * @param badgeNumber    Služební číslo přihlašovaného policisty.
	 * @param pin    PIN přihlašovaného policisty.
	 * @param imei    Identifikační číslo mobilního zařízení (IMEI), ze kterého se
	 * přihlašuje.
	 */
	public void loginToServer(int badgeNumber, int pin, String imei){
		if(networkInterface != null){
			System.out.println("ANDROID: LOGIN PROTOCOL SEND LOGIN");
			networkInterface.sendData(createLoginMessage(badgeNumber, pin, imei), this);
		}
	}

	/**
	 * Odhlašuje mobilního klienta ze serveru.
	 */
	public void logoutFromServer(){
		if(networkInterface != null){
			System.out.println("ANDROID: LOGIN PROTOCOL SEND LOGOUT");
			networkInterface.sendData(createLogoutMessage(), this);
		}
	}

	//================================================== PRIVÁTNÍ METODY ==================================================//

	/**
	 * Vytváří přihlašovací zprávu.
	 * 
	 * @param badgeNumber    Služební číslo přihlašovaného policisty.
	 * @param pin    PIN přihlašovaného policisty.
	 * @param imei    Identifikační číslo mobilního zařízení (IMEI), ze kterého se přihlašuje.
	 * @return Zpráva pro odeslání na server.
	 */
	protected byte[] createLoginMessage(int badgeNumber, int pin, String imei){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MobileMessageIDs.ID_MSG_AUTHENTICATE); //identifikátor zprávy
		msg.putByte(MobileProtocolConstants.MSG_AUTHENTICATE_REASON_LOGIN); //důvod autentizace - přihášení
		msg.putInt(badgeNumber); //služební číslo
		msg.putInt(pin); //PIN
		msg.putArray(imeiToByteArray(imei)); //IMEI převedené na pole bytů s ASCI hodnotami
		
		return msg.getByteArray();
	}
	
	/**
	 * Vytváří odhlašovací zprávu.
	 * @return Zpráva pro odeslání na server.
	 */
	protected byte[] createLogoutMessage(){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MobileMessageIDs.ID_MSG_LOGOUT); //identifikátor zprávy
		msg.putByte(MobileProtocolConstants.DUMMY_FIELD);
		
		return msg.getByteArray();
	}

	/**
	 * Převádí IMEI na pole ASCI znaků.
	 * @param imei Číslo IMEI.
	 * @return Bytové pole o patnácti prvcích ve kterých jsou zakódovány ASCI znaky čísla IMEI.
	 */
	private byte[] imeiToByteArray(String imei){
		try {
			return imei.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			return new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		}
	}
	
}
