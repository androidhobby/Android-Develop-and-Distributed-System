package com.cse586.fwu8.tcomessenger;

import java.util.UUID;

import com.cse586.fwu8.castmessage.AppServer;
import com.cse586.fwu8.castmessage.CastMessage;
import com.cse586.fwu8.message.MessageInfo;
import com.cse586.fwu8.message.Process;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MulticastMessengerActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String TAG = "MainActivity";
	TextView tv_1 = null;
	TextView tv_2 = null;
	EditText et = null;
	Button b_send = null;
	Button b_test1 = null;
	Button b_test2 = null;
	
	String history = null;
	String content = null;
	
	String portStr = null;
	String IP = "10.0.2.2";
	int portNumber = 0;
	int processID = 0;
	int count = 0;
	int[] vector = new int[5];
	Process[] p = new Process[5];
	int counter_2 = 0;    //for test2
	
	MessageInfo msginfo = null;
	
	private Handler myHandler = new Handler() {
		public void handleMessage(Message Msg) {
			content = Msg.getData().getString("content");
			if (history == null) {
				history = content;
				tv_2.setText(history);
			}
			else {
				history = history + "\n" + content;
				tv_2.setText(history);
			}
		}
	};
	
	public String getPortNumber() {
		TelephonyManager tel = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE); 
		return tel.getLine1Number().substring(tel.getLine1Number().length()-4);
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tv_1 = (TextView)findViewById(R.id.tv_1);
        tv_2 = (TextView)findViewById(R.id.tv_2);
        et = (EditText)findViewById(R.id.et);
        b_send = (Button)findViewById(R.id.b_send);
        b_test1 = (Button)findViewById(R.id.b_test1);
        b_test2 = (Button)findViewById(R.id.b_test2);
        
        portStr = getPortNumber();
        portNumber = Integer.parseInt(portStr);
        processID = getProcessID(portNumber);
        p[processID] = new Process();
        for (int i = 0; i < vector.length; i++) {
        	vector[i] = 0;
        }

        AppServer appserver = new AppServer(this.myHandler, this.processID);
        appserver.start();
        
        b_send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				msginfo = new MessageInfo();
				//p[processID] = new Process();
				String input = et.getText().toString();
				msginfo.setMessage(input);
				String id = UUID.randomUUID().toString();
				msginfo.setUid(id);
				msginfo.setMsgType(MessageInfo.NORMAL_MSG);
				msginfo.setMsgFrom(processID);
				count++;
				p[processID].setLocalCausalCount(count);
				//msginfo.setTempVector(p[processID].getTimestampVector());
				//vector[processID] = p[processID].getLocalCausalCount();
				//msginfo.setTimestampVector(vector);
				//msginfo.tempVector[processID] = p[processID].getLocalCausalCount(); 
				CastMessage cm = new CastMessage(IP, msginfo);
				cm.multicast();
			}
		});
        
        b_test1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Thread test1 = new Thread(new Test1());
				test1.start();
			}
		});
        
        b_test2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Thread test2 = new Thread(new Test2());
				test2.start();
			}
		});
    }
    
    public int getProcessID(int num) {
    	switch(num) {
    	case 5554:
    		return 0;
    	case 5556:
    		return 1;
    	case 5558:
    		return 2;
    	case 5560:
    		return 3;
    	case 5562:
    		return 4;
    	default:
    		return -1;	
    	}
    }
    
    public class Test1 implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int counter = 0, j = 0;
			for (int i = 0; i < 5; i++) {
				MessageInfo m = new MessageInfo();
				String msg = portStr + ":" + counter;
				String uid = UUID.randomUUID().toString();
				m.setUid(uid);
				m.setMessage(msg);
				m.setMsgType(MessageInfo.NORMAL_MSG);
				m.setMsgFrom(processID);
				p[processID] = new Process();
				j++;
				p[processID].setLocalCausalCount(j);
				vector[processID] = p[processID].getLocalCausalCount();
				m.setTimestampVector(vector);
				//m.tempVector[id] = p[id].getLocalCausalCount();
				CastMessage cm = new CastMessage("10.0.2.2", m);
				cm.multicast();
				counter++;
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
    
    public class Test2 implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int i = 0;
			MessageInfo m = new MessageInfo();
			String msg = portStr + ":" + counter_2;
			//counter++;
			String uid = UUID.randomUUID().toString();
			m.setMsgType(MessageInfo.NORMAL_MSG);
			m.setMessage(msg);
			m.setUid(uid);
			m.setMsgFrom(processID);
			p[processID] = new Process();
			i++;
			p[processID].setLocalCausalCount(i);
			vector[processID] = p[processID].getLocalCausalCount();
			m.setTimestampVector(vector);
			counter_2 ++;
			CastMessage cm = new CastMessage("10.0.2.2", m);
			cm.multicast();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    	
    }
}