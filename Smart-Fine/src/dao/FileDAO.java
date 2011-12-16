package dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.Context;

/**
 * T��da ulo�en�/nahr�n� serializovateln�ho objektu do/ze souboru
 * @author Martin �tajner
 * 
 */
public class FileDAO {

	/**
	 * Kontext aplikace
	 */
	Context context;
	
	/**
	 * Konstruktor FileDAO
	 * @param context
	 */
	public FileDAO(Context context) {
		this.context = context;
	}


	/**
	 * Ulo�en� serializovateln�ho objektu priv�tn� na disk
	 * 
	 * @param object
	 */
	public void saveObjectToFile(Object object, String path) throws Exception {
		FileOutputStream fos = context.openFileOutput(path, Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(object);
		oos.close();
		fos.close();
	}
	
	/**
	 * Na�ten� serializovateln�ho objektu z disku
	 * 
	 * @return object
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