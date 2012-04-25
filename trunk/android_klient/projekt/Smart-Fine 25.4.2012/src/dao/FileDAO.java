package dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;

/**
 * Tøída se stará o ukládání serializovatelných objektù do souboru a nahrávání
 * objektù zase ze souboru zpìt. Pøedstavuje nejnižší vrstvu ukládání dat.
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
	 * Konstruktor tøídy, který vytváøí objekt FileDAO. Do objektu pøiøadí
	 * pøístup ke kontextu aplikace.
	 * 
	 * @param context
	 *            Pøístup ke kontextu aplikace.
	 */
	public FileDAO(Context context) {
		this.context = context;
	}

	/**
	 * Metoda provede uložení objektu do souboru na disku. Konkrétnì do pamìti
	 * zaøízení a to v soukromém módu. To znamená, že k souboru má za normálních
	 * podmínek pøístup pouze aplikace, která ho vytvoøila. Objekt musí být
	 * serializovatelný. Pokud soubor již existuje, tak se pøepíše, jinak se
	 * vytvoøí nový soubor.
	 * 
	 * @param object
	 *            Objekt k uložení do souboru.
	 * @param path
	 *            Cesta k souboru, do kterého se má ukládat.
	 * @throws IOException
	 *             Výjimka vznikne v pøípadì chyby pøi ukládání.
	 */
	public void saveObjectToFile(Object object, String path) throws IOException {
		FileOutputStream fos = context.openFileOutput(path, Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(object);
		oos.close();
		fos.close();
	}

	/**
	 * Metoda provede naètení objektu ze souboru z disku.
	 * 
	 * @param path
	 *            Cesta k souboru, ze kterého se má èíst.
	 * @return Objekt naètený ze souboru.
	 * @throws StreamCorruptedException
	 *             Výjimka vznikne v pøípadì ze v souboru není uložen
	 *             serializovatelný objekt, který by šel naèíst.
	 * @throws IOException
	 *             Výjimka vznikne v pøípadì chyby pøi ètení.
	 * @throws ClassNotFoundException
	 *             Výjimka vznikne pokud nelze najít tøídu jednoho z objektù v
	 *             objektovém grafu.
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