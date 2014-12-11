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
    private ConnectFourGUI gui;
    private Player thisPlayer; // der Spieler, der den Controller erstellt (beim ersten Spiel auf dem UI).
    
    private static final int defaultPort = 10000;
    
    private List<IControllerListener> listeners = new ArrayList<>();

    public ConnectFourController(Player thisPlayer){
        this.thisPlayer = thisPlayer;
    }
    
    public void addListener(IControllerListener listener){
        listeners.add(listener);
    }

    /**
     * Erstellt ein neues Spiel gege die KI.
     *
     * @param listener
     * @param level
     */
    public void newComputerGame(IPlayerListener listener, ComputerLevel level) {
        thisPlayer.addPlayerListener(listener);
        TokenColor computerColor = thisPlayer.getTokenColor() == TokenColor.Yellow ? TokenColor.Red : TokenColor.Yellow;
        gameModel = new GameModel(thisPlayer, new ComputerPlayer(level, computerColor));
        runGame();
    }

    /**
     * Erstellt ein neues Spiel gegen einen lokalen Gegner
     *
     * @param enemyPlayer
     */
    public void newLocalGame(Player enemyPlayer) {
        gameModel = new GameModel(thisPlayer, enemyPlayer);
        runGame();
    }
    
    public void joinLanGame(String ip) {
        joinLanGame(defaultPort, ip);
    }

    /**
     * Verbindet den Spieler mit einem Gegner, der sich als Host zur Verfügung stellt.
     *
     * @param port
     * @param ip
     */
    public void joinLanGame(int port, String ip) {
        TokenColor lanColor = thisPlayer.getTokenColor() == TokenColor.Yellow ? TokenColor.Red : TokenColor.Yellow;
        ClientLanPlayer lanPlayer = new ClientLanPlayer(port, ip, lanColor);

        gameModel = new GameModel(thisPlayer, lanPlayer);

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
                ConnectFourController.this.getGameModel().setGameBoard(gameBoard);
                nextTurn();
            }
        });
        runGame();
    }

    public void hostLanGame(){
        hostLanGame(defaultPort);
    }

    /**
     * Verbindet den Spieler mit einem Gegner, der sich als Host zur Verfügung stellt.
     *
     * @param port
     */
    public void hostLanGame(int port) {
        TokenColor lanColor = thisPlayer.getTokenColor() == TokenColor.Yellow ? TokenColor.Red : TokenColor.Yellow;
        HostLanPlayer lanPlayer = new HostLanPlayer(port, lanColor);

        gameModel = new GameModel(thisPlayer, lanPlayer);

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
                ConnectFourController.this.getGameModel().setGameBoard(gameBoard);
                nextTurn();
            }
        });
        runGame();
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
        
        for (IControllerListener listener : listeners){
            listener.moveMade(this.getGameBoard(), nextPlayer.getTokenColor());
        }
    }
    
    public void runGame() {
        if (this.gameModel != null && this.gameModel.getGameBoard() != null){
            while(!this.hasWinner()){
                nextTurn();
            }
            
            TokenColor winnerColor = getGameModel().getCurrentPlayer().getTokenColor();
            for (IControllerListener listener : listeners){
                if (winnerColor.equals(thisPlayer.getTokenColor())){
                    listener.thisPlayerWonAGame(this.getGameBoard());
                }
                else{
                    listener.enemyPlayerWonAGame(this.getGameBoard());
                }
            }
        }
        this.gameModel = null;
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