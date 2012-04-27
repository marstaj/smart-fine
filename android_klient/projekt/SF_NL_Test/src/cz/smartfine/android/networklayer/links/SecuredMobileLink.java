package cz.smartfine.android.networklayer.links;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import android.os.Debug;
import android.util.DebugUtils;

import junit.framework.Assert;

import cz.smartfine.android.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.android.networklayer.util.Constants;
import cz.smartfine.android.networklayer.util.Conventer;

/**
 * Tøída pro zabezpeèenou komunikaci pøes WiFi nebo mobilní sí.
 * @author Pavel Brož
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SecuredMobileLink implements ILink{

	/**
	 * SSL kontext pro šifrovanou komunikaci
	 */
	private SSLContext context;
	/**
	 * Továrna na ssl sokety
	 */
	private SSLSocketFactory socketFactory;
    /**
     * Soket, který je používán pro pøenos dat
     */
	private SSLSocket socket;

    /**
     * Key Store dùvìryhodných certifikaèních autorit pro ovìøení serveru
     */
	private KeyStore trustedKS;
    /**
     * Tøída pro ovìøování certifikátu serveru
     */
	private TrustManagerFactory trstMngrFactory;
    
    /**
     * Adresa serveru
     */
	private InetSocketAddress address;
    
    /**
     * Tøída typu INetworkInterface, která využívá služeb této tøídy
     */
	private INetworkInterface networkInterface;
    
    /**
     * Stream vstupních dat
     */
	private InputStream in;
    /**
     * Stream výstupních dat
     */
	private OutputStream out;
    
    /**
	 * Interní tøída, která asynchronì pøijímá data
	 */
	private Receiver receiver;
	/**
	 * Vlákno pro pøíjem dat
	 */
	private Thread receiverThread;
	
	//================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
	
	public void finalize() throws Throwable {
		if(in != null){
			in.close();
		}
		if(out != null){
			out.close();
		}
		if(socket != null){
			socket.close();
		}
		if(receiverThread != null && receiverThread.isAlive()){
			receiverThread.interrupt();
		}
	}

	/**
	 * Konstruktor.
	 * 
	 * @param address    Adresa serveru.
	 * @param keyStoreStream InputStream obsahující KS.
	 * @param keyStorePassword Heslo do KS.
	 * @throws KeyStoreException 	Problém pøi vytváøení KS.
	 * @throws IOException Problém pøi naèítání KS ze streamu.
	 * @throws CertificateException Problém pøi naèítání KS ze streamu.
	 * @throws KeyManagementException Problém pøi inicializaci kontextu SSL.
	 */
	public SecuredMobileLink(InetSocketAddress address, InputStream keyStoreStream, String keyStorePassword) throws KeyStoreException, CertificateException, IOException, KeyManagementException {
		this.address=address;
		
		try {
				context = SSLContext.getInstance("TLSv1");

				trustedKS = KeyStore.getInstance("bks");
	            trustedKS.load(keyStoreStream, keyStorePassword.toCharArray());
	            
	            trstMngrFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	            trstMngrFactory.init(trustedKS);
	    		
	    		context.init(null, trstMngrFactory.getTrustManagers(), new SecureRandom());
				socketFactory = context.getSocketFactory();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
	}

	//================================================== GET/SET ==================================================//
	/**
	 * Nastavuje posluchaèe pøíjmu dat.
	 * 
	 * @param networkInterface    Základní protokol pro pøenos dat.
	 */
	public void setOnReceivedDataListener(INetworkInterface networkInterface){
		this.networkInterface = networkInterface;
		if(this.receiver != null){
			this.receiver.setNetworkInterface(networkInterface);
		}
	}
	
	/**
	 * Odebírá posluchaèe pøíjmu dat.
	 * 
	 * @param networkInterface    Základní protokol pro pøenos dat.
	 */
	public void removeOnReceivedDataListener(INetworkInterface networkInterface){
		this.networkInterface = null;
		if(this.receiver != null){
			this.receiver.setNetworkInterface(null);
		}
	}

	//================================================== VÝKONNÉ METODY ==================================================//
	/**
	 * Odesílá data na server.
	 * 
	 * @param dataToSend    Data urèená pro odeslání na server.
	 * @throws IOException 
	 */
	public void sendData(byte[] dataToSend) throws IOException{
		try{
			System.out.println("ANDROID: link.sendData()");
			out.write(dataToSend);
			out.flush();
		} catch (IOException e){
			System.out.println("ANDROID:  link.sendData() EXCEPTION: " + e.getMessage());
			closeConnection();
			throw e;
		}
	}

	/**
	 * Pøipojí se k serveru.
	 * @throws IOException Problém pøi vytváøení socketu.
	 */
	public void connect() throws IOException{
		closeConnection();

		socket = (SSLSocket) socketFactory.createSocket();
		socket.connect(address);
		
		in = socket.getInputStream();
		out = socket.getOutputStream();
		
		receiver = new SecuredMobileLink.Receiver(this.in, this.networkInterface);
		receiverThread = new Thread(receiver, "linkReceiverThread");
		receiverThread.start();
	}

	/**
	 * Odpojí se od serveru.
	 */
	public void disconnect(){
		closeConnection();
	}
	
	/**
	 * Zjišuje, zda existuje spojení se serverem.
	 */
	public boolean isConnected(){
		if(socket != null){
			return socket.isConnected();
		}
		return false;
	}

	//================================================== PRIVÁTNÍ METODY ==================================================//
	
	/**
	 * Volá posluchaèe událostí NetworkInterface pøi ztrátì spojení
	 */
	private void connectionTerminated(){
		if (networkInterface != null){
			System.out.println("ANDROID: LINK EVENT: CONECTION TERMINATED");
			networkInterface.onConnectionTerminated();
		}
	}
	
	/**
	 * Uzavøe síové spojení
	 */
	private void closeConnection(){
		try{
			if(in != null){
				in.close();
			}
		} catch (Exception e){}
		try{
			if(out != null){
				out.close();
			}
		} catch (Exception e){}
		try{
			if(socket != null){
				socket.close();
			}
		} catch (Exception e){}
	}
	
	//================================================== INTERNÍ TRÍDY ==================================================//
	
	/**
	 * Tøída zajišující pøíjem dat v jiném vláknì
	 * @author Pavel Brož
	 *
	 */
	private class Receiver implements Runnable{

		private InputStream in;
		private INetworkInterface networkInterface;
		
		
		/**
		 * Konstruktor.
		 * @param in Pøíchozí stream.
		 * @param networkInterface Síové rozhraní pro pøíjem dat.
		 */
		public Receiver(InputStream in, INetworkInterface networkInterface) {
			super();
			this.in = in;
			this.networkInterface = networkInterface;
		}


		/**
		 * Zprostøedkovává pøíjem dat ze serveru
		 */
		public void run() {
				boolean headerReading = true; //probíhá pøíjem hlavièky
				boolean messageReading = false; //probíhá ppøíjem tìla zprávy
				byte[] header = new byte[Constants.HEADER_SIZE]; //pole pro uložení hlavièky zprávy
				byte[] data = new byte[Constants.HEADER_SIZE]; //pole pro uložení pøíchozích dat (první pøichází hlavièka -> velikost pole = velikost hlavièky)
				int msgSize = 0; //délka tìla zprávy
				int datalength = 0; //# bytù naètený v posledním ètení
				int totalBytes = 0; //# bytù celkem naètených
		
				try {
					//ète data dokud není naèteno celé pole data[] nebo dokud není dosaženo konce streamu//
					while ((datalength = in.read(data, totalBytes, data.length - totalBytes)) != -1){
						totalBytes += datalength; //pøiènení právì naètených bytù, k celkovému poètu naètených bytù od zaèátku ètení zprávy (hlavièky nebo tìla)
						
						//dokonèení naètení tìla zprávy//
						if(messageReading && totalBytes == data.length){
							totalBytes = 0; //vynulování poèítadla naètených bytù
							//nastavení stavových promìnných --> následuje ètení hlavièky//
							headerReading = true;
							messageReading = false;
							
							if(networkInterface != null){
								byte[] copydata = new byte[data.length];
								System.arraycopy(data, 0, copydata, 0, data.length);
								networkInterface.onReceivedData(copydata); //odeslání pøijaté zprávy na NetworkInterface
							}
							
							data = new byte[Constants.HEADER_SIZE]; //nastavení ètecího pole na velikost hlavièky
							continue; //zpráva pøijat, oèekává se další hlavièka
						}
						
						//dokonèení naètení hlavièky zprávy//
						if (headerReading && totalBytes == Constants.HEADER_SIZE){
							//nastavení stavových promìnných --> následuje ètení tìla zprávy//
							headerReading = false;
							messageReading = true;
							
							header = data;
							msgSize = Conventer.byteArrayToInt(header, Constants.HEADER_LENGTH_OFFSET); //konverze bytù délky zprávy (z hlavièky) na integer
		
							data = new byte[Constants.HEADER_SIZE + msgSize]; //nastavení ètecího pole na velikost tìla zprávy
							System.arraycopy(header, 0, data, 0, Constants.HEADER_SIZE); //pøekopírování hlavièky do nového pole
							//continue;
						}
					} //while
					
				} 
				catch (IOException e) {
					//není potøeba nic dìlat, finally blok vše zaøídí
					System.out.println("ANDROID: LINK RECEIVER READ EXCEPTION: " + e.getMessage());
				}finally{
					connectionTerminated();//oznámení o ukonèení spojení
					closeConnection(); //ukonèení spojení
				}
				System.out.println("ANDROID: LINK RECEIVER THREAD END");
		}

		public synchronized INetworkInterface getNetworkInterface() {
			return networkInterface;
		}

		public synchronized void setNetworkInterface(INetworkInterface networkInterface) {
			this.networkInterface = networkInterface;
		}

	}
}
