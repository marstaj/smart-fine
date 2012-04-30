package cz.smartfine.android.dao;

import java.util.ArrayList;


import cz.smartfine.android.R;
import cz.smartfine.android.dao.interfaces.IFreqValuesDAO;
import cz.smartfine.android.model.Law;
import android.content.Context;

/**
 * T��da pro pr�ci s DAO pro laws
 * 
 * @author Martin Štajner
 * 
 */
public class FileLawDAO implements IFreqValuesDAO {

	/**
	 * Kontext aplikace
	 */
	private Context context;

	/**
	 * DAO pro ukl�d�n� do souboru
	 */
	private FileDAO dao;

	private ArrayList<Law> laws;

	/**
	 * Konstruktor
	 * 
	 * @param context
	 * @throws Exception
	 */
	public FileLawDAO(Context context) {
		super();
		this.context = context;
		this.dao = new FileDAO(context);
	}

	//TODO Dodelat javadoc komentare vsude

	/**
	 * Na�te nej�ast�j�� hodnoty z �lo�i�t� do pam�ti
	 */
	public void loadValues() {
		// Zkusi nacist MPZ ze souboru a najit v nich oblibene polozky, v pripade chyby je nacte ze XML
		try {
			laws = loadLawsFromFile(R.string.file_laws);
		} catch (Exception e) {
			// TODO Dodelat, odkud se vlastne budou ty zakony nacitat???
			laws = loadLawsFromHere();
		}
	}

	public void saveValues() throws Exception {
		dao.saveObjectToFile(laws, context.getString(R.string.file_laws));
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Law> loadLawsFromFile(int fileName) throws Exception {
		Object o = dao.loadObjectFromFile(context.getString(fileName));
		if (o instanceof ArrayList) {
			return (ArrayList<Law>) o;
		}
		return null;
	}

	public ArrayList<Law> getLaws() {
		return laws;
	}

	private ArrayList<Law> loadLawsFromHere() {
		// TODO Upravit Laws do nejake normalni podoby - nacitat ze souboru, jako listky? a mit to tady v onCreate
		if (laws == null) {
			laws = new ArrayList<Law>();
			Law a = new Law();
			a.setEventDescription("Parkov�n� v z�kazu zastaven�");
			a.setRuleOfLaw(1);
			a.setParagraph(1);
			a.setLetter("a");
			a.setLawNumber(1);
			a.setCollection(1);
			a.setDescriptionDZ("Kod 1");
			laws.add(a);
			Law b = new Law();
			b.setEventDescription("Parkov�n� v z�kazu st�n�");
			b.setRuleOfLaw(2);
			b.setParagraph(2);
			b.setLetter("b");
			b.setLawNumber(2);
			b.setCollection(2);
			b.setDescriptionDZ("Kod 2");
			laws.add(b);
			Law c = new Law();
			c.setEventDescription("Pod�ln� parkov�n� v protism�ru");
			c.setRuleOfLaw(3);
			c.setParagraph(3);
			c.setLetter("c");
			c.setLawNumber(3);
			c.setCollection(3);
			c.setDescriptionDZ("Kod 3");
			laws.add(c);
			Law d = new Law();
			d.setEventDescription("Parkov�n� na nezpevn�n�m povrchu");
			d.setRuleOfLaw(4);
			d.setParagraph(4);
			d.setLetter("d");
			d.setLawNumber(4);
			d.setCollection(4);
			d.setDescriptionDZ("Kod 4");
			laws.add(d);
			Law e = new Law();
			e.setEventDescription("Parkov�n� na p�echodu pro chodce");
			e.setRuleOfLaw(4);
			e.setParagraph(4);
			e.setLetter("d");
			e.setLawNumber(4);
			e.setCollection(4);
			e.setDescriptionDZ("Kod 5");
			laws.add(e);
			Law f = new Law();
			f.setEventDescription("Parkov�n� na vyhrazen�m m�st�");
			f.setRuleOfLaw(4);
			f.setParagraph(4);
			f.setLetter("d");
			f.setLawNumber(4);
			f.setCollection(4);
			f.setDescriptionDZ("Kod 6");
			laws.add(f);
			Law g = new Law();
			g.setEventDescription("Parkov�n� na placen�m parkovi�ti bez zaplacen�");
			g.setRuleOfLaw(4);
			g.setParagraph(4);
			g.setLetter("d");
			g.setLawNumber(4);
			g.setCollection(4);
			g.setDescriptionDZ("Kod 7");
			laws.add(g);
			Law h = new Law();
			h.setEventDescription("Parkov�n� mimo vyzna�en� sm�r");
			h.setRuleOfLaw(4);
			h.setParagraph(4);
			h.setLetter("d");
			h.setLawNumber(4);
			h.setCollection(4);
			h.setDescriptionDZ("Kod 8");
			laws.add(h);
			Law i = new Law();
			i.setEventDescription("Parkov�n� na zast�vce MHD");
			i.setRuleOfLaw(4);
			i.setParagraph(4);
			i.setLetter("d");
			i.setLawNumber(4);
			i.setCollection(4);
			i.setDescriptionDZ("Kod 9");
			laws.add(i);
			Law j = new Law();
			j.setEventDescription("Nedodr�en� minim�ln� ���ky j�zdn�ho pruhu");
			j.setRuleOfLaw(4);
			j.setParagraph(4);
			j.setLetter("d");
			j.setLawNumber(4);
			j.setCollection(4);
			j.setDescriptionDZ("Kod 10");
			laws.add(j);
			Law k = new Law();
			k.setEventDescription("Parkov�n� na chodn�ku (mimo vyzna�en� parkovi�t�)");
			k.setRuleOfLaw(4);
			k.setParagraph(4);
			k.setLetter("d");
			k.setLawNumber(4);
			k.setCollection(4);
			k.setDescriptionDZ("Kod 11");
			laws.add(k);
		}
		return laws;
	}

}