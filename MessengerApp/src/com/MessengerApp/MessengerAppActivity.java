package com.MessengerApp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MessengerAppActivity extends Activity 
{
    private TextView Content = null;
    private EditText Input = null;
    private Button Send = null;
    
    private String IpAddr = "10.0.2.2";
    private int tcp_port = 8001;
    
    private Handler MyHandler = new Handler()
    {
    	@Override
    	public void handleMessage(Message Msg)
    	{
    		String content = Msg.getData().getString("content");
    		Content.setText(content);
    	}
    };

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Send = (Button)findViewById(R.id.send);
        Content = (TextView)findViewById(R.id.content);
        Input = (EditText)findViewById(R.id.input);
        
        ServerThread serverthread = new ServerThread(this.MyHandler);
        serverthread.start();
        
        Send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendMsg(Input.getText().toString());
			}
		});
    }
    
    
    public void sendMsg(String msg)
    {
    	ClientThread clientthread = new ClientThread(IpAddr, tcp_port, msg, MyHandler);
    	clientthread.start();
    }
}