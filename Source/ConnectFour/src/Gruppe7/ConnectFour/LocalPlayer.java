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
public class LocalPlayer extends Player {

    public LocalPlayer(String nickname, TokenColor tokenColor) {
        super(nickname, tokenColor);
    }

    @Override
    public int makeMove(GameBoard gameBoard) {
        throw new UnsupportedOperationException("Ask UI for next move."); //To change body of generated methods, choose Tools | Templates.
    }
}
