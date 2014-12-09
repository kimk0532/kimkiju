package com.morp.server;

public class Launcher {

	public static void main(String[] args) {

		System.out.println("*********************************");
		System.out.println("        <MORP Server v1.0>       ");
		System.out.println("                                 ");
		System.out.println("                       by ±è ÇöÁØ");
		System.out.println("*********************************");
		System.out.println("Port: " + Server.PORT + "\n");

		Server server = new Server();
		server.start();
		// pPing morpPing = new MorpPing();

	}

}
