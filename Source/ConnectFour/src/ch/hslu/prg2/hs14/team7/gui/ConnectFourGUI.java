package ch.hslu.prg2.hs14.team7.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ConnectFourGUI extends JFrame implements Runnable {
    
    /**
     * Erstellt den Spielfeld Hintergrund (visuelles Game Board).
     */
    /*public final void gameBackground() {
        try {
            myPanel.setLayout(new GridLayout(y_Achse, x_Achse));
            jPanel2.setLayout(new GridLayout(1, x_Achse));

            for (int i = 0; i < x_Achse; i++) {
                myLabels.add(new ArrayList<JLabel>());
            }
            for (int i = 0; i < y_Achse; i++) {
                for (int k = 0; k < x_Achse; k++) {
                    myLabels.get(i).add(new JLabel(myPicture));
                    jPanel1.add(myLabels.get(i).get(k));
                }
            }

            pack();

        } catch (Exception ex) {
            Logger.getLogger(GUIMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    private boolean running;
    private Thread thread;
    
    BufferedImage foregroundImage;
    
    public ConnectFourGUI(){
        setUndecorated(false);
        //setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        try {
            foregroundImage = ImageIO.read(getClass().getResource("resources/foreground.png")); 
        } catch (Exception e) {
        }
    }
    
    public void initialize(){
    }
    
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(foregroundImage, 0, 0, rootPane);
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
    
    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ex) {
            // todo: handle exception
        }
        System.exit(1);
    }
       
    @Override
    public void run() {
        initialize();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
