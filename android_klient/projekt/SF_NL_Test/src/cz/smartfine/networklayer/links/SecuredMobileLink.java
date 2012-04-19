package cz.smartfine.networklayer.links;

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

import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Constants;
import cz.smartfine.networklayer.util.Conventer;

/**
 * Tøída pro zabezpeèenou komunikaci pøes WiFi nebo mobilní sí.
 * @author Pavel Bro
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SecuredMobileLink implements ILink, Runnable {

	/**
	 * SSL kontext pro šifrovanou komunikaci
	 */
	SSLContext context;
	/**
	 * Továrna na ssl sokety
	 */
    SSLSocketFactory socketFactory;
    /**
     * Soket, kterı je pouíván pro pøenos dat
     */
    SSLSocket socket;

    /**
     * Key Store dùvìryhodnıch certifikaèních autorit pro ovìøení serveru
     */
    KeyStore trustedKS;
    /**
     * Tøída pro ovìøování certifikátu serveru
     */
    TrustManagerFactory trstMngrFactory;
    
    /**
     * Adresa serveru
     */
    InetSocketAddress address;
    
    /**
     * Tøída typu INetworkInterface, která vyuívá slueb této tøídy
     */
    INetworkInterface networkInterface;
    
    /**
     * Stream vstupních dat
     */
    InputStream in;
    /**
     * Stream vıstupních dat
     */
    OutputStream out;
    
 
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
	}
	
	/**
	 * Odebírá posluchaèe pøíjmu dat.
	 * 
	 * @param networkInterface    Základní protokol pro pøenos dat.
	 */
	public void removeOnReceivedDataListener(INetworkInterface networkInterface){
		this.networkInterface = null;
	}

	//================================================== VİKONNÉ METODY ==================================================//
	/**
	 * Odesílá data na server.
	 * 
	 * @param dataToSend    Data urèená pro odeslání na server.
	 * @throws IOException 
	 */
	public void sendData(byte[] dataToSend) throws IOException{
		try{
			out.write(dataToSend);
			out.flush();
		} catch (IOException e){
			closeConnection();
			throw e;
		}
	}

	/**
	 * Zprostøedkovává pøíjem dat ze serveru
	 */
	public void run() {
			boolean headerReading = true; //probíhá pøíjem hlavièky
			boolean messageReading = false; //probíhá ppøíjem tìla zprávy
			byte[] header = new byte[Constants.HEADER_SIZE]; //pole pro uloení hlavièky zprávy
			byte[] data = new byte[Constants.HEADER_SIZE]; //pole pro uloení pøíchozích dat (první pøichází hlavièka -> velikost pole = velikost hlavièky)
			int msgSize = 0; //délka tìla zprávy
			int datalength = 0; //# bytù naètenı v posledním ètení
			int totalBytes = 0; //# bytù celkem naètenıch
	
			try {
				//ète data dokud není naèteno celé pole data[] nebo dokud není dosaeno konce streamu//
				while ((datalength = in.read(data, totalBytes, data.length - datalength)) != -1){
					totalBytes += datalength; //pøiènení právì naètenıch bytù, k celkovému poètu naètenıch bytù od zaèátku ètení zprávy (hlavièky nebo tìla)
					
					//dokonèení naètení tìla zprávy//
					if(messageReading && totalBytes == data.length){
						datalength = 0; //vynulování poètu naètenıch bytù
						totalBytes = 0; //vynulování poèítadla naètenıch bytù
						//nastavení stavovıch promìnnıch --> následuje ètení hlavièky//
						headerReading = true;
						messageReading = false;
						
						if(networkInterface != null){
							networkInterface.onReceivedData(data); //odeslání pøijaté zprávy na NetworkInterface
						}
						
						data = new byte[Constants.HEADER_SIZE]; //nastavení ètecího pole na velikost hlavièky
						continue; //zpráva pøijat, oèekává se další hlavièka
					}
					
					//dokonèení naètení hlavièky zprávy//
					if (headerReading && totalBytes == Constants.HEADER_SIZE){
						//nastavení stavovıch promìnnıch --> následuje ètení tìla zprávy//
						headerReading = false;
						messageReading = true;
						
						header = data;
						msgSize = Conventer.byteArrayToInt(header, Constants.HEADER_LENGTH_OFFSET); //konverze bytù délky zprávy (z hlavièky) na integer
	
						data = new byte[Constants.HEADER_SIZE + msgSize]; //nastavení ètecího pole na velikost tìla zprávy
						System.arraycopy(header, 0, data, 0, Constants.HEADER_SIZE); //pøekopírování hlavièky do nového pole
						//continue;
					}
				} //while
				
			} catch (IOException e) {
				//není potøeba nic dìlat, finally blok vše zaøídí
			} 
			finally{
				closeConnection(); //ukonèení spojení
				connectionTerminated();//oznámení o ukonèení spojení
			}
	}

	/**
	 * Pøipojí se k serveru.
	 * @throws IOException Problém pøi vytváøení socketu.
	 */
	public void connect() throws IOException{
		socket = (SSLSocket) socketFactory.createSocket();
		socket.connect(address);
	}

	/**
	 * Odpojí se od serveru.
	 */
	public void disconnect(){
		closeConnection();
		connectionTerminated();
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
}
