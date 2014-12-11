package ch.hslu.prg2.hs14.team7;

import ch.hslu.prg2.hs14.team7.player.ClientLanPlayer;
import ch.hslu.prg2.hs14.team7.player.HostLanPlayer;
import ch.hslu.prg2.hs14.team7.player.ILanPlayerListener;

/**
 * Created by Yves Hohl (yves.hohl@stud.hslu.ch) on 05.12.2014.
 */
public class Test {

	public static void main(String[] args) {
		HostLanPlayer host = new HostLanPlayer(1337, TokenColor.Yellow);
		ClientLanPlayer client = new ClientLanPlayer(1337, "localhost", TokenColor.Red);
		GameBoard board = new GameBoard();

		host.addPlayerListener(new ILanPlayerListener() {
			@Override
			public void isReady() {
				System.out.println("HOST is ready");
				host.makeMove(board);
			}

			@Override
			public void connectionLost() {
				System.out.println("HOST connection lost");
			}

			@Override
			public void moveMade(GameBoard gameBoard) {
				System.out.println("HOST move made");
			}
		});

		client.addPlayerListener(new ILanPlayerListener() {
			@Override
			public void isReady() {
				System.out.println("CLIENT is ready");
			}

			@Override
			public void connectionLost() {
				System.out.println("CLIENT connection lost");
			}

			@Override
			public void moveMade(GameBoard gameBoard) {
				System.out.println("CLIENT move made");
			}
		});

	}

}
