package dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import model.Ticket;
import android.content.Context;

/**
 * @author Martin Stajner
 * 
 */
public class FileDAO {

	/**
	 * Instance sama sebe - kvuli singletonu
	 */
	static FileDAO dao;
	/**
	 * Kontext aplikace - kvuli relativni ceste k ulozenym souborum aplikace
	 */
	Context context;

	private FileDAO(Context context) {
		this.context = context;
	}

	/**
	 * Singleton DAO
	 * 
	 * @return
	 */
	static FileDAO getInstance(Context context) {
		if (dao == null) {
			dao = new FileDAO(context);
		}
		return dao;
	}

	/**
	 * Ulozeni listu zaznamu privatne na disk
	 * 
	 * @param list
	 * @return
	 */
	public void saveObjectToFile(ArrayList<Ticket> list, String path) throws Exception {
		FileOutputStream fos;
		fos = context.openFileOutput(path, Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(list);
		oos.close();
		fos.close();
	}

	/**
	 * Nacteni listu zaznamu ze souboru z disku
	 * 
	 * @return
	 */
	public Object loadObjectFromFile(String path) throws Exception {
		FileInputStream fis = context.openFileInput(path);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object object = ois.readObject();
		ois.close();
		fis.close();
		return object;
	}
}