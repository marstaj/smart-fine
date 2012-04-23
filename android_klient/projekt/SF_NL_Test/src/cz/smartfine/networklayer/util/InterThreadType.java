/**
 * 
 */
package cz.smartfine.networklayer.util;

/**
 * Generická tøída pro mezi vláknovou komunikaci.
 * @author Pavel Brož
 */
public class InterThreadType<T> {
	private T data;
	private boolean dataSet = false;
	
	public synchronized T get() {
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
	
	public synchronized void put(T data) {
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
