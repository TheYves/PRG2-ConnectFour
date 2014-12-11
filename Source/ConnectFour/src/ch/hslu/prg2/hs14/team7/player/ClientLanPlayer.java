package ch.hslu.prg2.hs14.team7.player;

import ch.hslu.prg2.hs14.team7.TokenColor;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Yves Hohl (yves.hohl@stud.hslu.ch) on 05.12.2014.
 */
public class ClientLanPlayer extends LanPlayer implements Runnable {

	private String ip;
	private int port;

	public ClientLanPlayer(int port, String ip, TokenColor tokenColor) {
		super(tokenColor);

		this.ip = ip;
		this.port = port;

		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		try {
			Socket socket = new Socket(ip, port);
			System.out.println("ClientLanPlayer: Connection to host established.");
			setSocket(socket);
			isReady();
			listen();
		} catch (IOException e) {
			connectionLost();
		}
	}
}
