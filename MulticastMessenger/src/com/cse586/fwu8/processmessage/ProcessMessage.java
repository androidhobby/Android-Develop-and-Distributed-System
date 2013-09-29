package com.cse586.fwu8.processmessage;

import com.cse586.fwu8.castmessage.CastMessage;
import com.cse586.fwu8.message.MessageInfo;
import com.cse586.fwu8.message.Process;

public class ProcessMessage {
	boolean causal_flag = true;
	int i = 0;
	int j = 0;
	MessageInfo mi = new MessageInfo();
	Process[] process = new Process[5];
	
	public ProcessMessage(MessageInfo m, int serverID) {
		this.mi = m;
		this.i = serverID;
	}
	
	public void processMessage() {
		process[i] = new Process();
		if (mi.getMessageFrom() == i) {
			process[i].toQueue.add(mi);
			CastMessage cm = new CastMessage("10.0.2.2", mi);
			cm.unicast();
			return;
		}
		j = mi.getMessageFrom();
		process[i].coQueue.add(mi);
		int[] timestamp = mi.timestampVector;
		if (timestamp[j] == mi.timestampVector[j] + 1) {
			for (int k = 0; k < 5; k++) {
				if (k == j)
					continue;
				else if (mi.timestampVector[k] <= timestamp[k])
					continue;
				else {
					causal_flag = false;
					break;
				}
			}
		}
		else {
			causal_flag = false;                        //TODO
		}
		
		if (causal_flag == true) {
			timestamp[j] += 1;
			process[i].setMaxProp(Math.max(process[i].getMaxProp(), process[i].getMaxObsv()) + 1);
			mi.setPropNum(process[i].getMaxProp());
			mi.setMsgType(MessageInfo.PRONUM_MSG);
			process[i].toQueue.add(mi);
			CastMessage cm = new CastMessage("10.0.2.2", mi);
			cm.unicast();
		}
	}
	
}
