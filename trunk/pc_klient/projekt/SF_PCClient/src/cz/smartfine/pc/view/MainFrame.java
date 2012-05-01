/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.smartfine.pc.view;

import cz.smartfine.networklayer.model.pc.PCClientPermission;
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 *
 * @author Pavel Brož
 */
public class MainFrame extends JFrame implements ILoginPanelListener{

    JTabbedPane workingPane;
    LoginPanel pnlLogin;
    
    public MainFrame() {
        super("Smart-Fine - PC klient");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 800;
        int height = 600;
        int x = (int) ((screenSize.width / 2) - (width / 2));
        int y = (int) ((screenSize.height / 2) - (height / 2));

        setBounds(x, y, width, height);

/*
        JTabbedPane tabbedPane = new JTabbedPane();

        JComponent panel1 = makeTextPanel("Panel #1");
        tabbedPane.addTab("tab 1", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = makeTextPanel("Panel #2");
        tabbedPane.addTab("Tab 2", panel2);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = makeTextPanel("Panel #3");
        tabbedPane.addTab("Tab 3", panel3);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        JComponent panel4 = makeTextPanel("Panel #4 (has a preferred size of 410 x 50).");
        panel4.setPreferredSize(new Dimension(410, 50));
        tabbedPane.addTab("Tab 4", panel4);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        //this.setJMenuBar(createMenuBar()); //vytvoří menu
        this.setLayout(new BorderLayout()); //nastavý layout
        //this.add(createToolBar(), BorderLayout.NORTH); //vytvoří toolbar
        this.getContentPane().add(tabbedPane); //vloží desktop pane
*/
        this.setLayout(new BorderLayout()); //nastavý layout
        pnlLogin = new LoginPanel(this);
        this.add(pnlLogin, BorderLayout.CENTER);
        
        this.setVisible(true); //zobrazí
    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }


    /**
     * Handler události úspěšného přihlášení na server.
     *
     * @param permissions
     */
    @Override
    public void onLoggedIn(PCClientPermission permissions){
        
    }
    
}
