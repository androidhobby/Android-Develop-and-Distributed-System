package edu.buffalo.cse.cse486_586.simpledht.handlenodes;

import java.io.Serializable;

public class NodeInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final String TAG = "RingNodeInfo";

	private String nodeID = null;
	private String nodeIP = null;
	private String nodePort = null;
	private NodeInfo predecessor = null;
	private NodeInfo successor = null;
	
	public NodeInfo() { }
	
	public NodeInfo(String nodeID, String nodeIP, String nodePort,
						NodeInfo pre, NodeInfo suc) {
		this.nodeID = nodeID;
		this.nodeIP = nodeIP;
		this.nodePort = nodePort;
		this.predecessor = pre;
		this.successor = suc;
	}
	
	public void setNodeID(String ID) {
		this.nodeID = ID;
	}
	
	public String getNodeID() {
		return nodeID;
	}
	
	public void setNodeIP(String IP) {
		this.nodeIP = IP;
	}
	
	public String getNodeIP() {
		return nodeIP;
	}
	
	public void setNodePort(String Port) {
		this.nodePort = Port;
	}
	
	public String getNodePort() {
		return nodePort;
	}
	
	public void setPredecessor(NodeInfo pre) {
		this.predecessor = pre;
	}
	
	public NodeInfo getPredecessor() {
		return predecessor;
	}
	
	public void setSuccessor(NodeInfo suc) {
		this.successor = suc;
	}
	
	public NodeInfo getSuccessor() {
		return successor;
	}
 }
