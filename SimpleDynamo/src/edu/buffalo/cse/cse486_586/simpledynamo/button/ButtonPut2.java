/**
 * 
 */
package edu.buffalo.cse.cse486_586.simpledynamo.button;

import edu.buffalo.cse.cse486_586.simpledynamo.update.UpdateDynamo;

/**
 * @author Fang Wu
 * April 20, 2012
 *
 */
public class ButtonPut2 extends Thread {

	@SuppressWarnings("unused")
	private static final String TAG = "ButtonPut2";
	
	private UpdateDynamo ud = null;
	private int i = 0;
	private String[] value = null;
	
	public ButtonPut2(UpdateDynamo ud) {
		this.ud = ud;
	}
	
	public void run() {
		value = new String[10];
		for (i = 0; i < 10; i++) {
			value[i] = "Put2" + i;
		}
		for (i = 0; i < 10; i++) {
			String key = String.valueOf(i);
			ud.keyToInsertRing(key, value[i]);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
