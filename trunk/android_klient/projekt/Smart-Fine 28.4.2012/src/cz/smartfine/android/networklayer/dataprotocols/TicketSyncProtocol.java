package cz.smartfine.android.networklayer.dataprotocols;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.networklayer.business.listeners.ITicketProtocolListener;
import cz.smartfine.networklayer.dataprotocols.MobileMessageIDs;
import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.MessageBuilder;
/**
 * PĹ™edstavuje tĹ™Ă­du protokolu pro pĹ™enost PL na server.
 * @author Pavel BroĹľ
 * @version 1.0
 * @created 14-4-2012 18:48:49
 */
public class TicketSyncProtocol implements IDataProtocol {

	/**
	 * RozhranĂ­ pro pĹ™Ă­stup k odesĂ­lĂˇnĂ­ a pĹ™Ă­jĂ­mĂˇnĂ­ dat.
	 */
	private INetworkInterface networkInterface;
	/**
	 * PosluchaÄŤ udĂˇlostĂ­ z tĂ©to tĹ™Ă­dy.
	 */
	private ITicketProtocolListener ticketProtocolListener;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {

	}
	
	/**
	 * Konstruktor.
	 * @param networkInterface RozhranĂ­ pro pĹ™enost dat.
	 */
	public TicketSyncProtocol(INetworkInterface networkInterface) {
		this(networkInterface, null);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param networkInterface    RozhranĂ­ pro pĹ™enost dat.
	 * @param ticketProtocolListener PosluchaÄŤ udĂˇlostĂ­ z tĂ©to tĹ™Ă­dy.
	 */
	public TicketSyncProtocol(INetworkInterface networkInterface, ITicketProtocolListener ticketProtocolListener){
		this.networkInterface = networkInterface;
		this.ticketProtocolListener = ticketProtocolListener;
		this.networkInterface.setOnReceivedDataListener(this); //zaregistrovĂˇnĂ­ se jako posluchaÄŤ
	}

	//================================================== GET/SET ==================================================//
	
	/**
	 * Odebere posluchaÄŤe udĂˇlostĂ­ protokolu pro odesĂ­lĂˇnĂ­ PL.
	 * 
	 * @param ticketProtocolListener    PosluchaÄŤ udĂˇlostĂ­ protokolu pro odesĂ­lĂˇnĂ­ PL.
	 */
	public void removeTicketProtocolListener(ITicketProtocolListener ticketProtocolListener){
		this.ticketProtocolListener = null;
	}
	
	/**
	 * PĹ™idĂˇ posluchaÄŤe udĂˇlostĂ­ protokolu pro odesĂ­lĂˇnĂ­ PL.
	 * 
	 * @param ticketProtocolListener    PosluchaÄŤ udĂˇlostĂ­ protokolu pro odesĂ­lĂˇnĂ­ PL.
	 */
	public void setTicketProtocolListener(ITicketProtocolListener ticketProtocolListener){
		this.ticketProtocolListener = ticketProtocolListener;
	}
	
	//================================================== HANDLERY UDĂ�LOSTĂŤ ==================================================//

	/**
	 * Handler udĂˇlosti ukonÄŤenĂ­ spojenĂ­.
	 */
	public void onConnectionTerminated(){
		if (ticketProtocolListener != null){
			ticketProtocolListener.onConnectionTerminated();
		}
	}

	/**
	 * Handler na zpracovĂˇnĂ­ udĂˇlosti odeslĂˇnĂ­ zprĂˇvy.
	 * @param sentData OdeslanĂˇ data.   
	 */
	public void onMessageSent(byte[] sentData){
		if (ticketProtocolListener != null){
			ticketProtocolListener.onMessageSent();
		}
	}

	/**
	 * Handler udĂˇlosti pĹ™Ă­jmu dat.
	 * 
	 * @param receivedData    PĹ™ijmutĂˇ data uloĹľenĂˇ ve formÄ› bytovĂ©ho pole.
	 */
	public void onReceivedData(byte[] receivedData){
		//ĹľĂˇdnĂˇ data nepĹ™ijĂ­mĂˇ
	}
	
	//================================================== VĂťKONNĂ‰ METODY ==================================================//
	
	/**
	 * OdpojĂ­ datovĂ˝ protokol od zĂˇkladnĂ­ho protokolu.
	 */
	public void disconnectProtocol(){
		if(networkInterface != null){
			networkInterface.removeOnReceivedDataListener(this);
		}
	}

	/**
	 * OdesĂ­lĂˇ pĹ™edanĂ˝ PL na server.
	 * 
	 * @param ticket    PL, kterĂ˝ se mĂˇ odeslat na server.
	 * @param badgeNumber    SluĹľebnĂ­ ÄŤĂ­slo policisty, kterĂ˝ PL odesĂ­lĂˇ. Pozn.: nemusĂ­
	 * se shodovat s ÄŤĂ­slem policisty, kterĂ˝ je pĹ™ihlĂˇĹˇen.
	 * @throws IOException ProblĂ©m pĹ™i serializaci PL.
	 */
	public void sendTicket(Ticket ticket, int badgeNumber) throws IOException{
		if(networkInterface != null){
			networkInterface.sendData(createTicketMessage(ticket, badgeNumber), this);
		}
	}

	//================================================== PRIVĂ�TNĂŤ METODY ==================================================//
	
	/**
	 * VytvĂˇĹ™Ă­ zprĂˇvu pro nahrĂˇnĂ­ PL na server.
	 * @param ticket ParkovacĂ­ lĂ­stek.
	 * @param badgeNumber SluĹľebnĂ­ ÄŤĂ­slo policisty, kterĂ˝ nahrĂˇvĂˇ PL na server.
	 * @return ZprĂˇva pro odeslĂˇnĂ­ na server.
	 * @throws IOException ProblĂ©m pĹ™i serializaci PL.
	 */
	protected byte[] createTicketMessage(Ticket ticket, int badgeNumber) throws IOException{
		MessageBuilder msg = new MessageBuilder();
		
		msg.putByte(MobileMessageIDs.ID_MSG_UPLOAD_TICKET); //identifikĂˇtor zprĂˇvy
		msg.putInt(badgeNumber); //sluĹľebnĂ­ ÄŤĂ­slo policisty
		
		ByteArrayOutputStream ticketBytes = new ByteArrayOutputStream();
		ObjectOutputStream objOS = new ObjectOutputStream(ticketBytes);
		objOS.writeObject(ticket); //serializuje PL
		objOS.close();
		
		msg.putArrayWithIntLength(ticketBytes.toByteArray()); //vloĹľenĂ­ pole objektu PL
		
		ticketBytes.close();
		
		return msg.getByteArray();
	}
	
}
