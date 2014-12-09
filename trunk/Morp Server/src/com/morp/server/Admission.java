package com.morp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Admission extends Thread {

	public ServerSocket serverSocket;
	public List<Channel> channels;

	public Admission(ServerSocket serverSocket, List<Channel> channels) {
		this.serverSocket = serverSocket;
		this.channels = channels;
	}

	@Override
	public void run() {
		while (!serverSocket.isClosed() && serverSocket != null) {
			try {
				// 새 연결 요청이 들어올 때까지 대기
				Socket socket = serverSocket.accept();
				System.out.println("con");
				// 접속 정원에 맞을 때만 접속 가능
				if (Statistics.available()) {

					User user = new User();

					Connection connection = new Connection(user, socket,
							channels);
					Thread thread = new Thread(connection);
					thread.setDaemon(true);
					connection.thread = thread;

					thread.start();

					Statistics.count++;

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
