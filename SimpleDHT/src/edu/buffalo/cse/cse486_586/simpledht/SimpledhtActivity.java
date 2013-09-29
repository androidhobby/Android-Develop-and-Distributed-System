package edu.buffalo.cse.cse486_586.simpledht;

import java.security.NoSuchAlgorithmException;

import edu.buffalo.cse.cse486_586.simpledht.button.Dump;
import edu.buffalo.cse.cse486_586.simpledht.button.Test;
import edu.buffalo.cse.cse486_586.simpledht.castmessage.AppServer;
import edu.buffalo.cse.cse486_586.simpledht.handlenodes.GetHashKey;
import edu.buffalo.cse.cse486_586.simpledht.updateprocedure.UpdateDHT;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SimpledhtActivity extends Activity {
    /** Called when the activity is first created. */

	@SuppressWarnings("unused")
	private static final String TAG = "SimpledhtActivity";
	
	@SuppressWarnings("unused")
	private TextView tv_1 = null;
	private TextView tv_2 = null;
	private Button b_test = null;
	private Button b_dump = null;
	
	private String history = null;
	private String content = null;
	
	private String IP = "10.0.2.2";
	private String portStr = null;
	private String nodeId = null;
	
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
        b_test = (Button)findViewById(R.id.b_test);
        b_dump = (Button)findViewById(R.id.b_dump);
        
        portStr = getEmulatorNumber();
        try {
        	GetHashKey getHashKey = new GetHashKey();
			nodeId = getHashKey.genHash(portStr);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        
        final UpdateDHT ud = new UpdateDHT(nodeId, IP, portStr, getContentResolver());
        
        AppServer appserver = new AppServer(this.myHandler, ud);
        appserver.start();
        
        ud.initNodeInfo();
        ud.nodeToJoinRing();
        
        b_test.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Test test = new Test(ud);
				test.start();
			}
		});
        
        b_dump.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dump dump = new Dump(ud);
				dump.start();
			}
		});
    }
        
}