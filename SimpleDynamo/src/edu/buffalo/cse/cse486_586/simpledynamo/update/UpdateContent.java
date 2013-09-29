/**
 * 
 */
package edu.buffalo.cse.cse486_586.simpledynamo.update;

import java.util.HashMap;

import edu.buffalo.cse.cse486_586.simpledynamo.castmessage.CastMessage;
import edu.buffalo.cse.cse486_586.simpledynamo.information.MessageInfo;
import edu.buffalo.cse.cse486_586.simpledynamo.information.NodeInfo;
import edu.buffalo.cse.cse486_586.simpledynamo.provider.MyContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

/**
 * @author Fang Wu April 20, 2012
 * 
 */
public class UpdateContent {

	@SuppressWarnings("unused")
	private static final String TAG = "UpdateContent";

	private ContentResolver contentResolver = null;
	private NodeInfo node = null;

	public UpdateContent(ContentResolver cr, NodeInfo node) {
		this.contentResolver = cr;
		this.node = node;
	}

	public void insertNewPair(String key, String value, String attribute) {
		ContentValues keyValueToInsert = new ContentValues();
		keyValueToInsert.put(MyContentProvider.MyContent.KEY, key);
		keyValueToInsert.put(MyContentProvider.MyContent.VALUE, value);
		keyValueToInsert.put(MyContentProvider.MyContent.ATTRIBUTE, attribute);
		contentResolver.insert(MyContentProvider.MyContent.CONTENT_URI,
				keyValueToInsert);
	}

	// some codes learn from http://developer.android.com in this function
	public boolean isNewPairExist(String key) {
		boolean existFlag = false;
		String projection[] = new String[] { MyContentProvider.MyContent.ID,
				MyContentProvider.MyContent.KEY,
				MyContentProvider.MyContent.VALUE,
				MyContentProvider.MyContent.ATTRIBUTE };
		Cursor resultCursor = contentResolver.query(
				MyContentProvider.MyContent.CONTENT_URI, projection,
				MyContentProvider.MyContent.KEY + "=" + key, null, null);
		if (resultCursor != null) {
			existFlag = true;
			resultCursor.close();
		}
		return existFlag;
	}

	// some codes learn from http://developer.android.com in this function
	public boolean isContentProvierNull() {
		String projection[] = new String[] { MyContentProvider.MyContent.ID,
				MyContentProvider.MyContent.KEY,
				MyContentProvider.MyContent.VALUE,
				MyContentProvider.MyContent.ATTRIBUTE };
		Cursor cursorResult = contentResolver.query(
				MyContentProvider.MyContent.CONTENT_URI, projection, null,
				null, null);
		if (cursorResult != null) {
			return true;
		} else
			return false;
	}

	// some codes learn from http://developer.android.com in this function
	public String[] queryOnePair(String key) {
		String projection[] = new String[] { MyContentProvider.MyContent.ID,
				MyContentProvider.MyContent.KEY,
				MyContentProvider.MyContent.VALUE,
				MyContentProvider.MyContent.ATTRIBUTE };
		Cursor resultCursor = contentResolver.query(
				MyContentProvider.MyContent.CONTENT_URI, projection,
				MyContentProvider.MyContent.KEY + "=" + key, null, null);
		if (resultCursor != null) {
			String pair[] = new String[] { resultCursor.getString(1),
					resultCursor.getString(2), resultCursor.getString(3) };
			resultCursor.close();
			return pair;
		}
		return null;
	}

	// some codes learn from http://developer.android.com in this function
	public void displayLocalPairs() {
		String projection[] = new String[] { MyContentProvider.MyContent.ID,
				MyContentProvider.MyContent.KEY,
				MyContentProvider.MyContent.VALUE,
				MyContentProvider.MyContent.ATTRIBUTE };
		Cursor resultCursor = contentResolver.query(
				MyContentProvider.MyContent.CONTENT_URI, projection, null,
				null, null);
		if (resultCursor != null) {
			while (true) {
				MessageInfo displayMsg = new MessageInfo();
				HashMap<String, Object> pair = new HashMap<String, Object>();
				pair.put("queryKey", resultCursor.getString(1));
				pair.put("queryVal", resultCursor.getString(2));
				displayMsg.setTempPair(pair);
				displayMsg.setTempNode(this.node);
				displayMsg.setMsgType(MessageInfo.MSG_TYPE_DELIVER);
				sendMessage(this.node.getNodeIP(), this.node.getNodePort(),
						displayMsg);
				if (!resultCursor.moveToNext()) {
					resultCursor.close();
					break;
				}
			}
		}
	}

	// some codes learn from http://developer.android.com in this function
	public void getAllValue(MessageInfo msg) {
		String projection[] = new String[] { MyContentProvider.MyContent.ID,
				MyContentProvider.MyContent.KEY,
				MyContentProvider.MyContent.VALUE,
				MyContentProvider.MyContent.ATTRIBUTE };
		Cursor resultCursor = contentResolver.query(
				MyContentProvider.MyContent.CONTENT_URI, projection, null,
				null, null);
		if (resultCursor != null) {
			while (true) {
				MessageInfo valMsg = new MessageInfo();
				valMsg.setMsgType(MessageInfo.MSG_TYPE_RECOVAL);
				valMsg.setTempNode(this.node);
				HashMap<String, Object> pair = new HashMap<String, Object>();
				pair.put("getKey", resultCursor.getString(1));
				pair.put("getVal", resultCursor.getString(2));
				pair.put("getAtr", resultCursor.getString(3));
				valMsg.setTempPair(pair);
				sendMessage(msg.getTempNode().getNodeIP(), msg.getTempNode()
						.getNodePort(), valMsg);
				if(!resultCursor.moveToNext()) {
					resultCursor.close();
					break;
				}
			}
		}
	}

	public void sendMessage(String IP, String Port, MessageInfo msg) {
		CastMessage cm = new CastMessage(IP, Port, msg);
		cm.unicast();
	}
}
