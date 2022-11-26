import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * <h1>BSGamePanel</h1>
 * This class allows users to access the game panel conponents of the Battleship
 * game
 * <p>
 * The player boards are loaded to the game panel screen
 * <p>
 * Water tiles and ships can be drawn onto the board
 * <p>
 * Explosion and water splash animations can be drawn onto the board
 * <p>
 * This class is meant to be used in Java Swing/AWT programs
 * 
 * @author Lecia Cheng, Emily Kim, Amanda Tong
 * @version 1.0
 * @since 2022-01-27
 */
public class BSGamePanel extends JPanel implements ActionListener {

	// properties
	JTextArea thechat = new JTextArea("");
	JTextField thetexttosend = new JTextField("Enter chat here...");
	JButton thestart = new JButton("Start");
	JButton endturn = new JButton("Confirm Drop?");
	JButton restart = new JButton("Return to Main Screen");
	JButton returntomain = new JButton("Back");
	JTextArea thecoordsreceived = new JTextArea("Selected Grid \nCoordinates Will Appear \nHere...");
	JLabel winnerorloser = new JLabel();
	JScrollPane theScroll;
	Color backgroundcolor = Color.WHITE;
	int intNumberOfShips = 5;

	boolean blngetboardString = false;
	boolean blnafterEndTurn = true;

	boolean blnMoveShipsAway = false;

	BufferedImage ship0;
	BufferedImage ship0board;
	BufferedImage ship1;
	BufferedImage ship1board;
	BufferedImage ship2;
	BufferedImage ship2board;
	BufferedImage ship3;
	BufferedImage ship3board;
	BufferedImage ship4;
	BufferedImage ship4board;
	BufferedImage water;
	BufferedImage hit;
	BufferedImage missed;
	BufferedImage changedship0;
	BufferedImage changedship1;
	BufferedImage changedship2;
	BufferedImage changedship3;
	BufferedImage changedship4;
	int shipselected;
	String strPlayerBoard[][] = new String[9][9];
	String strEnemyBoard[][] = new String[9][9];
	// 0 -> not hit, 1 -> playing animation, 2 -> hit played
	int blnPlayerHitPlayed[][] = new int[9][9];
	Timer timerPlayerHitPlayed[][] = new Timer[9][9];
	int blnEnemyHitPlayed[][] = new int[9][9];
	Timer timerEnemyHitPlayed[][] = new Timer[9][9];
	int blnPlayerSplashPlayed[][] = new int[9][9];
	Timer timerPlayerSplashPlayed[][] = new Timer[9][9];
	int blnEnemySplashPlayed[][] = new int[9][9];
	Timer timerEnemySplashPlayed[][] = new Timer[9][9];
	String strboard1Final;
	String strboard2Final;
	int intWhosTurnIsIt = 1;

	Timer thetimer = new Timer(1000 / 60, this);
	int intPlayerNum;

	Ship[] shipArray = new Ship[intNumberOfShips];

	/**
	 * @param evt
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == thetimer) {
			this.repaint();
		}
	}

	/**
	 * @param g
	 */
	// method
	public void paintComponent(Graphics g) {
		g.drawImage(BATTLESHIP.theTheme.imggamescreen, 0, 0, this);

		// draw board tiles
		for (int intRow = 0; intRow < 9; intRow++) {
			for (int intCol = 0; intCol < 9; intCol++) {

				// Player Board
				int intX = 80 + intCol * 40;
				int intY = 40 + intRow * 40;

				String[] strFullTile = strPlayerBoard[intRow][intCol].split(";");
				String strTile = strFullTile[0];

				if (strTile.equals("water")) {
					g.drawImage(water, intX, intY, null);
				} else if (strTile.equals("ship0board")) {
					g.drawImage(ship0board, intX, intY, null);
				} else if (strTile.equals("ship1board")) {
					g.drawImage(ship1board, intX, intY, null);
				} else if (strTile.equals("ship2board")) {
					g.drawImage(ship2board, intX, intY, null);
				} else if (strTile.equals("ship3board")) {
					g.drawImage(ship3board, intX, intY, null);
				} else if (strTile.equals("ship4board")) {
					g.drawImage(ship4board, intX, intY, null);
				}
				if (strFullTile.length == 2) {
					if (strFullTile[1].equals("hit")) {
						if (blnPlayerHitPlayed[intRow][intCol] == 0) {
							blnPlayerHitPlayed[intRow][intCol] = 1;
							BSExplosionAnimation explosionAnimation = new BSExplosionAnimation();
							explosionAnimation.setLocation(intX, intY);
							add(explosionAnimation);
							final int intRowInLambda = intRow;
							final int intColInLambda = intCol;
							timerPlayerHitPlayed[intRow][intCol] = new Timer(3000, (evt) -> {
								timerPlayerHitPlayed[intRowInLambda][intColInLambda].stop();
								remove(explosionAnimation);
								blnPlayerHitPlayed[intRowInLambda][intColInLambda] = 2;
							});
							timerPlayerHitPlayed[intRow][intCol].start();
						} else if (blnPlayerHitPlayed[intRow][intCol] == 2) {
							g.drawImage(hit, intX, intY, null);
						}
					}
					if (strFullTile[1].equals("miss")) {
						if (blnEnemySplashPlayed[intRow][intCol] == 0) {
							blnEnemySplashPlayed[intRow][intCol] = 1;
							BSWaterAnimation waterAnimation = new BSWaterAnimation();
							waterAnimation.setLocation(intX, intY);
							add(waterAnimation);
							final int intRowInLambda = intRow;
							final int intColInLambda = intCol;
							timerEnemySplashPlayed[intRow][intCol] = new Timer(3000, (evt) -> {
								timerEnemySplashPlayed[intRowInLambda][intColInLambda].stop();
								remove(waterAnimation);
								blnEnemySplashPlayed[intRowInLambda][intColInLambda] = 2;
							});
							timerEnemySplashPlayed[intRow][intCol].start();
						} else if (blnEnemySplashPlayed[intRow][intCol] == 2) {
							g.drawImage(missed, intX, intY, null);
						}
					}
				}

				// Enemy Board
				intX = 840 + intCol * 40;
				intY = 40 + intRow * 40;

				String[] strFullTileEnemy = strEnemyBoard[intRow][intCol].split(";");
				String strTileEnemy = strFullTileEnemy[0];
				for (int a = 0; a < 5; a++) {
					if (strTileEnemy.equals("water") || strTileEnemy.equals("ship" + a + "board")) {
						g.drawImage(water, intX, intY, null);
					}
				}
				if (strFullTileEnemy.length == 2) {
					if (strFullTileEnemy[1].equals("hit")) {
						if (blnEnemyHitPlayed[intRow][intCol] == 0) {
							blnEnemyHitPlayed[intRow][intCol] = 1;
							BSExplosionAnimation explosionAnimation = new BSExplosionAnimation();
							explosionAnimation.setLocation(intX, intY);
							add(explosionAnimation);
							final int intRowInLambda = intRow;
							final int intColInLambda = intCol;
							timerEnemyHitPlayed[intRow][intCol] = new Timer(3000, (evt) -> {
								timerEnemyHitPlayed[intRowInLambda][intColInLambda].stop();
								remove(explosionAnimation);
								blnEnemyHitPlayed[intRowInLambda][intColInLambda] = 2;
							});
							timerEnemyHitPlayed[intRow][intCol].start();
						} else if (blnEnemyHitPlayed[intRow][intCol] == 2) {
							g.drawImage(hit, intX, intY, null);
						}
					}
					if (strFullTileEnemy[1].equals("miss")) {
						if (blnEnemySplashPlayed[intRow][intCol] == 0) {
							blnEnemySplashPlayed[intRow][intCol] = 1;
							BSWaterAnimation waterAnimation = new BSWaterAnimation();
							waterAnimation.setLocation(intX, intY);
							add(waterAnimation);
							final int intRowInLambda = intRow;
							final int intColInLambda = intCol;
							timerEnemySplashPlayed[intRow][intCol] = new Timer(3000, (evt) -> {
								timerEnemySplashPlayed[intRowInLambda][intColInLambda].stop();
								remove(waterAnimation);
								blnEnemySplashPlayed[intRowInLambda][intColInLambda] = 2;
							});
							timerEnemySplashPlayed[intRow][intCol].start();
						} else if (blnEnemySplashPlayed[intRow][intCol] == 2) {
							g.drawImage(missed, intX, intY, null);
						}
					}
				}
				//
			}
		}

		// Dont draw these after finish confirming ship locations? i think this is
		// covering the hit images
		if (blnafterEndTurn == true) {
			// drawing the initial ships
			for (int i = 0; i < intNumberOfShips; i++) {
				if (shipArray[i].isHorizontal) {
					g.drawImage(shipArray[i].shipImageChanged, shipArray[i].intX, shipArray[i].intY, null);
				} else {
					g.drawImage(shipArray[i].shipImage, shipArray[i].intX, shipArray[i].intY, null);
				}
			}
		}

	}

	/**
	 * Converts the array holding data for the player boards into a string
	 * <p>
	 * After board data has been converted into a string, data is ready to be sent
	 * over the network
	 * <p>
	 * You can use this to send over locations of where ships are placed, where
	 * players have dropped a bomb
	 * <p>
	 * 
	 * @param getMyOwnBoardString String to be sent over network
	 */
	public String getMyOwnBoardString() {
		System.out.println(Arrays.deepToString(strPlayerBoard));

		String strMessageSendToSSM = "";
		String[] strMyLine;

		for (int intRow = 0; intRow < 9; intRow++) {
			strMyLine = strPlayerBoard[intRow];
			String strFormattedLine = Arrays.toString(strMyLine).replace("[", "").replace("]", "").replace(",", "-")
					.replace(" ", "");
			strMessageSendToSSM += strFormattedLine;
			if (intRow != 8) {
				strMessageSendToSSM += ",";
			}
		}
		return strMessageSendToSSM;
	}

	// constructor
	/**
	 * BSGamePanel Constructor
	 * <p>
	 * loads the water, hit, miss, adn ship images
	 * <p>
	 */
	public BSGamePanel() throws Exception {
		super();
		for (int i = 0; i < intNumberOfShips; i++) {
			shipArray[i] = new Ship(i);
		}
		try {
			ship0board = ImageIO.read(new File("ship0board.png"));
			ship1board = ImageIO.read(new File("ship1board.png"));
			ship2board = ImageIO.read(new File("ship2board.png"));
			ship3board = ImageIO.read(new File("ship3board.png"));
			ship4board = ImageIO.read(new File("ship4board.png"));
			water = ImageIO.read(new File("water.png"));
			hit = ImageIO.read(new File("hit.png"));
			missed = ImageIO.read(new File("missed.png"));
			changedship0 = ImageIO.read(new File("changedship0.png"));
			changedship1 = ImageIO.read(new File("changedship1.png"));
			changedship2 = ImageIO.read(new File("changedship2.png"));
			changedship3 = ImageIO.read(new File("changedship3.png"));
			changedship4 = ImageIO.read(new File("changedship4.png"));

		} catch (IOException e) {
			System.out.println("Unable to load image");
		}

		// Initialize Boards
		for (int n = 0; n < 9; n++) {
			for (int m = 0; m < 9; m++) {
				strPlayerBoard[n][m] = ("water");
				strEnemyBoard[n][m] = ("water");
			}
		}
	}

}
