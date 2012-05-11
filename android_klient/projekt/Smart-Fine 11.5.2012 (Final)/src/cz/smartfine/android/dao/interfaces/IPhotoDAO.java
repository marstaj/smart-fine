package cz.smartfine.android.dao.interfaces;

import java.io.File;
import java.util.ArrayList;

import cz.smartfine.android.model.Ticket;

/**
 * Interface představující objekt, který pracuje s fotografiemi v parkovacích
 * lístcích.
 * 
 * @author Martin Štajner
 * 
 */
public interface IPhotoDAO {

	/**
	 * Metoda se pokusí smazat fotografii.
	 * 
	 * @param photo
	 *            Fotografie, která se má smazat
	 * @throws Exception
	 *             Výjimka vznikne, když se nepodaří fotografii smazat.
	 */
	public void deletePhoto(File photo) throws Exception;

	/**
	 * Metoda smaze vsechny fotografie z parkovacích lísktů obsažených v
	 * seznamu.
	 * 
	 * @param tickets
	 *            Seznam parkovacích lístků
	 */
	public void deleteAllPhotosFromTickets(ArrayList<Ticket> tickets);

	/**
	 * Metoda vytvoří a vrátí odkaz na novou fotografii.
	 * 
	 * @return Odkaz na novou fotografii
	 */
	public File newPhoto();

	/**
	 * Metododa kontroluje, zda existuje v uložišti složka, kam se nahrávají
	 * fotografie. Pokud neexistje, tak ji vytvoří.
	 * 
	 * @throws Exception
	 *             Výjimka vznikne, když složka nejde vytvořit
	 */
	public void fileDestination() throws Exception;

	/**
	 * Metoda smaže všechny fotografie z parkovacího lístku
	 * 
	 * @param ticket
	 *            Parkovací lístek, ze kterého se mají smazat fotografie.
	 */
	public void deleteAllPhotosFromTicket(Ticket ticket);

}