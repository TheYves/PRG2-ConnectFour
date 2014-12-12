/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7;

import ch.hslu.prg2.hs14.team7.player.ILanPlayerListener;
import ch.hslu.prg2.hs14.team7.player.IPlayerListener;
import ch.hslu.prg2.hs14.team7.player.LanPlayer;
import ch.hslu.prg2.hs14.team7.player.Player;
import java.util.Observable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Random;

/**
 *
 * @author Nick
 */
public class GameModel {
    private GameBoard gameBoard;
    private Player thisPlayer;
    private Player enemyPlayer;
    private Player currentPlayer;
    private GameMode gameMode;

    public enum GameMode {
        Local,
        Computer,
        LANHost,
        LANClient
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
}
