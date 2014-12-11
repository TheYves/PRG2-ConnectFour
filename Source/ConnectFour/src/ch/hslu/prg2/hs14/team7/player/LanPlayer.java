/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7.player;

import ch.hslu.prg2.hs14.team7.GameBoard;
import ch.hslu.prg2.hs14.team7.TokenColor;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick
 */
public abstract class LanPlayer extends Player {

	private TokenColor tokenColor;
	private Socket socket;
	protected List<ILanPlayerListener> lanListeners = new ArrayList<>();

	public LanPlayer(TokenColor tokenColor) {
		super("LAN Player", tokenColor);
		this.tokenColor = tokenColor;
	}

	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void addPlayerListener(ILanPlayerListener listener) {
		lanListeners.add(listener);
	}

	protected void isReady() {
		for (ILanPlayerListener listener : lanListeners) {
			listener.isReady();
		}
	}

	protected void connectionLost() {
		for (ILanPlayerListener listener : lanListeners) {
			listener.connectionLost();
		}
	}

	protected void moveMade(GameBoard gameBoard) {
		for (ILanPlayerListener listener : lanListeners) {
			listener.moveMade(gameBoard);
		}
	}

	@Override
	public void makeMove(GameBoard gameBoard) {
		if (socket == null || !socket.isConnected()) {
			connectionLost();
			return;
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(gameBoard);
		} catch (IOException e) {
			e.printStackTrace();
			connectionLost();
		}

		System.out.println("Board sent to network player. Waiting for an answer...");

		// listen for an answer
		listen();
	}

	protected void listen() {
		Thread t = new Thread(new SocketListener());
		t.start();
	}

	private class SocketListener implements Runnable {
		@Override
		public void run() {
			while (true) {
				if (socket == null || !socket.isConnected()) {
					connectionLost();
					break;
				}
				try {
					ObjectInputStream oos = new ObjectInputStream(socket.getInputStream());
					GameBoard gameBoard = (GameBoard) oos.readObject();
					moveMade(gameBoard);
					break;
				} catch (IOException e) {
					e.printStackTrace();
					connectionLost();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					connectionLost();
				}
			}
		}
	}

}
