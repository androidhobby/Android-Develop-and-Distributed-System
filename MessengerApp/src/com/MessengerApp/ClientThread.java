package com.MessengerApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ClientThread extends Thread 
{
	private String IPAddr = "";
	private int tcp_port = 0;
	
	private String Msg = null;
	private Handler MyHandler = null;
	
	private Socket clientsocket = null;
	private PrintWriter pw = null;
	
	public ClientThread(String IPAddr, int tcp_port, String Msg, Handler MyHandler) 
	{
		this.IPAddr = IPAddr;
		this.tcp_port = tcp_port;
		this.Msg = Msg;
		this.MyHandler = MyHandler;
	}
	
	@Override
	public void run() 
	{
		try {
			clientsocket = new Socket(IPAddr, tcp_port);
			pw = new PrintWriter(clientsocket.getOutputStream());
			pw.println(Msg);
			pw.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		  catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(Msg);
		
		Message handlerMsg = MyHandler.obtainMessage();
		Bundle bundleMsg = new Bundle();
		bundleMsg.putString("content", Msg);
		handlerMsg.setData(bundleMsg);
		MyHandler.sendMessage(handlerMsg);
	}	
}

