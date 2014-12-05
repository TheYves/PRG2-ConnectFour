/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.prg2.hs14.team7.gui;

/**
 * @author Pirmin
 */

import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class StartGameDialog extends JDialog
{
    //private String title = "Start new game.";
    
    private JButton startButton = new JButton("START");
    private JPanel titlePanel = new JPanel();
    private JLabel titleLabel = new JLabel("Connect Four - Please Select Mode");
    private String [] gameModes = {"Gegen PC", "2 Player - am PC", "2 Player - LAN"};
    private JPanel modesPanel = new JPanel();
    private JLabel modesLabel = new JLabel("Select Mode:");
    private JComboBox modesCombo = new JComboBox(gameModes);
    
    public StartGameDialog()
    {
        this.setTitle("Spielmodus auswählen");
        this.setLayout(new GridLayout(3,3));
        modesPanel.add(modesCombo);
        this.add(modesPanel);
        this.add(modesLabel);
        this.add(startButton);
        this.add(titleLabel);
        
        this.addWindowListener(new WindowListener(){

            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) { }

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) { }        
        });
        
        pack();
        setSize(400,100);
        setVisible(true);
    }
    

       
}
