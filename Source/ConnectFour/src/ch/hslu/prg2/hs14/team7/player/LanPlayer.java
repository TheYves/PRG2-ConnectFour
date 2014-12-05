/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7.player;

import ch.hslu.prg2.hs14.team7.GameBoard;
import ch.hslu.prg2.hs14.team7.TokenColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick
 */
public abstract class LanPlayer extends Player implements Runnable {

	private Thread thread;
	private TokenColor tokenColor;
	protected List<ILanPlayerListener> lanListeners = new ArrayList<ILanPlayerListener>();

	public LanPlayer(TokenColor tokenColor) {
		super("LAN Player", tokenColor);
		this.tokenColor = tokenColor;
		thread = new Thread(this);
		thread.run();
	}

	public void addPlayerListener(ILanPlayerListener listener) {
		lanListeners.add(listener);
	}

	protected void isReady() {
		for(ILanPlayerListener listener : lanListeners) {
			listener.isReady();
		}
	}

	protected void connectionLost() {
		for(ILanPlayerListener listener : lanListeners) {
			listener.connectionLost();
		}
	}

	@Override
	public void run() {
		// connect to lan player
		/*
		while () {
		}

		super.nickname = getnicknckslföfdjskljf();

		notifyAll();
		*/
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int makeMove(GameBoard gameBoard) {
		throw new UnsupportedOperationException("Network stuff."); //To change body of generated methods, choose Tools | Templates.
	}

}
