package com.MessengerApp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Server
{
	private ServerSocket serversocket = null;
	private int Port = 0;
	private String IP = "";
	
	public Server() 
	{
		IP = "10.0.2.15";
		Port = 9000;
		initServer(IP,Port);
	}
	
	public void initServer(String ip_addr, int port_num)
	{
		this.IP = ip_addr;
		this.Port = port_num;
		try {
			serversocket = new ServerSocket();
			serversocket.bind(new InetSocketAddress(IP,Port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ServerSocket getter() {
		return serversocket;
	}
}