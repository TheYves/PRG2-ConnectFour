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
public class Token {
    private Player player = null;
    
    public Token(Player player)
    {
        this.player = player;
    }
    
    public Player getPlayer()
    {
        return this.player;
    }
}
