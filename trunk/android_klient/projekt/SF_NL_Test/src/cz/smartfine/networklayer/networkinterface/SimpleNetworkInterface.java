package cz.smartfine.networklayer.networkinterface;
import java.io.IOException;

import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;
import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.networklayer.util.Constants;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.InterThreadType;

/**
 * Tvo�� rozhran� mezi protokoly pro p�enos specifick�ch dat a s�t�.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SimpleNetworkInterface implements INetworkInterface {

	/**
	 * Link pro komunikaci se serverem
	 */
	private ILink link;
	/**
	 * Datov� protokol, kter� vyu��v� slu�by t�to t��dy
	 */
	private IDataProtocol dataProtocol;
	
	/**
	 * Intern� t��da, kter� asynchron� odes�l� data
	 */
	private Sender sender;
	/**
	 * Intern� t��da, kter� asynchron� zpracov�v� p�ijat� data
	 */
	private Receiver receiver;
	/**
	 * Vl�kno pro odes�l�n� dat
	 */
	private Thread senderThread;
	/**
	 * Vl�kno pro p��jem dat
	 */
	private Thread receiverThread;
	
	/**
	 * P��choz� data
	 */
	private InterThreadType<byte[]> in = new InterThreadType<byte[]>();
	/**
	 * Odchoz� data
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
	 * @param link    Link pro odes�l�n� dat.
	 */
	public SimpleNetworkInterface(ILink link) {
		this.link = link;
		
		//vytvo�en� a nastartov�n� objektu pro p��j�m�n� dat v nov�m vl�kn�//
		receiver = new Receiver(this.in, this.dataProtocol);
		receiverThread = new Thread(receiver, "receiverThread");
		receiverThread.start();
		
		if (link != null){
			link.setOnReceivedDataListener(this); //registrace sebe sama jako poslucha�e ud�lost� p��jmu dat v objektu linku
		}
		
		//vytvo�en� a nastartov�n� objektu pro odes�l�n� dat v nov�m vl�kn�//
		sender = new Sender(this.link, this.out, this.dataProtocol);
		senderThread = new Thread(sender, "senderThread");
		senderThread.start();
	}

	//================================================== GET/SET ==================================================//

	/**
	 * Odebere poslucha�e ud�losti p��jmu dat.
	 * 
	 * @param dataProtocol    Datov� protokol poslouchaj�c� ud�lost p��jmu dat.
	 */
	public void removeOnReceivedDataListener(IDataProtocol dataProtocol){
		this.dataProtocol = null;
		this.sender.setDataProtocol(null);
		this.receiver.setDataProtocol(null);
	}

	/**
	 * P�id� poslucha�e ud�losti p��jmu dat.
	 * 
	 * @param dataProtocol    Datov� protokol poslouchaj�c� ud�lost p��jmu dat.
	 */
	public void setOnReceivedDataListener(IDataProtocol dataProtocol){
		this.dataProtocol = dataProtocol;
		this.sender.setDataProtocol(dataProtocol);
		this.receiver.setDataProtocol(dataProtocol);
	}
	
	/**
	 * Nastav� link pro p�ipojen� na s�.
	 * 
	 * @param link    S�ov� rozhran�.
	 */
	public void setLink(ILink link){
		if (this.link != null){
			this.link.removeOnReceivedDataListener(this); //odregistrov�n� se v sou�asn�m linku
		}
		
		this.link = link;
		
		if (this.link != null){
			link.setOnReceivedDataListener(this); //zaregistrov�n� se v nov�m linku
		}

		sender.setLink(link); //zm�na linku u senderu
	}

	//================================================== HANDLERY UD�LOST� ==================================================//

	/**
	 * Handler ud�losti p��jmu dat.
	 * 
	 * @param receivedData    P�ijmut� data ulo�en� ve form� bytov�ho pole.
	 */
	public void onReceivedData(byte[] receivedData){
		try {
			in.put(receivedData); //vlo�� data do inter thread objektu, aby si je mohlo vyzvednout vl�kno receiveru
		} catch (InterruptedException e) {} 
	}
	
	/**
	 * Handler pro zpracov�n� ud�losti ukon�en� spojen�.
	 */
	public void onConnectionTerminated(){
		if (dataProtocol != null){
			dataProtocol.onConnectionTerminated();
		}
	}
	
	//================================================== V�KONN� METODY ==================================================//
	
	/**
	 * Pos�l� data na server.
	 * 
	 * @param dataToSend    Data k odesl�n�.
	 */
	public void sendData(byte[] dataToSend) {
		try {
			out.put(dataToSend); //vlo�� data do inter thread objektu, aby si je mohlo vyzvednout vl�kno senderu
		} catch (InterruptedException e) {} 
	}
	
	//================================================== INTERN� TR�DY ==================================================//
	
	/**
	 * T��da zaji��uj�c� odes�l�n� dat v jin�m vl�kn�
	 * @author Pavel Bro�
	 *
	 */
	private class Sender implements Runnable{

		private ILink link;
		private InterThreadType<byte[]> out;
		private IDataProtocol dataProtocol;
		
		/**
		 * @param link Instance ILink pro odes�l�n� dat.
		 * @param out Objekt pro p�ed�v�n� zpr�v.
		 * @param dataProtocol Datov� protokol, kter� m� b�t informov�n o odesl�n� zpr�vy.
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
					byte[] protocolData = out.get(); //na�ten� dat
					byte[] dataToSend = new byte[Constants.HEADER_SIZE + protocolData.length]; //vytvo�en� pole pro hlavi�ku a data (pole, kter� se bude odes�lat)
					
					dataToSend[0] = Constants.PROTOCOL_VERSION; //nastaven� verze protokolu
					System.arraycopy(Conventer.intToByteArray(protocolData.length), 0, dataToSend, Constants.HEADER_LENGTH_OFFSET, 4); //p�evod d�lky dat na pole 4 byt� a zkop�rov�n� do pole pro odesl�n�
					System.arraycopy(protocolData, 0, dataToSend, Constants.HEADER_SIZE, protocolData.length); //zkop�rov�n� odes�lan�ch dat do pole pro odesl�n�
					
					try {
						if(link != null){
							link.sendData(dataToSend); //odesl�n� dat
						}
						if (dataProtocol != null){
							dataProtocol.onMessageSent(); //informov�n� datov�ho protokolu o odesl�n� dat
						}
					} catch (IOException e) {
						System.out.println("ANDROID: NET INTERFACE SENDER CONNECTION TERMINATED");
						onConnectionTerminated();
					}
				}
			} catch (InterruptedException e){
				//nen� pot�eba d�lat nic
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
	 * T��da zaji��uj�c� p��jem dat v jin�m vl�kn�
	 * @author Pavel Bro�
	 *
	 */
	private class Receiver implements Runnable{

		private InterThreadType<byte[]> in;
		private IDataProtocol dataProtocol;
		
		
		/**
		 * @param in Objekt pro p�ed�v�n� zpr�v
		 * @param dataProtocol Datov� protokol, kter� m� b�t informov�n o p�ijet� zpr�vy.
		 */
		public Receiver(InterThreadType<byte[]> in, IDataProtocol dataProtocol) {
			super();
			this.in = in;
			this.dataProtocol = dataProtocol;
		}


		public void run() {
			try {
				while(true){
					byte[] receivedData = in.get(); //na�te p�ijat� data
					byte[] protocolData = new byte[receivedData.length - Constants.HEADER_SIZE]; //vytvo�� pole na t�lo p�ijat� zpr�vy
					
					//z p�ijat�ch dat zkop�ruje t�lo zpr�vy (hlavi�ku vynech�)
					System.arraycopy(receivedData, Constants.HEADER_SIZE, protocolData, 0, receivedData.length - Constants.HEADER_SIZE);
					
					if (dataProtocol != null){
						dataProtocol.onReceivedData(protocolData); //po�le p�ijat� data datov�mu protokolu
					}
				}
			} catch (InterruptedException e){
				//nen� pot�eba d�lat nic
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