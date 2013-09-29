package com.cse586.fwu8.castmessage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DeliverMessage {
	private static final String TAG = "DeliverMessage";
	Handler handler = null;
	String Msg = null;
	
	public DeliverMessage(Handler myHandler, String msg) {
		this.handler = myHandler;
		this.Msg = msg;
	}
	
	public void deliver () {
		Message handlerMsg = handler.obtainMessage();
		Bundle bundleMsg = new Bundle();
		bundleMsg.putString("content", Msg);
		handlerMsg.setData(bundleMsg);
		handler.sendMessage(handlerMsg);
	}
}
