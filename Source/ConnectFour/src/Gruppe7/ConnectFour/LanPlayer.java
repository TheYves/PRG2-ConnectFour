/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gruppe7.ConnectFour;

/**
 *
 * @author Nick
 */
public class LanPlayer extends Player implements Runnable{

    private final boolean isHost;
    private Thread thread;
    private String ip;
    
    public LanPlayer(String ip, boolean isHost, TokenColor tokenColor){
        super("Lan Enemy", tokenColor);
        this.isHost = isHost;
        this.ip = ip;
    }
    
    public boolean isHost(){
        return isHost;
    }
    
    public void start(){
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    
    /**
     *
     * @return Ob der Spieler verbunden ist. Sollte vor jedem neuen Zug geprüft werden.
     */
    public boolean isConnected(){
       
    }

    @Override
    public void run() {
        // connect to lan player
        while () {
        }
        
        super.nickname = getnicknckslföfdjskljf();
        
        notifyAll();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int makeMove(GameBoard gameBoard) {
        throw new UnsupportedOperationException("Network stuff."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
