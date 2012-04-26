package cz.smartfine.networklayer.networkinterface;
import java.io.IOException;

import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.networklayer.util.Constants;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.InterThreadType;

/**
 * Tvoøí rozhraní mezi protokoly pro pøenos specifickıch dat a sítí.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SimpleNetworkInterface implements INetworkInterface {

	/**
	 * Link pro komunikaci se serverem
	 */
	private ILink link;
	/**
	 * Datovı protokol, kterı vyuívá sluby této tøídy
	 */
	private IDataProtocol dataProtocol;
	
	/**
	 * Interní tøída, která asynchronì odesílá data
	 */
	private Sender sender;
	/**
	 * Interní tøída, která asynchronì zpracovává pøijatá data
	 */
	private Receiver receiver;
	/**
	 * Vlákno pro odesílání dat
	 */
	private Thread senderThread;
	/**
	 * Vlákno pro pøíjem dat
	 */
	private Thread receiverThread;
	
	/**
	 * Pøíchozí data
	 */
	private InterThreadType<byte[]> in = new InterThreadType<byte[]>();
	/**
	 * Odchozí data
	 */
	private InterThreadType<byte[]> out = new InterThreadType<byte[]>();
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {
		if(senderThread != null && senderThread.isAlive()){
			senderThread.interrupt();
		}
		if(receiverThread != null && receiverThread.isAlive()){
			receiverThread.interrupt();
		}
	}

	/**
	 * Konstruktor.
	 * 
	 */
	public SimpleNetworkInterface() {
		this(null);
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param link    Link pro odesílání dat.
	 */
	public SimpleNetworkInterface(ILink link) {
		this.link = link;
		
		//vytvoøení a nastartování objektu pro pøíjímání dat v novém vláknì//
		receiver = new Receiver(this.in, this.dataProtocol);
		receiverThread = new Thread(receiver, "receiverThread");
		receiverThread.start();
		
		if (link != null){
			link.setOnReceivedDataListener(this); //registrace sebe sama jako posluchaèe událostí pøíjmu dat v objektu linku
		}
		
		//vytvoøení a nastartování objektu pro odesílání dat v novém vláknì//
		sender = new Sender(this.link, this.out, this.dataProtocol);
		senderThread = new Thread(sender, "senderThread");
		senderThread.start();
	}

	//================================================== GET/SET ==================================================//

	/**
	 * Odebere posluchaèe události pøíjmu dat.
	 * 
	 * @param dataProtocol    Datovı protokol poslouchající událost pøíjmu dat.
	 */
	public void removeOnReceivedDataListener(IDataProtocol dataProtocol){
		this.dataProtocol = null;
		this.sender.setDataProtocol(null);
		this.receiver.setDataProtocol(null);
	}

	/**
	 * Pøidá posluchaèe události pøíjmu dat.
	 * 
	 * @param dataProtocol    Datovı protokol poslouchající událost pøíjmu dat.
	 */
	public void setOnReceivedDataListener(IDataProtocol dataProtocol){
		this.dataProtocol = dataProtocol;
		this.sender.setDataProtocol(dataProtocol);
		this.receiver.setDataProtocol(dataProtocol);
	}
	
	/**
	 * Nastaví link pro pøipojení na sí.
	 * 
	 * @param link    Síové rozhraní.
	 */
	public void setLink(ILink link){
		if (this.link != null){
			this.link.removeOnReceivedDataListener(this); //odregistrování se v souèasném linku
		}
		
		this.link = link;
		
		if (this.link != null){
			link.setOnReceivedDataListener(this); //zaregistrování se v novém linku
		}

		sender.setLink(link); //zmìna linku u senderu
	}

	//================================================== HANDLERY UDÁLOSTÍ ==================================================//

	/**
	 * Handler události pøíjmu dat.
	 * 
	 * @param receivedData    Pøijmutá data uloená ve formì bytového pole.
	 */
	public void onReceivedData(byte[] receivedData){
		try {
			in.put(receivedData); //vloí data do inter thread objektu, aby si je mohlo vyzvednout vlákno receiveru
		} catch (InterruptedException e) {} 
	}
	
	/**
	 * Handler pro zpracování události ukonèení spojení.
	 */
	public void onConnectionTerminated(){
		if (dataProtocol != null){
			dataProtocol.onConnectionTerminated();
		}
	}
	
	//================================================== VİKONNÉ METODY ==================================================//
	
	/**
	 * Posílá data na server.
	 * 
	 * @param dataToSend    Data k odeslání.
	 */
	public void sendData(byte[] dataToSend) {
		try {
			out.put(dataToSend); //vloí data do inter thread objektu, aby si je mohlo vyzvednout vlákno senderu
		} catch (InterruptedException e) {} 
	}
	
	//================================================== INTERNÍ TRÍDY ==================================================//
	
	/**
	 * Tøída zajišující odesílání dat v jiném vláknì
	 * @author Pavel Bro
	 *
	 */
	private class Sender implements Runnable{

		private ILink link;
		private InterThreadType<byte[]> out;
		private IDataProtocol dataProtocol;
		
		/**
		 * @param link Instance ILink pro odesílání dat.
		 * @param out Objekt pro pøedávání zpráv.
		 * @param dataProtocol Datovı protokol, kterı má bıt informován o odeslání zprávy.
		 */
		public Sender(ILink link, InterThreadType<byte[]> out, IDataProtocol dataProtocol) {
			super();
			this.link = link;
			this.out = out;
			this.dataProtocol = dataProtocol;
		}


		public void run() {
			try {
				while(true){
					byte[] protocolData = out.get(); //naètení dat
					byte[] dataToSend = new byte[Constants.HEADER_SIZE + protocolData.length]; //vytvoøení pole pro hlavièku a data (pole, které se bude odesílat)
					
					dataToSend[0] = Constants.PROTOCOL_VERSION; //nastavení verze protokolu
					System.arraycopy(Conventer.intToByteArray(protocolData.length), 0, dataToSend, Constants.HEADER_LENGTH_OFFSET, 4); //pøevod délky dat na pole 4 bytù a zkopírování do pole pro odeslání
					System.arraycopy(protocolData, 0, dataToSend, Constants.HEADER_SIZE, protocolData.length); //zkopírování odesílanıch dat do pole pro odeslání
					
					try {
						if(link != null){
							link.sendData(dataToSend); //odeslání dat
						}
						if (dataProtocol != null){
							dataProtocol.onMessageSent(); //informování datového protokolu o odeslání dat
						}
					} catch (IOException e) {
						System.out.println("ANDROID: NET INTERFACE SENDER CONNECTION TERMINATED");
						onConnectionTerminated();
					}
				}
			} catch (InterruptedException e){
				//není potøeba dìlat nic
			}
		}

		public synchronized ILink getLink() {
			return link;
		}
		
		public synchronized void setLink(ILink link) {
			this.link = link;
		}

		public synchronized IDataProtocol getDataProtocol() {
			return dataProtocol;
		}

		public synchronized void setDataProtocol(IDataProtocol dataProtocol) {
			this.dataProtocol = dataProtocol;
		}

	}
	
	/**
	 * Tøída zajišující pøíjem dat v jiném vláknì
	 * @author Pavel Bro
	 *
	 */
	private class Receiver implements Runnable{

		private InterThreadType<byte[]> in;
		private IDataProtocol dataProtocol;
		
		
		/**
		 * @param in Objekt pro pøedávání zpráv
		 * @param dataProtocol Datovı protokol, kterı má bıt informován o pøijetí zprávy.
		 */
		public Receiver(InterThreadType<byte[]> in, IDataProtocol dataProtocol) {
			super();
			this.in = in;
			this.dataProtocol = dataProtocol;
		}


		public void run() {
			try {
				while(true){
					byte[] receivedData = in.get(); //naète pøijatá data
					byte[] protocolData = new byte[receivedData.length - Constants.HEADER_SIZE]; //vytvoøí pole na tìlo pøijaté zprávy
					
					//z pøijatıch dat zkopíruje tìlo zprávy (hlavièku vynechá)
					System.arraycopy(receivedData, Constants.HEADER_SIZE, protocolData, 0, receivedData.length - Constants.HEADER_SIZE);
					
					if (dataProtocol != null){
						dataProtocol.onReceivedData(protocolData); //pošle pøijatá data datovému protokolu
					}
				}
			} catch (InterruptedException e){
				//není potøeba dìlat nic
			}
		}

		public synchronized IDataProtocol getDataProtocol() {
			return dataProtocol;
		}

		public synchronized void setDataProtocol(IDataProtocol dataProtocol) {
			this.dataProtocol = dataProtocol;
		}

	}

}