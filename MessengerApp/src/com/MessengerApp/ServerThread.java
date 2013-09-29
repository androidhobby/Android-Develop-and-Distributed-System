package com.MessengerApp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ServerThread extends Thread
{
	private ServerSocket serversocket = null;
	private Server MyServer = null;
	private Handler MyHandler = null;
	private InputStreamReader is = null;
	private BufferedReader buf = null;
	private String Msg = "";

	public ServerThread(Handler MyHandler)
	{
		this.MyHandler = MyHandler;
	}

	public void run() 
	{
		MyServer = new Server();
		serversocket = MyServer.getter();
		while (true)
		{
			try {
				Socket clientsocket = serversocket.accept();
				is = new InputStreamReader(clientsocket.getInputStream());
				buf = new BufferedReader(is);
				Msg = buf.readLine();
				buf.close();
				
				System.out.println(this.Msg);

				Message handlerMsg = MyHandler.obtainMessage();
				Bundle bundleMsg = new Bundle();
				bundleMsg.putString("content", Msg);
				handlerMsg.setData(bundleMsg);
				MyHandler.sendMessage(handlerMsg);
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}	
	}
}