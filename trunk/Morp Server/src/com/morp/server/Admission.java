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
				// �� ���� ��û�� ���� ������ ���
				Socket socket = serverSocket.accept();
				System.out.println("con");
				// ���� ������ ���� ���� ���� ����
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
