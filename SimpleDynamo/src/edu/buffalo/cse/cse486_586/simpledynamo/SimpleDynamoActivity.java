package edu.buffalo.cse.cse486_586.simpledynamo;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import edu.buffalo.cse.cse486_586.simpledynamo.button.ButtonDump;
import edu.buffalo.cse.cse486_586.simpledynamo.button.ButtonGet;
import edu.buffalo.cse.cse486_586.simpledynamo.button.ButtonPut1;
import edu.buffalo.cse.cse486_586.simpledynamo.button.ButtonPut2;
import edu.buffalo.cse.cse486_586.simpledynamo.button.ButtonPut3;
import edu.buffalo.cse.cse486_586.simpledynamo.castmessage.AppServer;
import edu.buffalo.cse.cse486_586.simpledynamo.gethashkey.GetHashKey;
import edu.buffalo.cse.cse486_586.simpledynamo.information.NodeInfo;
import edu.buffalo.cse.cse486_586.simpledynamo.update.UpdateContent;
import edu.buffalo.cse.cse486_586.simpledynamo.update.UpdateDynamo;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Fang Wu
 * April 20, 2012
 *
 */

public class SimpleDynamoActivity extends Activity {
    /** Called when the activity is first created. */
	
	@SuppressWarnings("unused")
	private static final String TAG = "SimpleDynamoActivity";
	
	@SuppressWarnings("unused")
	private TextView tv_1 = null;
	private TextView tv_2 = null;
	private Button b_put1 = null;
	private Button b_put2 = null;
	private Button b_put3 = null;
	private Button b_get = null;
	private Button b_dump = null;
	
	
	private String history = null;
	private String content = null;
	
	private String IP = "10.0.2.2";
	private String portStr = null;
	private String nodeId = null;
	
	private ContentResolver cr = null;
	private NodeInfo node = null;
    HashMap<String, Object> nodeList = new HashMap<String, Object>();

	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			content = msg.getData().getString("context");
			if (history == null) {
				history = content;
				tv_2.setText(history);
			}
			else {
				history = content + "\n" + history;
				tv_2.setText(history);
			}
		}
	};
	
	private String getEmulatorNumber() {
		TelephonyManager tel = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE); 
		return tel.getLine1Number().substring(tel.getLine1Number().length()-4);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tv_1 = (TextView)findViewById(R.id.tv_1);
        tv_2 = (TextView)findViewById(R.id.tv_2);
        b_put1 = (Button)findViewById(R.id.b_put1);
        b_put2 = (Button)findViewById(R.id.b_put2);
        b_put3 = (Button)findViewById(R.id.b_put3);
        b_get = (Button)findViewById(R.id.b_get);
        b_dump = (Button)findViewById(R.id.b_dump);
        
        portStr = getEmulatorNumber();
        GetHashKey getHashKey = new GetHashKey();
        try {
			nodeId = getHashKey.genHash(portStr);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        cr = getContentResolver();
        node = new NodeInfo(nodeId, IP, portStr, null, null, null);
        
        final UpdateContent uc = new UpdateContent(this.cr, this.node);
        
        final UpdateDynamo ud = new UpdateDynamo(uc, this.node);
        
        AppServer appserver = new AppServer(this.myHandler, ud);
        appserver.start();
        
        ud.initRing();
        ud.initNodeInfo();
        ud.judgeNode();
        
        b_put1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ButtonPut1 B_Put1 = new ButtonPut1(ud);
				B_Put1.start();
			}
		});
        
        b_put2.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ButtonPut2 B_Put2 = new ButtonPut2(ud);
				B_Put2.start();
			}
		});
        
        b_put3.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ButtonPut3 B_Put3 = new ButtonPut3(ud);
				B_Put3.start();
			}
		});
        
        b_get.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ButtonGet B_Get = new ButtonGet(ud);
				B_Get.start();
			}
		});
        
        b_dump.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ButtonDump B_Dump = new ButtonDump(uc);
				B_Dump.start();
			}
		});
    }
}