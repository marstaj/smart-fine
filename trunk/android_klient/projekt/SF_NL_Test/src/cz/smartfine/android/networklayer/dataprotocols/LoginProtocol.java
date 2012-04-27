package cz.smartfine.android.networklayer.dataprotocols;
import java.io.UnsupportedEncodingException;

import cz.smartfine.android.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.android.networklayer.model.LoginFailReason;
import cz.smartfine.android.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.android.networklayer.util.MessageBuilder;

/**
 * Pøedstavuje tøídu protokolu pro pøihlášení na server.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class LoginProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro pøístup k odesílání a pøíjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchaè událostí z této tøídy.
	 */
	private ILoginProtocolListener loginProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public LoginProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro pøenost dat.
	 * @param loginProtocolListener Posluchaè událostí z této tøídy.
	 */
	public LoginProtocol(INetworkInterface networkInterface, ILoginProtocolListener loginProtocolListener){
		this.networkInterface = networkInterface;
		this.loginProtocolListener = loginProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchaè
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere posluchaèe událostí protokolu pro pøihlášení na server.
	 * 
	 * @param loginProtocolListener    Posluchaè událostí z pøihlašovacího protokolu.
	 */
	public void removeLoginProtocolListener(ILoginProtocolListener loginProtocolListener){
		this.loginProtocolListener = null;
	}

	/**
	 * Pøidá posluchaèe událostí protokolu pro pøihlášení na server.
	 * 
	 * @param loginProtocolListener    Posluchaè událostí z pøihlašovacího protokolu.
	 */
	public void setLoginProtocolListener(ILoginProtocolListener loginProtocolListener){
		this.loginProtocolListener = loginProtocolListener;
	}
	
	//================================================== HANDLERY UDÁLOSTÍ ==================================================//
	
	/**
	 * Handler události ukonèení spojení.
	 */
	public void onConnectionTerminated(){
		if (loginProtocolListener != null){
			loginProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){
		if (loginProtocolListener != null){
			loginProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//pokud není žádný posluchaè není nutné zprávy zpracovávat//
		if (loginProtocolListener != null){
			
			//kontrola typu zprávy//
			switch(receivedData[0]){
				case MessageIDs.ID_MSG_SUC_AUTH: //úspìšné pøihlášení//
					loginProtocolListener.onLoginConfirmed(); //zavolání handleru události
					break;
				case MessageIDs.ID_MSG_FAIL_AUTH: //neúspìšná autentizace//
					LoginFailReason reason; //dùvod neúspìšné autentizace
					
					//zjištìní dùvodu neúspìšné autentizace//
					switch (receivedData[1]){
						//neznámá chyba//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN:
							reason = LoginFailReason.UNKNOWN_REASON;
							break;
						//chybné služební èíslo nebo PIN//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN:
							reason = LoginFailReason.WRONG_BADGE_NUMBER_OR_PIN;
							break;
						//služební èíslo se nepáruje s IMEI//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_IMEI_AND_BN_DONT_MATCH:
							reason = LoginFailReason.IMEI_AND_BADGE_NUMBER_DONT_MATCH;
							break;
						//Neznámé IMEI//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN_IMEI:
							reason = LoginFailReason.UNKNOWN_IMEI;
							break;
						//neznámá chyba//
						default:
							reason = LoginFailReason.UNKNOWN_REASON;
					}

					loginProtocolListener.onLoginFailed(reason); //zavolání handleru události
					break;
				case MessageIDs.ID_MSG_TERM_CON: //ukonèení spojení ze strany serveru//
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
	 * Pøihlašuje mobilního klienta k serveru.
	 * 
	 * @param badgeNumber    Služební èíslo pøihlašovaného policisty.
	 * @param pin    PIN pøihlašovaného policisty.
	 * @param imei    Identifikaèní èíslo mobilního zaøízení (IMEI), ze kterého se
	 * pøihlašuje.
	 */
	public void loginToServer(int badgeNumber, int pin, String imei){
		if(networkInterface != null){
			System.out.println("ANDROID: LOGIN PROTOCOL SEND LOGIN");
			networkInterface.sendData(createLoginMessage(badgeNumber, pin, imei));
		}
	}

	/**
	 * Odhlašuje mobilního klienta ze serveru.
	 */
	public void logoutFromServer(){
		if(networkInterface != null){
			System.out.println("ANDROID: LOGIN PROTOCOL SEND LOGOUT");
			networkInterface.sendData(createLogoutMessage());
		}
	}

	//================================================== PRIVÁTNÍ METODY ==================================================//

	/**
	 * Vytváøí pøihlašovací zprávu.
	 * 
	 * @param badgeNumber    Služební èíslo pøihlašovaného policisty.
	 * @param pin    PIN pøihlašovaného policisty.
	 * @param imei    Identifikaèní èíslo mobilního zaøízení (IMEI), ze kterého se pøihlašuje.
	 * @return Zpráva pro odeslání na server.
	 */
	protected byte[] createLoginMessage(int badgeNumber, int pin, String imei){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_AUTHENTICATE); //identifikátor zprávy
		msg.putByte(ProtocolConstants.MSG_AUTHENTICATE_REASON_LOGIN); //dùvod autentizace - pøihášení
		msg.putInt(badgeNumber); //služební èíslo
		msg.putInt(pin); //PIN
		msg.putArray(imeiToByteArray(imei)); //IMEI pøevedené na pole bytù s ASCI hodnotami
		
		return msg.getByteArray();
	}
	
	/**
	 * Vytváøí odhlašovací zprávu.
	 * @return Zpráva pro odeslání na server.
	 */
	protected byte[] createLogoutMessage(){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_LOGOUT); //identifikátor zprávy
		msg.putByte(ProtocolConstants.DUMMY_FIELD);
		
		return msg.getByteArray();
	}

	/**
	 * Pøevádí IMEI na pole ASCI znakù.
	 * @param imei Èíslo IMEI.
	 * @return Bytové pole o patnácti prvcích ve kterých jsou zakódovány ASCI znaky èísla IMEI.
	 */
	private byte[] imeiToByteArray(String imei){
		try {
			return imei.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			return new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		}
	}
	
}
