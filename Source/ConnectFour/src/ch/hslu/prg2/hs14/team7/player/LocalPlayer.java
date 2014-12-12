/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7.player;

import ch.hslu.prg2.hs14.team7.GameBoard;
import ch.hslu.prg2.hs14.team7.TokenColor;

/**
 *
 * @author Christoph
 */
public class LocalPlayer extends Player {

    GameBoard gameBoard;

    public LocalPlayer(String nickname, TokenColor tokenColor) {
        super(nickname, tokenColor);
    }

    @Override
    public void makeMove(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void chooseColumn(int col) {
        gameBoard.insertToken(col, getTokenColor());
        moveMade(gameBoard);
    }
}
