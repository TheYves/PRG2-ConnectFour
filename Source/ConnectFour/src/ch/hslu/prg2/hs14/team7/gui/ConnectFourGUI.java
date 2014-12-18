package ch.hslu.prg2.hs14.team7.gui;

import ch.hslu.prg2.hs14.team7.*;
import ch.hslu.prg2.hs14.team7.player.LanPlayer;

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
	public static final int DEFAULT_PORT = 1337;
	public static final String TITLE = "Vier Gewinnt";
	private JFrame frame;
	private JComboBox modesCombo;
	private JTextField ipText;
	private JTextField portText;
	private JDialog modesDialog;

	private Thread thread;

	private BufferedImage background, overlay, grid, turnYellow, turnRed, turnComputer, turnMe, wonRed, wonYellow,
			wonComputer, tokenRed, tokenYellow, connectionLost, waitForPlayer;

	private GameModel gameModel;

	public ConnectFourGUI(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	public void selectGameMode() {

		GameModel.GameMode gameMode;
		modesDialog = new JDialog();
		modesDialog.setTitle(TITLE + " - Modus auswählen");
		modesDialog.setModal(true);
		modesDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel modesDialogPanel = new JPanel(new GridLayout(5, 1, 0, 0));
		modesDialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JPanel titlePanel = new JPanel();
		JLabel titleLabel = new JLabel("Vier Gewinnt - Bitte Spielemodus auswählen");

		String[] gameModes = {"Gegen Computer", "2 Spieler lokal", "LAN Server", "LAN Client"};
		JPanel modesPanel = new JPanel();
		JLabel modesLabel = new JLabel("Modus:");
		modesCombo = new JComboBox(gameModes);

		JPanel ipPanel = new JPanel();
		JLabel ipLabel = new JLabel("IP Adresse:");
		ipText = new JTextField("", 12);
		ipLabel.setLabelFor(ipText);
		ipPanel.setVisible(false);

		JPanel portPanel = new JPanel();
		JLabel portLabel = new JLabel("Port:");
		portText = new JTextField(DEFAULT_PORT + "", 5);
		portLabel.setLabelFor(portText);
		portPanel.setVisible(false);

		modesCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				portPanel.setVisible(false);
				ipPanel.setVisible(false);
				switch (modesCombo.getSelectedIndex()) {
					case 2:
						portPanel.setVisible(true);
						break;
					case 3:
						portPanel.setVisible(true);
						ipPanel.setVisible(true);
						break;
				}
			}
		});

		JButton start = new JButton("Spiel starten");
		start.setPreferredSize(new Dimension(150, 30));
		start.addActionListener(new StartButtonListener());

		titlePanel.add(titleLabel);

		modesPanel.add(modesLabel);
		modesPanel.add(modesCombo);

		ipPanel.add(ipLabel);
		ipPanel.add(ipText);

		portPanel.add(portLabel);
		portPanel.add(portText);

		modesDialogPanel.add(titlePanel);
		modesDialogPanel.add(modesPanel);
		modesDialogPanel.add(ipPanel);
		modesDialogPanel.add(portPanel);
		modesDialogPanel.add(start);

		modesDialog.add(modesDialogPanel);
		modesDialog.pack();
		modesDialog.setLocationRelativeTo(null);
		modesDialog.setVisible(true);
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

		addKeyListener(new GUIKeyListener());
		frame.addKeyListener(new GUIKeyListener());

		try {
			background = ImageIO.read(getClass().getResource("resources/background.jpg"));
			overlay = ImageIO.read(getClass().getResource("resources/overlay.png"));
			grid = ImageIO.read(getClass().getResource("resources/grid.png"));
			turnYellow = ImageIO.read(getClass().getResource("resources/turn_yellow.png"));
			turnRed = ImageIO.read(getClass().getResource("resources/turn_red.png"));
			turnComputer = ImageIO.read(getClass().getResource("resources/turn_computer.png"));
			turnMe = ImageIO.read(getClass().getResource("resources/turn_me.png"));
			wonYellow = ImageIO.read(getClass().getResource("resources/end_yellow.png"));
			wonRed = ImageIO.read(getClass().getResource("resources/end_red.png"));
			wonComputer = ImageIO.read(getClass().getResource("resources/end_computer.png"));
			tokenRed = ImageIO.read(getClass().getResource("resources/token_red.png"));
			tokenYellow = ImageIO.read(getClass().getResource("resources/token_yellow.png"));
			connectionLost = ImageIO.read(getClass().getResource("resources/connection_lost.png"));
			waitForPlayer = ImageIO.read(getClass().getResource("resources/wait_for_player.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		start();
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (true) {
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
		for (int columnCount = 0; columnCount < columns.length; columnCount++) {
			Token[] currentColumn = columns[columnCount];
			for (int rowCount = 0; rowCount < currentColumn.length; rowCount++) {
				Token token = currentColumn[rowCount];
				TokenColor currentColor = token.getTokenColor();
				BufferedImage tokenImage = null;
				int currentRow = currentColumn.length - rowCount - 1;

				switch (currentColor) {
					case Red:
						tokenImage = tokenRed;
						break;
					case Yellow:
						tokenImage = tokenYellow;
						break;
				}

				if (tokenImage != null) {
					g.drawImage(tokenImage, foregroundOffsetX + 20 + (70 * columnCount + 20), foregroundOffsetY + 24 + (70 * currentRow + 20), this);
				}
			}
		}

		g.drawImage(grid, foregroundOffsetX, foregroundOffsetY, this);

		switch (gameModel.getGameState()) {
			case GameOver:
				if (gameModel.getWinner().getTokenColor() == TokenColor.Yellow) {
					g.drawImage(wonYellow, 20, 10, this);
				} else {
					g.drawImage(wonRed, 20, 10, this);
				}
				break;
			case Disconnected:
				g.drawImage(connectionLost, 20, 10, this);
				break;
			case Ready:
				if (gameModel.getCurrentPlayer().equals(gameModel.getThisPlayer()) && gameModel.getEnemyPlayer() instanceof LanPlayer) {
					g.drawImage(turnMe, 20, 10, this);
				} else {
					if (gameModel.getCurrentPlayer().getTokenColor() == TokenColor.Yellow) {
						g.drawImage(turnYellow, 20, 10, this);
					} else {
						g.drawImage(turnRed, 20, 10, this);
					}
				}
				break;
			case WaitForPlayer:
				g.drawImage(waitForPlayer, 20, 10, this);
				break;
		}

		g.dispose();
		bs.show();
	}

	private void showError(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

	private class StartButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			GameModel.GameMode gameMode;
			int port = 0;

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
					gameModel.setIp(ipText.getText());
					break;
				default:
					gameMode = GameModel.GameMode.Computer;
					break;
			}
			gameModel.setGameMode(gameMode);

			// prüfen ob port zahl ist
			if (gameMode == GameModel.GameMode.LANClient || gameMode == GameModel.GameMode.LANHost) {
				try {
					port = Integer.parseInt(portText.getText());
					if (port < 1024 || port > 65535) {
						throw new Exception();
					}
				} catch (Exception e) {
					showError("Bitte gültigen Port zwischen 1024 und 65535 angeben.");
					return;
				}
			}
			gameModel.setPort(port);

			// prüfen ob ip/host angegeben wurde
			if (gameMode == GameModel.GameMode.LANClient && ipText.getText().length() <= 0) {
				showError("Bitte IP oder Hostname angeben.");
				return;
			}
			gameModel.setIp(ipText.getText());

			modesDialog.dispose();
		}
	}

	private class GUIKeyListener extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent keyEvent) {
			super.keyTyped(keyEvent);
			if (gameModel.getGameState() == GameModel.GameState.Ready) {
				char charcharBings = keyEvent.getKeyChar();
				if (Character.isDigit(charcharBings)) {
					int col = Character.getNumericValue(charcharBings) - 1;
					if (col >= 0 && col < gameModel.getGameBoard().getBoard().length) {
						gameModel.getCurrentPlayer().chooseColumn(col);
					}
				}

			}
		}
	}
}
