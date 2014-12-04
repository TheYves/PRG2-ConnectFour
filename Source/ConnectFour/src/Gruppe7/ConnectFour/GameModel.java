/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gruppe7.ConnectFour;

import java.awt.Color;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author Nick
 */
public class GameModel {
    private GameBoard gameBoard;
    private final Player thisPlayer;
    private Player enemyPlayer;
    
    public GameModel(Player thisPlayer){
        this.thisPlayer = thisPlayer;
    }
    
    /**
     * Mapping von Spieler zur TokenColor.
     * @param tokenColor
     * @return Mapping von Spieler zur TokenColor.
     */
    public Player getPlayer(TokenColor tokenColor){
        if (thisPlayer.getTokenColor() == tokenColor){
            return thisPlayer;
        }
        else{
            return enemyPlayer;
        }
    }
    
    /**
     * Startet ein neues Game gegen den spezifizierten Spieler.
     * @param enemyPlayer
     * @return Ob ein Spiel gestartet werden konnte.
     */
    public boolean startGame(Player enemyPlayer){
        boolean gameReady = true;
        if(enemyPlayer instanceof LanPlayer){
            LanPlayer lanPlayer = ((LanPlayer)enemyPlayer);
            lanPlayer.start();
            try {
                while (!lanPlayer.isConnected()){
                    lanPlayer.wait();
                }
                gameReady = ((LanPlayer)enemyPlayer).isConnected();
            }
            catch (InterruptedException ex) {
                gameReady = false;
            }
        }
        
        if (gameReady)
        {
            this.gameBoard = new GameBoard();
            this.enemyPlayer = enemyPlayer;
        }
        
        return gameReady;
    }
    
    /**
     * 
     * @return Ob ein Spiel läuft
     */
    public boolean isGameRunning(){
        return gameBoard != null;
    }
    
    /**
     *
     * @return Ob ein Spiel einen Gewinner hat.
     */
    public boolean hasWinner(){
        int height = 0;
        if (gameBoard.getBoard().length > 0)
        {
            height = gameBoard.getBoard()[0].length;
        }
        
        for (int column = 0; column < gameBoard.getBoard().length; column++) {
            for (int row = 0; row < height; row++){
                if (gameBoard.checkXInARow(column, row, 4, TokenColor.Yellow) 
                   || (gameBoard.checkXInARow(column, row, 4, TokenColor.Red)))
                {
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
        return gameBoard;
    }

    /**
     * @return Den "freundlichen" Spieler.
     */
    public Player getThisPlayer() {
        return thisPlayer;
    }

    /**
     * @return Den Gegenspieler.
     */
    public Player getEnemyPlayer() {
        return enemyPlayer;
    }
    
    public String serializeBoard(){
        throw new NotImplementedException();
    }
}
