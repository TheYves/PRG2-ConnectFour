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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Yves Hohl (yves.hohl@stud.hslu.ch) on 12.12.2014.
 */
public class ConnectFourGUI extends Canvas implements Runnable {

	public static final int WIDTH = 885;
	public static final int HEIGHT = 600;
	public static final String TITLE = "Vier Gewinnt";
	private JFrame frame;

	private boolean running = false;
	private boolean locked = false;
	private Thread thread;
	private int playingPlayer = 1;
	boolean isWin = false;

	private BufferedImage background;
	private BufferedImage overlay;
	private BufferedImage foreground;
	private BufferedImage player01Text;
	private BufferedImage player02Text;
	private BufferedImage playerPcText;
	private BufferedImage player01WinnerText;
	private BufferedImage player02WinnerText;
	private BufferedImage playerPcWinnerText;
	private BufferedImage girl01;
	private BufferedImage girl02;
	private BufferedImage[] stoneImages = new BufferedImage[5];

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

		String[] gameModes = {"Gegen PC", "2 Player - am PC", "LAN Server", "LAN Client"};
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
		//frame.setSize(WIDTH+200, HEIGHT);
		frame.setVisible(true);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent keyEvent) {
				super.keyTyped(keyEvent);
				if(gameModel.getCurrentPlayer() instanceof LocalPlayer) {
					char charcharBings =  keyEvent.getKeyChar();
					if(Character.isDigit(charcharBings)){
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
			foreground = ImageIO.read(getClass().getResource("resources/foreground.png"));
			player01Text = ImageIO.read(getClass().getResource("resources/player_01_text.png"));
			player02Text = ImageIO.read(getClass().getResource("resources/player_02_text.png"));
			playerPcText = ImageIO.read(getClass().getResource("resources/player_Pc_text.png"));
			player01WinnerText = ImageIO.read(getClass().getResource("resources/player_01_sieger.png"));
			player02WinnerText = ImageIO.read(getClass().getResource("resources/player_02_sieger.png"));
			playerPcWinnerText = ImageIO.read(getClass().getResource("resources/player_Pc_sieger.png"));
			girl01 = ImageIO.read(getClass().getResource("resources/girl_01.png"));
			girl02 = ImageIO.read(getClass().getResource("resources/girl_02.png"));
			stoneImages[1] = ImageIO.read(getClass().getResource("resources/player01.png"));
			stoneImages[2] = ImageIO.read(getClass().getResource("resources/player02.png"));
			stoneImages[3] = ImageIO.read(getClass().getResource("resources/player01_lowlight.png"));
			stoneImages[4] = ImageIO.read(getClass().getResource("resources/player02_lowlight.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		controller.addListener(new IControllerListener() {
			@Override
			public void moveMade(GameBoard board, Player nextPlayer) {
			}

			@Override
			public void gameFinished(GameBoard board, Player winner) {

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
		//g.drawImage(girl01, -35, 200, this);
		//g.drawImage(girl02, 635, 230, this);

		/*boolean isAnimated = false;
		for (int idx = 0; idx < stones.size(); idx++) {
			stones.get(idx).render(g);
			if (stones.get(idx).isMoving()) {
				isAnimated = true;
			}
		}
		setLocked(isAnimated);*/

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
						tokenImage = stoneImages[1];
						break;
					case Yellow:
						tokenImage = stoneImages[2];
						break;
				}

				if (tokenImage != null){
					g.drawImage(tokenImage, foregroundOffsetX + 20 + (70 * columnCount + 20), foregroundOffsetY + 24 + (70 * currentRow + 20), this);
				}
			}
		}

		g.drawImage(foreground, foregroundOffsetX, foregroundOffsetY, this);

		/*if (isWin) {
			switch (gameMode) {
				case Player_PC:
					if (playingPlayer - 1 == 1) {
						g.drawImage(player01WinnerText, 20, 10, this);
					} else {
						g.drawImage(playerPcWinnerText, 20, 10, this);
					}
					break;
				case Player_Player:
					if (playingPlayer - 1 == 1) {
						g.drawImage(player01WinnerText, 20, 10, this);
					} else {
						g.drawImage(player02WinnerText, 20, 10, this);
					}
					break;
			}
		} else {
			switch (gameMode) {
				case Player_PC:
					if (playingPlayer == 1) {
						g.drawImage(player01Text, 20, 10, this);
					} else {
						g.drawImage(playerPcText, 20, 10, this);
					}
					break;
				case Player_Player:
					if (playingPlayer == 1) {
						g.drawImage(player01Text, 20, 10, this);
					} else {
						g.drawImage(player02Text, 20, 10, this);
					}
					break;
			}
		}*/

		g.dispose();
		bs.show();
	}


/*
	public synchronized void insertStone(int col, int row, int player) {
		if (player == 1 || player == 2) {
			BufferedImage stoneImage = stoneImages[player];
			Spielstein newStone = new Spielstein(col, row, player, stoneImage, this);
			stones.add(newStone);
			switch (newStone.getPlayer()) {
				case 1:
					playingPlayer = 2;
					break;
				case 2:
					playingPlayer = 1;
					break;
			}
		} else {
			System.out.println("Player \"" + player + "\" does not exist.");
		}
	}



	public synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException ex) {
			Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.exit(1);
	}



	public void tick() {
		for (int idx = 0; idx < stones.size(); idx++) {
			stones.get(idx).tick();
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
		g.drawImage(girl01, -35, 200, this);
		g.drawImage(girl02, 635, 230, this);

		boolean isAnimated = false;
		for (int idx = 0; idx < stones.size(); idx++) {
			stones.get(idx).render(g);
			if (stones.get(idx).isMoving()) {
				isAnimated = true;
			}
		}
		setLocked(isAnimated);

		g.drawImage(foreground, 175, 156, this);

		if (isWin) {
			switch (gameMode) {
				case Player_PC:
					if (playingPlayer - 1 == 1) {
						g.drawImage(player01WinnerText, 20, 10, this);
					} else {
						g.drawImage(playerPcWinnerText, 20, 10, this);
					}
					break;
				case Player_Player:
					if (playingPlayer - 1 == 1) {
						g.drawImage(player01WinnerText, 20, 10, this);
					} else {
						g.drawImage(player02WinnerText, 20, 10, this);
					}
					break;
			}
		} else {
			switch (gameMode) {
				case Player_PC:
					if (playingPlayer == 1) {
						g.drawImage(player01Text, 20, 10, this);
					} else {
						g.drawImage(playerPcText, 20, 10, this);
					}
					break;
				case Player_Player:
					if (playingPlayer == 1) {
						g.drawImage(player01Text, 20, 10, this);
					} else {
						g.drawImage(player02Text, 20, 10, this);
					}
					break;
			}
		}

		g.dispose();
		bs.show();
	}

	public boolean isLocked() {
		return locked;
	}

	public void highlight(ArrayList<Spielstein> winnerStones) {
		for (Spielstein stone : stones) {
			boolean isWinnerStone = false;
			for (Spielstein winnerStone : winnerStones) {
				if (stone.getCol() == winnerStone.getCol()
						&& stone.getRow() == winnerStone.getRow()) {
					isWinnerStone = true;
					break;
				}
			}
			if (!isWinnerStone) {
				if (stone.getPlayer() == 1) {
					stone.setImage(stoneImages[3]);
				} else {
					stone.setImage(stoneImages[4]);
				}
			}
		}
		isWin = true;
	}

	public Thread getThread() {
		return thread;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	*/

}
