package com.cse586.fwu8.message;

import com.cse586.fwu8.message.MessageInfo;

public class TempMessage {
	private int counter = 0;
	private int maxPropNumber = 0;
	MessageInfo mi = null;
	
	public void setCount(int counter) {
		this.counter = counter;
	}
	
	public void setMaxPropNum(int maxProp) {
		this.maxPropNumber = maxProp;
	}
	
	public void setMsgInfo(MessageInfo m) {
		this.mi = m;
	}
	
	public int getCount() {
		return counter;
	}
	
	public int getMaxPropNum() {
		return maxPropNumber;
	}
	
	public MessageInfo getMsgInfo() {
		return mi;
	}
}
