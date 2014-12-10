package test;

import java.io.*;
import java.net.*;
import java.util.*;

/* Chatting Server Thread */
public class ChatServerThread extends Thread {
	private static List<ChatServerThread> threads=
		new ArrayList<ChatServerThread>(); // Thread
	private Socket socket; // Socket
	
	// Constructor
	public ChatServerThread(Socket socket) {
		super();
		this.socket=socket;
		threads.add(this);
	}
	
	// process
	public void run() {
		InputStream in =null;
		String message;
		int size;
		byte[] w=new byte[10240];
		try {
			// stream
			in =socket.getInputStream();
			while(true) {
				try {
					// wait for receiving
					size=in.read(w);
					
					// exit
					if (size<=0) throw new IOException();
					
					// read
					message=new String(w,0,size,"UTF8");
					
					// send a message for all
					sendMessageAll(message);
					System.err.print(message+" send!");
				} catch (IOException e) {
					socket.close();
					threads.remove(this);
					return;
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	// send a message for all
	public void sendMessageAll(String message) {
		ChatServerThread thread;
		for (int i=0;i<threads.size();i++) {
			thread=(ChatServerThread)threads.get(i);
			if (thread.isAlive()) thread.sendMessage(this,message);
		}
		System.out.println(message);
	}
	
	// send a message
	public void sendMessage(ChatServerThread talker,String message){
		try {
			OutputStream out=socket.getOutputStream();
			byte[] w=message.getBytes("UTF8");
			out.write(w);
			out.flush();
		} catch (IOException e) {
		}
	}
}