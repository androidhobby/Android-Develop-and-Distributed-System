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
public class NodeInfo implements Serializable {

	private static final long serialVersionUID = -8034269353468516813L;

	@SuppressWarnings("unused")
	private static final String TAG = "NodeInfo";
	
	private String nodeId = null;
	private String nodeIP = null;
	private String nodePort = null;
	private NodeInfo predecessor = null;
	private NodeInfo successor = null;
	private HashMap<String, Object> nodeList = null;
	
	public NodeInfo() {}
	
	public NodeInfo(String Id, String IP, String Port, NodeInfo pre, NodeInfo suc, HashMap<String, Object> list) {
		this.setNodeID(Id);
		this.setNodeIP(IP);
		this.setNodePort(Port);
		this.setPredecessor(pre);
		this.setSuccessor(suc);
		this.setNodeList(list);
	}
	
	public String getNodeId() {
		return nodeId;
	}
	
	public void setNodeID(String Id) {
		this.nodeId = Id;
	}
	
	public void setNodeIP(String IP) {
		this.nodeIP = IP;
	}
	
	public String getNodeIP() {
		return nodeIP;
	}
	
	public String getNodePort() {
		return nodePort;
	}
	
	public void setNodePort(String Port) {
		this.nodePort = Port;
	}
	
	public NodeInfo getPredecessor() {
		return predecessor;
	}
	
	public void setPredecessor(NodeInfo pre) {
		this.predecessor = pre;
	}
	
	public NodeInfo getSuccessor() {
		return successor;
	}
	
	public void setSuccessor(NodeInfo suc) {
		this.successor = suc;
	}
	
	public HashMap<String, Object> getNodeList() {
		return nodeList;
	}
	
	public void setNodeList(HashMap<String, Object> list) {
		this.nodeList = list;
	}
}
