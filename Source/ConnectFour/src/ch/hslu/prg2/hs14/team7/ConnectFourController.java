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

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Random;

/**
 * @author Nick
 */
public class ConnectFourController {
	private GameModel gameModel; // das model
	private Player thisPlayer; // der Spieler, der den Controller erstellt (beim ersten Spiel auf dem UI).
	private ConnectFourGUI gui;

	private static final int defaultPort = 10000;

	private List<IControllerListener> listeners = new ArrayList<>();

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
		Player thisPlayer = new LocalPlayer("Player 1", TokenColor.Yellow);
		Player enemyPlayer;

		switch(gameModel.getGameMode()) {
			case Computer:
				enemyPlayer = new ComputerPlayer(ComputerLevel.High, TokenColor.Red);
				break;
			case Local:
				enemyPlayer = new LocalPlayer("Player 2", TokenColor.Red);
				break;
			/*case LANClient:
				//TODO
				break;
			case LANHost:
				//TODO
				break;*/
			default:
				enemyPlayer = new LocalPlayer("Player 2", TokenColor.Red);
				break;
		}

		thisPlayer.addPlayerListener(new IPlayerListener() {
			@Override
			public void moveMade(GameBoard gameBoard) {
				gameModel.setGameBoard(gameBoard);
				nextTurn();
			}
		});

		enemyPlayer.addPlayerListener(new ILanPlayerListener() {
			@Override
			public void isReady() {
				nextTurn();
			}

			@Override
			public void connectionLost() {
				//TODO
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

		if(!(enemyPlayer instanceof LanPlayer)) {
			nextTurn();
		}
	}

	public void addListener(IControllerListener listener) {
		listeners.add(listener);
	}


	/**
	 * Mapping von Spieler zur TokenColor.
	 *
	 * @param tokenColor
	 * @return Mapping von Spieler zur TokenColor.
	 */
	public Player getPlayer(TokenColor tokenColor) {
		if (getGameModel().getThisPlayer().getTokenColor() == tokenColor) {
			return getGameModel().getThisPlayer();
		} else {
			return getGameModel().getEnemyPlayer();
		}
	}

	public void nextTurn() {
		// TODO: prüfen ob spieler gewonnen hat
		Player currentPlayer = getGameModel().getCurrentPlayer();
		Player nextPlayer;
		Player thisPlayer = getGameModel().getThisPlayer();
		Player enemyPlayer = getGameModel().getEnemyPlayer();
		if (currentPlayer == null) {
			nextPlayer = new Random().nextInt(2) == 0 ? thisPlayer : enemyPlayer;
		} else {
			nextPlayer = currentPlayer == thisPlayer ? enemyPlayer : thisPlayer;
		}
		getGameModel().setCurrentPlayer(nextPlayer);

		nextPlayer.makeMove(getGameModel().getGameBoard());

		for (IControllerListener listener : listeners) {
			listener.moveMade(this.getGameBoard(), nextPlayer);
		}
	}


	/**
	 * @return Ob ein Spiel läuft
	 */
	public boolean isGameRunning() {
		return getGameModel() != null;
	}

	/**
	 * @return Ob ein Spiel einen Gewinner hat.
	 */
	public boolean hasWinner() {
		GameBoard gameBoard = getGameModel().getGameBoard();
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
		return getGameModel().getGameBoard();
	}

	/**
	 * @return Den Gegenspieler.
	 */
	public Player getEnemyPlayer() {
		return getGameModel().getEnemyPlayer();
	}

	public String serializeBoard() {
		throw new NotImplementedException();
	}

	/**
	 * @return Den "freundlichen" Spieler.
	 */
	public Player getThisPlayer() {
		return getGameModel().getThisPlayer();
	}

	/**
	 * @return the gameModel
	 */
	public GameModel getGameModel() {
		return gameModel;
	}
}