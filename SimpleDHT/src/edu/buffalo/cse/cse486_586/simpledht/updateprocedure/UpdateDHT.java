/*
 * This part we designed together.
 * It mainly contains the procedure of updating the predecessor and successor.
 * The method is sending message object to each other.
 */
package edu.buffalo.cse.cse486_586.simpledht.updateprocedure;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import edu.buffalo.cse.cse486_586.simpledht.castmessage.CastMessage;
import edu.buffalo.cse.cse486_586.simpledht.handlenodes.GetHashKey;
import edu.buffalo.cse.cse486_586.simpledht.handlenodes.NodeInfo;
import edu.buffalo.cse.cse486_586.simpledht.message.MessageInfo;
import edu.buffalo.cse.cse486_586.simpledht.provider.MyContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class UpdateDHT {
	
	private static final String TAG = "UpdateDHT";
	
	NodeInfo node;
	ContentResolver contentResolver;
	String nodeID;
	String nodeIP;
	String nodePort;
	
	public UpdateDHT(String ID, String IP, String port, ContentResolver cr) {
		this.nodeID = ID;
		this.nodeIP = IP;
		this.nodePort = port;
		this.contentResolver = cr;
	}
	
	// initialize the node
	public void initNodeInfo() {
		node = new NodeInfo(this.nodeID, this.nodeIP, this.nodePort, null, null);
		node.setPredecessor(this.node);
		node.setSuccessor(this.node);
	}
	
	public void insertNewPair(String key, String value) {
		ContentValues keyValueToInsert = new ContentValues();
		keyValueToInsert.put(MyContentProvider.MyContent.KEY, key);
		keyValueToInsert.put(MyContentProvider.MyContent.VALUE, value);
		contentResolver.insert(MyContentProvider.MyContent.CONTENT_URI, keyValueToInsert);
	}
	
	// some codes learn from http://developer.android.com in this function
	public boolean isNewPairExist(String key) {          
		boolean existFlag = false;
		String projection[] = new String[] { 
				MyContentProvider.MyContent.ID,
				MyContentProvider.MyContent.KEY, 
				MyContentProvider.MyContent.VALUE };
		Cursor resultCursor = contentResolver.query(MyContentProvider.MyContent.CONTENT_URI, projection, 
					MyContentProvider.MyContent.KEY + "=" + key, null, null);
		if (resultCursor != null) {
			existFlag = true;
			resultCursor.close();
		}
		return existFlag;
	}
	
	// some codes learn from http://developer.android.com in this function
	public String[] queryOnePair(String key) {       
		String projection[] = new String[] {
				MyContentProvider.MyContent.ID,
				MyContentProvider.MyContent.KEY,
				MyContentProvider.MyContent.VALUE };
		Cursor resultCursor = contentResolver.query(MyContentProvider.MyContent.CONTENT_URI, projection, 
					MyContentProvider.MyContent.KEY + "=" + key, null, null);
		if (resultCursor != null) {
			String pair[] = new String[] { resultCursor.getString(1), resultCursor.getString(2) };
			resultCursor.close();
			return pair;
		} 
		return null; 
	}
	
	// some codes learn from http://developer.android.com in this function
	public void displayLocalPairs() {  
		String projection[] = new String[] {
				MyContentProvider.MyContent.ID,
				MyContentProvider.MyContent.KEY,
				MyContentProvider.MyContent.VALUE };
		Cursor resultCursor = contentResolver.query(MyContentProvider.MyContent.CONTENT_URI, projection, 
					null, null, null);   
		if (resultCursor != null) {
			while (true) {   
				MessageInfo displayMsg = new MessageInfo();
				HashMap<String, Object> pair = new HashMap<String, Object>();
 				pair.put("queryKey", resultCursor.getString(1));
				pair.put("queryVal", resultCursor.getString(2));
				displayMsg.setTempPair(pair);
				displayMsg.setTempNode(this.node);
				displayMsg.setMsgType(MessageInfo.MSG_TYPE_DELIVER_MSG);
				sendMessage(this.nodeIP, this.nodePort, displayMsg);
				if (!resultCursor.moveToNext()) {  
					resultCursor.close();
					break;
				}
			}
		}
	}
	
	/* 
	 * The following part of the algorithm is mainly designed by us together.
	 * The pseudo-codes are listed in the design document.  
	 */
	// send a join message to 5554 emulator
	public void nodeToJoinRing() {
		MessageInfo joinMsg = new MessageInfo();
		joinMsg.setTempNode(this.node);
		joinMsg.setMsgType(MessageInfo.MSG_TYPE_JOIN_REQUEST);
		sendMessage(this.nodeIP, "5554", joinMsg);
	}
	
	// handle the join request message
	public void joinRequest(MessageInfo msg) { 
		JudgeSituation judge = new JudgeSituation(this.node, this.nodeID);
		String newNodeID = msg.getTempNode().getNodeID();
		NodeInfo preNode = new NodeInfo();   // set node predecessor buffer
		NodeInfo sucNode = new NodeInfo();	 // set node successor buffer
		System.out.println(newNodeID);
		
		if (judge.isNodeSelfOfRing()) {
			preNode = sucNode = this.node;
			this.node.setPredecessor(msg.getTempNode());
			this.node.setSuccessor(msg.getTempNode());
			joinRequestMsg(preNode, sucNode, msg);
		}
		else if (judge.isNewNodeToJoin(newNodeID)) {
			preNode = this.node;
			sucNode = this.node.getSuccessor();
			this.node.setSuccessor(msg.getTempNode());
			joinRequestMsg(preNode, sucNode, msg);
		}
		else
			sendMessage(this.node.getSuccessor().getNodeIP(), this.node.getSuccessor().getNodePort(), msg);
	}
	
	public void joinRequestMsg(NodeInfo preNode, NodeInfo sucNode, MessageInfo msg) {
		HashMap<String, Object> pair = new HashMap<String, Object>();
		MessageInfo replyMsg = new MessageInfo();
		pair.put("preNode", preNode);
		pair.put("sucNode", sucNode);
		replyMsg.setTempPair(pair);
		replyMsg.setTempNode(this.node);
		replyMsg.setMsgType(MessageInfo.MSG_TYPE_JOINED_REPLY);
		sendMessage(msg.getTempNode().getNodeIP(), msg.getTempNode().getNodePort(), replyMsg);
	}
	
	// update the predecessor and successor
	public void joinedReply(MessageInfo msg) {
		MessageInfo updateMsg = new MessageInfo();
		NodeInfo preNode = new NodeInfo();
		HashMap<String, Object> pair = new HashMap<String, Object>();
		this.node.setPredecessor((NodeInfo)msg.getTempPair().get("preNode"));  // set predecessor
		this.node.setSuccessor((NodeInfo)msg.getTempPair().get("sucNode"));	   // set successor
		preNode = this.node;
		pair.clear();
		pair.put("preNode", preNode);
		updateMsg.setTempPair(pair);
		updateMsg.setTempNode(this.node);
		updateMsg.setMsgType(MessageInfo.MSG_TYPE_UPDATE_REQUEST);
		sendMessage(this.node.getSuccessor().getNodeIP(), this.node.getSuccessor().getNodePort(), updateMsg); 
	}
	
	public void updateChordRing(MessageInfo  msg) {
		this.node.setPredecessor((NodeInfo)msg.getTempPair().get("preNode"));
		Log.d(TAG, msg.getTempNode().getNodePort() + "Joined into the ring");
	}
	
	// new key to insert into the ring
	public void keyToInsertRing(String insertKey, String insertVal) {   
		MessageInfo insertMsg = new MessageInfo();
		HashMap<String, Object> pair = new HashMap<String, Object>();
		String insertHashKey = null;
		GetHashKey hashKey = new GetHashKey();
		try {
			insertHashKey = hashKey.genHash(insertKey);  // get new key's hash_key
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		pair.put("insertHashKey", insertHashKey);
		pair.put("insertKey", insertKey);
		pair.put("insertVal", insertVal);
		insertMsg.setTempNode(this.node);
		insertMsg.setTempPair(pair);
		insertMsg.setMsgType(MessageInfo.MSG_TYPE_INSERT_REQUEST);
		sendMessage(this.node.getNodeIP(), this.node.getNodePort(), insertMsg);
	}
	
	// handle insert request message
	public void insertRequest(MessageInfo msg) {
		JudgeSituation judge = new JudgeSituation(this.node, this.nodeID);
		String newHashKey = (String) msg.getTempPair().get("insertHashKey");
		if (judge.isNodeSelfOfRing()) {   // only one node in this ring
			insertNewPair((String)msg.getTempPair().get("insertKey"), (String)msg.getTempPair().get("insertVal"));
			Log.d(TAG, "a new key inserted");
		}
		else if (judge.isMulNodeRing(newHashKey)) {	// two or more nodes in this ring
			insertNewPair((String)msg.getTempPair().get("insertKey"), (String)msg.getTempPair().get("insertVal"));
			Log.d(TAG, "a new key inserted");
		}
		else   // the local node cannot handle this situation, send to its successor
			sendMessage(this.node.getSuccessor().getNodeIP(), this.node.getSuccessor().getNodePort(), msg);
	}
	
	// query key from the ring
	public void keyQueryFromRing(String queryKey) {
		MessageInfo queryMsg = new MessageInfo();
		HashMap<String, Object> pair = new HashMap<String, Object>();
		pair.put("queryKey", queryKey);
		queryMsg.setTempNode(this.node);
		queryMsg.setTempPair(pair);
		queryMsg.setMsgType(MessageInfo.MSG_TYPE_QUERY_REQUEST);
		sendMessage(this.nodeIP, this.nodePort, queryMsg);
	}
	
	// handle query request message
	public void queryRequest(MessageInfo msg) {
		JudgeSituation judge = new JudgeSituation(this.node, this.nodeID);
		String newQueryKey = (String) msg.getTempPair().get("queryKey");
		String newHashKey = null;
		HashMap<String, Object> pair = new HashMap<String, Object>();
		MessageInfo queriedMsg = new MessageInfo();
		GetHashKey getHashKey = new GetHashKey();
		try {
			newHashKey = getHashKey.genHash(newQueryKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (isNewPairExist(newQueryKey)) {  // judge whether the query key is in the ring
			if (judge.isNodeSelfOfRing()) {  // only one node in the ring
				String[] queryResult = queryOnePair(newQueryKey);  // get query result
				pair.put("queryResult", queryResult);
				queriedMsg.setTempPair(pair);
				queriedMsg.setTempNode(msg.getTempNode());
				queriedMsg.setMsgType(MessageInfo.MSG_TYPE_QUERIED_REPLY);
				sendMessage(msg.getTempNode().getNodeIP(), msg.getTempNode().getNodePort(), queriedMsg);
			}
			else if (judge.isMulNodeRing(newHashKey)) {  // two or more nodes in the ring
				String[] queryResult = queryOnePair(newQueryKey);  // get query result
				pair.put("queryResult", queryResult);
				queriedMsg.setTempPair(pair);
				queriedMsg.setTempNode(msg.getTempNode());
				queriedMsg.setMsgType(MessageInfo.MSG_TYPE_QUERIED_REPLY);
				sendMessage(msg.getTempNode().getNodeIP(), msg.getTempNode().getNodePort(), queriedMsg);
			}
			else
				sendMessage(msg.getTempNode().getSuccessor().getNodeIP(), msg.getTempNode().getSuccessor().getNodePort(), msg);
		}
		else
			return;
	}
	
	// display the query result
	public void displayQueryResult(MessageInfo msg) {
		MessageInfo showMsg = new MessageInfo();
		HashMap<String, Object> pair = new HashMap<String, Object>();
		pair.put("queryKey", ((String[])msg.getTempPair().get("queryResult"))[0]); // learn from http://developer.android.com 
		pair.put("queryVal", ((String[])msg.getTempPair().get("queryResult"))[1]); // learn from http://developer.android.com 
		showMsg.setTempPair(pair);
		showMsg.setTempNode(msg.getTempNode());
		showMsg.setMsgType(MessageInfo.MSG_TYPE_DELIVER_MSG);
		sendMessage(this.nodeIP, this.nodePort, showMsg);
	}
		
	public void sendMessage(String IP, String Port, MessageInfo m) {
		CastMessage cm = new CastMessage(IP, Port, m);
		cm.unicast();
	}
}
