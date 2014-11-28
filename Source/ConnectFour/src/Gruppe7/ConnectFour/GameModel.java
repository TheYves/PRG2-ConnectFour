/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gruppe7.ConnectFour;

/**
 *
 * @author Christoph
 */
public class GameModel {
    Token[][] board;
    
    public GameModel()
    {
        this.board = new Token[7][6];
    }
    
    public GameModel(int x, int y)
    {
        this.board = new Token[x][y];
    }
}
