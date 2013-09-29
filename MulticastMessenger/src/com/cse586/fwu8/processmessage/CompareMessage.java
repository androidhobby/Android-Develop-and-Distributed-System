package com.cse586.fwu8.processmessage;

import java.util.Comparator;

import com.cse586.fwu8.message.MessageInfo;

public class CompareMessage implements Comparator <MessageInfo>{

	@Override
	public int compare(MessageInfo lhs, MessageInfo rhs) {
		// TODO Auto-generated method stub
		if (lhs.getPropNum() == rhs.getPropNum()) 
			return lhs.getMessageFrom() - rhs.getMessageFrom();
		else
			return lhs.getPropNum() - rhs.getPropNum();
	}

}
