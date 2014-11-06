package com.example.homecartv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

public class BluetoothService {
	private static final String NAME="BluetoothCar";
	private static final UUID MY_UUID=	UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard SPP UUID
	
	public static final int STATE_NONE =0;
	public static final int STATE_LISTEN =1;
	public static final int STATE_CONNECTING=2;
	public static final int STATE_CONNECTED =3;
	
	private BluetoothAdapter adapter;
	private Handler handler;
	private AcceptThread acceptThread;
	private ConnectThread connectThread;
	private ConnectedThread connectedThread;
	private int state;
	
	public BluetoothService(Context context,Handler handler) {
		this.adapter=BluetoothAdapter.getDefaultAdapter();
		this.state =STATE_NONE;
		this.handler=handler;
	}
	
	private synchronized void setState(int state) {
		this.state=state;
		handler.obtainMessage(
			MainActivity.MSG_STATE_CHANGE,state,-1).sendToTarget();
	}
	
	public synchronized int getState() {
		return state;
	}
	
	public synchronized void start() {
		if (connectThread!=null) {
			connectThread.cancel() ;connectThread=null;}
		if (connectedThread!=null) {
			connectedThread.cancel() ;connectedThread=null;}
		if (acceptThread==null) {
			acceptThread=new AcceptThread();
			acceptThread.start();
		}
		setState(STATE_LISTEN);
	}
	
	public synchronized void connect(BluetoothDevice device) {
		if (state==STATE_CONNECTING) {
			if (connectThread!=null) {
				connectThread.cancel() ;connectThread=null;}
		}
		if (connectedThread!=null) {
			connectedThread.cancel() ;connectedThread=null;}
		connectThread=new ConnectThread(device);
		connectThread.start();
		setState(STATE_CONNECTING);
	}
	
	public synchronized void connected(BluetoothSocket socket,
		BluetoothDevice device) {
		if (connectThread!=null) {
			connectThread.cancel() ;connectThread=null;}
		if (connectedThread!=null) {
			connectedThread.cancel() ;connectedThread=null;}
		if (acceptThread!=null) {
			acceptThread.cancel() ;acceptThread=null;}
		connectedThread=new ConnectedThread(socket);
		connectedThread.start();
		setState(STATE_CONNECTED);
	}
	
	public synchronized void stop() {
		if (connectThread!=null) {
			connectThread.cancel() ;connectThread=null;}
		if (connectedThread!=null) {
			connectedThread.cancel() ;connectedThread=null;}
		if (acceptThread!=null) {
			acceptThread.cancel() ;acceptThread=null;}
		setState(STATE_NONE);
	}
	
	public void write(byte[] out) {
		ConnectedThread r;
		synchronized (this) {
			if (state!=STATE_CONNECTED) return;
			r=connectedThread;
		}
		r.write(out);
	}
	
	private class AcceptThread extends Thread {
		private BluetoothServerSocket serverSocket;
		public AcceptThread() {
			try {
				serverSocket=adapter.
					listenUsingRfcommWithServiceRecord(NAME,MY_UUID);
			} catch (IOException e) {
			}
		}
		
		public void run() {
			BluetoothSocket socket=null;
			while (state!=STATE_CONNECTED) {
				try {
					socket=serverSocket.accept();
				} catch (IOException e) {
					break;
				}
				if (socket!=null) {
					synchronized (BluetoothService.this) {
						switch (state) {
						case STATE_LISTEN:
						case STATE_CONNECTING:
							connected(socket,socket.getRemoteDevice());
							break;
						case STATE_NONE:
						case STATE_CONNECTED:
							try {
								socket.close();
							} catch (IOException e) {
							}
							break;
						}
					}
				}
			}
		}
		
		public void cancel() {
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
		}
	}
	
	private class ConnectThread extends Thread {
		private BluetoothDevice device;
		private BluetoothSocket socket;
		public ConnectThread(BluetoothDevice device) {
			try {
				this.device=device;
				this.socket=device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
			}
		}
		
		public void run() {
			adapter.cancelDiscovery();
			try {
				socket.connect();
			} catch (IOException e) {
				setState(STATE_LISTEN);
				try {
					socket.close();
				} catch (IOException e2) {
				}
				BluetoothService.this.start();
				return;
			}
			synchronized (BluetoothService.this) {
				connectThread=null;
			}
			connected(socket,device);
		}
		
		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
	
	private class ConnectedThread extends Thread {
		private BluetoothSocket socket;
		private InputStream input;
		private OutputStream output;
		
		public ConnectedThread(BluetoothSocket socket) {
			try {
				this.socket=socket;
				this.input =socket.getInputStream();
				this.output=socket.getOutputStream();
			} catch (IOException e) {
			}
		}
		
		public void run() {
			byte[] buf=new byte[1024];
			int bytes;
			while (true) {
				try {
					bytes=input.read(buf);
					handler.obtainMessage(MainActivity.MSG_READ,
						bytes,-1,buf).sendToTarget();
				} catch (IOException e) {
					setState(STATE_LISTEN);
					break;
				}
			}
		}
		
		// 湲곗엯
		public void write(byte[] buf) {
			try {
				output.write(buf);
			} catch (IOException e) {
			}
		}
		
		// 痍⑥냼
		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}