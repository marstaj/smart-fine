package cz.smartfine.android.networklayer.util;

public class Conventer {

	/**
	 * Pøevede èíslo int na pole ètyø bytù.
	 * @param value Èíslo int.
	 * @return Pole ètyø bytù reprezentující int.
	 */
	public static final byte[] intToByteArray(int value) {
		return new byte[] {(byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)value};
	}
	
	/**
	 * Pøevede pole bytù na int. Metoda zaèíná v poli na indexu 0.
	 * @param b Pole bytù o minimální délce 4.
	 * @return Èíslo int.
	 */
	public static final int byteArrayToInt(byte [] b) {
		return byteArrayToInt(b, 0);
	}
	
	/**
	 * Pøevede pole bytù na int. Metoda zaèíná v poli na indexu urèenım parametrem offset.
	 * @param b Pole bytù o minimální délce 4 + offset.
	 * @param offset Zaèátek èísla int.
	 * @return Èíslo int.
	 */
	public static final int byteArrayToInt(byte [] b, int offset) {
		return ((b[offset] << 24) + ((b[1 + offset] & 0xFF) << 16) + ((b[2 + offset] & 0xFF) << 8) + (b[3 + offset] & 0xFF));
	}
	
	/**
	 * Pøevede èíslo long na pole osmy bytù.
	 * @param value Èíslo long.
	 * @return Pole osmy bytù reprezentující long.
	 */
	public static final byte[] longToByteArray(long value) {
		return new byte[] {(byte)(value >>> 56), (byte)(value >>> 48), (byte)(value >>> 40), (byte)(value >>> 32),
							(byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)value};
	}
	
	/**
	 * Pøevede pole bytù na long. Metoda zaèíná v poli na indexu urèenım parametrem offset.
	 * @param b Pole bytù o minimální délce 8 + offset.
	 * @param offset Zaèátek èísla long.
	 * @return Èíslo long.
	 */
	public static final long byteArrayToLong(byte [] b, int offset) {
		return (((long)b[offset] << 56) + ((long)(b[1 + offset] & 0xFF) << 48) + ((long)(b[2 + offset] & 0xFF) << 40) + ((long)(b[3 + offset] & 0xFF) << 32) + 
				((long)(b[4 + offset] & 0xFF) << 24) + ((long)(b[5 + offset] & 0xFF) << 16) + ((long)(b[6 + offset] & 0xFF) << 8) + ((long)(b[7 + offset] & 0xFF)));
	}
}
