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
    
    public LanPlayer(boolean isHost){
        this.isHost = isHost;
    }
    
    public boolean isHost(){
        return isHost;
    }
    
    @Override
    public int makeMove(int previousEnemyColumn) {
        throw new UnsupportedOperationException("Network stuff."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void start(){
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    
    public boolean isConnected(){
       
    }

    @Override
    public void run() {
        // connect to lan player
        while () {
        }
        
        notifyAll();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
