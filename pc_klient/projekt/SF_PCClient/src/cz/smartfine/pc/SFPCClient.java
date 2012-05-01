/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.smartfine.pc;

import cz.smartfine.pc.networklayer.ConnectionProvider;
import cz.smartfine.pc.view.MainFrame;

/**
 *
 * @author Pavel Brož
 */
public class SFPCClient {

    private static SFPCClient application;
    private MainFrame mainFrame;
    private ConnectionProvider connectionProvider;
    
    
    private SFPCClient() {
        this.application = this;
        this.mainFrame = new MainFrame();
    }

    public static SFPCClient getApplication(){
        return application;
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public void setConnectionProvider(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SFPCClient();
    }
}
