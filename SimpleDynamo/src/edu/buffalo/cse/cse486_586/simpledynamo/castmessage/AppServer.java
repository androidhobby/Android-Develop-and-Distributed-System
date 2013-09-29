/**
 * 
 */
package edu.buffalo.cse.cse486_586.simpledynamo.castmessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import edu.buffalo.cse.cse486_586.simpledynamo.deliver.DeliverMessage;
import edu.buffalo.cse.cse486_586.simpledynamo.information.MessageInfo;
import edu.buffalo.cse.cse486_586.simpledynamo.update.UpdateDynamo;

import android.os.Handler;
import android.util.Log;

/**
 * @author Fang Wu April 20, 2012
 * 
 */
public class AppServer extends Thread {

	private static final String TAG = "AppServer";

	private ServerSocket serversocket = null;
	private Handler myHandler = null;
	private ObjectInputStream is = null;
	private MessageInfo msg = null;
	private UpdateDynamo ud = null;

	public AppServer(Handler handler, UpdateDynamo ud) {
		this.myHandler = handler;
		this.ud = ud;
	}

	public void run() {
		try {
			serversocket = new ServerSocket();
			serversocket.bind(new InetSocketAddress("10.0.2.15", 10000));
		} catch (IOException e) {
			Log.d(TAG, "IOException");
			e.printStackTrace();
		}
		int type = -1;
		while (true) {
			try {
				Socket clientsocket = serversocket.accept();
				is = new ObjectInputStream(clientsocket.getInputStream());
				msg = (MessageInfo) is.readObject();
				is.close();
				type = msg.getMsgType();
				Log.d(TAG, "Get value!");
				switch (type) {
				case MessageInfo.MSG_TYPE_REQUEST:
					Log.d(TAG, "Request");
					break;
				case MessageInfo.MSG_TYPE_JUDGE:
					Log.d(TAG, "Judge");
					ud.judgeContentProvider(msg);
					break;
				case MessageInfo.MSG_TYPE_INSERT:
					Log.d(TAG, "Insert key");
					ud.insertRequest(msg);
					break;
				case MessageInfo.MSG_TYPE_INSERTTORING:
					Log.d(TAG, "Insert key too");
					ud.insertTooMsg(msg);
					break;
				case MessageInfo.MSG_TYPE_REPLICA:
					Log.d(TAG, "Insert replica key");
					ud.insertReplica(msg);
					break;
				case MessageInfo.MSG_TYPE_QUERY:
					Log.d(TAG, "Query key");
					ud.queryRequest(msg);
					break;
				case MessageInfo.MSG_TYPE_QUERYTOO:
					Log.d(TAG, "Query key too");
					ud.queryTooRequest(msg);
					break;
				case MessageInfo.MSG_TYPE_QUERIED:
					Log.d(TAG, "Queried key");
					ud.displayQueryResult(msg);
					break;
				case MessageInfo.MSG_TYPE_RECOVERY:
					Log.d(TAG, "Recovery value");
					ud.getValue(msg);
					break;
				case MessageInfo.MSG_TYPE_RECOVAL:
					Log.d(TAG, "Get value");
					ud.handleRecoveryValue(msg);
					break;
				case MessageInfo.MSG_TYPE_FAILNODE:
					Log.d(TAG, "Get value");
					ud.handleRecovery();
					break;
				case MessageInfo.MSG_TYPE_NOTFAILNODE:
					Log.d(TAG, "Not fail node");
					break;
				case MessageInfo.MSG_TYPE_DELIVER:
					Log.d(TAG, "Display Message");
					String message = "<"
							+ (String) msg.getTempPair().get("queryKey") + ","
							+ (String) msg.getTempPair().get("queryVal") + ">";
					DeliverMessage dm = new DeliverMessage(myHandler, message);
					dm.deliver();
					break;
				default:
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
