package edu.buffalo.cse.cse486_586.simpledht.deliver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DeliverMessage {
	
	@SuppressWarnings("unused")
	private static final String TAG = "DeliverMessage";
	
	private Handler handler = null;
	private String Msg = null;
	
	public DeliverMessage(Handler myHandler, String msg) {
		this.handler = myHandler;
		this.Msg = msg;
	}
	
	public void deliver() {
		Message handlerMsg = handler.obtainMessage();
		Bundle bundleMsg = new Bundle();
		bundleMsg.putString("context", Msg);
		handlerMsg.setData(bundleMsg);
		handler.sendMessage(handlerMsg);
	}
}