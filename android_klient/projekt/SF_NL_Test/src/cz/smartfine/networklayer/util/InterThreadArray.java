/**
 * 
 */
package cz.smartfine.networklayer.util;

/**
 * Tøída pro mezi vláknovou komunikaci s polem bytù
 * @author Pavel Brož
 */
public class InterThreadArray {
	private byte[] data;
	private boolean dataSet = false;
	
	public synchronized byte[] get() {
		if(!dataSet){
			try {
				wait();
			} catch(InterruptedException e) {
				//TODO
			}
		}
		
		dataSet = false;
		notify();
		return data;
	}
	
	public synchronized void put(byte[] data) {
		if(dataSet){
			try {
				wait();
			} catch(InterruptedException e) {
				//TODO
			}
		}
		
		this.data = data;
		dataSet = true;
		notify();
	}
}
