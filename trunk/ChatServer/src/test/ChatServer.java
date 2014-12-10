package test;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

	// start
	public void start(int port) {
		ServerSocket server; // �꽌踰꾩냼耳�.
		Socket socket; // �냼耳�.
		ChatServerThread thread;

		try {
			server = new ServerSocket(port);
			System.err.println("梨꾪똿�꽌踰� �떆�옉 :" + port);
			while (true) {
				try {
					// wait for connecting
					socket = server.accept();
					System.err.println("socket accepted! ");
					// chatting server thread start
					thread = new ChatServerThread(socket);
					thread.start();
				} catch (IOException e) {
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	// main
	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.start(7777);
	}
}