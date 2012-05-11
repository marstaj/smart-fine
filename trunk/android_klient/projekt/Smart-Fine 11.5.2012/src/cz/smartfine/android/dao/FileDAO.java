package cz.smartfine.android.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;

/**
 * Třída představující objekt, který se stará ukládání serializovatelných
 * objektů do souboru a nahrávání zase ze souboru zpět. Představuje nejnižší
 * vrstvu ukládání dat.
 * 
 * @author Martin Štajner
 * 
 */
public class FileDAO {

	/**
	 * Kontext aplikace
	 */
	private final Context appContext;

	/**
	 * Konstruktor třídy.
	 * 
	 * @param context
	 *            Kontex aplikace
	 */
	public FileDAO(Context context) {
		this.appContext = context;
	}

	/**
	 * Metoda provede uložení objektu do souboru na disku. Konkrétně do paměti
	 * zařízení a to v soukromém módu. To znamená, že k souboru má za normálních
	 * podmínek přístup pouze aplikace, která ho vytvořila. Objekt musí být
	 * serializovatelný. Pokud soubor již existuje, tak se přepíe, jinak se
	 * vytvoří nový soubor.
	 * 
	 * @param object
	 *            Objekt k uloýení do souboru.
	 * @param path
	 *            Cesta k souboru, do kterého se má ukládat.
	 * @throws IOException
	 *             Výjimka vznikne v případě chyby při ukládání.
	 */
	public void saveObjectToFile(Object object, String path) throws IOException {
		FileOutputStream fos = appContext.openFileOutput(path, Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(object);
		oos.close();
		fos.close();
	}

	/**
	 * Metoda provede načtení objektu ze souboru z disku.
	 * 
	 * @param path
	 *            Cesta k souboru, ze kterého se má číst.
	 * @return Objekt načtený ze souboru.
	 * @throws StreamCorruptedException
	 *             Výjimka vznikne v případě ze v souboru není uložen
	 *             serializovatelný objekt, který by šel načíst.
	 * @throws IOException
	 *             Výjimka vznikne v případě chyby při čtení.
	 * @throws ClassNotFoundException
	 *             Výjimka vznikne pokud nelze najít třídu jednoho z objektů v
	 *             objektovém grafu.
	 */
	public Object loadObjectFromFile(String path) throws StreamCorruptedException, IOException, ClassNotFoundException {
		FileInputStream fis = appContext.openFileInput(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object object = ois.readObject();
		ois.close();
		fis.close();
		return object;
	}
}