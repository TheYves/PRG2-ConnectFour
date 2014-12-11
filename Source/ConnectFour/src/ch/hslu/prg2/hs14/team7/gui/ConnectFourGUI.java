/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7.gui;

import ch.hslu.prg2.hs14.team7.ConnectFourController;
import ch.hslu.prg2.hs14.team7.GameBoard;
import ch.hslu.prg2.hs14.team7.GameModel;
import ch.hslu.prg2.hs14.team7.IControllerListener;
import ch.hslu.prg2.hs14.team7.TokenColor;
import ch.hslu.prg2.hs14.team7.player.ComputerLevel;
import ch.hslu.prg2.hs14.team7.player.IPlayerListener;
import ch.hslu.prg2.hs14.team7.player.LocalPlayer;
import ch.hslu.prg2.hs14.team7.player.Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DebugGraphics;
import javax.swing.JLabel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 *
 * @author Nick
 */
public class ConnectFourGUI extends javax.swing.JFrame implements Runnable {

    // UI Elemente
    private BufferedImage gameBoardPartImage;
    private BufferedImage redTokenImage;
    private BufferedImage yellowTokenImage;
    private JPanel gameBoardPanel = new JPanel();
    
    // UI Konstanten
    private final Dimension tokenSize = new Dimension(70, 70);
    private Dimension defaultWindowDimension = new Dimension(720, 480);
    
    // Multithreading
    private boolean running;
    private Thread thread;
    
    // Connect Four logic
    private ConnectFourController controller;
    private IPlayerListener uiUpdater;
    
    // events
    private List<IGUIListener> listeners = new ArrayList<IGUIListener>();
    
    /**
     * Creates new form ConnectFourGUI
     */
    public ConnectFourGUI() {
        initComponents();
        
        //setUndecorated(true);
        //setResizable(false);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);        
        
        // JFrame in der Mitte des Bildschirmes
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);
        
        try {
            yellowTokenImage = ImageIO.read(getClass().getResource("resources/YellowToken.png")); 
            redTokenImage = ImageIO.read(getClass().getResource("resources/RedToken.png"));
            gameBoardPartImage = ImageIO.read(getClass().getResource("resources/GameBoardPart.png"));
        } catch (IOException ioe) {
            //@todo: handle IOException
        }
        
        centerPanel.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null && controller.isGameRunning()){
                    boolean isCurrentPlayersTurn = controller.getGameModel().getCurrentPlayer() == controller.getThisPlayer();
                    if (isCurrentPlayersTurn
                            || controller.getEnemyPlayer() instanceof LocalPlayer)
                    {
                        Point p = e.getPoint();
                        int column = getTokenColumn(p);
                        if (isCurrentPlayersTurn){
                            for(IGUIListener listener : listeners){
                                listener.moveMade(controller.getGameBoard(), column, controller.getThisPlayer().getTokenColor());
                            }
                        }
                        else{
                            for(IGUIListener listener : listeners){
                                listener.moveMade(controller.getGameBoard(), column, controller.getEnemyPlayer().getTokenColor());
                            }
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
                
        registerLocalPlayer();
        
        controller.addListener(new IControllerListener() {

            @Override
            public void moveMade(GameBoard board, int column, TokenColor tokenColor) {
                // es gibt momentan nur ein einziges Board, daher gibt es keinen Switch auf das GameBoard.
                drawGameBoard(board);
            }

            @Override
            public void enemyPlayerWonAGame(GameBoard board) {
                drawGameBoard(board);
                displayWinner(controller.getEnemyPlayer());
            }

            @Override
            public void thisPlayerWonAGame(GameBoard board) {
                drawGameBoard(board);
                displayWinner(controller.getThisPlayer());
            }
            
            @Override
            public void newGame(GameBoard board) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    private void displayWinner(Player player) {
        titleLabel.setText("Player: " + player.getNickname() + " has won!");
        titleLabel.setForeground(player.getTokenColor() == TokenColor.Yellow ? Color.YELLOW : Color.RED);
    }
    
    private void addListener(IGUIListener listener){
        listeners.add(listener);
    }
    
    public final void registerLocalPlayer(){
        if (controller ==  null){
            Player thisPlayer = null;
            String defaultNickname = "";
            try {
                String localMachineName = java.net.InetAddress.getLocalHost().getHostName();
                String localUserName = System.getProperty("user.name");
                defaultNickname = localUserName + " spielt auf " + localMachineName;
            } catch (UnknownHostException e) {
            }

            if (!defaultNickname.equals("")){
                int response = JOptionPane.showConfirmDialog(null, "Möchtest du " + defaultNickname + " heissen?", "Wer bist du?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION){
                  thisPlayer = new LocalPlayer(defaultNickname, TokenColor.Yellow);
                }
            }

            while (thisPlayer == null){
                String playerName = (String)JOptionPane.showInputDialog(null, "Nicknamen eingeben: ", "Wer bist du?", JOptionPane.PLAIN_MESSAGE);
                if (playerName != null && !playerName.equals("")){
                    thisPlayer = new LocalPlayer(playerName, TokenColor.Yellow);
                }
            }
            controller = new ConnectFourController(thisPlayer);
        }
    }
    
    public void insertToken(int column, TokenColor tokenColor){
        Point tokenPosition = getNextTokenPosition(column);
        this.getGraphics().drawImage((tokenColor == TokenColor.Red ? redTokenImage : yellowTokenImage), tokenPosition.x, tokenPosition.y, this);
    }
    
    public Point getNextTokenPosition(int column){
        int xCoord = gameBoardPanel.getX() + (column * (int)tokenSize.getWidth());
        int row = controller.getGameBoard().getTokenRow(column);
        int yCoord = (gameBoardPanel.getY() + (row * (int)tokenSize.getHeight()));
        return new Point(xCoord, yCoord);
    }
    
    public int getTokenColumn(Point mousePosition){
        return 0; // @todo: hole colimn index von der maus position.
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
        
    }
    
    public void drawGameBoard(GameBoard gameBoard) {
        centerPanel.removeAll();
        this.repaint();

        int width = gameBoard.getBoard().length;
        int height = gameBoard.getBoard()[0].length;
        gameBoardPanel = new JPanel();
        gameBoardPanel.setBounds(0, 0, tokenSize.width * width, tokenSize.height * height);
        for (int columnCount = 0; columnCount < width; columnCount++){
            for (int rowCount = 0; rowCount < height; rowCount++){
                TokenColor currentTokenColor = gameBoard.getBoard()[columnCount][rowCount].getTokenColor();
                JPanel jp = new JPanel();
                jp.setBounds(columnCount * tokenSize.width, rowCount * tokenSize.width, tokenSize.width, tokenSize.height);
                //jp.getGraphics().drawImage(currentTokenColor == TokenColor.Red ? redTokenImage : yellowTokenImage, tokenSize.width, tokenSize.height, null);
                JLabel label = new JLabel();
                label.setBounds(0,0, tokenSize.width, tokenSize.height);
                label.setText(currentTokenColor.name());
                jp.add(label);
            }    
        }
        centerPanel.add(gameBoardPanel);
        this.repaint();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        centerPanel = new javax.swing.JPanel();
        startGomputerGame = new javax.swing.JButton();
        startLocalMultiplayerGame = new javax.swing.JButton();
        startLANGame = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(720, 405));
        setMinimumSize(new java.awt.Dimension(720, 405));
        setPreferredSize(new java.awt.Dimension(720, 405));

        titleLabel.setText("Willkommen zu Viergewinnt!");
        getContentPane().add(titleLabel, java.awt.BorderLayout.PAGE_START);

        centerPanel.setLayout(new java.awt.GridLayout(1, 0));

        startGomputerGame.setText("Computer");
        startGomputerGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startGomputerGameActionPerformed(evt);
            }
        });
        centerPanel.add(startGomputerGame);

        startLocalMultiplayerGame.setText("Local Multiplayer");
        startLocalMultiplayerGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startLocalMultiplayerGameActionPerformed(evt);
            }
        });
        centerPanel.add(startLocalMultiplayerGame);

        startLANGame.setText("LAN");
        startLANGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startLANGameActionPerformed(evt);
            }
        });
        centerPanel.add(startLANGame);

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startGomputerGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startGomputerGameActionPerformed
        if (controller != null
            && !controller.isGameRunning()) {
            ComputerLevel[] levels = ComputerLevel.values();
            ComputerLevel computerLevel = (ComputerLevel) JOptionPane.showInputDialog(null,
                "Welches Level soll der Gegner haben?",
                "Computer Level wählen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                null,
                levels,
                levels[0]);
            if (computerLevel != null){
                controller.newComputerGame(uiUpdater, computerLevel);
                drawGameBoard(controller.getGameBoard());
            }
        }
    }//GEN-LAST:event_startGomputerGameActionPerformed

    private void startLANGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startLANGameActionPerformed
        if (controller != null
            && !controller.isGameRunning()) {
            controller.newLocalGame(new LocalPlayer("Couch Buddy", controller.getThisPlayer().getEnemyColor()));
        }
        drawGameBoard(controller.getGameBoard());
    }//GEN-LAST:event_startLANGameActionPerformed

    private void startLocalMultiplayerGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startLocalMultiplayerGameActionPerformed
        if (controller != null
            && !controller.isGameRunning()) {
            Object[] hostOrJoinOptions = {"Host", "Join"};
            int hostOrJoin = JOptionPane.showOptionDialog(null,
                "Welches Level soll der Gegner haben?",
                "Computer Level wählen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                hostOrJoinOptions,
                hostOrJoinOptions[0]);
            switch (hostOrJoin){
                case -1: // rotes kreuz
                    break;
                case 0: // host
                    controller.hostLanGame();
                    break;
                case 1: // join
                    String ip = "";
                    Pattern ipPattern = Pattern.compile("[0-255](\\.[0-255]){3}");
                    while (ipPattern.matcher(ip).matches()){
                        ip = (String)JOptionPane.showInputDialog(null, "Geben sie die IP vom Host an: ", "IP Adresse angeben", JOptionPane.YES_OPTION);
                    }
                    
                    controller.joinLanGame(ip);
                    break;
            }
        }
        drawGameBoard(controller.getGameBoard());
    }//GEN-LAST:event_startLocalMultiplayerGameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel centerPanel;
    private javax.swing.JButton startGomputerGame;
    private javax.swing.JButton startLANGame;
    private javax.swing.JButton startLocalMultiplayerGame;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
