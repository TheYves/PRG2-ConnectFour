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
    private TokenColor tokenColor = TokenColor.None;
    
    public Token(TokenColor tokenColor)
    {
        this.tokenColor = tokenColor;
    }
    
    public TokenColor getTokenColor()
    {
        return this.tokenColor;
    }
    
    public void setTokenColor(TokenColor tokenColor){
        this.tokenColor = tokenColor;
    }
}
