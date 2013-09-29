package edu.buffalo.cse.cse486_586.simpledht.updateprocedure;

import edu.buffalo.cse.cse486_586.simpledht.handlenodes.NodeInfo;

public class JudgeSituation {

	@SuppressWarnings("unused")
	private static final String TAG = "JudgeSituation";
	
	NodeInfo node;
	String nodeID;
	
	public JudgeSituation(NodeInfo node, String nodeID) {
		this.node = node;
		this.nodeID = nodeID;
	}
	
	public boolean isNodeSelfOfRing() {
		if (this.nodeID.compareTo(this.node.getSuccessor().getNodeID()) == 0 &&
					this.nodeID.compareTo(this.node.getPredecessor().getNodeID()) == 0) {
			return true;
		}
		else
			return false;
	}
	
	public boolean isNewNodeToJoin(String newNodeID) {
		if (newNodeID.compareTo(this.nodeID) >0 &&
				newNodeID.compareTo(this.node.getSuccessor().getNodeID()) < 0) {
			return true;
		}
		else if (newNodeID.compareTo(this.nodeID) > 0 &&
				newNodeID.compareTo(this.node.getSuccessor().getNodeID()) > 0 &&
				this.nodeID.compareTo(this.node.getSuccessor().getNodeID()) > 0) {
			return true;
		}
		else 
			return false;
	}
	
	public boolean isMulNodeRing(String newHashKey) {
		if (newHashKey.compareTo(this.nodeID) < 0 &&
				newHashKey.compareTo(this.node.getPredecessor().getNodeID()) > 0) {
			return true;
		}
		else if (newHashKey.compareTo(this.nodeID) < 0 &&
				newHashKey.compareTo(this.node.getPredecessor().getNodeID()) < 0 &&
				this.nodeID.compareTo(this.node.getPredecessor().getNodeID()) < 0) {
			return true;
		}
		else if (newHashKey.compareTo(this.nodeID) > 0 &&
				newHashKey.compareTo(this.node.getPredecessor().getNodeID()) > 0 &&
				this.nodeID.compareTo(this.node.getPredecessor().getNodeID()) < 0) {
			return true;
		}
		else {
			return false;
		}

	}
	
}
