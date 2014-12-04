/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gruppe7.ConnectFour;

/**
 *
 * @author Nick
 */
public class GameModel {
    private GameBoard gameBoard;
    private Player thisPlayer;
    private Player enemyPlayer;
    
    public GameModel(Player thisPlayer){
        this.thisPlayer = thisPlayer;
    }
    
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
            this.gameBoard = new GameBoard(thisPlayer, enemyPlayer);
            this.enemyPlayer = enemyPlayer;
        }
        
        return gameReady;
    }
    
    public boolean startGame(Player thisPlayer, Player enemyPlayer, int sizeX, int sizeY){
        this.gameBoard = new GameBoard(thisPlayer, enemyPlayer, sizeX, sizeY);
        this.enemyPlayer = enemyPlayer;
    }
    
    public boolean isGameRunning(){
        return gameBoard != null;
    }
    
    public boolean hasWinner(){
        return gameBoard.checkXInARow(0, 0, 4, thisPlayer) || gameBoard.checkXInARow(0, 0, 4, enemyPlayer);
    }

    protected int insertToken(int column, Player player)
    {
        int rowCount = 0;
        Token[] insertingColumn = this.gameBoard[column];
        for (Token row : insertingColumn)
        {
            if (row.getPlayer() == null)
            {
                return rowCount;
            }
            
            rowCount++;
        }
        return -1; // Row is full of tokens
    }
}
