package cz.smartfine.android.model.util;

import java.io.Serializable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Třída reprezentující pomocnoý objekt, který vytváří a posílá zpravy daneému
 * handleru.
 * 
 * @author Martin Štajner
 * 
 */
public class Messenger {

	/**
	 * Metoda vytvoří textovou zprávu a odešle ji do handleru.
	 * 
	 * @param handler
	 *            Cílový handler
	 * @param message
	 *            Text zprávy
	 */
	public static void sendStringMessage(Handler handler, String message) {
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putString("msg", message);
		msg.setData(data);
		handler.sendMessage(msg);
	}

	/**
	 * Metoda vytvoří zprávu, připojí do ní serializovatelný objekt, a odešle ji
	 * do handleru.
	 * 
	 * @param handler
	 *            Cílový handler
	 * @param object
	 *            Objekt k poslání
	 */
	public static void sendSerializableObjectMessage(Handler handler, Serializable object) {
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putSerializable("data", object);
		msg.setData(data);
		handler.sendMessage(msg);
	}

	/**
	 * Metoda vytvoří zprávu, přidá do ní textový příkaz, a odešle ji do
	 * handleru.
	 * 
	 * @param handler
	 *            Cílový handler
	 * @param string
	 *            Příkaz k poslání
	 */
	public static void sendCommand(Handler handler, String string) {
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putSerializable("command", string);
		msg.setData(data);
		handler.sendMessage(msg);
	}

}
