package com.example.cnclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String return_msg;
	EditText et;
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startActivity(new Intent(this,Splash.class));
		et = (EditText)findViewById(R.id.editText1);
		tv = (TextView)findViewById(R.id.textView1);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		Button btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(et.getText().toString() != null || !et.getText().toString().equals("")){
					TCPclient tcp = new TCPclient(et.getText().toString());
					tcp.run();
					Toast.makeText(getApplicationContext(), return_msg, Toast.LENGTH_SHORT).show();
					tv.setText(return_msg);
				}
			}
		});
		btn = (Button)findViewById(R.id.button2);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Map.class);
				startActivity(i);
			}
		});
	}
	
	private class TCPclient implements Runnable{
		private static final String serverIP = "121.136.100.69";
		private static final int serverPort = 9000;
		private String msg;
		
		public TCPclient(String _msg){
			this.msg = _msg;
		}
		
		@Override
		public void run() {
			try {
				InetAddress serverAddr = InetAddress.getByName(serverIP);
				Log.d("TCP","C : Conneting...");
				Socket socket = new Socket(serverAddr, serverPort);
				try {
					Log.d("TCP","C : Sending : "+msg);
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
					out.println(msg);
					Log.d("TCP", "C : Sent");
					Log.d("TCP", "C : Done");
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					return_msg = in.readLine();
					Log.d("TCP", "C : Server send to me this message --> "+return_msg);
				} catch (Exception e) {
					Log.e("TCP", "C: Error1", e);
				}finally{
					socket.close();
				}
			} catch (Exception e) {
				Log.e("TCP", "C : Error2", e);
			}
		}
	}
}
