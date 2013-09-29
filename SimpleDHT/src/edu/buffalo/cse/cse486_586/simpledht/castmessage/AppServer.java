package edu.buffalo.cse.cse486_586.simpledht.castmessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import edu.buffalo.cse.cse486_586.simpledht.deliver.DeliverMessage;
import edu.buffalo.cse.cse486_586.simpledht.message.MessageInfo;
import edu.buffalo.cse.cse486_586.simpledht.updateprocedure.UpdateDHT;

import android.os.Handler;
import android.util.Log;

public class AppServer extends Thread{
	
	private static final String TAG = "AppServer";
	
	private ServerSocket serversocket = null;
	private Handler myHandler = null;
	private ObjectInputStream is = null;
	private MessageInfo msg = null;
	private UpdateDHT updateDHT;
	
	public AppServer(Handler handler, UpdateDHT ud) {
		this.myHandler = handler;
		this.updateDHT = ud;
	}
	
	public void run() {
		try {
			serversocket = new ServerSocket();
			serversocket.bind(new InetSocketAddress("10.0.2.15", 10000));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int type = -1;
		while (true) {
			try {
				Socket clientsocket = serversocket.accept();
				is = new ObjectInputStream(clientsocket.getInputStream());
				msg = (MessageInfo)is.readObject();
				is.close();
				type = msg.getMsgType();
				Log.d(TAG, "Get value!");
				switch (type) {
				case MessageInfo.MSG_TYPE_JOIN_REQUEST:
					Log.d(TAG, "Join a node");
					updateDHT.joinRequest(msg);
					break;
				case MessageInfo.MSG_TYPE_JOINED_REPLY:
					Log.d(TAG, "Get joined");
					updateDHT.joinedReply(msg);
					break;
				case MessageInfo.MSG_TYPE_INSERT_REQUEST:
					Log.d(TAG, "Insert a new key");
					updateDHT.insertRequest(msg);
					break;
				case MessageInfo.MSG_TYPE_QUERY_REQUEST:
					Log.d(TAG, "Query a key");
					updateDHT.queryRequest(msg);
					break;
				case MessageInfo.MSG_TYPE_QUERIED_REPLY:
					Log.d(TAG, "Get queried");
					updateDHT.displayQueryResult(msg);
					break;
				case MessageInfo.MSG_TYPE_UPDATE_REQUEST:
				    Log.d(TAG, "Get update");
					updateDHT.updateChordRing(msg);
					break;
				case MessageInfo.MSG_TYPE_DELIVER_MSG:
					Log.d(TAG, "Display message");
					String message = "<" + (String)msg.getTempPair().get("queryKey") + "," + 
									(String)msg.getTempPair().get("queryVal") + ">";
					DeliverMessage dm = new DeliverMessage(myHandler, message); 
					dm.deliver();
					break;
				default:
					break;	
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
}
