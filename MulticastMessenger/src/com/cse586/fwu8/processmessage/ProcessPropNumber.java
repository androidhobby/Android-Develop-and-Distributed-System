package com.cse586.fwu8.processmessage;

import com.cse586.fwu8.castmessage.CastMessage;
import com.cse586.fwu8.message.MessageInfo;
import com.cse586.fwu8.message.Process;
import com.cse586.fwu8.message.TempMessage;

public class ProcessPropNumber {
	
	MessageInfo mi = new MessageInfo();
	private int id = 0;
	Process[] process = new Process[5];
	
	public ProcessPropNumber(MessageInfo m, int serverID) {
		this.mi = m;
		this.id = serverID;
	}
	
	public void processPropNum() {
		String uid = mi.getUid();
		process[id] = new Process();
		if (process[id].tempMessage.containsKey(uid)) {
			TempMessage tm = (TempMessage)process[id].tempMessage.get(uid);
			process[id].setReply(tm.getCount() + 1);
			process[id].setMaxProp(tm.getMaxPropNum());
			if (process[id].getMaxProp() < mi.getPropNum()) {
				process[id].setMaxProp(mi.getPropNum());
				if(process[id].getMaxProp() > process[id].getMaxObsv()) {
					process[id].setMaxObsv(process[id].getMaxProp());
				}
				tm.getMsgInfo().setMsgFrom(mi.getMessageFrom());
				tm.setMaxPropNum(process[id].getMaxProp());
			}
			
			if (process[id].getReply() == 5) {
				tm.getMsgInfo().setAgreeNum(process[id].getMaxProp());
				tm.getMsgInfo().setMsgType(MessageInfo.AGRNUM_MSG);
				CastMessage cm = new CastMessage("10.0.2.2", tm.getMsgInfo());
				cm.multicast();
				process[id].tempMessage.remove(uid);
			}
		}
		else {
			TempMessage tm = new TempMessage();
			tm.setCount(1);
			tm.setMaxPropNum(mi.getPropNum());
			if (mi.getPropNum() > process[id].getMaxObsv()) {
				process[id].setMaxObsv(mi.getPropNum());
			}
			tm.setMsgInfo(mi);
			process[id].tempMessage.put(uid, tm);
		}
	}
}
