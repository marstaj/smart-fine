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
 * T��da pro zabezpe�enou komunikaci p�es WiFi nebo mobiln� s�.
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:48
 */
public class SecuredMobileLink implements ILink, Runnable {

	/**
	 * SSL kontext pro �ifrovanou komunikaci
	 */
	SSLContext context;
	/**
	 * Tov�rna na ssl sokety
	 */
    SSLSocketFactory socketFactory;
    /**
     * Soket, kter� je pou��v�n pro p�enos dat
     */
    SSLSocket socket;

    /**
     * Key Store d�v�ryhodn�ch certifika�n�ch autorit pro ov��en� serveru
     */
    KeyStore trustedKS;
    /**
     * T��da pro ov��ov�n� certifik�tu serveru
     */
    TrustManagerFactory trstMngrFactory;
    
    /**
     * Adresa serveru
     */
    InetSocketAddress address;
    
    /**
     * T��da typu INetworkInterface, kter� vyu��v� slu�eb t�to t��dy
     */
    INetworkInterface networkInterface;
    
    /**
     * Stream vstupn�ch dat
     */
    InputStream in;
    /**
     * Stream v�stupn�ch dat
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
	 * @param keyStoreStream InputStream obsahuj�c� KS.
	 * @param keyStorePassword Heslo do KS.
	 * @throws KeyStoreException 	Probl�m p�i vytv��en� KS.
	 * @throws IOException Probl�m p�i na��t�n� KS ze streamu.
	 * @throws CertificateException Probl�m p�i na��t�n� KS ze streamu.
	 * @throws KeyManagementException Probl�m p�i inicializaci kontextu SSL.
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
	 * Nastavuje poslucha�e p��jmu dat.
	 * 
	 * @param networkInterface    Z�kladn� protokol pro p�enos dat.
	 */
	public void setOnReceivedDataListener(INetworkInterface networkInterface){
		this.networkInterface = networkInterface;
	}
	
	/**
	 * Odeb�r� poslucha�e p��jmu dat.
	 * 
	 * @param networkInterface    Z�kladn� protokol pro p�enos dat.
	 */
	public void removeOnReceivedDataListener(INetworkInterface networkInterface){
		this.networkInterface = null;
	}

	//================================================== V�KONN� METODY ==================================================//
	/**
	 * Odes�l� data na server.
	 * 
	 * @param dataToSend    Data ur�en� pro odesl�n� na server.
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
	 * Zprost�edkov�v� p��jem dat ze serveru
	 */
	public void run() {
			boolean headerReading = true; //prob�h� p��jem hlavi�ky
			boolean messageReading = false; //prob�h� pp��jem t�la zpr�vy
			byte[] header = new byte[Constants.HEADER_SIZE]; //pole pro ulo�en� hlavi�ky zpr�vy
			byte[] data = new byte[Constants.HEADER_SIZE]; //pole pro ulo�en� p��choz�ch dat (prvn� p�ich�z� hlavi�ka -> velikost pole = velikost hlavi�ky)
			int msgSize = 0; //d�lka t�la zpr�vy
			int datalength = 0; //# byt� na�ten� v posledn�m �ten�
			int totalBytes = 0; //# byt� celkem na�ten�ch
	
			try {
				//�te data dokud nen� na�teno cel� pole data[] nebo dokud nen� dosa�eno konce streamu//
				while ((datalength = in.read(data, totalBytes, data.length - datalength)) != -1){
					totalBytes += datalength; //p�i�nen� pr�v� na�ten�ch byt�, k celkov�mu po�tu na�ten�ch byt� od za��tku �ten� zpr�vy (hlavi�ky nebo t�la)
					
					//dokon�en� na�ten� t�la zpr�vy//
					if(messageReading && totalBytes == data.length){
						datalength = 0; //vynulov�n� po�tu na�ten�ch byt�
						totalBytes = 0; //vynulov�n� po��tadla na�ten�ch byt�
						//nastaven� stavov�ch prom�nn�ch --> n�sleduje �ten� hlavi�ky//
						headerReading = true;
						messageReading = false;
						
						if(networkInterface != null){
							networkInterface.onReceivedData(data); //odesl�n� p�ijat� zpr�vy na NetworkInterface
						}
						
						data = new byte[Constants.HEADER_SIZE]; //nastaven� �tec�ho pole na velikost hlavi�ky
						continue; //zpr�va p�ijat, o�ek�v� se dal�� hlavi�ka
					}
					
					//dokon�en� na�ten� hlavi�ky zpr�vy//
					if (headerReading && totalBytes == Constants.HEADER_SIZE){
						//nastaven� stavov�ch prom�nn�ch --> n�sleduje �ten� t�la zpr�vy//
						headerReading = false;
						messageReading = true;
						
						header = data;
						msgSize = Conventer.byteArrayToInt(header, Constants.HEADER_LENGTH_OFFSET); //konverze byt� d�lky zpr�vy (z hlavi�ky) na integer
	
						data = new byte[Constants.HEADER_SIZE + msgSize]; //nastaven� �tec�ho pole na velikost t�la zpr�vy
						System.arraycopy(header, 0, data, 0, Constants.HEADER_SIZE); //p�ekop�rov�n� hlavi�ky do nov�ho pole
						//continue;
					}
				} //while
				
			} catch (IOException e) {
				//nen� pot�eba nic d�lat, finally blok v�e za��d�
			} 
			finally{
				closeConnection(); //ukon�en� spojen�
				connectionTerminated();//ozn�men� o ukon�en� spojen�
			}
	}

	/**
	 * P�ipoj� se k serveru.
	 * @throws IOException Probl�m p�i vytv��en� socketu.
	 */
	public void connect() throws IOException{
		socket = (SSLSocket) socketFactory.createSocket();
		socket.connect(address);
	}

	/**
	 * Odpoj� se od serveru.
	 */
	public void disconnect(){
		closeConnection();
		connectionTerminated();
	}
	
	/**
	 * Zji��uje, zda existuje spojen� se serverem.
	 */
	public boolean isConnected(){
		if(socket != null){
			return socket.isConnected();
		}
		return false;
	}

	//================================================== PRIV�TN� METODY ==================================================//
	
	/**
	 * Vol� poslucha�e ud�lost� NetworkInterface p�i ztr�t� spojen�
	 */
	private void connectionTerminated(){
		if (networkInterface != null){
			networkInterface.onConnectionTerminated();
		}
	}
	
	/**
	 * Uzav�e s�ov� spojen�
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