/**
 * 
 */
package edu.buffalo.cse.cse486_586.simpledynamo.update;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;

import edu.buffalo.cse.cse486_586.simpledynamo.castmessage.CastMessage;
import edu.buffalo.cse.cse486_586.simpledynamo.gethashkey.GetHashKey;
import edu.buffalo.cse.cse486_586.simpledynamo.information.MessageInfo;
import edu.buffalo.cse.cse486_586.simpledynamo.information.NodeInfo;

/**
 * @author Fang Wu April 20, 2012
 * 
 */
public class UpdateDynamo {

	private static final String TAG = "UpdateDynamo";

	public static boolean failFlag;

	private UpdateContent uc = null;
	private NodeInfo node = null;
	private NodeInfo node_5554 = null;
	private NodeInfo node_5556 = null;
	private NodeInfo node_5558 = null;
	private NodeInfo node_5560 = null;
	private NodeInfo node_5562 = null;
	HashMap<String, Object> NodeList = new HashMap<String, Object>();

	private NodeInfo failNode = null;
	private String failId = null;

	public UpdateDynamo() {
	}

	public UpdateDynamo(UpdateContent uc, NodeInfo node) {
		this.setUpdateContent(uc);
		this.setNodeInfo(node);
	}

	public UpdateContent getUpdateContent() {
		return uc;
	}

	public void setUpdateContent(UpdateContent uc) {
		this.uc = uc;
	}

	public NodeInfo getNodeInfo() {
		return node;
	}

	public void setNodeInfo(NodeInfo node) {
		this.node = node;
	}

	public void initRing() {
		GetHashKey getHashKey = new GetHashKey();
		try {
			this.node_5554 = new NodeInfo(getHashKey.genHash("5554"),
					"10.0.2.2", "5554", null, null, null);
			this.node_5556 = new NodeInfo(getHashKey.genHash("5556"),
					"10.0.2.2", "5556", null, null, null);
			this.node_5558 = new NodeInfo(getHashKey.genHash("5558"),
					"10.0.2.2", "5558", null, null, null);
			this.node_5560 = new NodeInfo(getHashKey.genHash("5560"),
					"10.0.2.2", "5560", null, null, null);
			this.node_5562 = new NodeInfo(getHashKey.genHash("5562"),
					"10.0.2.2", "5562", null, null, null);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		this.node_5554.setPredecessor(this.node_5556);
		this.node_5554.setSuccessor(this.node_5558);
		this.node_5556.setPredecessor(this.node_5562);
		this.node_5556.setSuccessor(this.node_5554);
		this.node_5558.setPredecessor(this.node_5554);
		this.node_5558.setSuccessor(this.node_5560);
		this.node_5560.setPredecessor(this.node_5558);
		this.node_5560.setSuccessor(this.node_5562);
		this.node_5562.setPredecessor(this.node_5560);
		this.node_5562.setSuccessor(this.node_5556);
		try {
			NodeList.put(getHashKey.genHash("5554"), this.node_5554);
			NodeList.put(getHashKey.genHash("5556"), this.node_5556);
			NodeList.put(getHashKey.genHash("5558"), this.node_5558);
			NodeList.put(getHashKey.genHash("5560"), this.node_5560);
			NodeList.put(getHashKey.genHash("5562"), this.node_5562);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		this.node_5554.setNodeList(NodeList);
		this.node_5556.setNodeList(NodeList);
		this.node_5558.setNodeList(NodeList);
		this.node_5560.setNodeList(NodeList);
		this.node_5562.setNodeList(NodeList);
	}

	public void initNodeInfo() {
		int port = Integer.parseInt(this.node.getNodePort());
		switch (port) {
		case 5554:
			this.node = this.node_5554;
			break;
		case 5556:
			this.node = this.node_5556;
			break;
		case 5558:
			this.node = this.node_5558;
			break;
		case 5560:
			this.node = this.node_5560;
			break;
		case 5562:
			this.node = this.node_5562;
			break;
		default:
			break;
		}
	}

	public void judgeNode() {
		sendRequestMessage(this.node.getSuccessor());
		if (UpdateDynamo.failFlag == true) {
			return;
		} else {
			MessageInfo judgeMsg = new MessageInfo();
			judgeMsg.setMsgType(MessageInfo.MSG_TYPE_JUDGE);
			judgeMsg.setTempNode(this.node);
			sendMessage(this.node.getSuccessor().getNodeIP(), this.node
					.getSuccessor().getNodePort(), judgeMsg);
		}
	}

	public void judgeContentProvider(MessageInfo msg) {
		MessageInfo reMsg = new MessageInfo();
		boolean recoveryFlag = uc.isContentProvierNull();
		if (recoveryFlag == true) {
			reMsg.setMsgType(MessageInfo.MSG_TYPE_FAILNODE);
			sendMessage(msg.getTempNode().getNodeIP(), msg.getTempNode()
					.getNodePort(), reMsg);
		} else {
			reMsg.setMsgType(MessageInfo.MSG_TYPE_NOTFAILNODE);
			sendMessage(msg.getTempNode().getNodeIP(), msg.getTempNode()
					.getNodePort(), reMsg);
		}
	}

	public void keyToInsertRing(String insertKey, String insertVal) {
		MessageInfo insertMsg = new MessageInfo();
		HashMap<String, Object> pair = new HashMap<String, Object>();
		String insertHashKey = null;
		GetHashKey getHashKey = new GetHashKey();
		try {
			insertHashKey = getHashKey.genHash(insertKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		pair.put("insertHashKey", insertHashKey);
		pair.put("insertKey", insertKey);
		pair.put("insertVal", insertVal);
		insertMsg.setTempNode(this.node);
		insertMsg.setTempPair(pair);
		insertMsg.setMsgType(MessageInfo.MSG_TYPE_INSERT);
		sendMessage(this.node.getNodeIP(), this.node.getNodePort(), insertMsg);
	}

	public void insertRequest(MessageInfo msg) {
		UpdateJudge judge = new UpdateJudge(this.node);
		String newHashKey = (String) msg.getTempPair().get("insertHashKey");
		MessageInfo insertTooMsg = new MessageInfo();
		MessageInfo insertReplica = new MessageInfo();
		insertTooMsg.setMsgType(MessageInfo.MSG_TYPE_INSERTTORING);
		insertReplica.setMsgType(MessageInfo.MSG_TYPE_REPLICA);
		NodeInfo nodeToInsert = new NodeInfo();
		nodeToInsert = judge.isMulNodeRing(newHashKey);
		if (nodeToInsert.getNodeId().compareTo(this.node.getNodeId()) == 0) {
			uc.insertNewPair((String) msg.getTempPair().get("insertKey"),
					(String) msg.getTempPair().get("insertVal"), msg
							.getTempNode().getNodePort());
			msg.setMsgType(MessageInfo.MSG_TYPE_REPLICA);

			// UpdateDynamo.failFlag = false;
			sendRequestMessage(this.node.getSuccessor());
			if (UpdateDynamo.failFlag == true) {
				Log.d(TAG, this.node.getSuccessor().getNodePort() + "fail");
			} else
				sendMessage(this.node.getSuccessor().getNodeIP(), this.node
						.getSuccessor().getNodePort(), msg);

			// UpdateDynamo.failFlag = false;
			sendRequestMessage(this.node.getSuccessor().getSuccessor());
			if (UpdateDynamo.failFlag == true) {
				Log.d(TAG, this.node.getSuccessor().getSuccessor()
						.getNodePort()
						+ "fail");
			} else
				sendMessage(
						this.node.getSuccessor().getSuccessor().getNodeIP(),
						this.node.getSuccessor().getSuccessor().getNodePort(),
						msg);

		} else {
			insertTooMsg.setTempPair(msg.getTempPair());
			insertTooMsg.setTempNode(nodeToInsert);

			// UpdateDynamo.failFlag = false;
			sendRequestMessage(nodeToInsert);
			if (UpdateDynamo.failFlag == true) {
				Log.d(TAG, nodeToInsert.getNodePort() + "fail");
				System.out.println("xxxxx");
				sendMessage(nodeToInsert.getSuccessor().getNodeIP(),
						nodeToInsert.getSuccessor().getNodePort(),
						insertReplica);
				sendMessage(nodeToInsert.getSuccessor().getSuccessor()
						.getNodeIP(), nodeToInsert.getSuccessor()
						.getSuccessor().getNodePort(), insertReplica);
			} else {
				sendMessage(nodeToInsert.getNodeIP(),
						nodeToInsert.getNodePort(), insertTooMsg);
				insertReplica.setTempNode(nodeToInsert);
				insertReplica.setTempPair(msg.getTempPair());

				sendRequestMessage(nodeToInsert.getSuccessor());
				if (UpdateDynamo.failFlag == true) {
					Log.d(TAG, nodeToInsert.getSuccessor().getNodePort()
							+ "fail");
					sendMessage(nodeToInsert.getSuccessor().getSuccessor()
							.getNodeIP(), nodeToInsert.getSuccessor()
							.getSuccessor().getNodePort(), insertReplica);
				} else {
					sendMessage(nodeToInsert.getSuccessor().getNodeIP(),
							nodeToInsert.getSuccessor().getNodePort(),
							insertReplica);
					sendRequestMessage(nodeToInsert.getSuccessor()
							.getSuccessor());
					if (UpdateDynamo.failFlag == true) {
						Log.d(TAG, nodeToInsert.getSuccessor().getSuccessor()
								.getNodePort()
								+ "fail");
					} else
						sendMessage(nodeToInsert.getSuccessor().getSuccessor()
								.getNodeIP(), nodeToInsert.getSuccessor()
								.getSuccessor().getNodePort(), insertReplica);
				}

			}
		}
	}

	public void insertTooMsg(MessageInfo msg) {
		uc.insertNewPair((String) msg.getTempPair().get("insertKey"),
				(String) msg.getTempPair().get("insertVal"), msg.getTempNode()
						.getNodePort());
	}

	public void insertReplica(MessageInfo msg) {
		uc.insertNewPair((String) msg.getTempPair().get("insertKey"),
				(String) msg.getTempPair().get("insertVal"), msg.getTempNode()
						.getNodePort());
	}

	public void keyQueryFromRing(String queryKey) {
		String queryHashKey = null;
		MessageInfo queryMsg = new MessageInfo();
		HashMap<String, Object> pair = new HashMap<String, Object>();
		pair.put("queryKey", queryKey);
		GetHashKey getHashKey = new GetHashKey();
		try {
			queryHashKey = getHashKey.genHash(queryKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		pair.put("queryHashKey", queryHashKey);
		queryMsg.setTempNode(this.node);
		queryMsg.setTempPair(pair);
		queryMsg.setMsgType(MessageInfo.MSG_TYPE_QUERY);
		sendMessage(this.node.getNodeIP(), this.node.getNodePort(), queryMsg);
	}

	public void queryRequest(MessageInfo msg) {
		HashMap<String, Object> pair = new HashMap<String, Object>();
		MessageInfo queriedMsg = new MessageInfo();
		NodeInfo queryNode = new NodeInfo();
		UpdateJudge judge = new UpdateJudge(this.node);
		String newQueryKey = (String) msg.getTempPair().get("queryKey");
		String newQueryHashKey = (String) msg.getTempPair().get("queryHashKey");
		if (uc.isNewPairExist(newQueryKey)) { // local node has the key
			String[] queryResult = uc.queryOnePair(newQueryKey);
			pair.put("queryResult", queryResult);
			queriedMsg.setTempPair(pair);
			queriedMsg.setTempNode(msg.getTempNode());
			queriedMsg.setMsgType(MessageInfo.MSG_TYPE_QUERIED);
			sendMessage(msg.getTempNode().getNodeIP(), msg.getTempNode()
					.getNodePort(), queriedMsg);
		} else {
			queryNode = judge.isMulNodeRing(newQueryHashKey);
			msg.setMsgType(MessageInfo.MSG_TYPE_QUERYTOO);
			sendRequestMessage(queryNode);
			if (UpdateDynamo.failFlag == true) {
				Log.d(TAG, queryNode.getNodePort() + "fail");
				sendMessage(queryNode.getSuccessor().getNodeIP(), queryNode
						.getSuccessor().getNodePort(), msg);
			} else
				sendMessage(queryNode.getNodeIP(), queryNode.getNodePort(), msg);
		}
	}

	public void queryTooRequest(MessageInfo msg) {
		MessageInfo returnMsg = new MessageInfo();
		HashMap<String, Object> pair = new HashMap<String, Object>();
		String newQueryKey = (String) msg.getTempPair().get("queryKey");
		if (uc.isNewPairExist(newQueryKey)) {
			String[] queryResult = uc.queryOnePair(newQueryKey);
			pair.put("queryResult", queryResult);
			returnMsg.setTempPair(pair);
			returnMsg.setTempNode(msg.getTempNode());
			returnMsg.setMsgType(MessageInfo.MSG_TYPE_QUERIED);
			sendMessage(msg.getTempNode().getNodeIP(), msg.getTempNode()
					.getNodePort(), returnMsg);
		} else
			return;
	}

	public void displayQueryResult(MessageInfo msg) {
		MessageInfo showMsg = new MessageInfo();
		HashMap<String, Object> pair = new HashMap<String, Object>();
		pair.put("queryKey",
				((String[]) msg.getTempPair().get("queryResult"))[0]);
		pair.put("queryVal",
				((String[]) msg.getTempPair().get("queryResult"))[1]);
		showMsg.setTempNode(msg.getTempNode());
		showMsg.setTempPair(pair);
		showMsg.setMsgType(MessageInfo.MSG_TYPE_DELIVER);
		sendMessage(this.node.getNodeIP(), this.node.getNodePort(), showMsg);
	}

	public void handleFailure(int port, MessageInfo msg) { // TODO
		String portStr = String.valueOf(port / 2);
		GetHashKey getHashKey = new GetHashKey();
		try {
			failId = getHashKey.genHash(portStr);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Iterator<String> iterator = this.node.getNodeList().keySet().iterator();
		while (iterator.hasNext()) {
			String id = iterator.next();
			if (id.compareTo(failId) == 0) {
				this.failNode = (NodeInfo) this.node.getNodeList().get(id);
				break;
			} else
				continue;
		}
		sendMessage(failNode.getSuccessor().getNodeIP(), failNode
				.getSuccessor().getNodePort(), msg);
	}

	public void handleRecovery() { 
		MessageInfo recoveryMsg = new MessageInfo();
		recoveryMsg.setTempNode(this.node);
		recoveryMsg.setMsgType(MessageInfo.MSG_TYPE_RECOVERY);
		sendMessage(this.node.getSuccessor().getNodeIP(), this.node
				.getSuccessor().getNodePort(), recoveryMsg);
		sendMessage(this.node.getPredecessor().getNodeIP(), this.node
				.getPredecessor().getNodePort(), recoveryMsg);
	}

	public void getValue(MessageInfo msg) {
		uc.getAllValue(msg);
	}

	public void handleRecoveryValue(MessageInfo msg) {
		String attribute = (String) msg.getTempPair().get("getAtr");
		if (attribute.compareTo(this.node.getNodePort()) == 0) {
			uc.insertNewPair((String) msg.getTempPair().get("getKey"),
					(String) msg.getTempPair().get("getVal"), attribute);
		} else if (attribute
				.compareTo(this.node.getPredecessor().getNodePort()) == 0) {
			uc.insertNewPair((String) msg.getTempPair().get("getKey"),
					(String) msg.getTempPair().get("getVal"), attribute);
		} else if (attribute.compareTo(this.node.getPredecessor()
				.getPredecessor().getNodePort()) == 0) {
			uc.insertNewPair((String) msg.getTempPair().get("getKey"),
					(String) msg.getTempPair().get("getVal"), attribute);
		} else
			return;
	}

	public void sendRequestMessage(NodeInfo sendToNode) {
		UpdateDynamo.failFlag = false;
		MessageInfo requestMsg = new MessageInfo();
		requestMsg.setTempNode(sendToNode);
		requestMsg.setMsgType(MessageInfo.MSG_TYPE_REQUEST);
		sendMessage(sendToNode.getNodeIP(), sendToNode.getNodePort(),
				requestMsg);
	}

	public void sendMessage(String IP, String Port, MessageInfo msg) {
		CastMessage cm = new CastMessage(IP, Port, msg);
		cm.unicast();
	}
}
