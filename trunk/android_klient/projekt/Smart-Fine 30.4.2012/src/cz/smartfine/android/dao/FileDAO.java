package cz.smartfine.android.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;

/**
 * T��da se star� o ukl�d�n� serializovateln�ch objekt� do souboru a nahr�v�n�
 * objekt� zase ze souboru zp�t. P�edstavuje nejni��� vrstvu ukl�d�n� dat.
 * 
 * @author Martin Štajner
 * 
 */
public class FileDAO {

	/**
	 * Kontext aplikace
	 */
	private final Context context;

	/**
	 * Konstruktor t��dy, kter� vytv��� objekt FileDAO. Do objektu p�i�ad�
	 * p��stup ke kontextu aplikace.
	 * 
	 * @param context
	 *            P��stup ke kontextu aplikace.
	 */
	public FileDAO(Context context) {
		this.context = context;
	}

	/**
	 * Metoda provede ulo�en� objektu do souboru na disku. Konkr�tn� do pam�ti
	 * za��zen� a to v soukrom�m m�du. To znamen�, �e k souboru m� za norm�ln�ch
	 * podm�nek p��stup pouze aplikace, kter� ho vytvo�ila. Objekt mus� b�t
	 * serializovateln�. Pokud soubor ji� existuje, tak se p�ep�e, jinak se
	 * vytvo�� nov� soubor.
	 * 
	 * @param object
	 *            Objekt k ulo�en� do souboru.
	 * @param path
	 *            Cesta k souboru, do kter�ho se m� ukl�dat.
	 * @throws IOException
	 *             V�jimka vznikne v p��pad� chyby p�i ukl�d�n�.
	 */
	public void saveObjectToFile(Object object, String path) throws IOException {
		FileOutputStream fos = context.openFileOutput(path, Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(object);
		oos.close();
		fos.close();
	}

	/**
	 * Metoda provede na�ten� objektu ze souboru z disku.
	 * 
	 * @param path
	 *            Cesta k souboru, ze kter�ho se m� ��st.
	 * @return Objekt na�ten� ze souboru.
	 * @throws StreamCorruptedException
	 *             V�jimka vznikne v p��pad� ze v souboru nen� ulo�en
	 *             serializovateln� objekt, kter� by �el na��st.
	 * @throws IOException
	 *             V�jimka vznikne v p��pad� chyby p�i �ten�.
	 * @throws ClassNotFoundException
	 *             V�jimka vznikne pokud nelze naj�t t��du jednoho z objekt� v
	 *             objektov�m grafu.
	 */
	public Object loadObjectFromFile(String path) throws StreamCorruptedException, IOException, ClassNotFoundException {
		FileInputStream fis = context.openFileInput(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object object = ois.readObject();
		ois.close();
		fis.close();
		return object;
	}
}