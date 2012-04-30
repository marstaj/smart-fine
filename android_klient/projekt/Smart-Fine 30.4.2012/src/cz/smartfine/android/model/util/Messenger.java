package cz.smartfine.android.model.util;

import java.io.Serializable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Pomocná třída pro zobrazovaní toast upozornění
 * 
 * @author Martin Štajner
 * 
 */
public class Messenger {

	/**
	 * TODO dodelat
	 * 
	 * @param message
	 * @param length
	 */
	public static void sendStringMessage(Handler handler, String message) {
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putString("msg", message);
		msg.setData(data);
		handler.sendMessage(msg);
	}

	/**
	 * TODO dodelat
	 * 
	 * @param message
	 * @param length
	 */
	public static void sendSerializableObjectMessage(Handler handler, Serializable object) {
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putSerializable("data", object);
		msg.setData(data);
		handler.sendMessage(msg);
	}

	/**
	 * @param handler
	 * @param string
	 */
	public static void sendCommand(Handler handler, String string) {
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putSerializable("command", string);
		msg.setData(data);
		handler.sendMessage(msg);
	}

}
