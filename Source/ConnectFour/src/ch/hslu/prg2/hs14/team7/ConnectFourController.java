/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7;

import ch.hslu.prg2.hs14.team7.player.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Random;

/**
 * @author Nick
 */
public class ConnectFourController {
	private GameModel gameModel; // das mmodel

	public ConnectFourController(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	/**
	 * Erstellt ein neues Spiel gege die KI.
	 *
	 * @param level
	 */
	public void newComputerGame(String nickname, ComputerLevel level) {
		gameModel = new GameModel(new LocalPlayer(nickname, TokenColor.Yellow), new ComputerPlayer(level, TokenColor.Red));
		nextTurn();
	}

	/**
	 * Erstellt ein neues Spiel gegen einen lokalen Gegner
	 *
	 * @param localName
	 */
	public void newLocalGame(String nickname, String enemyNickname) {
		gameModel = new GameModel(new LocalPlayer(nickname, TokenColor.Yellow), new LocalPlayer(enemyNickname, TokenColor.Red));
		nextTurn();
	}

	/**
	 * Verbindet den Spieler mit einem Gegner, der sich als Host zur Verfügung stellt.
	 *
	 * @param ip
	 */
	public void joinLanGame(String nickname, int port, String ip) {
		ClientLanPlayer lanPlayer = new ClientLanPlayer(port, ip, TokenColor.Red);

		gameModel = new GameModel(new LocalPlayer(nickname, TokenColor.Yellow), lanPlayer);

		lanPlayer.addPlayerListener(new ILanPlayerListener() {
			@Override
			public void isReady() {
				nextTurn();
			}

			@Override
			public void connectionLost() {
				// TODO
			}

			@Override
			public void moveMade(GameBoard gameBoard) {
				ConnectFourController.this.gameModel.setGameBoard(gameBoard);
				nextTurn();
			}
		});
	}


	/**
	 * Verbindet den Spieler mit einem Gegner, der sich als Host zur Verfügung stellt.
	 *
	 * @param ip
	 */
	public void hostLanGame(String nickname, int port) {
		HostLanPlayer lanPlayer = new HostLanPlayer(port, TokenColor.Red);

		gameModel = new GameModel(new LocalPlayer(nickname, TokenColor.Yellow), lanPlayer);

		lanPlayer.addPlayerListener(new ILanPlayerListener() {
			@Override
			public void isReady() {
				nextTurn();
			}

			@Override
			public void connectionLost() {
				// TODO
			}

			@Override
			public void moveMade(GameBoard gameBoard) {
				ConnectFourController.this.gameModel.setGameBoard(gameBoard);
				nextTurn();
			}
		});
	}

	/**
	 * Mapping von Spieler zur TokenColor.
	 *
	 * @param tokenColor
	 * @return Mapping von Spieler zur TokenColor.
	 */
	public Player getPlayer(TokenColor tokenColor) {
		if (gameModel.getThisPlayer().getTokenColor() == tokenColor) {
			return gameModel.getThisPlayer();
		} else {
			return gameModel.getEnemyPlayer();
		}
	}

	public void nextTurn() {
		// TODO: prüfen ob spieler gewonnen hat
		Player currentPlayer = gameModel.getCurrentPlayer();
		Player nextPlayer;
		Player thisPlayer = gameModel.getThisPlayer();
		Player enemyPlayer = gameModel.getEnemyPlayer();
		if (currentPlayer == null) {
			nextPlayer = new Random().nextInt(2) == 0 ? thisPlayer : enemyPlayer;
		} else {
			nextPlayer = currentPlayer == thisPlayer ? enemyPlayer : thisPlayer;
		}
		gameModel.setCurrentPlayer(nextPlayer);
		nextPlayer.makeMove(gameModel.getGameBoard());
	}

	/**
	 * @return Ob ein Spiel läuft
	 */
	public boolean isGameRunning() {
		return gameModel != null;
	}

	/**
	 * @return Ob ein Spiel einen Gewinner hat.
	 */
	public boolean hasWinner() {
		GameBoard gameBoard = gameModel.getGameBoard();
		int height = 0;
		if (gameBoard.getBoard().length > 0) {
			height = gameBoard.getBoard()[0].length;
		}

		for (int column = 0; column < gameBoard.getBoard().length; column++) {
			for (int row = 0; row < height; row++) {
				if (gameBoard.checkXInARow(column, row, 4, TokenColor.Yellow)
						|| (gameBoard.checkXInARow(column, row, 4, TokenColor.Red))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return Das Gameboard.
	 */
	public GameBoard getGameBoard() {
		return gameModel.getGameBoard();
	}

	/**
	 * @return Den "freundlichen" Spieler.
	 */
	public Player getThisPlayer() {
		return gameModel.getThisPlayer();
	}

	/**
	 * @return Den Gegenspieler.
	 */
	public Player getEnemyPlayer() {
		return gameModel.getEnemyPlayer();
	}

	public String serializeBoard() {
		throw new NotImplementedException();
	}
}