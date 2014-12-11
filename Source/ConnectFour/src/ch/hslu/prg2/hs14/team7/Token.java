/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7;

import java.io.Serializable;

/**
 *
 * @author Christoph
 */
public class Token implements Serializable {
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
