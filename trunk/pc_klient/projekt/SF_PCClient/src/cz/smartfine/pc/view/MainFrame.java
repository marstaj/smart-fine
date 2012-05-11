package cz.smartfine.pc.view;

import cz.smartfine.networklayer.model.pc.PCClientPermission;
import cz.smartfine.pc.SFPCClient;
import cz.smartfine.pc.networklayer.ConnectionProvider;
import cz.smartfine.pc.networklayer.dataprotocols.LoginProtocol;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author Pavel Brož
 */
public class MainFrame extends JFrame implements ILoginPanelListener, ILogoutListener {

    /**
     * Záložkový panel.
     */
    JTabbedPane workingPane;
    /**
     * Logovací obrazovka.
     */
    LoginPanel pnlLogin;

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    /**
     * Konstruktor okna.
     */
    public MainFrame() {
        super(SFPCClient.getApplication().getLocalization().getString("app.mainform.title"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                ConnectionProvider conProvider = SFPCClient.getApplication().getConnectionProvider();
                if (conProvider != null && conProvider.isConnected()) {
                    LoginProtocol lp = new LoginProtocol(conProvider.getNetworkInterface());
                    lp.logoutFromServer();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 800;
        int height = 600;
        int x = (int) ((screenSize.width / 2) - (width / 2));
        int y = (int) ((screenSize.height / 2) - (height / 2));

        setBounds(x, y, width, height);

        this.setLayout(new GridBagLayout()); //nastavý layout
        pnlLogin = new LoginPanel(this);
        this.getContentPane().add(pnlLogin, new GridBagConstraints());

        this.setVisible(true); //zobrazí
    }

    //================================================== HANDLERY ==================================================//
    /**
     * Handler události úspěšného přihlášení na server.
     *
     * @param permissions
     */
    @Override
    public void onLoggedIn(PCClientPermission permissions) {
        workingPane = new JTabbedPane();

        if (permissions.isPermShowOwnTickets()) {
            TicketPanel panel = new TicketPanel(true, false);
            workingPane.addTab(SFPCClient.getApplication().getLocalization().getString("tabs.showownticket.title"), panel);
        }

        if (permissions.isPermShowTickets()) {
            TicketPanel panel;
            if (permissions.isPermDeleteTickets()) {
                panel = new TicketPanel(false, true);
            } else {
                panel = new TicketPanel(false, false);
            }

            workingPane.addTab(SFPCClient.getApplication().getLocalization().getString("tabs.showticket.title"), panel);
        }

        if (permissions.isPermShowGeodata()) {
            GeoDataPanel panel = new GeoDataPanel();
            workingPane.addTab(SFPCClient.getApplication().getLocalization().getString("tabs.geodata.title"), panel);
        }

        if (permissions.isPermAdminAssoc()) {
            AdminAssocPanel panel = new AdminAssocPanel();
            workingPane.addTab(SFPCClient.getApplication().getLocalization().getString("tabs.adminassoc.title"), panel);
        }

        if (permissions.isPermAdminDevices()) {
            RegisterDevicePanel panel = new RegisterDevicePanel();
            workingPane.addTab(SFPCClient.getApplication().getLocalization().getString("tabs.registerdev.title"), panel);

            DeleteDevicePanel panel1 = new DeleteDevicePanel();
            workingPane.addTab(SFPCClient.getApplication().getLocalization().getString("tabs.deletedev.title"), panel1);
        }

        if (permissions.isPermChngPin()) {
            ChangePinPanel panel = new ChangePinPanel();
            workingPane.addTab(SFPCClient.getApplication().getLocalization().getString("tabs.changepin.title"), panel);
        }
        LogoutPanel logoutPanel = new LogoutPanel(this);
        workingPane.addTab(SFPCClient.getApplication().getLocalization().getString("tabs.logout.title"), logoutPanel);

        this.getContentPane().remove(pnlLogin);
        this.setLayout(new BorderLayout()); //nastavý layout
        this.getContentPane().add(workingPane);

        validate();
        repaint();
    }

    /**
     * Handler události odhlášení.
     */
    @Override
    public void onLogout() {
        this.getContentPane().remove(workingPane);
        this.setLayout(new GridBagLayout()); //nastavý layout
        pnlLogin = new LoginPanel(this);
        this.getContentPane().add(pnlLogin, new GridBagConstraints());

        validate();
        repaint();
    }
}
