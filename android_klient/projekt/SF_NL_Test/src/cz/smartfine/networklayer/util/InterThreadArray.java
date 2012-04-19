/**
 * 
 */
package cz.smartfine.networklayer.util;

/**
 * T��da pro mezi vl�knovou komunikaci s polem byt�
 * @author Pavel Bro�
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
