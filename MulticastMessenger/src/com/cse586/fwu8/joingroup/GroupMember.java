package com.cse586.fwu8.joingroup;

public class GroupMember {
	int[] port = {11108, 11112, 11116, 11120, 11124};
	
	public int[] getPortNumber() {
		return port;
	}
	
	public int getEveryPortNum(int i) {
		return port[i];
	}
}
