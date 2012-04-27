package cz.smartfine.android.networklayer.util;

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

	public MessageBuilder putInt(int intField){
		msg.add(Conventer.intToByteArray(intField));
		return this;
	}
	
	public MessageBuilder putByte(byte byteField){
		msg.add(new byte[] { byteField });
		return this;
	}
	
	public MessageBuilder putArray(byte[] arrayField){
		msg.add(arrayField);
		return this;
	}
	
	public MessageBuilder putArrayWithIntLength(byte[] arrayField){
		msg.add(Conventer.intToByteArray(arrayField.length));
		msg.add(arrayField);
		return this;
	}
	
	public byte[] getByteArray(){
		byte[] retArray = new byte[this.getMessageLength()];
		int offset = 0;
		
		for (byte[] field : msg){
			java.lang.System.arraycopy(field, 0, retArray, offset, field.length);
			offset += field.length;
		}
		
		return retArray;
	}
	
	public void deleteLastField(){
		if (msg.size() > 0){
			deleteFieldAt(msg.size() - 1);
		}
	}
	
	public void deleteFieldAt(int position){
		msg.remove(position);
	}
	
	public byte[] getFirstField(){
		if (msg.size() > 0){
			return msg.get(0);
		}
		return null;
	}
	
	public byte[] getLastField(){
		if (msg.size() > 0){
			return msg.get(msg.size() - 1);
		}
		return null;
	}
	
	public byte[] getFieldAt(int position){
		if (msg.size() > 0){
			return msg.get(position);
		}
		return null;
	}
	
	public int getFieldCount(){
		return msg.size();
	}
	
	public int getMessageLength(){
		int totalLength = 0;
		for (byte[] field : msg){
			totalLength += field.length;
		}
		return totalLength;
	}
}
