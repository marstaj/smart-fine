package cz.smartfine.android.networklayer.util;

public class Conventer {

	/**
	 * P�evede ��slo int na pole �ty� byt�.
	 * @param value ��slo int.
	 * @return Pole �ty� byt� reprezentuj�c� int.
	 */
	public static final byte[] intToByteArray(int value) {
		return new byte[] {(byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)value};
	}
	
	/**
	 * P�evede pole byt� na int. Metoda za��n� v poli na indexu 0.
	 * @param b Pole byt� o minim�ln� d�lce 4.
	 * @return ��slo int.
	 */
	public static final int byteArrayToInt(byte [] b) {
		return byteArrayToInt(b, 0);
	}
	
	/**
	 * P�evede pole byt� na int. Metoda za��n� v poli na indexu ur�en�m parametrem offset.
	 * @param b Pole byt� o minim�ln� d�lce 4 + offset.
	 * @param offset Za��tek ��sla int.
	 * @return ��slo int.
	 */
	public static final int byteArrayToInt(byte [] b, int offset) {
		return ((b[offset] << 24) + ((b[1 + offset] & 0xFF) << 16) + ((b[2 + offset] & 0xFF) << 8) + (b[3 + offset] & 0xFF));
	}
	
	/**
	 * P�evede ��slo long na pole osmy byt�.
	 * @param value ��slo long.
	 * @return Pole osmy byt� reprezentuj�c� long.
	 */
	public static final byte[] longToByteArray(long value) {
		return new byte[] {(byte)(value >>> 56), (byte)(value >>> 48), (byte)(value >>> 40), (byte)(value >>> 32),
							(byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)value};
	}
	
	/**
	 * P�evede pole byt� na long. Metoda za��n� v poli na indexu ur�en�m parametrem offset.
	 * @param b Pole byt� o minim�ln� d�lce 8 + offset.
	 * @param offset Za��tek ��sla long.
	 * @return ��slo long.
	 */
	public static final long byteArrayToLong(byte [] b, int offset) {
		return (((long)b[offset] << 56) + ((long)(b[1 + offset] & 0xFF) << 48) + ((long)(b[2 + offset] & 0xFF) << 40) + ((long)(b[3 + offset] & 0xFF) << 32) + 
				((long)(b[4 + offset] & 0xFF) << 24) + ((long)(b[5 + offset] & 0xFF) << 16) + ((long)(b[6 + offset] & 0xFF) << 8) + ((long)(b[7 + offset] & 0xFF)));
	}
}
