package cz.smartfine.networklayer.util;

public class Conventer {

    /**
     * Převede číslo int na pole čtyř bytů.
     *
     * @return Pole čtyř bytů reprezentující int.
     *
     * @param value Číslo int.
     */
    public static byte[] intToByteArray(int value) {
        return new byte[]{(byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
    }

    /**
     * Převede pole bytů na int. Metoda začíná v poli na indexu 0.
     *
     * @return Číslo int.
     *
     * @param b Pole bytů o minimální délce 4.
     */
    public static int byteArrayToInt(byte[] b) {
        return byteArrayToInt(b, 0);
    }

    /**
     * Převede pole bytů na int. Metoda začíná v poli na indexu určeným parametrem offset.
     *
     * @return Číslo int.
     *
     * @param b Pole bytů o minimální délce 4 + offset.
     * @param offset Začátek čísla int.
     */
    public static int byteArrayToInt(byte[] b, int offset) {
        return ((b[offset] << 24) + ((b[1 + offset] & 0xFF) << 16) + ((b[2 + offset] & 0xFF) << 8) + (b[3 + offset] & 0xFF));
    }

    /**
     * Převede číslo long na pole osmy bytů.
     *
     * @return Pole osmy bytů reprezentující long.
     *
     * @param value Číslo long.
     */
    public static byte[] longToByteArray(long value) {
        return new byte[]{(byte) (value >>> 56), (byte) (value >>> 48), (byte) (value >>> 40), (byte) (value >>> 32),
                    (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
    }

    /**
     * Převede pole bytů na long. Metoda začíná v poli na indexu určeným parametrem offset.
     *
     * @return Číslo long.
     *
     * @param b Pole bytů o minimální délce 8 + offset.
     * @param offset Začátek čísla long.
     */
    public static long byteArrayToLong(byte[] b, int offset) {
        return (((long) b[offset] << 56) + ((long) (b[1 + offset] & 0xFF) << 48) + ((long) (b[2 + offset] & 0xFF) << 40) + ((long) (b[3 + offset] & 0xFF) << 32)
                + ((long) (b[4 + offset] & 0xFF) << 24) + ((long) (b[5 + offset] & 0xFF) << 16) + ((long) (b[6 + offset] & 0xFF) << 8) + ((long) (b[7 + offset] & 0xFF)));
    }
}
