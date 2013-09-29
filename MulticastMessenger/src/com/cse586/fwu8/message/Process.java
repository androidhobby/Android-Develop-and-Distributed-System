package com.cse586.fwu8.message;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.cse586.fwu8.processmessage.CompareMessage;

public class Process {
	//public int[] timestampVector;
	private int maxObsv = 0;        //max observed sequence number in group
	private int maxProp = 0;        //max proposed sequence number
	private int localCausalCount = 0;
	private int reply = 0;

	public Set<MessageInfo> coQueue = Collections.synchronizedSet(new HashSet<MessageInfo>());
	public SortedSet<MessageInfo> toQueue = Collections.synchronizedSortedSet(new TreeSet<MessageInfo>(new CompareMessage()));
	TreeSet<MessageInfo> treeSet = new TreeSet<MessageInfo>(new CompareMessage());
	public Map<String, TempMessage> tempMessage = new ConcurrentHashMap<String, TempMessage>();
	
	public void setMaxObsv(int maxObsv) {
		this.maxObsv = maxObsv;
	}
	
	public void setMaxProp(int maxProp) {
		this.maxProp = maxProp;
	}
	
	public void setReply(int reply) {
		this.reply = reply;
	}
	
	public void setLocalCausalCount(int count) {
		this.localCausalCount = count;
	}
		
	public int getMaxObsv() {
		return maxObsv;
	}
	
	public int getMaxProp() {
		return maxProp;
	}
	
	public int getReply() {
		return reply;
	}
	
	public int getLocalCausalCount() {
		return localCausalCount;
	}
	
}
