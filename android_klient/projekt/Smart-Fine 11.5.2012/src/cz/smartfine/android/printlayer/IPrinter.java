package cz.smartfine.android.printlayer;

import cz.smartfine.android.model.Ticket;
import com.starmicronics.stario.StarIOPortException;

/**
 * Interface pro práci s mobilní tiskárnou a tiskem parkovacích lístků.
 * 
 * @author Martin Štajner
 * 
 */
public interface IPrinter {

	/**
	 * Metoda připraví parkovací lístek k tisku vytiskne ho.
	 * 
	 * @param ticket
	 *            Parkovací lístek určený k tisku.
	 * @throws StarIOPortException
	 *             Výjimka nastane v případě, že nastane chyba při komunikaci s
	 *             tiskárnou.
	 */
	public void printTicket(Ticket ticket) throws StarIOPortException;

	/**
	 * Metoda zavře port k tiskárně
	 * 
	 * @throws StarIOPortException
	 *             Výjimka nastane v případě, že nastane chyba při komunikaci s
	 *             tiskárnou.
	 */
	public void closePort() throws StarIOPortException;

	/**
	 * Metoda otevře port k tiskárně
	 * 
	 * @param mac
	 *            Mac aresa tiskárny
	 * @throws StarIOPortException
	 *             Výjimka nastane v případě, že nastane chyba při komunikaci s
	 *             tiskárnou.
	 */
	public void openPort(String mac) throws StarIOPortException;

}