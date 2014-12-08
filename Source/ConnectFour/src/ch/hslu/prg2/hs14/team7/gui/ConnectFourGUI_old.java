package ch.hslu.prg2.hs14.team7.gui;

import ch.hslu.prg2.hs14.team7.ConnectFourController;
import ch.hslu.prg2.hs14.team7.GameBoard;
import ch.hslu.prg2.hs14.team7.GameModel;
import ch.hslu.prg2.hs14.team7.TokenColor;
import ch.hslu.prg2.hs14.team7.player.ComputerLevel;
import ch.hslu.prg2.hs14.team7.player.ComputerPlayer;
import ch.hslu.prg2.hs14.team7.player.ILanPlayerListener;
import ch.hslu.prg2.hs14.team7.player.IPlayerListener;
import ch.hslu.prg2.hs14.team7.player.LocalPlayer;
import ch.hslu.prg2.hs14.team7.player.Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class ConnectFourGUI_old extends JFrame implements Runnable {
    
    // UI Controls
    //private BufferedImage gameBoardImage;
    private BufferedImage gameBoardPartImage;
    private BufferedImage redTokenImage;
    private BufferedImage yellowTokenImage;
    private JPanel gameBoardPanel;
    
    // UI constants
    private final Dimension tokenSize = new Dimension(70, 70);
    private Dimension defaultWindowDimension = new Dimension(720, 480);
    
    // Multithreading
    private boolean running;
    private Thread thread;
    
    // Connect Four logic
    private ConnectFourController controller;
    private IPlayerListener uiUpdater;
    
    public ConnectFourGUI_old(){
        
        //setUndecorated(true);
        //setResizable(false);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(defaultWindowDimension);
        setSize(defaultWindowDimension);
        setMinimumSize(defaultWindowDimension);
        setVisible(true);
        setLayout(new BorderLayout());
        
        
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
        
        // game board UI control
        gameBoardPanel = new JPanel();
        rootPane.add(gameBoardPanel, BorderLayout.CENTER);
        gameBoardPanel.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null && controller.isGameRunning()){
                    boolean isCurrentPlayersTurn = controller.getGameModel().getCurrentPlayer() == controller.getThisPlayer();
                    if (isCurrentPlayersTurn
                            || controller.getEnemyPlayer() instanceof LocalPlayer)
                    {
                        Point p = e.getPoint();
                        if (isCurrentPlayersTurn)
                            controller.getThisPlayer().makeMove(controller.getGameBoard());
                        else
                            controller.getEnemyPlayer().makeMove(controller.getGameBoard());
                    }
                }
                else{
                    startNewGame();
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
        
        uiUpdater = new IPlayerListener() {
            @Override
            public void moveMade(GameBoard gameBoard, int col) {
                insertToken(col, controller.getGameModel().getCurrentPlayer().getTokenColor()); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        JButton startComputerGameButton = new JButton();
        startComputerGameButton.setText("Start Computer Game");
        gameBoardPanel.add(startComputerGameButton);
        
        //paintComponents(this.getGraphics());
    }
    
    public void startNewGame(){
        if (controller ==  null){
            Player thisPlayer = null;
            String defaultNickname = "";
            try {
                String localMachineName = java.net.InetAddress.getLocalHost().getHostName();
                String localUserName = System.getProperty("user.name");
                defaultNickname = localUserName + " on " + localMachineName;
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
                String playerName = (String)JOptionPane.showInputDialog(null, "Nicknamen eingeben: ", "Wer bist du?", JOptionPane.YES_OPTION);
                if (!playerName.equals("")){
                    thisPlayer = new LocalPlayer(playerName, TokenColor.Yellow);
                }
            }

            controller = new ConnectFourController(thisPlayer);
        }

        if (!controller.isGameRunning()){
            Object[] options = {"Computer",
            "Couch Spieler",
            "LAN Spieler"};
            String answer = (String)JOptionPane.showInputDialog(gameBoardPanel.getParent(),
                "Gegen wen möchten Sie sich duellieren?",
                "Zeit für ein D-D-D-Duell!",
                JOptionPane.YES_NO_CANCEL_OPTION,
                null,
                options,
                options[0]);

            switch (answer){
                case "": // rotes kreuz gedrückt.

                    break;
                case "Computer":
                    ComputerLevel[] levels = ComputerLevel.values();
                    ComputerLevel computerLevel = (ComputerLevel) JOptionPane.showInputDialog(gameBoardPanel,
                        "Welches Level soll der Gegner haben?",
                        "Computer Level wählen",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        null,
                        levels,
                        levels[0]);
                    controller.newComputerGame(uiUpdater, computerLevel);
                    break;
                case "Couch Multiplayer":
                    controller.newLocalGame(new LocalPlayer("Couch Buddy", controller.getThisPlayer().getEnemyColor()));
                    break;
                case "LAN Spiel":
                    Object[] hostOrJoinOptions = {"Host", "Join"};
                    int hostOrJoin = JOptionPane.showOptionDialog(gameBoardPanel,
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
                            break;
                        case 1: // join
                            break;
                    }
                    break;
            }
        }
    }
    
    /**
     *
     * @param column
     * @param tokenColor
     */
    public void insertToken(int column, TokenColor tokenColor){
        Point tokenPosition = getNextTokenPosition(column);
        this.getGraphics().drawImage((tokenColor == TokenColor.Red ? redTokenImage : yellowTokenImage), tokenPosition.x, tokenPosition.y, this);
    }
    
    /**
     *
     * @param column 0-basierender Index für die Spalte.
     * @return
     */
    public Point getNextTokenPosition(int column){
        int xCoord = gameBoardPanel.getX() + (column * (int)tokenSize.getWidth());
        int row = controller.getGameBoard().getTokenRow(column);
        int yCoord = (gameBoardPanel.getX() + (row * (int)tokenSize.getHeight()));
        return new Point(xCoord, yCoord);
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
    
}
