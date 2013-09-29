/**
 * 
 */
package edu.buffalo.cse.cse486_586.simpledynamo.castmessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.buffalo.cse.cse486_586.simpledynamo.information.MessageInfo;
import edu.buffalo.cse.cse486_586.simpledynamo.update.UpdateDynamo;

import android.util.Log;

/**
 * @author Fang Wu April 20, 2012
 * 
 */
public class AppClient extends Thread {

	private static final String TAG = "AppClient";

	private Socket clientsocket = null;
	private ObjectOutputStream os = null;
	private String IP = null;
	private int port = 0;
	private MessageInfo msg = null;
	
	public AppClient(String IP, int port, MessageInfo msg) {
		this.IP = IP;
		this.port = port;
		this.msg = msg;
	}

	public void run() {
		try {
			clientsocket = new Socket(IP, port);	
			os = new ObjectOutputStream(clientsocket.getOutputStream());
			os.writeObject(msg);
			os.close();
		} catch (StreamCorruptedException e) {
			UpdateDynamo.failFlag = true;
			Log.d(TAG, msg.getTempNode().getNodePort() + "fail");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			Log.d(TAG, "UnknownHost");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(TAG, "IOException");
			e.printStackTrace();
		}
	}
}
