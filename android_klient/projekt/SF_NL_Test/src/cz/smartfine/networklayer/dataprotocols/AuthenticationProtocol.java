package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.AuthenticationFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.MessageBuilder;

/**
 * P�edstavuje t��du protokolu pro ov��en� identity.
 * @author Pavel Bro�
 * @version 1.0
 */
public class AuthenticationProtocol implements IDataProtocol {

	/**
	 * Rozhran� pro p��stup k odes�l�n� a p��j�m�n� dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Poslucha� ud�lost� z t�to t��dy.
	 */
	private IAuthenticationProtocolListener authenticationProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhran� pro p�enost dat.
	 */
	public AuthenticationProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhran� pro p�enost dat.
	 * @param authenticationProtocolListener Poslucha� ud�lost� z t�to t��dy.
	 */
	public AuthenticationProtocol(INetworkInterface networkInterface, IAuthenticationProtocolListener authenticationProtocolListener){
		this.networkInterface = networkInterface;
		this.authenticationProtocolListener = authenticationProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrov�n� se jako poslucha�
	}
	
	//================================================== GET/SET ==================================================//

	/**
	 * Odebere poslucha�e ud�lost� protokolu pro ov��en� identity.
	 * 
	 * @param authenticationProtocolListener    Poslucha� ud�lost� z autentiza�n�ho protokolu.
	 */
	public void removeAuthenticationProtocolListener(IAuthenticationProtocolListener authenticationProtocolListener){
		this.authenticationProtocolListener = null;
	}

	/**
	 * P�id� poslucha�e ud�lost� protokolu pro ov��en� identity.
	 * 
	 * @param authenticationProtocolListener    Poslucha� ud�lost� z autentiza�n�ho protokolu.
	 */
	public void setAuthenticationProtocolListener(IAuthenticationProtocolListener authenticationProtocolListener){
		this.authenticationProtocolListener = authenticationProtocolListener;
	}
	
	//================================================== HANDLERY UD�LOST� ==================================================//
	
	/**
	 * Handler ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){
		if (authenticationProtocolListener != null){
			authenticationProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracov�n� ud�losti odesl�n� zpr�vy.
	 */
	public void onMessageSent(){
		if (authenticationProtocolListener != null){
			authenticationProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//pokud nen� ��dn� poslucha� nen� nutn� zpr�vy zpracov�vat//
		if (authenticationProtocolListener != null){
			
			//kontrola typu zpr�vy//
			switch (receivedData[0]){
				case MessageIDs.ID_MSG_SUC_AUTH: //�sp�n� autentizace//
					authenticationProtocolListener.onAuthenticationConfirmed(); //zavol�n� handleru ud�losti
					break;
				case MessageIDs.ID_MSG_FAIL_AUTH: //ne�sp�n� autentizace//
					AuthenticationFailReason reason; //d�vod ne�sp�n� autentizace
					
					//zji�t�n� d�vodu ne�sp�n� autentizace//
					switch (receivedData[1]){
						//nezn�m� chyba//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN:
							reason = AuthenticationFailReason.UNKNOWN_REASON;
						//chybn� slu�ebn� ��slo nebo PIN//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN:
							reason = AuthenticationFailReason.WRONG_BADGE_NUMBER_OR_PIN;
						//nezn�m� chyba//
						default:
							reason = AuthenticationFailReason.UNKNOWN_REASON;
					}
		
					authenticationProtocolListener.onAuthenticationFailed(reason); //zavol�n� handleru ud�losti
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
	 * Ov��uje identitu policisty.
	 * 
	 * @param badgeNumber    Slu�ebn� ��slo policisty.
	 * @param pin    PIN policisty.
	 */
	public void authenticate(int badgeNumber, int pin){
		if(networkInterface != null){
			networkInterface.sendData(createAuthenticationMessage(badgeNumber, pin));
		}
	}
	
	//================================================== PRIV�TN� METODY ==================================================//
	
	/**
	 * Vytv��� autentiza�n� zpr�vu.
	 * @param badgeNumber Slu�ebn� ��slo policisty.
	 * @param pin PIN policisty.
	 * @return Zpr�va pro odesl�n� na server
	 */
	private byte[] createAuthenticationMessage(int badgeNumber, int pin){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_AUTHENTICATE); //identifik�tor zpr�vy
		msg.putByte(ProtocolConstants.MSG_AUTHENTICATE_REASON_AUTHENTICATION); //d�vod autentizace - autentizace policisty
		msg.putInt(badgeNumber); //slu�ebn� ��slo
		msg.putInt(pin); //PIN
		msg.putArray(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}); //pole nahrazuj�c� IMEI
		
		return msg.getByteArray();
	}
	
}
