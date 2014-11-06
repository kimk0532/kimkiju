package com.example.homecartv;

import java.io.IOException;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
		
		wb.getSettings().setJavaScriptEnabled(true);
		wb.getSettings().setPluginState(PluginState.ON);
		wb.getSettings().setSupportMultipleWindows(true);
		wb.loadUrl("http://192.168.0.41:8081/");
		
		wb.setWebChromeClient(new WebChromeClient());
		wb.setWebViewClient(new WebViewClient());
		
		btAdapter=BluetoothAdapter.getDefaultAdapter();
		
		up.setOnTouchListener(touchButton());
		down.setOnTouchListener(touchButton());
		left.setOnTouchListener(touchButton());
		right.setOnTouchListener(touchButton());
	}

	private OnTouchListener touchButton() {
		return new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent mEvent) {
				if(mEvent.getAction()==MotionEvent.ACTION_UP){ // if left out right buttons are up
					String message="c";
					btService.write(message.getBytes());
					System.out.println("c");
				}
				else{
					if(v.getId()==up.getId()){ // left button is down
						String message="f";
						btService.write(message.getBytes());
						System.out.println("f");
					}
					else if(v.getId()==down.getId()){ // right button is down
						String message="b";
						btService.write(message.getBytes());
					}
					else if(v.getId()==left.getId()){
						String message="l";
						btService.write(message.getBytes());
					}
					else if(v.getId()==right.getId()){
						String message="r";
						btService.write(message.getBytes());
					}
				}
				return false;
			}
		};
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
	
	@Override
	public synchronized void onResume() {
		super.onResume();
		if (btService!=null) {
			if (btService.getState() ==BluetoothService.STATE_NONE) {
				btService.start();
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (btService!=null) btService.stop();
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
	
	public void onActivityResult(int requestCode,int resultCode,Intent data) {
		switch (requestCode) {
		case RQ_CONNECT_DEVICE:
			if (resultCode==Activity.RESULT_OK) {
				String address=data.getExtras().
					getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				
				BluetoothDevice device=btAdapter.getRemoteDevice(address);
				btService.connect(device);
			}
			break;
		case RQ_ENABLE_BT:
			if (resultCode==Activity.RESULT_OK) {
				btService=new BluetoothService(this,handler);
			} else {
				Toast.makeText(this,"Bluetooth",Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}
	
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
		MenuItem item0=menu.add(0,0,0,"BluetoothMenu");
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
