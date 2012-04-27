package cz.smartfine.networklayer.networkinterface;

public class BaseProtocolConstants {
	/**
	 * Verze protokolu
	 */
	public static final byte PROTOCOL_VERSION = 1;
    /**
	 * Délka hlavičky zprávy v bytech
	 */
    public static final int HEADER_SIZE = 5;
    /**
	 * Offset, kde začíná pole, délky těla zprávy
	 */
    public static final int HEADER_LENGTH_OFFSET = 1;
}
