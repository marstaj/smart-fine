package cz.smartfine.android.dao;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.dao.interfaces.IFreqValuesDAO;
import cz.smartfine.android.model.Law;
import android.content.Context;

/**
 * Třída představující objekt, který ukládá seznam zákonů a operuje s nimi.
 * 
 * @author Martin Štajner
 * 
 */
public class FileLawDAO implements IFreqValuesDAO {

	/**
	 * Kontext aplikace
	 */
	private Context appContext;

	/**
	 * Instance objektu pro ukládání do souboru
	 */
	private FileDAO dao;

	/**
	 * Seznam zákonů
	 */
	private ArrayList<Law> laws;

	/**
	 * Konstruktor třídy
	 * 
	 * @param context
	 *            Kontext aplikace
	 */
	public FileLawDAO(Context context) {
		super();
		this.appContext = context;
		this.dao = new FileDAO(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.IFreqValuesDAO#loadValues()
	 */
	public void loadValues() {
		// Zkusi nacist MPZ ze souboru a najit v nich oblibene polozky, v pripade chyby je nacte ze XML
		try {
			laws = loadLawsFromFile(R.string.file_laws);
		} catch (Exception e) {
			laws = loadLawsFromHere();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.IFreqValuesDAO#saveValues()
	 */
	public void saveValues() throws Exception {
		dao.saveObjectToFile(laws, appContext.getString(R.string.file_laws));
	}

	/**
	 * Metoda načte zákony ze souboru.
	 * 
	 * @param fileName
	 *            Soubor, ze kterého se má načítat
	 * @return Seznam zákonů
	 * @throws Exception
	 *             Výjimka vznikne když nastane chyba při čtení ze souboru.
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Law> loadLawsFromFile(int fileName) throws Exception {
		Object o = dao.loadObjectFromFile(appContext.getString(fileName));
		if (o instanceof ArrayList) {
			return (ArrayList<Law>) o;
		}
		return null;
	}

	/**
	 * Metoda vrátí seznam zákonů.
	 * 
	 * @return seznam zákonů
	 */
	public ArrayList<Law> getLaws() {
		return laws;
	}

	/**
	 * Metoda vytvoří seznam zákonů.
	 * 
	 * @return Seznam zákonů
	 */
	private ArrayList<Law> loadLawsFromHere() {
		// TODO Načítání LAWS 
		// Upravit Laws do nejake normalni podoby - nacitat ze souboru, jako listky? a mit to tady v onCreate
		if (laws == null) {
			laws = new ArrayList<Law>();
			Law a = new Law();
			a.setEventDescription("Parkování v zákazu zastavení");
			a.setRuleOfLaw(1);
			a.setParagraph(1);
			a.setLetter("a");
			a.setLawNumber(1);
			a.setCollection(1);
			a.setDescriptionDZ("Kód 1");
			laws.add(a);
			Law b = new Law();
			b.setEventDescription("Parkování v zákazu stání");
			b.setRuleOfLaw(2);
			b.setParagraph(2);
			b.setLetter("b");
			b.setLawNumber(2);
			b.setCollection(2);
			b.setDescriptionDZ("Kód 2");
			laws.add(b);
			Law c = new Law();
			c.setEventDescription("Podélné parkování v protisměru");
			c.setRuleOfLaw(3);
			c.setParagraph(3);
			c.setLetter("c");
			c.setLawNumber(3);
			c.setCollection(3);
			c.setDescriptionDZ("Kód 3");
			laws.add(c);
			Law d = new Law();
			d.setEventDescription("Parkování na nezpevněném povrchu");
			d.setRuleOfLaw(4);
			d.setParagraph(4);
			d.setLetter("d");
			d.setLawNumber(4);
			d.setCollection(4);
			d.setDescriptionDZ("Kód 4");
			laws.add(d);
			Law e = new Law();
			e.setEventDescription("Parkování na přechodu pro chodce");
			e.setRuleOfLaw(4);
			e.setParagraph(4);
			e.setLetter("d");
			e.setLawNumber(4);
			e.setCollection(4);
			e.setDescriptionDZ("Kód 5");
			laws.add(e);
			Law f = new Law();
			f.setEventDescription("Parkování na vyhrazeném místě");
			f.setRuleOfLaw(4);
			f.setParagraph(4);
			f.setLetter("d");
			f.setLawNumber(4);
			f.setCollection(4);
			f.setDescriptionDZ("Kód 6");
			laws.add(f);
			Law g = new Law();
			g.setEventDescription("Parkování na placeném parkovišti bez zaplacení");
			g.setRuleOfLaw(4);
			g.setParagraph(4);
			g.setLetter("d");
			g.setLawNumber(4);
			g.setCollection(4);
			g.setDescriptionDZ("Kód 7");
			laws.add(g);
			Law h = new Law();
			h.setEventDescription("Parkování mimo vyznačený směr");
			h.setRuleOfLaw(4);
			h.setParagraph(4);
			h.setLetter("d");
			h.setLawNumber(4);
			h.setCollection(4);
			h.setDescriptionDZ("Kód 8");
			laws.add(h);
			Law i = new Law();
			i.setEventDescription("Parkování na zastávce MHD");
			i.setRuleOfLaw(4);
			i.setParagraph(4);
			i.setLetter("d");
			i.setLawNumber(4);
			i.setCollection(4);
			i.setDescriptionDZ("Kód 9");
			laws.add(i);
			Law j = new Law();
			j.setEventDescription("Nedodržení minimální šířky jízdního pruhu");
			j.setRuleOfLaw(4);
			j.setParagraph(4);
			j.setLetter("d");
			j.setLawNumber(4);
			j.setCollection(4);
			j.setDescriptionDZ("Kód 10");
			laws.add(j);
			Law k = new Law();
			k.setEventDescription("Parkování na chodníku (mimo vyznačené parkoviště)");
			k.setRuleOfLaw(4);
			k.setParagraph(4);
			k.setLetter("d");
			k.setLawNumber(4);
			k.setCollection(4);
			k.setDescriptionDZ("Kód 11");
			laws.add(k);
		}
		return laws;
	}

}