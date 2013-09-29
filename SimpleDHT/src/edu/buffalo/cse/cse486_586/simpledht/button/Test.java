package edu.buffalo.cse.cse486_586.simpledht.button;

import edu.buffalo.cse.cse486_586.simpledht.updateprocedure.UpdateDHT;

public class Test extends Thread {

	@SuppressWarnings("unused")
	private static final String TAG = "Test";
	
 	private int i = 0;
	private String[] value = null;
	private UpdateDHT updateDHT;
	
	public Test(UpdateDHT ud) {
		this.updateDHT = ud;
	}
		
	public void run() {
		value = new String[10];
		for (i = 0; i < 10; i++) {
			value[i] = "Test" + i;
		}
		for (i = 0; i < 10; i++) {
			String key = String.valueOf(i);
			updateDHT.keyToInsertRing(key, value[i]);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (i = 0; i < 10; i++) {
			String key = String.valueOf(i);
			updateDHT.keyQueryFromRing(key);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
