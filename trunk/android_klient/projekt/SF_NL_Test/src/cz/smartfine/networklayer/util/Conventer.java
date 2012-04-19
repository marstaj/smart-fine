package cz.smartfine.networklayer.util;

public class Conventer {

	public static final byte[] intToByteArray(int value) {
		return new byte[] {(byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)value};
	}
	
	public static final int byteArrayToInt(byte [] b) {
		return byteArrayToInt(b, 0);
	}
	
	public static final int byteArrayToInt(byte [] b, int offset) {
		return ((b[offset] << 24) + ((b[1 + offset] & 0xFF) << 16) + ((b[2 + offset] & 0xFF) << 8) + (b[3 + offset] & 0xFF));
	}
}
