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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Random;

/**
 *
 * @author Nick
 */
public class GameModel {
    private GameBoard gameBoard;
    private final Player thisPlayer;
    private final Player enemyPlayer;
    private Player currentPlayer;

    public GameModel(Player thisPlayer, Player enemyPlayer){
        this.thisPlayer = thisPlayer;
        this.enemyPlayer = enemyPlayer;
        this.gameBoard = new GameBoard();
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

}
