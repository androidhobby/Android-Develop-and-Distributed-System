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
public class ButtonGet extends Thread {

	@SuppressWarnings("unused")
	private static final String TAG = "ButtonGet";
	
	private UpdateDynamo ud = null;
	private int i = 0;
	
	public ButtonGet(UpdateDynamo ud) {
		this.ud = ud;
	}
	
	public void run() {
		for (i = 0; i < 10; i++) {
			String key = String.valueOf(i);
			ud.keyQueryFromRing(key);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
