/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gruppe7.ConnectFour;

import java.util.ArrayList;

/**
 *
 * @author Nick
 */
public class ConnectFourController {
    private GameModel gameModel;
    private GameView gameView;
    
    public ConnectFourController(GameView gameView, GameModel gameModel){
        this.gameView = gameView;
        this.gameModel = gameModel;
    }
    
    public void newGame(){
        Thread t = new Thread();
        t.start();
        boolean startGame = gameModel.startGame(new Computer(gameModel, Player.ComputerLevel.Low));
    }
}
