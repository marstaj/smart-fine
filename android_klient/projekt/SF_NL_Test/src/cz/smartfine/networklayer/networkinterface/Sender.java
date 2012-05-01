package cz.smartfine.networklayer.networkinterface;

import cz.smartfine.networklayer.links.ILink;
import cz.smartfine.networklayer.util.Conventer;
import cz.smartfine.networklayer.util.DataPackage;
import cz.smartfine.networklayer.util.InterThreadType;
import java.io.IOException;

/**
 * Třída zajišťující odesílání dat v jiném vlákně.
 * 
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:46
 */
public class Sender implements Runnable {

	private ILink link;
	private InterThreadType<DataPackage> out;
	private INetworkInterface networkInterface;

	/**
	 * Konstruktor.
	 * 
	 * @param link
	 *            Instance ILink pro odesílání dat.
	 * @param out
	 *            Objekt pro předávání zpráv.
	 * @param networkInterface
	 *            Síťové rozhraní, které přes tuto třídu odesílá data.
	 */
	public Sender(ILink link, InterThreadType<DataPackage> out,
			INetworkInterface networkInterface) {
		super();
		this.link = link;
		this.out = out;
		this.networkInterface = networkInterface;
	}

	@Override
	public void run() {
		try {
			while (true) {
				DataPackage dp = out.get(); // získání zprávy
				byte[] protocolData = dp.getMessage(); // načtení dat
				byte[] dataToSend = new byte[BaseProtocolConstants.HEADER_SIZE
						+ protocolData.length]; // vytvoření pole pro hlavičku a
												// data (pole, které se bude
												// odesílat)

				dataToSend[0] = BaseProtocolConstants.PROTOCOL_VERSION; // nastavení
																		// verze
																		// protokolu
				System.arraycopy(Conventer.intToByteArray(protocolData.length),
						0, dataToSend,
						BaseProtocolConstants.HEADER_LENGTH_OFFSET, 4); // převod
																		// délky
																		// dat
																		// na
																		// pole
																		// 4
																		// bytů
																		// a
																		// zkopírování
																		// do
																		// pole
																		// pro
																		// odeslání
				System.arraycopy(protocolData, 0, dataToSend,
						BaseProtocolConstants.HEADER_SIZE, protocolData.length); // zkopírování
																					// odesílaných
																					// dat
																					// do
																					// pole
																					// pro
																					// odeslání

				try {
					if (link != null) {
						link.sendData(dataToSend); // odeslání dat

						if (dp.getSender() != null) {
							dp.getSender().onMessageSent(protocolData); // informování
																		// datového
																		// protokolu
																		// o
																		// odeslání
																		// dat
						}
					}
				} catch (IOException e) {
					System.out
							.println("ANDROID: NET INTERFACE SENDER CONNECTION TERMINATED");
					if (networkInterface != null) {
						networkInterface.onConnectionTerminated();
					}
				}
			}
		} catch (InterruptedException e) {
			// není potřeba dělat nic
		}
	}

	public synchronized ILink getLink() {
		return link;
	}

	public synchronized void setLink(ILink link) {
		this.link = link;
	}
}