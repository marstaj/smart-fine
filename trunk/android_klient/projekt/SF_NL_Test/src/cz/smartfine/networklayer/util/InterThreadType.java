/**
 * 
 */
package cz.smartfine.networklayer.util;

/**
 * Generická třída pro mezi vláknovou komunikaci.
 * @author Pavel Brož
 * @version 1.0
 * @updated 27-4-2012 18:18:48
 */
public class InterThreadType<T> {
	private T data;
	private boolean dataSet = false;
	
	public synchronized T get() throws InterruptedException {
		if(!dataSet){
			wait();
		}
		
		dataSet = false;
		notify();
		return data;
	}
	
	public synchronized void put(T data) throws InterruptedException {
		if(dataSet){
			wait();
		}
		
		this.data = data;
		dataSet = true;
		notify();
	}
}
