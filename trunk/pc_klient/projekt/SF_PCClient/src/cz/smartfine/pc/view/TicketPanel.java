package cz.smartfine.pc.view;

import cz.smartfine.model.NetworkTicket;
import cz.smartfine.model.PolicemanDB;
import cz.smartfine.networklayer.model.pc.QueryList;
import cz.smartfine.networklayer.model.pc.QueryState;
import cz.smartfine.pc.SFPCClient;
import cz.smartfine.pc.networklayer.business.listeners.IQueryProtocolListener;
import cz.smartfine.pc.networklayer.dataprotocols.QueryProtocol;
import cz.smartfine.pc.preferences.PCClientPreferences;
import cz.smartfine.pc.query.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

/**
 * Panel pro zobrazení PL.
 *
 * @author Pavel Brož
 */
public class TicketPanel extends javax.swing.JPanel implements IQueryProtocolListener, IPolicemanListLoaderListener {

    /**
     * Příznak zobrazení vlastních lístků.
     */
    boolean ownTickets;
    /**
     * Příznak oprávnění k mazání lístků.
     */
    boolean deleteTickets;
    /**
     * Protokol pro získávání dat.
     */
    private QueryProtocol qp;
    /**
     * Seznam policistů získaný ze serveru.
     */
    private List<PolicemanDB> policemanList;
    /**
     * Pomocný objekt, který načítá seznam policistů.
     */
    private PolicemanListLoader pll;
    /**
     * Model, který slouží pro zobrazení PL v tabulce.
     */
    private TicketTableModel ticketModel = null;
    /**
     * Identifikátor mazacího dotazu.
     */
    private int idDeleteQuery = -1;
    /**
     * Index smazaného řádku.
     */
    private int deletedRowIx = -1;

    //================================================== KONSTRUKTORY & DESTRUKTORY ==================================================//
    /**
     * Konstruktor.
     */
    public TicketPanel() {
        initComponents();
    }

    /**
     * Konstruktor.
     *
     * @param ownTickets Zobrazují se vlastní PL.
     * @param deleteTickets Policista může PL mazat.
     */
    public TicketPanel(boolean ownTickets, boolean deleteTickets) {
        this.ownTickets = ownTickets;
        this.deleteTickets = deleteTickets;
        
        initComponents();
    }

    //================================================== UI HANDLERY ==================================================//
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnuContext = new javax.swing.JPopupMenu();
        mnuiDelete = new javax.swing.JMenuItem();
        dateSince = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dateUntil = new com.toedter.calendar.JDateChooser();
        butLoadTickets = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        cmbPolicemen = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTickets = new javax.swing.JTable();

        mnuContext.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                mnuContextPopupMenuWillBecomeVisible(evt);
            }
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        mnuiDelete.setText(SFPCClient.getApplication().getLocalization().getString("showticket.contextmenu.delete.text"));
        mnuiDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuiDeleteActionPerformed(evt);
            }
        });
        mnuContext.add(mnuiDelete);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setText(SFPCClient.getApplication().getLocalization().getString("showticket.since.text"));

        jLabel2.setText(SFPCClient.getApplication().getLocalization().getString("showticket.until.text"));

        butLoadTickets.setText(SFPCClient.getApplication().getLocalization().getString("showticket.loadbut.text"));
        butLoadTickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butLoadTicketsActionPerformed(evt);
            }
        });

        jLabel3.setText(SFPCClient.getApplication().getLocalization().getString("showticket.policeman.text"));

        tblTickets.setToolTipText("");
        tblTickets.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblTickets.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblTickets);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(butLoadTickets)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbPolicemen, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dateSince, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                                    .addComponent(dateUntil, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbPolicemen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(dateSince, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateUntil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(butLoadTickets)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Načte PL.
     *
     * @param evt
     */
    private void butLoadTicketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butLoadTicketsActionPerformed
        if (dateSince.getDate() != null && dateUntil.getDate() != null && cmbPolicemen.getSelectedItem() != null
                && (dateSince.getDate().before(dateUntil.getDate()) || dateSince.getDate().equals(dateUntil.getDate()))) {

            loadTickets();
        } else {
            JOptionPane.showMessageDialog(null, SFPCClient.getApplication().getLocalization().getString("exception.showticketpanel.invalidinp.msg"),
                    SFPCClient.getApplication().getLocalization().getString("exception.showticketpanel.invalidinp.title"), JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_butLoadTicketsActionPerformed

    /**
     * Zobrazení panelu.
     *
     * @param evt
     */
    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        //přednastaví dnešní datum//
        if (dateSince.getDate() == null) {
            dateSince.setDate(new Date());
        }
        //přednastaví dnešní datum//
        if (dateUntil.getDate() == null) {
            dateUntil.setDate(new Date());
        }
        
        qp = new QueryProtocol(SFPCClient.getApplication().getConnectionProvider().getNetworkInterface(), this); //vytvoření protokolu

        //pokud je možné mazat PL, nastaví se mazací menu//
        if (deleteTickets) {
            tblTickets.setComponentPopupMenu(mnuContext);
        }
        //pokud se zobrazují vlastní PL, zablokuje se seznam policistů; pokud se zobrazují všechny PL načte se seznam policistů z DB//
        if (!ownTickets) {
            pll = new PolicemanListLoader(qp);
            pll.loadPolicemanList(this);
        } else {
            preparePolicemanCombo();
        }
    }//GEN-LAST:event_formComponentShown

    /**
     * Zobrazení menu pro smazání.
     *
     * @param evt
     */
    private void mnuContextPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_mnuContextPopupMenuWillBecomeVisible
        if (tblTickets.getSelectedRow() == -1) {
            mnuiDelete.setEnabled(false);
        } else {
            mnuiDelete.setEnabled(true);
        }
    }//GEN-LAST:event_mnuContextPopupMenuWillBecomeVisible

    /**
     * Odstranění PL.
     *
     * @param evt
     */
    private void mnuiDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuiDeleteActionPerformed
        if (JOptionPane.showConfirmDialog(null, SFPCClient.getApplication().getLocalization().getString("showticket.delete.question.msg"), SFPCClient.getApplication().getLocalization().getString("showticket.delete.question.title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            deletedRowIx = tblTickets.getSelectedRow();
            int ticketid = ticketModel.getTickets().get(deletedRowIx).getId();
            idDeleteQuery = IdCreator.getID();
            
            qp.executeQuery(idDeleteQuery, QueryList.QUERY_DELETE_TICKET, new QueryParameterBuilder().putInt("ticketid", ticketid).getParametersString());
        }
    }//GEN-LAST:event_mnuiDeleteActionPerformed
    //================================================== UI PRVKY ==================================================//
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butLoadTickets;
    private javax.swing.JComboBox cmbPolicemen;
    private com.toedter.calendar.JDateChooser dateSince;
    private com.toedter.calendar.JDateChooser dateUntil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu mnuContext;
    private javax.swing.JMenuItem mnuiDelete;
    private javax.swing.JTable tblTickets;
    // End of variables declaration//GEN-END:variables

    //================================================== PRIVÁTNÍ METODY ==================================================//
    /**
     * Načítá PL ze serveru.
     */
    private void loadTickets() {
        QueryParameterBuilder qpb = new QueryParameterBuilder();
        
        //vynulování času//
        Calendar since = new GregorianCalendar();
        since.setTime(dateSince.getDate());
        since.set(Calendar.HOUR_OF_DAY, 0);
        since.set(Calendar.MINUTE, 0);
        since.set(Calendar.SECOND, 0);
        //vynulování času//
        Calendar until = new GregorianCalendar();
        until.setTime(dateUntil.getDate());
        until.set(Calendar.HOUR_OF_DAY, 23);
        until.set(Calendar.MINUTE, 59);
        until.set(Calendar.SECOND, 59);
        
        qpb.putLong("since", since.getTimeInMillis());
        qpb.putLong("until", until.getTimeInMillis());
        
        qp.setQueryProtocolListener(this);
        if (ownTickets) {
            qp.executeQuery(IdCreator.getID(), QueryList.QUERY_GET_OWN_TICKETS, qpb.getParametersString());
        } else {
            qpb.putInt("badgenumber", ((PolicemanDB) cmbPolicemen.getSelectedItem()).getBadgeNumber());
            qp.executeQuery(IdCreator.getID(), QueryList.QUERY_GET_TICKETS, qpb.getParametersString());
        }
    }

    /**
     * Připraví combo box pro načtení seznamu policistů.
     */
    private void preparePolicemanCombo() {
        cmbPolicemen.removeAllItems();
        
        if (ownTickets) {
            PolicemanDB me = new PolicemanDB();
            me.setBadgeNumber(PCClientPreferences.getBadgeNumber());
            cmbPolicemen.addItem(me);
            cmbPolicemen.setEnabled(false);
        } else {
            for (PolicemanDB policeman : policemanList) {
                cmbPolicemen.addItem(policeman);
            }
        }
    }

    /**
     * Připraví nově příchozí data pro zobrazení v tabulce.
     *
     * @param tickets Přijaté PL.
     */
    private void setNewData(List<NetworkTicket> tickets) {
        if (ticketModel == null) {
            ticketModel = new TicketTableModel(tickets);
            tblTickets.setModel(ticketModel);
        } else {
            ticketModel.setTickets(tickets);
        }
        
        TableColumn column = null;
        for (int i = 0; i < tblTickets.getColumnModel().getColumnCount(); i++) {
            column = tblTickets.getColumnModel().getColumn(i);
            column.setPreferredWidth(SwingUtilities.computeStringWidth(getFontMetrics(tblTickets.getFont()), column.getHeaderValue().toString()) + 20);
        }
    }
    
    private void deleteTicketFromTable() {
        tblTickets.removeRowSelectionInterval(deletedRowIx, deletedRowIx);
        idDeleteQuery = -1;
        deletedRowIx = -1;
    }
    //================================================== HANDLERY PROTOKOLŮ ==================================================//

    @Override
    public void onConnectionTerminated() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, SFPCClient.getApplication().getLocalization().getString("exception.showticketpanel.conterm.msg"),
                        SFPCClient.getApplication().getLocalization().getString("exception.showticketpanel.conterm.title"), JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    @Override
    public void onMessageSent() {
    }
    
    @Override
    public void onQueryResultReply(int id, QueryState state, Object result) {
        class ResultResender implements Runnable {
            
            private Object result;
            private int id;
            
            public ResultResender(int id, Object result) {
                this.id = id;
                this.result = result;
            }
            
            @Override
            public void run() {
                if (id == idDeleteQuery) {
                    deleteTicketFromTable();
                } else {
                    setNewData((List<NetworkTicket>) this.result);
                }
            }
        }
        
        if (state == QueryState.QUERY_OK) {
            javax.swing.SwingUtilities.invokeLater(new ResultResender(id, result));
        } else {
            if (id == idDeleteQuery) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    
                    @Override
                    public void run() {
                        idDeleteQuery = -1;
                        deletedRowIx = -1;
                        
                        JOptionPane.showMessageDialog(null, SFPCClient.getApplication().getLocalization().getString("exception.showticketpanel.delerr.msg"),
                                SFPCClient.getApplication().getLocalization().getString("exception.showticketpanel.delerr.title"), JOptionPane.ERROR_MESSAGE);
                    }
                });
            } else {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(null, SFPCClient.getApplication().getLocalization().getString("exception.showticketpanel.queryticketserr.msg"),
                                SFPCClient.getApplication().getLocalization().getString("exception.showticketpanel.queryticketserr.title"), JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
            
        }
    }
    
    @Override
    public void onPolicemanListReceived(List<PolicemanDB> policemen) {
        
        class PolicemenListResender implements Runnable {
            
            private List<PolicemanDB> policemen;
            
            public PolicemenListResender(List<PolicemanDB> policemen) {
                this.policemen = policemen;
            }
            
            @Override
            public void run() {
                if (pll != null) {
                    pll.disconnectProtocol();
                }
                policemanList = this.policemen;
                
                preparePolicemanCombo();
            }
        }
        javax.swing.SwingUtilities.invokeLater(new PolicemenListResender(policemen));
    }
    
    @Override
    public void onErrorReceived(String message) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                if (pll != null) {
                    pll.disconnectProtocol();
                }
                JOptionPane.showMessageDialog(null, SFPCClient.getApplication().getLocalization().getString("exception.showticketpanel.conterm.msg"),
                        SFPCClient.getApplication().getLocalization().getString("exception.showticketpanel.conterm.title"), JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}