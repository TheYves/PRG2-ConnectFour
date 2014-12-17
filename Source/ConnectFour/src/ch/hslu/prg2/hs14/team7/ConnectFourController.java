/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7;

import ch.hslu.prg2.hs14.team7.gui.ConnectFourGUI;
import ch.hslu.prg2.hs14.team7.player.*;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

/**
 * @author Nick
 */
public class ConnectFourController {

	private GameModel gameModel; // das model
	private ConnectFourGUI gui;

	public ConnectFourController() {
	}

	public void start() {
		// create new model
		gameModel = new GameModel();

		// create new gui
		gui = new ConnectFourGUI(this, gameModel);

		// show select mode dialog
		gui.selectGameMode();

		// start game
		startGame();
	}

	private void startGame() {
		gameModel.setGameState(GameModel.GameState.WaitForPlayer);
		Player enemyPlayer;
		TokenColor thisPlayerTokenColor = TokenColor.Yellow;

		switch (gameModel.getGameMode()) {
			case Computer:
				enemyPlayer = new ComputerPlayer(ComputerLevel.High, TokenColor.Red);
				break;
			case Local:
				enemyPlayer = new LocalPlayer("Player 2", TokenColor.Red);
				break;
			case LANClient:
				enemyPlayer = new ClientLanPlayer(gameModel.getPort(), gameModel.getIp(), TokenColor.Yellow);
				thisPlayerTokenColor = TokenColor.Red;
				break;
			case LANHost:
				enemyPlayer = new HostLanPlayer(gameModel.getPort(), TokenColor.Red);
				break;
			default:
				enemyPlayer = new LocalPlayer("Player 2", TokenColor.Red);
				break;
		}

		Player thisPlayer = new LocalPlayer("Player 1", thisPlayerTokenColor);

		thisPlayer.addPlayerListener(new IPlayerListener() {
			@Override
			public void moveMade(GameBoard gameBoard) {
				gameModel.setGameBoard(gameBoard);
				nextTurn();
			}

			@Override
			public void isReady() {
			}

			@Override
			public void connectionLost() {
			}
		});

		enemyPlayer.addPlayerListener(new IPlayerListener() {
			@Override
			public void isReady() {
				if (enemyPlayer instanceof ClientLanPlayer) {
					nextTurn(enemyPlayer);
				} else if (enemyPlayer instanceof HostLanPlayer) {
					nextTurn(thisPlayer);
				} else {
					nextTurn();
				}
			}

			@Override
			public void connectionLost() {
				gameModel.setGameState(GameModel.GameState.Disconnected);
			}

			@Override
			public void moveMade(GameBoard gameBoard) {
				gameModel.setGameBoard(gameBoard);
				nextTurn();
			}
		});

		gameModel.setThisPlayer(thisPlayer);
		gameModel.setEnemyPlayer(enemyPlayer);

		gui.startGame();

		// weil die lokalen spieler isReady() nie aufrufen
		if (!(enemyPlayer instanceof LanPlayer)) {
			nextTurn();
		}
	}

	/**
	 * Mapping von Spieler zur TokenColor.
	 *
	 * @param tokenColor
	 * @return Mapping von Spieler zur TokenColor.
	 */
	public Player getPlayerByColor(TokenColor tokenColor) {
		if (getGameModel().getThisPlayer().getTokenColor() == tokenColor) {
			return getGameModel().getThisPlayer();
		} else {
			return getGameModel().getEnemyPlayer();
		}
	}

	public void nextTurn() {
		Player currentPlayer = gameModel.getCurrentPlayer();
		Player nextPlayer;
		Player thisPlayer = gameModel.getThisPlayer();
		Player enemyPlayer = gameModel.getEnemyPlayer();

		// if there's no current player, we randomly choose one
		if (currentPlayer == null) {
			nextPlayer = new Random().nextInt(2) == 0 ? thisPlayer : enemyPlayer;
		} else {
			nextPlayer = currentPlayer == thisPlayer ? enemyPlayer : thisPlayer;
		}

		nextTurn(nextPlayer);
	}

	public void nextTurn(Player nextPlayer) {
		// game finished?
		Player winner = getWinner();
		if (winner != null) {
			gameModel.setGameState(GameModel.GameState.GameOver);
			gameModel.setWinner(winner);
		} else {
			gameModel.setGameState(GameModel.GameState.Ready);
			gameModel.setCurrentPlayer(nextPlayer);

			// inform player that he has to make the next move
			nextPlayer.makeMove(gameModel.getGameBoard());
		}
	}


	/**
	 * @return Ob ein Spiel einen Gewinner hat.
	 */
	public Player getWinner() {
		GameBoard gameBoard = getGameModel().getGameBoard();
		int height = 0;
		if (gameBoard.getBoard().length > 0) {
			height = gameBoard.getBoard()[0].length;
		}

		for (int column = 0; column < gameBoard.getBoard().length; column++) {
			for (int row = 0; row < height; row++) {
				if (gameBoard.checkXInARow(column, row, 4, TokenColor.Yellow)) {
					return getPlayerByColor(TokenColor.Yellow);
				} else if (gameBoard.checkXInARow(column, row, 4, TokenColor.Red)) {
					return getPlayerByColor(TokenColor.Red);
				}
			}
		}
		return null;
	}

	/**
	 * @return Das Gameboard.
	 */
	public GameBoard getGameBoard() {
		return getGameModel().getGameBoard();
	}

	/**
	 * @return the gameModel
	 */
	public GameModel getGameModel() {
		return gameModel;
	}
}