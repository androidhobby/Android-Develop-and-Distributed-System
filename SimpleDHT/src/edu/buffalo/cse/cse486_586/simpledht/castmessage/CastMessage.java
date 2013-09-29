package edu.buffalo.cse.cse486_586.simpledht.castmessage;

import edu.buffalo.cse.cse486_586.simpledht.message.MessageInfo;

public class CastMessage {
	
	@SuppressWarnings("unused")
	private static final String TAG = "CastMessage";
	
	private String addrIP = null;
	private String port = null;
	private MessageInfo msg = null;
	
	public CastMessage(String IP, String port, MessageInfo m) {
		this.addrIP = IP;
		this.port = port;
		this.msg = m;
	}
	
	public void unicast() {
		int Port = Integer.parseInt(port) * 2;
		AppClient appclient = new AppClient(addrIP, Port, msg);
		appclient.start();
	}
}
