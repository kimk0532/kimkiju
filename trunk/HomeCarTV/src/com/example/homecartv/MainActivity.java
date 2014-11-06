package com.example.homecartv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static final int MSG_STATE_CHANGE = 1;
	public static final int MSG_READ = 2;
	private static final int RQ_CONNECT_DEVICE=1;
	private static final int RQ_ENABLE_BT =2;
	private BluetoothAdapter btAdapter;
	private BluetoothService btService;
	
	TextView stateView;
	TextView recView;
	Button up;
	Button down;
	Button left;
	Button right;
	WebView wb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		stateView = (TextView)findViewById(R.id.stateView);
		recView = (TextView)findViewById(R.id.recView);
		up = (Button)findViewById(R.id.btnup);
		down = (Button)findViewById(R.id.btndown);
		left = (Button)findViewById(R.id.btnleft);
		right = (Button)findViewById(R.id.btnright);
		wb = (WebView)findViewById(R.id.wb);
		
		up.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String message="u";
				btService.write(message.getBytes());
			}
		});
		down.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String message="d";
				btService.write(message.getBytes());
			}
		});
		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String message="l";
				btService.write(message.getBytes());
			}
		});
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String message="r";
				btService.write(message.getBytes());
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!btAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent,RQ_ENABLE_BT);
		} else {
			if (btService==null) 
				btService =	new BluetoothService(this,handler);
		}
	}
	
	private final Handler handler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_STATE_CHANGE:
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					stateView.setText("connected");break;
				case BluetoothService.STATE_CONNECTING:
					stateView.setText("connecting");break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					stateView.setText("disconnected");break;
				}
				break;
			case MSG_READ:
					byte[] readBuf=(byte[]) msg.obj;
					RecSub(new String(readBuf,0,msg.arg1));
					break;
			}
		}
	};
	
	private void RecSub(final String str) {      
		handler.post(new Runnable(){
			public void run() {
				recView.setText(str);
				if(str.equals("X")){
					Builder builder = new  AlertDialog.Builder(MainActivity.this);
					builder.setMessage("Warning!Warning!Warning!").setPositiveButton("", null);
					builder.show();
				}
				else if(str.equals("b")){
					
				}
			}
		});
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem item0=menu.add(0,0,0,"아니!");
		item0.setIcon(android.R.drawable.ic_search_category_default);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {			
		case 0:
			Intent serverIntent=new Intent(this,DeviceListActivity.class);
			startActivityForResult(serverIntent,RQ_CONNECT_DEVICE);
			return true;
		}
		return false;
	}
}
