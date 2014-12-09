package com.morp.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PingConnection {

	public Socket connection;
	public DataOutputStream dataOutputStream;
	public long deltaTime;
	public boolean invalid = false;

	public PingConnection(Socket connection) throws IOException {
		this.connection = connection;
		deltaTime = System.currentTimeMillis();
		dataOutputStream = new DataOutputStream(connection.getOutputStream());

		sendUTF("t%" + deltaTime + "%");
	}

	public void sendUTF(String string) {
		try {
			dataOutputStream.writeUTF(string);
			dataOutputStream.flush();
		} catch (IOException e) {
			invalid = true;
			// e.printStackTrace();
		}

	}

	public String getInfo() {
		return connection.getInetAddress() + "[" + connection.getPort() + "]";
	}
}
