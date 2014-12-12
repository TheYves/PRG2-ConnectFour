package ch.hslu.prg2.hs14.team7.gui;

import ch.hslu.prg2.hs14.team7.*;
import ch.hslu.prg2.hs14.team7.player.LocalPlayer;
import ch.hslu.prg2.hs14.team7.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Yves Hohl (yves.hohl@stud.hslu.ch) on 12.12.2014.
 */
public class ConnectFourGUI extends Canvas implements Runnable {

	public static final int WIDTH = 885;
	public static final int HEIGHT = 634;
	public static final String TITLE = "Vier Gewinnt";
	private JFrame frame;

	private Thread thread;
	Player winner = null;

	private BufferedImage background, overlay, grid, turnYellow, turnRed, turnComputer, wonRed, wonYellow,
			wonComputer, tokenRed, tokenYellow;

	private GameModel gameModel;
	private ConnectFourController controller;

	public ConnectFourGUI(ConnectFourController ctrl, GameModel gameModel) {
		this.gameModel = gameModel;
		this.controller = ctrl;
	}

	public void selectGameMode() {

		GameModel.GameMode gameMode;
		final JDialog modesDialog = new JDialog();
		modesDialog.setTitle(TITLE + " - Modus auswählen");
		modesDialog.setModal(true);
		modesDialog.setLayout(new GridLayout(2, 1));
		modesDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JPanel titlePanel = new JPanel();
		JLabel titleabel = new JLabel("Vier Gewinnt - Bitte Spielemodus auswählen");

		String[] gameModes = {"Gegen Computer", "2 Spieler lokal", "LAN Server", "LAN Client"};
		JPanel modesPanel = new JPanel();
		JLabel modesLabel = new JLabel("Modus:");
		JComboBox modesCombo = new JComboBox(gameModes);

		JButton start = new JButton("Spiel starten");
		start.setPreferredSize(new Dimension(150, 30));
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modesDialog.dispose();
			}
		});

		titlePanel.add(titleabel);

		modesPanel.add(modesLabel);
		modesPanel.add(modesCombo);
		modesPanel.add(start);

		modesDialog.add(titlePanel);
		modesDialog.add(modesPanel);
		modesDialog.pack();
		modesDialog.setLocationRelativeTo(null);
		modesDialog.setVisible(true);

		switch (modesCombo.getSelectedIndex()) {
			case 0:
				gameMode = GameModel.GameMode.Computer;
				break;
			case 1:
				gameMode = GameModel.GameMode.Local;
				break;
			case 2:
				gameMode = GameModel.GameMode.LANHost;
				break;
			case 3:
				gameMode = GameModel.GameMode.LANClient;
				break;
			default:
				gameMode = GameModel.GameMode.Computer;
				break;
		}

		gameModel.setGameMode(gameMode);
	}


	public void startGame() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		GridLayout layout = new GridLayout(1, 2);
		layout.setHgap(20);

		frame = new JFrame(TITLE);
		frame.setLayout(layout);
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent keyEvent) {
				super.keyTyped(keyEvent);
				if (gameModel.getCurrentPlayer() instanceof LocalPlayer) {
					char charcharBings = keyEvent.getKeyChar();
					if (Character.isDigit(charcharBings)) {
						int col = Character.getNumericValue(charcharBings) - 1;
						if (col >= 0 && col < gameModel.getGameBoard().getBoard().length) {
							((LocalPlayer) gameModel.getCurrentPlayer()).chooseColumn(col);
						}
					}

				}
			}
		});

		try {
			background = ImageIO.read(getClass().getResource("resources/background.jpg"));
			overlay = ImageIO.read(getClass().getResource("resources/overlay.png"));
			grid = ImageIO.read(getClass().getResource("resources/grid.png"));
			turnYellow = ImageIO.read(getClass().getResource("resources/turn_yellow.png"));
			turnRed = ImageIO.read(getClass().getResource("resources/turn_red.png"));
			turnComputer = ImageIO.read(getClass().getResource("resources/turn_computer.png"));
			wonYellow = ImageIO.read(getClass().getResource("resources/end_yellow.png"));
			wonRed = ImageIO.read(getClass().getResource("resources/end_red.png"));
			wonComputer = ImageIO.read(getClass().getResource("resources/end_computer.png"));
			tokenRed = ImageIO.read(getClass().getResource("resources/token_red.png"));
			tokenYellow = ImageIO.read(getClass().getResource("resources/token_yellow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		controller.addListener(new IControllerListener() {
			@Override
			public void gameFinished(GameBoard board, Player winner) {
				ConnectFourGUI.this.winner = winner;
			}
		});

		start();
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while(true) {
			render();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.drawImage(background, 0, 0, this);
		g.drawImage(overlay, 175, 156, this);

		int foregroundOffsetX = 175;
		int foregroundOffsetY = 156;
		Token[][] columns = this.gameModel.getGameBoard().getBoard();
		for (int columnCount = 0; columnCount < columns.length; columnCount++){
			Token[] currentColumn = columns[columnCount];
			for (int rowCount = 0; rowCount < currentColumn.length; rowCount++){
				Token token = currentColumn[rowCount];
				TokenColor currentColor = token.getTokenColor();
				BufferedImage tokenImage = null;
				int currentRow = currentColumn.length - rowCount -1;

				switch (currentColor){
					case Red:
						tokenImage = tokenRed;
						break;
					case Yellow:
						tokenImage = tokenYellow;
						break;
				}

				if (tokenImage != null){
					g.drawImage(tokenImage, foregroundOffsetX + 20 + (70 * columnCount + 20), foregroundOffsetY + 24 + (70 * currentRow + 20), this);
				}
			}
		}

		g.drawImage(grid, foregroundOffsetX, foregroundOffsetY, this);

		if (winner != null) {
			switch (winner.getTokenColor()) {
				case Yellow:
					g.drawImage(wonYellow, 20, 10, this);
					break;
				case Red:
					g.drawImage(wonRed, 20, 10, this);
					break;
			}
		} else {
			switch (gameModel.getCurrentPlayer().getTokenColor()) {
				case Yellow:
					g.drawImage(turnYellow, 20, 10, this);
					break;
				case Red:
					g.drawImage(turnRed, 20, 10, this);
					break;
			}
		}

		g.dispose();
		bs.show();
	}

}
