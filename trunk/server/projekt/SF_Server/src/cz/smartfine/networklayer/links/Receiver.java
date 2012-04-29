package cz.smartfine.networklayer.links;

import cz.smartfine.networklayer.networkinterface.BaseProtocolConstants;
import cz.smartfine.networklayer.networkinterface.INetworkInterface;
import cz.smartfine.networklayer.util.Conventer;
import java.io.IOException;
import java.io.InputStream;

/**
 * Třída zajišťující příjem dat v jiném vlákně.
 *
 * @author Pavel Brož
 * @version 1.0 @updated 27-4-2012 18:18:44
 */
public class Receiver implements Runnable {

    private InputStream in;
    private INetworkInterface networkInterface;
    private ILink link;

    /**
     * Konstruktor.
     *
     * @param in Příchozí stream.
     * @param networkInterface Síťové rozhraní pro příjem dat.
     * @param link Link, pro který Receiver přijímá data.
     */
    public Receiver(InputStream in, INetworkInterface networkInterface, ILink link) {
        this.in = in;
        this.networkInterface = networkInterface;
        this.link = link;
    }

    /**
     * Zprostředkovává příjem dat ze sítě.
     */
    @Override
    public void run() {
        boolean headerReading = true; //probíhá příjem hlavičky
        boolean messageReading = false; //probíhá ppříjem těla zprávy
        byte[] header = new byte[BaseProtocolConstants.HEADER_SIZE]; //pole pro uložení hlavičky zprávy
        byte[] data = new byte[BaseProtocolConstants.HEADER_SIZE]; //pole pro uložení příchozích dat (první přichází hlavička -> velikost pole = velikost hlavičky)
        int msgSize = 0; //délka těla zprávy
        int datalength = 0; //# bytů načtený v posledním čtení
        int totalBytes = 0; //# bytů celkem načtených

        try {
            //čte data dokud není načteno celé pole data[] nebo dokud není dosaženo konce streamu//
            while ((datalength = in.read(data, totalBytes, data.length - totalBytes)) != -1) {
                totalBytes += datalength; //přičnení právě načtených bytů, k celkovému počtu načtených bytů od začátku čtení zprávy (hlavičky nebo těla)

                //dokončení načtení těla zprávy//
                if (messageReading && totalBytes == data.length) {
                    totalBytes = 0; //vynulování počítadla načtených bytů
                    //nastavení stavových proměnných --> následuje čtení hlavičky//
                    headerReading = true;
                    messageReading = false;

                    if (networkInterface != null) {
                        byte[] copydata = new byte[data.length];
                        System.arraycopy(data, 0, copydata, 0, data.length);
                        networkInterface.onReceivedData(copydata); //odeslání přijaté zprávy na NetworkInterface
                    }

                    data = new byte[BaseProtocolConstants.HEADER_SIZE]; //nastavení čtecího pole na velikost hlavičky
                    continue; //zpráva přijat, očekává se další hlavička
                }

                //dokončení načtení hlavičky zprávy//
                if (headerReading && totalBytes == BaseProtocolConstants.HEADER_SIZE) {
                    //nastavení stavových proměnných --> následuje čtení těla zprávy//
                    headerReading = false;
                    messageReading = true;

                    header = data;
                    msgSize = Conventer.byteArrayToInt(header, BaseProtocolConstants.HEADER_LENGTH_OFFSET); //konverze bytů délky zprávy (z hlavičky) na integer

                    data = new byte[BaseProtocolConstants.HEADER_SIZE + msgSize]; //nastavení čtecího pole na velikost těla zprávy
                    System.arraycopy(header, 0, data, 0, BaseProtocolConstants.HEADER_SIZE); //překopírování hlavičky do nového pole
                    //continue;
                }
            } //while

        } catch (IOException e) {
            //není potřeba nic dělat, finally blok vše zařídí
            System.out.println("ANDROID: LINK RECEIVER READ EXCEPTION: " + e.getMessage());
        } finally {
            if (networkInterface != null) {
                System.out.println("ANDROID: LINK EVENT: CONECTION TERMINATED");
                networkInterface.onConnectionTerminated(); //oznámení o ukončení spojení
            }
            link.disconnect(); //ukončení spojení
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