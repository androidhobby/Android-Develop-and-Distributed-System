/**
 * 
 */
package edu.buffalo.cse.cse486_586.simpledynamo.update;

import java.util.Iterator;

import edu.buffalo.cse.cse486_586.simpledynamo.information.NodeInfo;

/**
 * @author Fang Wu
 * April 20, 2012
 *
 */
public class UpdateJudge {

	@SuppressWarnings("unused")
	private static final String TAG = "UpdateJudge";
	
	private NodeInfo localNode = null;
	private NodeInfo nodeToInsert = null;
	
	public UpdateJudge(NodeInfo node) {
		this.localNode = node;
	}
	
	public NodeInfo isMulNodeRing(String newHashKey) {
		Iterator<String> iterator = this.localNode.getNodeList().keySet().iterator();
		while (iterator.hasNext()) {
			String id = (String) iterator.next();
			this.nodeToInsert = (NodeInfo) this.localNode.getNodeList().get(id);
			if (newHashKey.compareTo(this.nodeToInsert.getNodeId()) < 0 &&
					newHashKey.compareTo(this.nodeToInsert.getPredecessor().getNodeId()) > 0) {
				return nodeToInsert;
			}
			else if (newHashKey.compareTo(this.nodeToInsert.getNodeId()) < 0 &&
					newHashKey.compareTo(this.nodeToInsert.getPredecessor().getNodeId()) < 0 &&
					this.nodeToInsert.getNodeId().compareTo(this.nodeToInsert.getPredecessor().getNodeId()) < 0) {
				return nodeToInsert;
			}
			else if (newHashKey.compareTo(this.nodeToInsert.getNodeId()) > 0 &&
					newHashKey.compareTo(this.nodeToInsert.getPredecessor().getNodeId()) > 0 &&
					this.nodeToInsert.getNodeId().compareTo(this.nodeToInsert.getPredecessor().getNodeId()) < 0) {
				return nodeToInsert;
			}
			else
				continue;
		}
		return localNode;		
	}
}
