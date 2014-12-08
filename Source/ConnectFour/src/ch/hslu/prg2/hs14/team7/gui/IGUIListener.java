/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7.gui;

import ch.hslu.prg2.hs14.team7.GameBoard;
import ch.hslu.prg2.hs14.team7.TokenColor;

/**
 *
 * @author Nick
 */
public interface IGUIListener {
    public void  moveMade(GameBoard board, int column, TokenColor tokenColor);
}
