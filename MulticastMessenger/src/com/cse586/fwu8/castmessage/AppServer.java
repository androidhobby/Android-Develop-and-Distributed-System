package com.cse586.fwu8.castmessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.cse586.fwu8.message.MessageInfo;
import com.cse586.fwu8.processmessage.ProcessAgreeNumber;
import com.cse586.fwu8.processmessage.ProcessMessage;
import com.cse586.fwu8.processmessage.ProcessPropNumber;

import android.os.Handler;
import android.util.Log;

public class AppServer extends Thread {
	private static final String TAG = "AppServer";
	ServerSocket serversocket = null;
	Handler myHandler = null;
	ObjectInputStream is = null;
	MessageInfo msginfo = null;
	int serverID = 0;
	
	public AppServer(Handler handler, int processID) {
		this.myHandler = handler;
		this.serverID = processID;
	}
	
	public void run() {
		try {
			serversocket = new ServerSocket();
			Log.d(TAG, "AppServer1");
			serversocket.bind(new InetSocketAddress("10.0.2.15", 10000));
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(true) {
			try {
				Socket clientsocket = serversocket.accept();
				Log.d(TAG, "AppServer2");
				is = new ObjectInputStream(clientsocket.getInputStream());
				msginfo = (MessageInfo)is.readObject();
				is.close();
				
				if (msginfo.getMessageType() == MessageInfo.CASTED_MSG) {
					DeliverMessage dm = new DeliverMessage(myHandler, msginfo.getMsg());
					dm.deliver();
				}
				else if (msginfo.getMessageType() == MessageInfo.NORMAL_MSG) {
					ProcessMessage pm = new ProcessMessage(msginfo, serverID);
					pm.processMessage();
				}
				else if (msginfo.getMessageType() == MessageInfo.PRONUM_MSG) {
					DeliverMessage dm = new DeliverMessage(myHandler, msginfo.getMsg());
					dm.deliver();
					ProcessPropNumber ppn = new ProcessPropNumber(msginfo, serverID);
					ppn.processPropNum();
				}
				else if (msginfo.getMessageType() == MessageInfo.AGRNUM_MSG) {
					ProcessAgreeNumber pan = new ProcessAgreeNumber(msginfo, serverID);
					pan.processAgreeNum();
				}			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}