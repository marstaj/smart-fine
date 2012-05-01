/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.smartfine.server.networklayer.dataprotocols.pc;

import cz.smartfine.networklayer.dataprotocols.interfaces.IDataProtocol;

/**
 *
 * @author Pavel Brož
 */
public class BasicServiceProtocol implements IDataProtocol  {

    @Override
    public void disconnectProtocol() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onConnectionTerminated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onMessageSent(byte[] sentData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onReceivedData(byte[] receivedData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
