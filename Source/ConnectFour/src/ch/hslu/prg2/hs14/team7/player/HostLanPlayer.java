package ch.hslu.prg2.hs14.team7.player;

import ch.hslu.prg2.hs14.team7.TokenColor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Yves Hohl (yves.hohl@stud.hslu.ch) on 05.12.2014.
 */
public class HostLanPlayer extends LanPlayer implements Runnable {

	private final int port;

	public HostLanPlayer(int port, TokenColor tokenColor) {
		super(tokenColor);

		this.port = port;

		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			Socket socket = serverSocket.accept();
			System.out.println("HostLanPlayer: Client connected.");
			setSocket(socket);
			isReady();
		} catch (IOException e) {
			connectionLost();
			e.printStackTrace();
		}
	}

}
