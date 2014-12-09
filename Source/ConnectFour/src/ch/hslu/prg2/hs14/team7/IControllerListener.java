/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7;

/**
 *
 * @author Nick
 */
public interface IControllerListener {
    public void moveMade(GameBoard board, int column, TokenColor tokenColor);
    public void newGame(GameBoard board);
    public void enemyPlayerWonAGame(GameBoard board);
    public void thisPlayerWonAGame(GameBoard board);
}
