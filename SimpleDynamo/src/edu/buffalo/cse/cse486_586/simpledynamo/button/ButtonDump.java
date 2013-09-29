/**
 * 
 */
package edu.buffalo.cse.cse486_586.simpledynamo.button;

import edu.buffalo.cse.cse486_586.simpledynamo.update.UpdateContent;

/**
 * @author Fang Wu
 * April 20, 2012
 *
 */
public class ButtonDump extends Thread {

	@SuppressWarnings("unused")
	private static final String TAG = "ButtonDump";
	
	private UpdateContent uc = null;
	
	public ButtonDump(UpdateContent uc) {
		this.uc = uc;
	}
	
	public void run() {
		uc.displayLocalPairs();
	}
}
