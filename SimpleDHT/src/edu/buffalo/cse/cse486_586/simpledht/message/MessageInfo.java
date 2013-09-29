/*
 * This part we designed together.
 * It mainly has three parameters: a node object, a hash map and message type.
 */
package edu.buffalo.cse.cse486_586.simpledht.message;

import java.io.Serializable;
import java.util.HashMap;

import edu.buffalo.cse.cse486_586.simpledht.handlenodes.NodeInfo;

public class MessageInfo implements Serializable {

	private static final long serialVersionUID = 2L;
	@SuppressWarnings("unused")
	private static final String TAG = "MessageInfo";
	
	public static final int MSG_TYPE_JOIN_REQUEST = 0;
	public static final int MSG_TYPE_JOINED_REPLY = 1;
	public static final int MSG_TYPE_INSERT_REQUEST = 2;
	public static final int MSG_TYPE_QUERY_REQUEST = 3;
	public static final int MSG_TYPE_QUERIED_REPLY = 4;
	public static final int MSG_TYPE_DELIVER_MSG = 5;
	public static final int MSG_TYPE_UPDATE_REQUEST = 6;
	
	private NodeInfo tempNode = null;     // To store the node information
	private HashMap<String, Object> tempPair = null; // easy to put and get the data
	private int msgType = -1;   // judge how to handle the received message
	
	public MessageInfo() { }
	
	public MessageInfo(NodeInfo node, HashMap<String, Object> pair, int type) {
		this.tempNode = node;
		this.tempPair = pair;
		this.msgType = type;
	}

	public void setTempNode (NodeInfo tmpNode) {
		this.tempNode = tmpNode;
	}
	
	public NodeInfo getTempNode() {
		return tempNode;
	}
	
	public void setTempPair (HashMap<String, Object> tmpPair) {
		this.tempPair = tmpPair;
	}
	
	public HashMap<String, Object> getTempPair() {
		return tempPair;
	}
	
	public void setMsgType (int msgType) {
		this.msgType = msgType;
	}
	
	public int getMsgType() {
		return msgType;
	}
}
