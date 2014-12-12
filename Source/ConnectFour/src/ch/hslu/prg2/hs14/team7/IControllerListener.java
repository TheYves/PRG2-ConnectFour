/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7;

import ch.hslu.prg2.hs14.team7.player.Player;

/**
 *
 * @author Nick
 */
public interface IControllerListener {
    public void gameFinished(GameBoard board, Player winner);
}
