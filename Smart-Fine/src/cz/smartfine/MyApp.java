package cz.smartfine;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import model.Law;
import model.util.Toaster;
import dao.TicketDAO;
import dao.FileTicketDAO;
import android.app.Application;

/**
 * Tøída reprezentující aplikaci
 * @author Martin Štajner
 * 
 */
public class MyApp extends Application {

	/**
	 * Pristup k DAO
	 */
	TicketDAO ticketDAO;
	
	// TODO presunout nekam na spravne misto
	private ArrayList<Law> laws;

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		Toaster.context = getApplicationContext();
		try {
			ticketDAO = new FileTicketDAO(getApplicationContext());
			ticketDAO.loadTickets();
		} catch (FileNotFoundException e) {
			Toaster.toast("Soubor nenalezen", Toaster.LONG);
		} catch (Exception e) {
			Toaster.toast("Nepodaøilo se naèíst uložené lístky", Toaster.LONG);
		}		
	}

	/**
	 * Vrátí pøístup k DAO
	 * @return pøístup k DAO
	 */
	public TicketDAO getTicketDao() {
		return ticketDAO;
	}
	
	/**
	 * Nastaví pøístup k DAO
	 * @param dao
	 */
	public void setTicketDao(TicketDAO dao) {
		this.ticketDAO = dao;
	}
	
	public ArrayList<Law> getLaws() {
		// TODO Upravit Laws do nejake normalni podoby
		if (laws == null) {
			laws = new ArrayList<Law>();
			Law a = new Law(); a.setDescription("Zákon è. 1"); a.setRuleOfLaw(1); a.setParagraph(1); a.setLetter("a"); a.setLawNumber(1); a.setCollection(1); laws.add(a);
			Law b = new Law(); b.setDescription("Zákon è. 2"); b.setRuleOfLaw(2); b.setParagraph(2); b.setLetter("b"); b.setLawNumber(2); b.setCollection(2); laws.add(b);
			Law c = new Law(); c.setDescription("Zákon è. 3"); c.setRuleOfLaw(3); c.setParagraph(3); c.setLetter("c"); c.setLawNumber(3); c.setCollection(3); laws.add(c);
			Law d = new Law(); d.setDescription("Zákon è. 4"); d.setRuleOfLaw(4); d.setParagraph(4); d.setLetter("d"); d.setLawNumber(4); d.setCollection(4); laws.add(d);
		}
		return laws;
	}

}
