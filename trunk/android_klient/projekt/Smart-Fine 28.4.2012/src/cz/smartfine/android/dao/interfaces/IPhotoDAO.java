package cz.smartfine.android.dao.interfaces;

import java.io.File;
import java.util.ArrayList;

import cz.smartfine.android.model.Ticket;

/**
 * Tøída pro práci s fotografiemi
 * @author Martin Štajner
 * 
 */
public interface IPhotoDAO {
	
	/**
	 * Smaze fotografii
	 * @param photo
	 * @throws Exception
	 */
	public void deletePhoto(File photo) throws Exception;
	
	/**
	 *  Smaze vsechny fotografie z parkovacích lístkù
	 */
	public void deleteAllPhotosFromTickets(ArrayList<Ticket> tickets);
	
	/**
	 * Vytvori a vrati odkaz na novou fotku
	 * @return
	 */
	public File newPhoto();
	
	
	/**
	 * Kontroluje, zda existuje na karte slozka, kam se nahravaji fotografie.. pokud ne, tak ji vytvori
	 * @throws Exception
	 */
	public void fileDestination() throws Exception;

	

}