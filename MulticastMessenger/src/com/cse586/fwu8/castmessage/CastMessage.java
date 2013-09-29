package com.cse586.fwu8.castmessage;

import com.cse586.fwu8.joingroup.GroupMember;
import com.cse586.fwu8.message.MessageInfo;

public class CastMessage {
	private static final String TAG = "CastMessage";
	String IP = null;
	MessageInfo m = null;
	GroupMember gm = new GroupMember();
	
	public CastMessage(String ip, MessageInfo msginfo) {
		this.IP = ip;
		this.m = msginfo;
	}
	
	public void multicast() {
		int[] portNumber;
    	portNumber = gm.getPortNumber();
    	for (int i = 0; i < portNumber.length; i++) {
    		AppClient appclient = new AppClient(IP, portNumber[i], m);
    		appclient.start();
    	}
	}
	
	public void unicast() {
		int portNum = gm.getEveryPortNum(m.getMessageFrom());
		AppClient appclient = new AppClient(IP, portNum, m);
		appclient.start();
	}
	
}
