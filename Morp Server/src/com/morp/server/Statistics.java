package com.morp.server;

public final class Statistics {

	public static int count = 0;

	public static boolean available() {
		if (count < Server.MAX_USER) {
			return true;
		} else {
			return false;
		}

	}

}
