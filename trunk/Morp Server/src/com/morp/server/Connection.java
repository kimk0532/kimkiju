package com.morp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Connection implements Runnable {

	public Socket socket;
	public List<Channel> channels;
	public User user;
	public Thread thread;

	public String id;
	public Channel channel;
	public int channelIndex = -1;
	public int priority = -1;

	protected DataInputStream input;
	protected DataOutputStream output;

	private boolean register = false;

	public Connection(User user, Socket socket, List<Channel> channels)
			throws IOException {
		this.user = user;
		user.connection = this;
		this.socket = socket;
		this.channels = channels;

		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());

		send(Server.DESCRIPTON);
	}

	@Override
	public void run() {
		try {

			String received;
			while ((received = input.readUTF()) != null) {
				String[] buffer = received.split("`");

				if (!register) {
					// 구분자 `를 기준으로 버퍼화

					if (buffer.length >= 4 && buffer[0].charAt(0) == 'r') {
						// r`1채널`1우선순위`아이디`

						this.channelIndex = Integer.parseInt(buffer[1]);
						this.priority = Integer.parseInt(buffer[2]);
						this.id = buffer[3].trim();
						this.channel = channels.get(channelIndex);
						if (channel == null) {
							this.channel = channels.get(0);
						}

						channel.users.add(user);

						register = true;

						channel.broadcast(Header.NOTICE, id + "님이 입장했습니다.");
						Server.log(id + "님이 입장했습니다. [" + getInfo() + "]");
					}
				}

				switch (buffer[0].charAt(0)) {
				case Header.CLOSE:
					// channel.broadcast(received);
					close();
					break;
				case Header.CHAT:
					channel.broadcast(Header.CHAT, id + "`" + buffer[1]);
					break;
				default:
				}
			}

		} catch (Exception e) {
			try {
				close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void close() throws IOException {
		channel.users.remove(user);
		socket.close();
		Statistics.count--;
		channel.broadcast(Header.NOTICE, id + "님이 퇴장했습니다.");
		Server.log(id + "님이 퇴장했습니다. [" + getInfo() + "]");
		thread.interrupt();
	}

	public void send(char header, String data) throws IOException {
		output.writeUTF(header + "`" + data);
		output.flush();
	}

	public void send(String data) throws IOException {
		output.writeUTF(data);
		output.flush();
	}

	public String getInfo() {
		return socket.getInetAddress() + ":" + socket.getPort();
	}

	public static class Header {
		private Header() {
		}

		public final static char NOTICE = 'n';
		public final static char REGISTER = 'r';
		public final static char CLOSE = 'x';
		public final static char CHAT = 'c';

	}
}
