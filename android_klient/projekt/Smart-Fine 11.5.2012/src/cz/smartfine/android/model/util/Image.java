package cz.smartfine.android.model.util;

import java.io.File;
import java.io.FileInputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Třída reprezentující pomocný objekt pro práci s fotografí
 * 
 * @author Martin Štajner
 * 
 */
public class Image {

	/**
	 * Metoda dekóduje fotografii z File.
	 * 
	 * @param file
	 *            Odkaz na fotografii
	 * @param imageSize
	 *            Velikost fotografie
	 * @return Bitmapa obrázku
	 * @throws Exception
	 *             Výjima vznikne v případě, že vznikne chyba při čtení souboru
	 */
	public static Bitmap decodeFile(File file, int imageSize) throws Exception {
		Bitmap b = null;
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;

		FileInputStream fis = new FileInputStream(file);
		BitmapFactory.decodeStream(fis, null, o);

		fis.close();
		int scale = 1;
		if (o.outHeight > imageSize || o.outWidth > imageSize) {
			scale = (int) Math.pow(2, (int) Math.round(Math.log(imageSize / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		fis = new FileInputStream(file);
		b = BitmapFactory.decodeStream(fis, null, o2);
		fis.close();
		return b;
	}

}
