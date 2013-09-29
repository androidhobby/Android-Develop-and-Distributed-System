package edu.buffalo.cse.cse486_586.simpledht.button;

import edu.buffalo.cse.cse486_586.simpledht.updateprocedure.UpdateDHT;

public class Dump extends Thread {

	@SuppressWarnings("unused")
	private static final String TAG = "Dump";
	
	private UpdateDHT updateDHT;
	
	public Dump(UpdateDHT ud) {
		this.updateDHT = ud;
	}
	
	public void run() {;
		updateDHT.displayLocalPairs();
	}
}
