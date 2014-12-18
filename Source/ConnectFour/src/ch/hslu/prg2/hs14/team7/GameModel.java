package ch.hslu.prg2.hs14.team7;

import ch.hslu.prg2.hs14.team7.player.Player;

/**
 *
 * @author Nick
 */
public class GameModel {
    private GameBoard gameBoard;
    private Player thisPlayer;
    private Player enemyPlayer;
    private Player currentPlayer;
    private Player winner;
    private GameMode gameMode;
    private GameState gameState;
    private String ip;
    private int port;

    public enum GameMode {
        Local,
        Computer,
        LANHost,
        LANClient
    }

    public enum GameState {
        Ready,
        WaitForPlayer,
        Disconnected,
        GameOver
    }

    public GameModel(){
        this.gameBoard = new GameBoard();
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public Player getEnemyPlayer() {
        return enemyPlayer;
    }

    public void setThisPlayer(Player thisPlayer) {
        this.thisPlayer = thisPlayer;
    }

    public void setEnemyPlayer(Player enemyPlayer) {
        this.enemyPlayer = enemyPlayer;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
