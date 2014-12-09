package com.morp.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Channel {

	public List<User> users;

	public Channel() {
		users = new LinkedList<User>();
	}

	public boolean broadcast(char header, String data) {
		try {
			for (User user : users) {
				user.connection.send(header, data);
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public boolean broadcast(String data) {
		try {
			for (User user : users) {
				user.connection.send(data);
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
