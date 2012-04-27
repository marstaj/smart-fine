/**
 * 
 */
package cz.smartfine.android.networklayer.util;

/**
 * Generick� t��da pro mezi vl�knovou komunikaci.
 * @author Pavel Bro�
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
