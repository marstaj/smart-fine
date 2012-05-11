package cz.smartfine.pc.view;

import cz.smartfine.networklayer.model.pc.PCClientPermission;
import cz.smartfine.networklayer.model.pc.PCLoginFailReason;
import cz.smartfine.pc.SFPCClient;
import cz.smartfine.pc.networklayer.ConnectionProvider;
import cz.smartfine.pc.networklayer.business.listeners.ILoginProtocolListener;
import cz.smartfine.pc.networklayer.dataprotocols.LoginProtocol;
import cz.smartfine.pc.networklayer.links.SecuredPCClientLink;
import cz.smartfine.networklayer.networkinterface.SimpleNetworkInterface;
import cz.smartfine.pc.preferences.PCClientPreferences;
import cz.smartfine.pc.view.util.UIValidator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Panel pro přihlášení k serveru.
 *
 * @author Pavel Brož
 */
public class LoginPanel extends javax.swing.JPanel implements ILoginProtocolListener {

    /**
     * Pomocná třída pro připojení k serveru bez blokování GUI vlákna.
     */
    private class Connector extends SwingWorker<Boolean, Object> {

        LoginProtocol loginProtocol;
        LoginPanel thisPanel; //login panel pro zobrazení dialogů a jako posluchač událostí
        int badgeNumber;
        int pin;

        /**
         * Konstruktor.
         * @param loginProtocol Login protokol.
         * @param thisPanel Vlastník třídy.
         * @param badgeNumber Služební číslo.
         * @param pin PIN.
         */
        public Connector(LoginProtocol loginProtocol, LoginPanel thisPanel, int badgeNumber, int pin) {
            this.loginProtocol = loginProtocol;
            this.thisPanel = thisPanel;
            this.badgeNumber = badgeNumber;
            this.pin = pin;
        }

        @Override
        public Boolean doInBackground() {
            return SFPCClient.getApplication().getConnectionProvider().connect(); //připojí se k serveru
        }

        /**
         * Metoda se zavolá po dokončení připojení.
         */
        @Override
        protected void done() {
            try {
                //načte výsledek připojení; pokud bylo připojení úspěšné, přihlásí se, jinak ohlásí chybu//
                if (get()) {
                    //vytvoří třídu přihlašovacího protokolu a přihlásí se//
                    loginProtocol = new LoginProtocol(SFPCClient.getApplication().getConnectionProvider().getNetworkInterface(), thisPanel);
                    loginProtocol.loginToServer(badgeNumber, pin);
                } else {
                    JOptionPane.showMessageDialog(null, SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.connecting.fail.msg"),
                            SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.connecting.fail.title"), JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception e) {
            }
        }
    }
    /**
     * Posluchač přihlášení.
     */
    ILoginPanelListener loginListener;
    /**
     * Přihlašovací protokol.
     */
    LoginProtocol loginProtocol;
    /**
     * Poslední zadané služební číslo pro zapamatování.
     */
    private int lastBadgeNumber;
    /**
     * Poslední zadaný PIN pro zapamatování.
     */
    private int lastPIN;
    /**
     * Proměnná udávající stav přihlášení.
     */
    private boolean loggedIn = false;

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    
    /**
     * Konstruktor.
     */
    public LoginPanel() {
        initComponents();
    }

    /**
     * Konstruktor.
     *
     * @param loginListener Posluchač události přihlášení.
     */
    public LoginPanel(ILoginPanelListener loginListener) {
        this();
        this.loginListener = loginListener;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtBadgeNumber = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        txtPin = new javax.swing.JPasswordField();
        butLogin = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText(SFPCClient.getApplication().getLocalization().getString("loginpanel.badgenumberfield.text"));
        jLabel1.setToolTipText("");

        try {
            txtBadgeNumber.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("######")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtBadgeNumber.setText("123456");

        jLabel2.setText(SFPCClient.getApplication().getLocalization().getString("loginpanel.pinfield.text"));
        jLabel2.setToolTipText("");

        txtPin.setText("54321");

        butLogin.setText(SFPCClient.getApplication().getLocalization().getString("loginpanel.loginbutton.text"));
        butLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(butLogin)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtPin, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtBadgeNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(lblStatus))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBadgeNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(butLogin)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    //================================================== UI HANDLERY ==================================================//
    
    /**
     * Reaguje na stisknutí tlačítka přihlášení.
     *
     * @param evt
     */
    private void butLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butLoginActionPerformed
        if (!UIValidator.checkLoginFormat(txtBadgeNumber.getText(), txtPin.getPassword())) {
            JOptionPane.showMessageDialog(this, SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.loginformat.msg"),
                    SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.loginformat.title"), JOptionPane.WARNING_MESSAGE);
        } else {
            ConnectionProvider cp = SFPCClient.getApplication().getConnectionProvider(); //načte con. prov. z hlavní třídy
            //pokud už není con. prov. vytvořen, vytvoří ho//
            if (cp == null) {
                InetSocketAddress address = new InetSocketAddress(PCClientPreferences.getServerAddress(), PCClientPreferences.getServerPort()); //adresa serveru a port z nastavení
                InputStream inKeyStore = new ByteArrayInputStream(PCClientPreferences.getKeyStore()); //keystore z nastavení

                SecuredPCClientLink link;
                try {
                    //vytvoří síťovou strukturu//
                    link = new SecuredPCClientLink(address, inKeyStore, PCClientPreferences.getKeyStorePassword());
                    SimpleNetworkInterface ni = new SimpleNetworkInterface();
                    cp = new ConnectionProvider(link, ni);

                    SFPCClient.getApplication().setConnectionProvider(cp); //nastaví con. prov.
                } catch (Exception e) { //chyba při pokusu o vytvoření síťové struktury
                    JOptionPane.showMessageDialog(this, SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.createcp.msg"),
                            SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.createcp.title"), JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            lblStatus.setText(SFPCClient.getApplication().getLocalization().getString("loginpanel.status.connecting.text")); //vypíše zprávu o připojování//

            //po přijetí zprávy ze serveru o přihlášení, budou tyto údaje uloženy pro případ nutnosti znovu ustanovit spojení na server//
            lastBadgeNumber = Integer.parseInt(txtBadgeNumber.getText());
            lastPIN = Integer.parseInt(new String(txtPin.getPassword()));
            txtPin.setText(""); //odstranění PINu z pole

            //připojí se na server v jiném vlákně//
            (new Connector(loginProtocol, this, lastBadgeNumber, lastPIN)).execute();
        }
    }//GEN-LAST:event_butLoginActionPerformed

    //================================================== UI PRVKY ==================================================//
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JFormattedTextField txtBadgeNumber;
    private javax.swing.JPasswordField txtPin;
    // End of variables declaration//GEN-END:variables

    //================================================== HANDLERY PROTOKOLŮ ==================================================//
    
    @Override
    public void onConnectionTerminated() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                //nastaví výchozí text
                lblStatus.setText("");

                if (loginProtocol != null) {
                    loginProtocol.disconnectProtocol();
                }
                //zobrazí hlášku že došlo k ukončení spojení
                JOptionPane.showMessageDialog(null, SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.login.termcon.msg"),
                        SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.login.termcon.title"), JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void onLoginConfirmed(PCClientPermission permissions) {
        /**
         * pomocná třída pro předání oprávnění do vlákna GUI
         */
        class LoginConfirmedResender implements Runnable {

            private PCClientPermission permissions;

            public LoginConfirmedResender(PCClientPermission permissions) {
                this.permissions = permissions;
            }

            @Override
            public void run() {
                loggedIn = true;
                if (loginProtocol != null) {
                    loginProtocol.disconnectProtocol();
                }
                //uloží údaje pro pozdější použití//
                PCClientPreferences.setBadgeNumber(lastBadgeNumber);
                PCClientPreferences.setPin(lastPIN);

                loginListener.onLoggedIn(this.permissions); //zavolá posluchače událostí o přihlášení - hlavní okno
            }
        }

        javax.swing.SwingUtilities.invokeLater(new LoginConfirmedResender(permissions)); //zavolání handleru na hlavním okně a předání oprávnění
    }

    @Override
    public void onMessageSent() {}

    @Override
    public void onLoginFailed(PCLoginFailReason reason) {
        switch (reason) {
            case UNKNOWN_REASON: //neznámý důvod
                //zavolání dialogu ve vlákně GUI//
                javax.swing.SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        lblStatus.setText(""); //nastaví výchozí text
                        if (loginProtocol != null) {
                            loginProtocol.disconnectProtocol();
                        }
                        JOptionPane.showMessageDialog(null, SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.login.fail.unknownerr.msg"),
                                SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.login.fail.title"), JOptionPane.ERROR_MESSAGE);
                    }
                });
                break;
            case WRONG_BADGE_NUMBER_OR_PIN: //chybné služební číslo nebo pin
                //zavolání dialogu ve vlákně GUI//
                javax.swing.SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        lblStatus.setText(""); //nastaví výchozí text
                        if (loginProtocol != null) {
                            loginProtocol.disconnectProtocol();
                        }
                        JOptionPane.showMessageDialog(null, SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.login.fail.wrongbnorpin.msg"),
                                SFPCClient.getApplication().getLocalization().getString("exception.loginpanel.login.fail.title"), JOptionPane.ERROR_MESSAGE);
                    }
                });
                break;
        }

    }
}
