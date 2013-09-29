package com.cse586.fwu8.castmessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import com.cse586.fwu8.message.MessageInfo;

public class AppClient extends Thread {
	private static final String TAG = "AppClient";
	String IP = null;
	int port = 0;	
    MessageInfo m = null;
    
	public AppClient(String IP, int port, MessageInfo msginfo) {
		this.IP = IP;
		this.port = port;
		this.m = msginfo;
	}
	
	public void run() {
		try {
			Socket clientsocket = new Socket(IP, port);
			Log.d(TAG, "AppClient1");
			ObjectOutputStream os = new ObjectOutputStream(clientsocket.getOutputStream());
			m.setMsgType(MessageInfo.CASTED_MSG);
			os.writeObject(m);
			os.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}