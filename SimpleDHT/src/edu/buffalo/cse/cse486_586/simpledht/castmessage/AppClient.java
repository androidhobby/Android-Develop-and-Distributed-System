package edu.buffalo.cse.cse486_586.simpledht.castmessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import edu.buffalo.cse.cse486_586.simpledht.message.MessageInfo;

public class AppClient extends Thread{
	
	@SuppressWarnings("unused")
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
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
