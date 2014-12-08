/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7.player;

/**
 *
 * @author Nick
 */
public enum ComputerLevel {
    Low(0), Medium(1), High(2);
    
    private final int value;

    private ComputerLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
