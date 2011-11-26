package dao;

import java.util.ArrayList;

import cz.smartfine.R;
import android.content.Context;
import model.Ticket;

/**
 * @author Martin Stajner
 * 
 */
public class FileTicketDAO implements TicketDAO {

        /**
         * Kontext aplikace - kvuli relativni ceste k ulozenym souborum aplikace
         */
        Context context;
        /**
         * DAO
         */
        FileDAO dao;
        /**
         * List vsech lokalne ulozenych listku
         */
        ArrayList<Ticket> locals;
        /**
         * Instance sama sebe - kvuli singletonu
         */
        static FileTicketDAO ticketDAO;

        /**
         * Singleton TicketDAO
         * 
         * @return
         */
        public static FileTicketDAO getInstance(Context context) {
                if (ticketDAO == null) {
                        ticketDAO = new FileTicketDAO(context);
                }
                return ticketDAO;
        }
        
        /**
         * Konstruktor
         * 
         * @param context
         */
        public FileTicketDAO(Context context) {
                super();
                this.context = context;
                this.dao = FileDAO.getInstance(context);
                this.locals = new ArrayList<Ticket>();
        }

        /* (non-Javadoc)
		 * @see dao.TicketDAO#saveTicket(model.Ticket)
		 */
        public void saveTicket(Ticket ticket) throws Exception{
                locals.add(ticket);
                dao.saveObjectToFile(locals, context.getString(R.string.file_tickets));
        }

        /* (non-Javadoc)
		 * @see dao.TicketDAO#loadTickets()
		 */
        @SuppressWarnings("unchecked")
        public void loadTickets() throws Exception {
                Object o = dao.loadObjectFromFile(context.getString(R.string.file_tickets));
                if (o instanceof ArrayList) {
                        locals = (ArrayList<Ticket>) o;
                }
        }

        /* (non-Javadoc)
		 * @see dao.TicketDAO#getLocals()
		 */
        public ArrayList<Ticket> getLocals() {
                return locals;
        }
}