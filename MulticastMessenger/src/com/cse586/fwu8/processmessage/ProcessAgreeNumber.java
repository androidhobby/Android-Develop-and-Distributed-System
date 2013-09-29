package com.cse586.fwu8.processmessage;

import java.util.Iterator;

import com.cse586.fwu8.castmessage.CastMessage;
import com.cse586.fwu8.message.MessageInfo;
import com.cse586.fwu8.message.Process;

public class ProcessAgreeNumber {
	
	MessageInfo mi = new MessageInfo();
	private int id = 0;
	Process[] process = new Process[5];
	boolean agreed_check = true;
	
	public ProcessAgreeNumber(MessageInfo m, int serverID) {
		this.mi = m;
		this.id = serverID;
	}
	
	public void processAgreeNum() {
		process[id] = new Process();
		if (mi.getAgreeNum() > process[id].getMaxObsv())
			process[id].setMaxObsv(mi.getAgreeNum());
		Iterator<MessageInfo> ir = process[id].toQueue.iterator();
		MessageInfo m = new MessageInfo();
		while(ir.hasNext()) {
			m = (MessageInfo)ir.next();
			if (m.getUid().compareTo(mi.getUid()) == 0) 
				break;
		}
		process[id].toQueue.remove(m);
		m.setAgreed(true);
		if (m.getPropNum() < mi.getAgreeNum()) {
			m.setPropNum(mi.getAgreeNum());
			m.setMsgFrom(mi.getMessageFrom());
		}
		process[id].toQueue.add(m);

		while (agreed_check) {
			if (process[id].toQueue.isEmpty())
				break;
			m = process[id].toQueue.first();
			if (m.getAgreed() == true) {
				process[id].toQueue.remove(m);
				m.setMsgFrom(id);
				m.setMsgType(MessageInfo.CASTED_MSG);
				CastMessage cm = new CastMessage("10.0.2.2", m);
				cm.unicast();
			}
			else
				agreed_check = false;
		}
	}
}
