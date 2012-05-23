package cz.smartfine.android.printlayer;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.text.TextPaint;
import cz.smartfine.android.R;
import cz.smartfine.android.model.Ticket;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

/**
 * Třída reprezentující mobilní tiskárnu. Objekt poskytuje metody na připojení a
 * odpojení portu a tisku parkovacího lístku.
 * 
 * @author Martin Štajner
 * 
 */
public class StarT300BTPrinter implements IPrinter {

	/**
	 * Kontext aplikace
	 */
	Context appContext;

	/**
	 * Jméno portu pro komunikaci s tiskárnou
	 */
	String portName;

	/**
	 * Port pro komunikaci s tiskárnou
	 */
	StarIOPort port;

	/**
	 * Konstruktor třídy
	 * 
	 * @param context
	 *            Kontext aplikace
	 */
	public StarT300BTPrinter(Context context) {
		this.appContext = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.printlayer.IPrinter#openPort(java.lang.String)
	 */
	@Override
	public void openPort(String macAdress) throws StarIOPortException {

		if (portName == null) {
			portName = "BT:";
		}

		//zjistí mac adresu a pripoji ji ke jmenu portu
		portName += macAdress;

		port = StarIOPort.getPort(portName, "mini", 10000);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.printlayer.IPrinter#printTicket(cz.smartfine.android
	 * .model.Ticket)
	 */
	@Override
	public void printTicket(Ticket ticket) throws StarIOPortException {

		// Definice tucneho pisma, kurzivy a normalniho pisma
		String data;
		Typeface bold = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
		Typeface normal = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
		Typeface italic = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);

		// Cara ohraniceni
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		p.setStyle(Paint.Style.STROKE);

		// Definice ruznych stylu textu
		TextPaint title = new TextPaint();
		title.setStyle(Paint.Style.FILL);
		title.setColor(Color.BLACK);
		title.setAntiAlias(true);
		title.setTypeface(bold);
		title.setTextSize(54);
		title.setTextAlign(Align.CENTER);

		TextPaint subtitle = new TextPaint();
		subtitle.setStyle(Paint.Style.FILL);
		subtitle.setColor(Color.BLACK);
		subtitle.setAntiAlias(true);
		subtitle.setTypeface(bold);
		subtitle.setTextSize(25);
		subtitle.setTextAlign(Align.CENTER);

		TextPaint section = new TextPaint();
		section.setStyle(Paint.Style.FILL);
		section.setColor(Color.BLACK);
		section.setAntiAlias(true);
		section.setTypeface(bold);
		section.setTextSize(24);
		section.setTextAlign(Align.LEFT);

		TextPaint information = new TextPaint();
		information.setStyle(Paint.Style.FILL);
		information.setColor(Color.BLACK);
		information.setAntiAlias(true);
		information.setTypeface(normal);
		information.setTextSize(24);
		information.setTextAlign(Align.LEFT);

		TextPaint tiny = new TextPaint();
		tiny.setStyle(Paint.Style.FILL);
		tiny.setColor(Color.BLACK);
		tiny.setAntiAlias(true);
		tiny.setTypeface(italic);
		tiny.setTextSize(20);
		tiny.setTextAlign(Align.LEFT);

		TextPaint down = new TextPaint();
		down.setStyle(Paint.Style.FILL);
		down.setColor(Color.BLACK);
		down.setAntiAlias(true);
		down.setTypeface(normal);
		down.setTextSize(24);
		down.setTextAlign(Align.CENTER);

		// Vytvori bitmapu, do ktere pak bude kreslit
		Bitmap bitmap = Bitmap.createBitmap(576, 950, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bitmap);
		c.drawColor(Color.WHITE);
		c.translate(0, 0);

		// hodnota urcujici vertikalni polohu elementu od zhora
		int row = 0;

		row += 80;
		c.drawText(appContext.getText(R.string.printedTicket_title).toString(), 288, row, title); // Title
		row += 50;
		c.drawText(appContext.getText(R.string.printedTicket_subtitle).toString(), 288, row, subtitle); // Subtitle

		row += 70;
		c.drawText(appContext.getText(R.string.printedTicket_date).toString() + " ", 20, row, section); // Datum
		data = String.valueOf(ticket.getDate().getDate()) + ". " + String.valueOf(ticket.getDate().getMonth()) + ". " + String.valueOf(ticket.getDate().getYear() + 1900);
		c.drawText(data, 200, row, information); // Datum

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_time).toString() + " ", 20, row, section); // Cas
		data = String.valueOf(ticket.getDate().getHours()) + ":" + String.valueOf(ticket.getDate().getMinutes());
		c.drawText(data, 200, row, information); // Cas

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_tow).toString() + " ", 20, row, section); // Odtah
		if (ticket.isTow()) {
			data = appContext.getText(R.string.printedTicket_yes).toString();
		} else {
			data = appContext.getText(R.string.printedTicket_no).toString();
		}
		c.drawText(data, 200, row, information); // Odtah

		row += 60;
		c.drawText(appContext.getText(R.string.printedTicket_spz).toString() + " ", 20, row, section); // SPZ
		data = ticket.getSpz();
		c.drawText(data, 200, row, information); // SPZ

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_mpz).toString() + " ", 370, row, section); // MPZ
		data = ticket.getMpz();
		c.drawText(data, 480, row, information); // MPZ

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_spzColor).toString() + " ", 20, row, section); // barva SPZ
		data = ticket.getSpzColor();
		c.drawText(data, 200, row, information); //  barva SPZ

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_vehicleType).toString() + " ", 20, row, section); // druh vozidla
		data = ticket.getVehicleType();
		c.drawText(data, 200, row, information); //  druh vozidla

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_vehicleBrand).toString() + " ", 20, row, section); // tovarni znacka
		data = ticket.getVehicleBrand();
		c.drawText(data, 200, row, information); //  tovarni znacka

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_city).toString() + " ", 20, row, section); // Mesto
		data = ticket.getCity();
		c.drawText(data, 200, row, information); // mesto

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_street).toString() + " ", 20, row, section); // ulice
		data = ticket.getStreet();
		c.drawText(data, 200, row, information); // ulice

		c.drawText(appContext.getText(R.string.printedTicket_number).toString() + " ", 370, row, section); // cislo popisne
		data = String.valueOf(ticket.getNumber());
		c.drawText(data, 480, row, information); // cislo popisne

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_description).toString() + " ", 20, row, section); // popis jednani
		data = ticket.getLaw().getEventDescription();
		row += 30;
		c.drawText(data, 20, row, information); // popis jednani

		// zakon
		row += 60;
		c.drawText(appContext.getText(R.string.printedTicket_law1).toString() + " ", 20, row, section); // paragraf
		data = String.valueOf(ticket.getLaw().getRuleOfLaw());
		c.drawText(data, 120, row, information); // paragraf

		c.drawText(appContext.getText(R.string.printedTicket_law2).toString(), 180, row, section); // odstavec
		data = String.valueOf(ticket.getLaw().getParagraph());
		c.drawText(data, 270, row, information); // odstavec

		c.drawText(appContext.getText(R.string.printedTicket_law3).toString() + " ", 370, row, section); // pismeno
		data = ticket.getLaw().getLetter();
		c.drawText(data, 480, row, information); // pismeno

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_law4).toString(), 20, row, section); // zákon cislo
		data = String.valueOf(ticket.getLaw().getLawNumber());
		c.drawText(data, 120, row, information); // zákon cislo

		c.drawText("/", 200, row, section); // odstavec
		data = String.valueOf(ticket.getLaw().getCollection());
		c.drawText(data, 270, row, information); // sbirky		
		c.drawText(appContext.getText(R.string.printedTicket_law5).toString() + " ", 370, row, section); // sbirky

		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_info1).toString(), 20, row, tiny); // dostavit se na sluzebnu
		row += 30;
		c.drawText(appContext.getText(R.string.printedTicket_info2).toString(), 20, row, tiny); // dostavit se na sluzebnu

		row += 210;
		c.drawText(appContext.getText(R.string.printedTicket_stamp).toString(), 144, row, down); // razitko
		c.drawText(appContext.getText(R.string.printedTicket_signature).toString(), 432, row, down); // podpis straznika

		row += 30;
		// Ohraniceni
		c.drawLine(0, 0, 575, 0, p);
		c.drawLine(0, 0, 0, row - 1, p);
		c.drawLine(0, row - 1, 575, row - 1, p);
		c.drawLine(575, 0, 575, row - 1, p);

		// Pripravi bitmapu pro tisk
		StarBitmap starbitmap = new StarBitmap(bitmap, false, 576);
		byte[] command = starbitmap.getImageEscPosDataForPrinting();

		// Posle do tiskarny listek
		port.writePort(command, 0, command.length);

		// Odradkovani
		ArrayList<Byte> commands = new ArrayList<Byte>();
		commands.add((byte) 10);
		commands.add((byte) 10);

		// posle do tiskarny odradkovani
		byte[] commandsToSendToPrinter = getByteArray(commands);
		port.writePort(commandsToSendToPrinter, 0, commandsToSendToPrinter.length);
	}

	/**
	 * Metoda vrátí pole bytů ze ArrayListu bytů
	 * 
	 * @param commands
	 *            ArrayList bytů
	 * @return Pole bytů
	 */
	private byte[] getByteArray(ArrayList<Byte> commands) {
		byte[] commandsToSendToPrinter = new byte[commands.size()];
		for (int index = 0; index < commands.size(); index++) {
			commandsToSendToPrinter[index] = commands.get(index);
		}
		return commandsToSendToPrinter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.printlayer.IPrinter#closePort()
	 */
	@Override
	public void closePort() throws StarIOPortException {
		if (port != null) {
			StarIOPort.releasePort(port);
		}
	}
}