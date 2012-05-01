package cz.smartfine.networklayer.util;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

	private List<byte[]> msg;

	/**
	 * Konstruktor.
	 */
	public MessageBuilder() {
		super();
		msg = new ArrayList<byte[]>();
	}

	/**
	 * Vloží číslo int.
	 * 
	 * @param intField
	 * @return
	 */
	public MessageBuilder putInt(int intField) {
		msg.add(Conventer.intToByteArray(intField));
		return this;
	}

	/**
	 * Vloží číslo byte.
	 * 
	 * @param byteField
	 * @return
	 */
	public MessageBuilder putByte(byte byteField) {
		msg.add(new byte[] { byteField });
		return this;
	}

	/**
	 * Vloží pole bytů.
	 * 
	 * @param arrayField
	 * @return
	 */
	public MessageBuilder putArray(byte[] arrayField) {
		msg.add(arrayField);
		return this;
	}

	/**
	 * Vloží délku předaného pole jako int a pole bytů samotné.
	 * 
	 * @param arrayField
	 * @return
	 */
	public MessageBuilder putArrayWithIntLength(byte[] arrayField) {
		msg.add(Conventer.intToByteArray(arrayField.length));
		msg.add(arrayField);
		return this;
	}

	/**
	 * Získá pole bytů celé zprávy.
	 * 
	 * @return
	 */
	public byte[] getByteArray() {
		byte[] retArray = new byte[this.getMessageLength()];
		int offset = 0;

		for (byte[] field : msg) {
			java.lang.System
					.arraycopy(field, 0, retArray, offset, field.length);
			offset += field.length;
		}

		return retArray;
	}

	/**
	 * Odstraní poslední vloženou položku.
	 */
	public void deleteLastField() {
		if (msg.size() > 0) {
			deleteFieldAt(msg.size() - 1);
		}
	}

	/**
	 * Odstraní položku na dané pozici.
	 * 
	 * @param position
	 */
	public void deleteFieldAt(int position) {
		msg.remove(position);
	}

	/**
	 * Získá první vloženou položku.
	 * 
	 * @return
	 */
	public byte[] getFirstField() {
		if (msg.size() > 0) {
			return msg.get(0);
		}
		return null;
	}

	/**
	 * Získá poslední vloženou položku.
	 * 
	 * @return
	 */
	public byte[] getLastField() {
		if (msg.size() > 0) {
			return msg.get(msg.size() - 1);
		}
		return null;
	}

	/**
	 * Získá první položku na zadné pozici.
	 * 
	 * @param position
	 * @return
	 */
	public byte[] getFieldAt(int position) {
		if (msg.size() > 0) {
			return msg.get(position);
		}
		return null;
	}

	/**
	 * Získá počet vložených položek.
	 * 
	 * @return
	 */
	public int getFieldCount() {
		return msg.size();
	}

	/**
	 * Získá celkou délku v bytech.
	 * 
	 * @return
	 */
	public int getMessageLength() {
		int totalLength = 0;
		for (byte[] field : msg) {
			totalLength += field.length;
		}
		return totalLength;
	}
}
