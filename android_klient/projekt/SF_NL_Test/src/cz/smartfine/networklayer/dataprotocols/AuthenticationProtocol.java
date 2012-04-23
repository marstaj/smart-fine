package cz.smartfine.networklayer.dataprotocols;
import cz.smartfine.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.model.AuthenticationFailReason;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.MessageBuilder;

/**
 * Pøedstavuje tøídu protokolu pro ovìøení identity.
 * @author Pavel Brož
 * @version 1.0
 */
public class AuthenticationProtocol implements IDataProtocol {

	/**
	 * Rozhraní pro pøístup k odesílání a pøíjímání dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * Posluchaè událostí z této tøídy.
	 */
	private IAuthenticationProtocolListener authenticationProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface Rozhraní pro pøenost dat.
	 */
	public AuthenticationProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    Rozhraní pro pøenost dat.
	 * @param authenticationProtocolListener Posluchaè událostí z této tøídy.
	 */
	public AuthenticationProtocol(INetworkInterface networkInterface, IAuthenticationProtocolListener authenticationProtocolListener){
		this.networkInterface = networkInterface;
		this.authenticationProtocolListener = authenticationProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrování se jako posluchaè
	}
	
	//================================================== GET/SET ==================================================//

	/**
	 * Odebere posluchaèe událostí protokolu pro ovìøení identity.
	 * 
	 * @param authenticationProtocolListener    Posluchaè událostí z autentizaèního protokolu.
	 */
	public void removeAuthenticationProtocolListener(IAuthenticationProtocolListener authenticationProtocolListener){
		this.authenticationProtocolListener = null;
	}

	/**
	 * Pøidá posluchaèe událostí protokolu pro ovìøení identity.
	 * 
	 * @param authenticationProtocolListener    Posluchaè událostí z autentizaèního protokolu.
	 */
	public void setAuthenticationProtocolListener(IAuthenticationProtocolListener authenticationProtocolListener){
		this.authenticationProtocolListener = authenticationProtocolListener;
	}
	
	//================================================== HANDLERY UDÁLOSTÍ ==================================================//
	
	/**
	 * Handler události ukonèení spojení.
	 */
	public void onConnectionTerminated(){
		if (authenticationProtocolListener != null){
			authenticationProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracování události odeslání zprávy.
	 */
	public void onMessageSent(){
		if (authenticationProtocolListener != null){
			authenticationProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uložená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//pokud není žádný posluchaè není nutné zprávy zpracovávat//
		if (authenticationProtocolListener != null){
			
			//kontrola typu zprávy//
			switch (receivedData[0]){
				case MessageIDs.ID_MSG_SUC_AUTH: //úspìšná autentizace//
					authenticationProtocolListener.onAuthenticationConfirmed(); //zavolání handleru události
					break;
				case MessageIDs.ID_MSG_FAIL_AUTH: //neúspìšná autentizace//
					AuthenticationFailReason reason; //dùvod neúspìšné autentizace
					
					//zjištìní dùvodu neúspìšné autentizace//
					switch (receivedData[1]){
						//neznámá chyba//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_UNKNOWN:
							reason = AuthenticationFailReason.UNKNOWN_REASON;
						//chybné služební èíslo nebo PIN//
						case ProtocolConstants.MSG_FAIL_AUTH_ERR_WRONG_BN_OR_PIN:
							reason = AuthenticationFailReason.WRONG_BADGE_NUMBER_OR_PIN;
						//neznámá chyba//
						default:
							reason = AuthenticationFailReason.UNKNOWN_REASON;
					}
		
					authenticationProtocolListener.onAuthenticationFailed(reason); //zavolání handleru události
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
	 * Ovìøuje identitu policisty.
	 * 
	 * @param badgeNumber    Služební èíslo policisty.
	 * @param pin    PIN policisty.
	 */
	public void authenticate(int badgeNumber, int pin){
		if(networkInterface != null){
			networkInterface.sendData(createAuthenticationMessage(badgeNumber, pin));
		}
	}
	
	//================================================== PRIVÁTNÍ METODY ==================================================//
	
	/**
	 * Vytváøí autentizaèní zprávu.
	 * @param badgeNumber Služební èíslo policisty.
	 * @param pin PIN policisty.
	 * @return Zpráva pro odeslání na server
	 */
	private byte[] createAuthenticationMessage(int badgeNumber, int pin){
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MessageIDs.ID_MSG_AUTHENTICATE); //identifikátor zprávy
		msg.putByte(ProtocolConstants.MSG_AUTHENTICATE_REASON_AUTHENTICATION); //dùvod autentizace - autentizace policisty
		msg.putInt(badgeNumber); //služební èíslo
		msg.putInt(pin); //PIN
		msg.putArray(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}); //pole nahrazující IMEI
		
		return msg.getByteArray();
	}
	
}
