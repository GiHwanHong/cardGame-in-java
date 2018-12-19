package com.sec.hong.samecardgame;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReceiver extends Thread {
	Socket socket;
	DataInputStream in;

	ClientReceiver(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
		}
	}

	public void run() {
		while (in != null) {
			try {
				String re = in.readUTF();
				System.out.println(re);
			} catch (IOException e) {
			}
		}
	} 
}