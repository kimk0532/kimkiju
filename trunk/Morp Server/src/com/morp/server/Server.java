package com.morp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {

	public final static String DESCRIPTON = "welcome to MORP server^^";
	public final static int PORT = 15464;
	public final static int VERSION = 0;
	public final static int CHANNEL_COUNT = 4;
	public final static int MAX_USER = 100;

	public static SimpleDateFormat dateFormat;

	public boolean started = false;

	static {
		dateFormat = new SimpleDateFormat("[hh:mm:ss]");
	}

	public ServerSocket serverSocket;

	public Command command;
	public Admission admission;
	public List<Channel> channels;

	public Server() {

	}

	private boolean initialize() {

		try {

			channels = new ArrayList<Channel>(CHANNEL_COUNT);
			
			for (int i = 0; i < CHANNEL_COUNT; i++) {
				channels.add(new Channel());
			}

			serverSocket = new ServerSocket(Server.PORT);
			command = new Command();
			admission = new Admission(this.serverSocket, this.channels);

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void log(Object logMessage) {
		System.out.println(timestamp() + " " + logMessage);
	}

	public static String timestamp() {
		return dateFormat.format(new Date());
	}

	public void start() {
		if (started)
			return;
		if (initialize()) {

			// command.start();
			admission.start();
			started = true;

			log("서버를 성공적으로 초기화했습니다.");

		} else {
			log("서버를 초기화할 수 없습니다.");
		}
	}

}
