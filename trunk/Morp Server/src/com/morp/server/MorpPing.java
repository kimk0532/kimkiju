package com.morp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MorpPing {

	public static final int PORT = 15464;
	public static SimpleDateFormat dateFormat;

	static {
		dateFormat = new SimpleDateFormat("[hh:mm:ss]");
	}

	public List<PingConnection> connections;
	public ServerSocket server;
	public ConnectionReceiver connectionReceiver;
	public PingSender pingSender;

	public MorpPing() {
		if (initialize()) {
			log("초기화 완료");
			pingSender.start();
			connectionReceiver.start();

		} else {
			log("서버를 초기화할 수 없습니다.");
		}
	}

	private boolean initialize() {
		try {

			server = new ServerSocket(PORT);
			connections = new LinkedList<PingConnection>();
			connectionReceiver = new ConnectionReceiver(server, connections);
			pingSender = new PingSender(connections);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static void log(Object message) {
		System.out.println(getTime() + "" + message);
	}

	public static String getTime() {
		return dateFormat.format(new Date());
	}

	public static class ConnectionReceiver extends Thread {

		public List<PingConnection> connectionList;
		public ServerSocket serverSocket;

		public ConnectionReceiver(ServerSocket serverSocket,
				List<PingConnection> connectionList) {

			this.serverSocket = serverSocket;
			this.connectionList = connectionList;

		}

		@Override
		public void run() {

			while (true) {
				try {

					Socket connection = serverSocket.accept();
					log(connection.getInetAddress() + "["
							+ connection.getPort() + "]"
							+ " 에서 새 연결 요청을 받았습니다.");
					synchronized (connectionList) {
						connectionList.add(new PingConnection(connection));
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

	}

	public static class PingSender extends Thread {
		public List<PingConnection> connectionList;
		public List<Integer> invalidList;

		public PingSender(List<PingConnection> connectionList) {
			this.connectionList = connectionList;
			invalidList = new ArrayList<Integer>();

		}

		@Override
		public void run() {
			while (true) {
				long delta = System.currentTimeMillis();
				synchronized (connectionList) {

					invalidList.removeAll(invalidList);

					for (PingConnection ping : connectionList) {
						if ((delta - ping.deltaTime) > 1000) {
							ping.deltaTime = delta;
							ping.sendUTF("p%" + delta + "%+\n");

							if (ping.invalid) {
								invalidList.add(connectionList.indexOf(ping));
								log(ping.getInfo() + " 와 접속이 끊어졌습니다.");

							} else {

								log(ping.getInfo() + " 에 핑 보냄");
							}
						}
					}

					for (int index : invalidList) {
						connectionList.remove(index);
					}

				}

			}

		}
	}

}
