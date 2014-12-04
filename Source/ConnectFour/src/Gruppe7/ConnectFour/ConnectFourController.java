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
    private GameModel gameModel; // das mmodel
    private GameView gameView; // das UI
    
    public ConnectFourController(GameView gameView, GameModel gameModel){
        this.gameView = gameView;
        this.gameModel = gameModel;
    }
    
    /**
     * Erstellt ein neues Spiel gege die KI.
     * @param level
     */
    public void newComputerGame(ComputerLevel level){
        boolean startGame = gameModel.startGame(new Computer(ComputerLevel.Low, gameModel.getThisPlayer().getEnemyColor()));
    }
    
    /**
     * Erstellt ein neues Spiel gegen einen lokalen Gegner
     * @param localName
     */
    public void newLocalGame(String localName){
        boolean startGame = gameModel.startGame(new LocalPlayer(localName, gameModel.getThisPlayer().getEnemyColor()));
    }
    
    /**
     * Verbindet den Spieler mit einem Gegner, der sich als Host zur Verfügung stellt.
     * @param ip
     */
    public void joinLanGame(String ip){
        TokenColor color; //hol die andere Farb vom Gegner (z.B. wenn Gegner Red ist, muss der jetztige Spieler Yellow sein).
        boolean startGame = gameModel.startGame(new LanPlayer(ip, true, color));
    }
    
    /**
     * Wartet auf eine Verbindung von einem Client.
     */
    public void waitForLanGame(){
        while (!gameModel.isGameRunning()){
            String ip; // die IP vom Gegner
            gameModel.startGame(new LanPlayer(ip, false, gameModel.getThisPlayer().getEnemyColor()));
        }
    }
}