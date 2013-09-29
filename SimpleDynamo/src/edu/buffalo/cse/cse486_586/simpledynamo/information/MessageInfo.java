/**
 * 
 */
package edu.buffalo.cse.cse486_586.simpledynamo.information;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Fang Wu
 * April 20, 2012
 *
 */
public class MessageInfo implements Serializable {

	private static final long serialVersionUID = 7741665418313210917L;

	@SuppressWarnings("unused")
	private static final String TAG = "MessageInfo";
	
	public static final int MSG_TYPE_REQUEST = 0;
	public static final int MSG_TYPE_JUDGE = 1;
	public static final int MSG_TYPE_INSERT = 2;
	public static final int MSG_TYPE_INSERTTORING = 3;
	public static final int MSG_TYPE_REPLICA = 4;
	public static final int MSG_TYPE_QUERY = 5;
	public static final int MSG_TYPE_QUERYTOO = 6;
	public static final int MSG_TYPE_QUERIED = 7;
	public static final int MSG_TYPE_DELIVER = 8;
	public static final int MSG_TYPE_FAILJUDGE = 9;
	public static final int MSG_TYPE_FAILNODE = 10;
	public static final int MSG_TYPE_NOTFAILNODE = 11;
	public static final int MSG_TYPE_RECOVERY = 12;
	public static final int MSG_TYPE_RECOVAL = 13;
	
	private NodeInfo tempNode = null;
	private HashMap<String, Object> tempPair = null;
	private int msgType = -1;
	
	public MessageInfo() {}
	
	public MessageInfo(NodeInfo node, HashMap<String, Object> pair, int type) {
		this.setTempNode(node);
		this.setTempPair(pair);
		this.setMsgType(type);
	}
	
	public NodeInfo getTempNode() {
		return tempNode;
	}
	
	public void setTempNode (NodeInfo node) {
		this.tempNode = node;
	}
	
	public HashMap<String, Object> getTempPair() {
		return tempPair;
	}
	
	public void setTempPair(HashMap<String, Object> pair) {
		this.tempPair = pair;
	}
	
	public int getMsgType() {
		return msgType;
	}
	
	public void setMsgType(int type) {
		this.msgType = type;
	}
}
