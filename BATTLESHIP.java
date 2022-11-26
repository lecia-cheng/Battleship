
/*
 *  Lecia Cheng, Emily Kim, Amanda Tong 
 *  Version 2.0
 *  Created on 2022.01.20
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.Math;
import java.util.*;

public class BATTLESHIP implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
	// properties
	JFrame theframe = new JFrame("BattleShip");
	/// Start Panel
	BSStartPanel StartPanel;
	BSGamePanel GamePanel;
	static Themes theTheme;
	BSHelpPanel HelpScreen;

	// connect components
	SuperSocketMaster ssm;
	boolean blnConnected = false;
	String connectArray[] = new String[5];
	String gameArray[] = new String[5];
	String chatArray[] = new String[5];
	String attackArray[] = new String[5];
	String sinkArray[] = new String[5];
	String winArray[] = new String[5];
	String boardArray[] = new String[9];
	String board1Array[] = new String[9];
	boolean blnGameStarted = true;
	int intSecondGridX;
	int intSecondGridY;
	int intTemp;
	int intTotalShips = 15;
	int intGridCoordsX;
	int intGridCoordsY;

	int intShipNum;
	boolean blnShipsRotated;
	boolean blnSpace1;
	boolean blnSpace2;

	int intSelected = 3;

	/**
	 * @param evt
	 */
	// methods
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == StartPanel.theclient) {
			StartPanel.theserver.setEnabled(false);
			StartPanel.theclient.setEnabled(false);
			StartPanel.theip.setEnabled(false);
			ssm = new SuperSocketMaster(StartPanel.theip.getText(), 6112, this);
			boolean gotConnect = ssm.connect();
			if (gotConnect) {
				GamePanel.intPlayerNum = 2;
				StartPanel.thereceiving.append("My Address: " + ssm.getMyAddress() + "\n");
				StartPanel.thereceiving.append("My Hostname: " + ssm.getMyHostname() + "\n");
				StartPanel.thereceiving.append("User " + StartPanel.theusername.getText() + " is connected\n");
				ssm.sendText("connect," + "User " + StartPanel.theusername.getText() + " is connected");

			} else {
				StartPanel.theclient.setEnabled(true);
				StartPanel.theserver.setEnabled(true);
				StartPanel.theip.setEnabled(true);
			}
		} else if (evt.getSource() == StartPanel.theserver) {
			StartPanel.theserver.setEnabled(false);
			StartPanel.theclient.setEnabled(false);
			StartPanel.theip.setEnabled(false);
			StartPanel.theentergame.setEnabled(true);
			ssm = new SuperSocketMaster(6112, this);
			boolean gotConnect = ssm.connect();
			if (gotConnect) {
				GamePanel.intPlayerNum = 1;
				StartPanel.thereceiving.append("My Address: " + ssm.getMyAddress() + "\n");
				StartPanel.thereceiving.append("My Hostname: " + ssm.getMyHostname() + "\n");
				StartPanel.thereceiving.append("User " + StartPanel.theusername.getText() + " is connected\n");
				ssm.sendText("console," + "User " + StartPanel.theusername.getText() + " is connected");

			} else {
				StartPanel.theserver.setEnabled(true);
				StartPanel.theclient.setEnabled(true);
				StartPanel.theip.setEnabled(true);
			}
		}
		if (evt.getSource() == StartPanel.themenuhelp) {
			System.out.println("Help Selected");
			HelpScreen.setLayout(null);
			HelpScreen.setPreferredSize(new Dimension(1280, 720));
			theframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			theframe.setContentPane(HelpScreen);
			theframe.pack();
			theframe.setVisible(true);

			HelpScreen.thechat.append("Drag and drop your ships to the board on the left:)" + "\n");
			HelpScreen.thechat.append(
					"Once you enter the actual game... you will have access to more features such as rotating ships" + "\n");
			HelpScreen.thechat.append("" + "\n");
			HelpScreen.thechat.append("The board on the right holds your enemies ships that you have to sink!" + "\n");
			HelpScreen.thechat.append("Once you are happy with your ships placement, press 'Start'" + "\n");

			HelpScreen.thetexttosend.setLocation(40, 650);
			HelpScreen.thetexttosend.setSize(1200, 20);
			HelpScreen.add(GamePanel.thetexttosend);
			HelpScreen.thetexttosend.addActionListener(this);
			HelpScreen.thechat.setLocation(40, 450);
			HelpScreen.thechat.setSize(1200, 180);
			HelpScreen.add(HelpScreen.thechat);
			HelpScreen.thestart.setLocation(550, 400);
			HelpScreen.thestart.setSize(150, 25);
			HelpScreen.add(HelpScreen.thestart);
			HelpScreen.thestart.addActionListener(this);

			HelpScreen.addMouseMotionListener(this);
			HelpScreen.addMouseListener(this);
			// Scroll
			HelpScreen.theScroll = new JScrollPane(HelpScreen.thechat);
			HelpScreen.theScroll.setSize(1200, 180);
			HelpScreen.theScroll.setLocation(40, 450);
			HelpScreen.add(HelpScreen.theScroll);
			// Confirm Drop
			HelpScreen.endturn.setLocation(550, 400);
			HelpScreen.endturn.setSize(150, 25);
			HelpScreen.add(HelpScreen.endturn);
			HelpScreen.endturn.setVisible(false);
			HelpScreen.endturn.addActionListener(this);
			// Coordinates
			HelpScreen.thecoordsreceived.setLocation(550, 330);
			HelpScreen.thecoordsreceived.setSize(150, 75);
			HelpScreen.add(GamePanel.thecoordsreceived);
			HelpScreen.thecoordsreceived.setVisible(false);
			// Restart
			HelpScreen.restart.setLocation(525, 250);
			HelpScreen.restart.setSize(200, 50);
			HelpScreen.add(GamePanel.restart);
			HelpScreen.restart.setVisible(false);
			HelpScreen.restart.addActionListener(this);

		}
		if (evt.getSource() == StartPanel.theusername) {
			System.out.println("username entered: " + StartPanel.theusername.getText());
			StartPanel.theusername.setEnabled(false);
			StartPanel.thereceiving.append(StartPanel.theusername.getText() + " is your username\n");
		}
		if (evt.getSource() == StartPanel.theip) {
			System.out.println("ip address entered: " + StartPanel.theip.getText());
		}
		if (evt.getSource() == StartPanel.theentergame) {
			System.out.println("game entered"); // players enter game and can select where to place their ships
			GamePanel.setPreferredSize(new Dimension(1280, 720));
			theframe.addKeyListener(this);
			theframe.requestFocus();
			theframe.setContentPane(GamePanel);
			theframe.setResizable(false);
			theframe.pack();
			theframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			theframe.setVisible(true);
			GamePanel.setLayout(null);
			GamePanel.setPreferredSize(new Dimension(1280, 720));
			GamePanel.addMouseMotionListener(this);
			GamePanel.addMouseListener(this);
			// theframe.addKeyListener(this);

			GamePanel.thetexttosend.setLocation(40, 650);
			GamePanel.thetexttosend.setSize(1200, 20);
			GamePanel.add(GamePanel.thetexttosend);
			GamePanel.thetexttosend.addActionListener(this);
			GamePanel.thechat.setLocation(40, 450);
			GamePanel.thechat.setSize(1200, 180);
			GamePanel.add(GamePanel.thechat);
			GamePanel.thestart.setLocation(550, 400);
			GamePanel.thestart.setSize(150, 25);
			GamePanel.add(GamePanel.thestart);
			GamePanel.thestart.addActionListener(this);
			// Scroll
			GamePanel.theScroll = new JScrollPane(GamePanel.thechat);
			GamePanel.theScroll.setSize(1200, 180);
			GamePanel.theScroll.setLocation(40, 450);
			GamePanel.add(GamePanel.theScroll);
			// Confirm Drop
			GamePanel.endturn.setLocation(550, 400);
			GamePanel.endturn.setSize(150, 25);
			GamePanel.add(GamePanel.endturn);
			GamePanel.endturn.setVisible(false);
			GamePanel.endturn.addActionListener(this);
			// Coordinates
			GamePanel.thecoordsreceived.setLocation(550, 330);
			GamePanel.thecoordsreceived.setSize(150, 75);
			GamePanel.add(GamePanel.thecoordsreceived);
			GamePanel.thecoordsreceived.setVisible(false);
			// Restart
			GamePanel.restart.setLocation(525, 250);
			GamePanel.restart.setSize(200, 50);
			GamePanel.add(GamePanel.restart);
			GamePanel.restart.setVisible(false);
			GamePanel.restart.addActionListener(this);

		}
		if (evt.getSource() == StartPanel.theme1) {
			System.out.println("theme 1 selected");
			try {
				theTheme.changeTheme(0);
				StartPanel.repaint();
			} catch (Exception e) {
			}
		}
		if (evt.getSource() == StartPanel.theme2) {
			System.out.println("theme 2 selected");
			try {
				theTheme.changeTheme(1);
				StartPanel.repaint();
			} catch (Exception e) {
			}
		}
		if (evt.getSource() == StartPanel.theme3) {
			System.out.println("theme 3 selected");
			try {
				theTheme.changeTheme(2);
				StartPanel.repaint();
			} catch (Exception e) {
			}
		}
		if (evt.getSource() == GamePanel.thetexttosend) {
			if (ssm != null) {
				ssm.sendText("chat," + StartPanel.theusername.getText() + "," + GamePanel.thetexttosend.getText());
				GamePanel.thechat
						.append(StartPanel.theusername.getText() + ": " + GamePanel.thetexttosend.getText() + "\n");
				System.out.println(GamePanel.thetexttosend.getText());
			}
		}
		if (evt.getSource() == ssm) {

			String[] strIncoming = ssm.readText().split(",");

			for (var str : strIncoming) {
				System.out.println(str);
			}

			if (strIncoming[0].equals("connect")) {
				StartPanel.thereceiving.append(strIncoming[1] + "\n");
			}

			if (strIncoming[0].equals("chat")) {
				GamePanel.thechat.append(strIncoming[1] + ": " + strIncoming[2] + "\n");
			}

			if (strIncoming[0].equals("board")) {
				System.out.println(GamePanel.strPlayerBoard + " " + GamePanel.strEnemyBoard);
				for (int intRow = 0; intRow < 9; intRow++) {
					GamePanel.strEnemyBoard[intRow] = strIncoming[intRow + 1].split("-");
				}
				GamePanel.repaint();
			}
			if (strIncoming[0].equals("attack")) {
				System.out.println("Incoming Data:");
				System.out.println(Arrays.deepToString(strIncoming));

				for (int intRow = 0; intRow < 9; intRow++) {
					GamePanel.strPlayerBoard[intRow] = strIncoming[intRow + 1].split("-");
				}

				System.out.println("About to repaint after incoming attack:");
				System.out.println(Arrays.deepToString(GamePanel.strPlayerBoard));
				GamePanel.repaint();
			}
			if (strIncoming[0].equals("miss")) {
				System.out.println("Incoming Data:");
				System.out.println(Arrays.deepToString(strIncoming));

				for (int intRow = 0; intRow < 9; intRow++) {
					GamePanel.strPlayerBoard[intRow] = strIncoming[intRow + 1].split("-");
				}

				System.out.println("About to repaint after incoming attack:");
				System.out.println(Arrays.deepToString(GamePanel.strPlayerBoard));
				GamePanel.repaint();
			}
			if (strIncoming[0].equals("win")) {
				GamePanel.thechat.append(strIncoming[1] + " has won! Congrats!" + "\n");
				GamePanel.restart.setVisible(true);
			}
		}
		// finalizing ships onto csv file
		if (evt.getSource() == GamePanel.thestart) {
			boolean blnAllShipsValid = true;
			blnGameStarted = false;

			for (int c = 0; c < 5; c++) {
				int intGridCoordsX = GamePanel.shipArray[c].intGridX - 2;
				int intGridCoordsY = GamePanel.shipArray[c].intGridY - 1;

				if (intGridCoordsX < 0 || intGridCoordsY < 0) {
					GamePanel.thestart.enable(false);
					blnAllShipsValid = false;
					System.out.println("Ship " + c + " failed Grid coords: " + intGridCoordsX + " " + intGridCoordsY);
				} else {
					System.out.println("Ship " + c + " replaced Grid coords: " + intGridCoordsX + " " + intGridCoordsY);
					// Replace water tile with ship tile
					if (GamePanel.strPlayerBoard[intGridCoordsY][intGridCoordsX].equals("water")) {
						if (GamePanel.shipArray[c].isHorizontal == false) {
							for (int i = 0; i < GamePanel.shipArray[c].intHeight; i++) {
								for (int j = 0; j < GamePanel.shipArray[c].intWidth; j++) {
									GamePanel.strPlayerBoard[intGridCoordsY + i][intGridCoordsX + j] = "ship" + c
											+ "board";
								}
							}
						} else {
							for (int i = 0; i < GamePanel.shipArray[c].intWidth; i++) {
								for (int j = 0; j < GamePanel.shipArray[c].intHeight; j++) {
									GamePanel.strPlayerBoard[intGridCoordsY + i][intGridCoordsX + j] = "ship" + c
											+ "board";
								}
							}
						}
					}
				}
			}
			// sending player board to enemy board through ssm
			GamePanel.blngetboardString = true;

			if (blnAllShipsValid == true) {
				System.out.println(GamePanel.strPlayerBoard[2][1]);
				String strBoardInfo = GamePanel.getMyOwnBoardString();
				ssm.sendText("board," + strBoardInfo);

				GamePanel.thestart.setVisible(false);
				GamePanel.endturn.setVisible(true);
				GamePanel.thecoordsreceived.setVisible(true);
			}
			// after getting final 2d array
			// pass it to GamePanel.setBoard()
			GamePanel.shipselected = -1;
		}
		if (evt.getSource() == HelpScreen.thestart) {
			boolean blnAllShipsValid = true;
			blnGameStarted = false;

			HelpScreen.thechat.append("" + "\n");
			HelpScreen.thechat.append("Now on the right board click the coordinate you wish to drop a torpedo" + "\n");
			HelpScreen.thechat.append("You will either miss or hit an enemy ship!" + "\n");
			HelpScreen.thechat.append("For the purposes of this tutorial, you are playing against your own board." + "\n");
			HelpScreen.thechat.append("" + "\n");
			HelpScreen.thechat.append("Hope this helps! Enjoy the game~");

			for (int c = 0; c < 5; c++) {
				int intGridCoordsX = HelpScreen.shipArray[c].intGridX - 2;
				int intGridCoordsY = HelpScreen.shipArray[c].intGridY - 1;

				if (intGridCoordsX < 0 || intGridCoordsY < 0) {
					HelpScreen.thestart.enable(false);
					blnAllShipsValid = false;
					System.out.println("Ship " + c + " failed Grid coords: " + intGridCoordsX + " " + intGridCoordsY);
				} else {
					System.out.println("Ship " + c + " replaced Grid coords: " + intGridCoordsX + " " + intGridCoordsY);
					// Replace water tile with ship tile
					if (HelpScreen.strPlayerBoard[intGridCoordsY][intGridCoordsX].equals("water")) {
						if (HelpScreen.shipArray[c].isHorizontal == false) {
							for (int i = 0; i < HelpScreen.shipArray[c].intHeight; i++) {
								for (int j = 0; j < HelpScreen.shipArray[c].intWidth; j++) {
									HelpScreen.strPlayerBoard[intGridCoordsY + i][intGridCoordsX + j] = "ship" + c
											+ "board";
								}
							}
						} else {
							for (int i = 0; i < HelpScreen.shipArray[c].intWidth; i++) {
								for (int j = 0; j < HelpScreen.shipArray[c].intHeight; j++) {
									HelpScreen.strPlayerBoard[intGridCoordsY + i][intGridCoordsX + j] = "ship" + c
											+ "board";
								}
							}
						}
					}
				}
			}
			// sending player board to enemy board through ssm
			HelpScreen.blngetboardString = true;

			if (blnAllShipsValid == true) {
				System.out.println(HelpScreen.strPlayerBoard[2][1]);

				HelpScreen.thestart.setVisible(false);
				HelpScreen.endturn.setVisible(true);
				HelpScreen.thecoordsreceived.setVisible(true);
			}
			// after getting final 2d array
			// pass it to GamePanel.setBoard()
			HelpScreen.shipselected = -1;
		}
		if (evt.getSource() == GamePanel.endturn) {
			System.out.println(Arrays.deepToString(GamePanel.strEnemyBoard));
			// if the coordinates selected are a ship then it is a hit
			// GamePanel.blnMoveShipsAway = true;
			for (int i = 0; i < 5; i++) {
				GamePanel.shipArray[i].moveShips();
			}

			for (int s = 0; s < 5; s++) {
				GamePanel.blnafterEndTurn = false;
				System.out.println("Checking against: " + GamePanel.strEnemyBoard[intSecondGridY][intSecondGridX]);
				if (GamePanel.strEnemyBoard[intSecondGridY][intSecondGridX].equals("ship" + s + "board")) {
					GamePanel.strEnemyBoard[intSecondGridY][intSecondGridX] = ("ship" + s + "board;hit");
					// GamePanel.strPlayerBoard[intSecondGridY][intSecondGridX] =
					// ("ship"+s+"board;hit");
					intTotalShips--;
					String strMessageSendToSSM = "";
					String[] strMyLine;
					System.out.println(intTotalShips);

					for (int intRow = 0; intRow < 9; intRow++) {
						strMyLine = GamePanel.strEnemyBoard[intRow];
						String strFormattedLine = Arrays.toString(strMyLine).replace("[", "").replace("]", "")
								.replace(",", "-").replace(" ", "");
						strMessageSendToSSM += strFormattedLine;
						if (intRow != 8) {
							strMessageSendToSSM += ",";
						}
					}

					ssm.sendText("attack," + strMessageSendToSSM);
				}

				if (GamePanel.strEnemyBoard[intSecondGridY][intSecondGridX].equals("water")) {
					GamePanel.strEnemyBoard[intSecondGridY][intSecondGridX] = ("water;miss");
					String strMessageSendToSSM1 = "";
					String[] strMyLine;
					System.out.println(intTotalShips);

					for (int intRow = 0; intRow < 9; intRow++) {
						strMyLine = GamePanel.strEnemyBoard[intRow];
						String strFormattedLine = Arrays.toString(strMyLine).replace("[", "").replace("]", "")
								.replace(",", "-").replace(" ", "");
						strMessageSendToSSM1 += strFormattedLine;
						if (intRow != 8) {
							strMessageSendToSSM1 += ",";
						}
					}
					ssm.sendText("miss," + strMessageSendToSSM1);
				}
				GamePanel.repaint();
			}

			// win condition
			if (intTotalShips == 0) {
				if (ssm != null) {
					ssm.sendText("win," + StartPanel.theusername.getText());
					GamePanel.thechat.append(StartPanel.theusername.getText() + " has won the game!" + "\n");
					System.out.println(StartPanel.theusername.getText());
					GamePanel.restart.setVisible(true);
				}

			}
			System.out.println("wow the end turn is: ");
			System.out.println(Arrays.deepToString(GamePanel.strEnemyBoard));
		}
		if (evt.getSource() == HelpScreen.endturn) {
			System.out.println(Arrays.deepToString(HelpScreen.strPlayerBoard));
			for (int i = 0; i < 5; i++) {
				HelpScreen.shipArray[i].moveShips();
			}

			for (int s = 0; s < 5; s++) {
				HelpScreen.blnafterEndTurn = false;
				System.out.println("Checking against: " + HelpScreen.strPlayerBoard[intSecondGridY][intSecondGridX]);

				if (HelpScreen.strPlayerBoard[intSecondGridY][intSecondGridX].equals("ship" + s + "board")) {
					HelpScreen.strPlayerBoard[intSecondGridY][intSecondGridX] = ("ship" + s + "board;hit");
					intTotalShips--;
					System.out.println(intTotalShips);
				}

				if (HelpScreen.strPlayerBoard[intSecondGridY][intSecondGridX].equals("water")) {
					HelpScreen.strPlayerBoard[intSecondGridY][intSecondGridX] = ("water;miss");
					System.out.println(intTotalShips);
				}
				HelpScreen.repaint();
			}

		}

		// if(GamePanel.intWhosTurnIsIt == 1){
		//
		// }else if(GamePanel.intWhosTurnIsIt == 2){
		//

		// }

		if (evt.getSource() == GamePanel.restart) {
			// return to start screen
			System.out.println("returning to the start screen");
			StartPanel.setPreferredSize(new Dimension(1280, 720));
			theframe.addKeyListener(this);
			theframe.requestFocus();
			theframe.setContentPane(StartPanel);
			theframe.setResizable(false);
			theframe.pack();
			theframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			theframe.setVisible(true);
			StartPanel.setLayout(null);
			StartPanel.setPreferredSize(new Dimension(1280, 720));
			StartPanel.addMouseMotionListener(this);
			StartPanel.addMouseListener(this);
			// resetting start components
			StartPanel.theclient.setEnabled(true);
			StartPanel.theserver.setEnabled(true);
			StartPanel.theip.setEnabled(true);
			StartPanel.theusername.setEnabled(true);
			StartPanel.theusername.setText("");

			// resetting game components
			// the boards
			for (int c = 0; c < 9; c++) {
				for (int d = 0; d < 9; d++) {
					GamePanel.strPlayerBoard[c][d] = "water";
					GamePanel.strEnemyBoard[c][d] = "water";
				}
			}
			//
		}
		// help panel components
		if (evt.getSource() == GamePanel.returntomain) {
			System.out.println("returning to the start screen");
			StartPanel.setPreferredSize(new Dimension(1280, 720));
			theframe.addKeyListener(this);

			theframe.requestFocus();
			theframe.setContentPane(StartPanel);
			theframe.pack();
			theframe.setVisible(true);
		}
	}

	/**
	 * @param evt
	 */
	public void mouseExited(MouseEvent evt) {
	}

	/**
	 * @param evt
	 */
	public void mouseEntered(MouseEvent evt) {
	}

	/**
	 * @param evt
	 */
	public void mouseReleased(MouseEvent evt) {
		int intMouseX = evt.getX();
		int intMouseY = evt.getY();
		System.out.println("Mouse released" + intMouseX + " " + intMouseY);

		if (blnGameStarted == true) {
			intGridCoordsX = GamePanel.shipArray[intShipNum].intGridX - 2;
			intGridCoordsY = GamePanel.shipArray[intShipNum].intGridY - 1;

			int intGridX = intMouseX / 40; // top left corner of board is 2,1

			int intGridY = intMouseY / 40;

			System.out.printf("Ship: %d %d %d %d\n", intGridX, GamePanel.shipArray[GamePanel.shipselected].intWidth,
					intGridY, GamePanel.shipArray[GamePanel.shipselected].intHeight);

			if (GamePanel.shipArray[GamePanel.shipselected].isHorizontal != true &&
					(intGridX < 2 || intGridX + GamePanel.shipArray[GamePanel.shipselected].intWidth - 1 > 10
							|| intGridY < 1
							|| intGridY + GamePanel.shipArray[GamePanel.shipselected].intHeight - 1 > 9)) {
				GamePanel.shipArray[GamePanel.shipselected].resetShip();
			} else if (GamePanel.shipArray[GamePanel.shipselected].isHorizontal == true &&
					(intGridX < 2 || intGridX + GamePanel.shipArray[GamePanel.shipselected].intHeight - 1 > 10
							|| intGridY < 1
							|| intGridY + GamePanel.shipArray[GamePanel.shipselected].intWidth - 1 > 9)) {
				GamePanel.shipArray[GamePanel.shipselected].resetShip();
			} else {

				int intShipSelected = GamePanel.shipselected;
				Ship shipSelected = GamePanel.shipArray[intShipSelected];

				shipSelected.intGridX = intGridX;
				shipSelected.intGridY = intGridY;

				for (int intShipOther = 0; intShipOther < 5; intShipOther++) {
					Ship shipOther = GamePanel.shipArray[intShipOther];
					if (intShipOther != intShipSelected && shipOther.intGridX > 0 & shipOther.intGridY > 0) {
						System.out.println(intShipOther + " "
								+ shipOther.getRectangle());
						// if these two ships cover with each other after rotating
						if (shipSelected.getRectangle().intersects(shipOther.getRectangle()) == true) {
							shipSelected.resetShip();
							return;
						}
					}
				}

				int intRoundedX = 40 * intGridX;
				int intRoundedY = 40 * intGridY;

				GamePanel.shipArray[GamePanel.shipselected].intX = intRoundedX;
				GamePanel.shipArray[GamePanel.shipselected].intY = intRoundedY;
				System.out.println(intGridX + " " + intGridY);
			}
			// }
		}
		// help screen
		System.out.println("Mouse released" + intMouseX + " " + intMouseY);

		if (blnGameStarted == true) {
			intGridCoordsX = HelpScreen.shipArray[intShipNum].intGridX - 2;
			intGridCoordsY = HelpScreen.shipArray[intShipNum].intGridY - 1;

			int intGridX = intMouseX / 40; // top left corner of board is 2,1

			int intGridY = intMouseY / 40;

			System.out.printf("Ship: %d %d %d %d\n", intGridX, HelpScreen.shipArray[GamePanel.shipselected].intWidth,
					intGridY, GamePanel.shipArray[HelpScreen.shipselected].intHeight);

			if (HelpScreen.shipArray[GamePanel.shipselected].isHorizontal != true &&
					(intGridX < 2 || intGridX + HelpScreen.shipArray[GamePanel.shipselected].intWidth - 1 > 10
							|| intGridY < 1
							|| intGridY + GamePanel.shipArray[GamePanel.shipselected].intHeight - 1 > 9)) {
				HelpScreen.shipArray[GamePanel.shipselected].resetShip();
			} else if (HelpScreen.shipArray[HelpScreen.shipselected].isHorizontal == true &&
					(intGridX < 2 || intGridX + HelpScreen.shipArray[HelpScreen.shipselected].intHeight - 1 > 10
							|| intGridY < 1
							|| intGridY + HelpScreen.shipArray[HelpScreen.shipselected].intWidth - 1 > 9)) {
				HelpScreen.shipArray[HelpScreen.shipselected].resetShip();
			} else {

				int intShipSelected = HelpScreen.shipselected;
				Ship shipSelected = HelpScreen.shipArray[intShipSelected];

				shipSelected.intGridX = intGridX;
				shipSelected.intGridY = intGridY;

				for (int intShipOther = 0; intShipOther < 5; intShipOther++) {
					Ship shipOther = HelpScreen.shipArray[intShipOther];
					if (intShipOther != intShipSelected && shipOther.intGridX > 0 & shipOther.intGridY > 0) {
						System.out.println(intShipOther + " "
								+ shipOther.getRectangle());
						// if these two ships cover with each other after rotating
						if (shipSelected.getRectangle().intersects(shipOther.getRectangle()) == true) {
							shipSelected.resetShip();
							return;
						}
					}
				}

				int intRoundedX = 40 * intGridX;
				int intRoundedY = 40 * intGridY;

				HelpScreen.shipArray[HelpScreen.shipselected].intX = intRoundedX;
				HelpScreen.shipArray[HelpScreen.shipselected].intY = intRoundedY;
				System.out.println(intGridX + " " + intGridY);
			}
			// }
		}
	}

	/**
	 * @param evt
	 */
	public void mousePressed(MouseEvent evt) {
		GamePanel.thetimer.start();
		int intMouseX = evt.getX();
		int intMouseY = evt.getY();
		int intButton = evt.getButton();
		if (blnGameStarted == true) {
			System.out.println("triggered mousePressed button " + intMouseX + ", " + intMouseY);
			if (intButton == 1) {
				for (int i = 0; i < 5; i++) {
					if (intMouseX >= GamePanel.shipArray[i].intX & intMouseX <= GamePanel.shipArray[i].intX + 40) {
						if (intMouseY >= GamePanel.shipArray[i].intY
								& intMouseY <= GamePanel.shipArray[i].intY + 40 * (i + 1)) {
							System.out.println("ship" + i + " selected");
							GamePanel.shipselected = i;
						}
					}
				}
			}
		}
		if (intMouseX <= 1200 && intMouseX >= 840 && intMouseY <= 400 && intMouseY >= 40) {
			intSecondGridX = ((int) Math.ceil(Math.abs(intMouseX / 40)) - 21);
			intSecondGridY = ((int) Math.ceil(Math.abs(intMouseY / 40) - 1));
			System.out.println(intSecondGridX - 20 + " " + intSecondGridY);
			GamePanel.thecoordsreceived.setText(intSecondGridX + ", " + intSecondGridY);
		}
		// help screen
		HelpScreen.thetimer.start();
		if (blnGameStarted == true) {
			System.out.println("triggered mousePressed button " + intMouseX + ", " + intMouseY);
			if (intButton == 1) {
				for (int i = 0; i < 5; i++) {
					if (intMouseX >= HelpScreen.shipArray[i].intX & intMouseX <= HelpScreen.shipArray[i].intX + 40) {
						if (intMouseY >= HelpScreen.shipArray[i].intY
								& intMouseY <= HelpScreen.shipArray[i].intY + 40 * (i + 1)) {
							System.out.println("ship" + i + " selected");
							HelpScreen.shipselected = i;
						}
					}
				}
			}
		}
		if (intMouseX <= 1200 && intMouseX >= 840 && intMouseY <= 400 && intMouseY >= 40) {
			intSecondGridX = ((int) Math.ceil(Math.abs(intMouseX / 40)) - 21);
			intSecondGridY = ((int) Math.ceil(Math.abs(intMouseY / 40) - 1));
			System.out.println(intSecondGridX - 20 + " " + intSecondGridY);
			HelpScreen.thecoordsreceived.setText(intSecondGridX + ", " + intSecondGridY);
		}
	}

	/**
	 * @param evt
	 */
	public void mouseClicked(MouseEvent evt) {
	}

	/**
	 * @param evt
	 */
	public void mouseDragged(MouseEvent evt) {
		System.out.println("Selected Ship: " + GamePanel.shipselected);
		if (blnGameStarted == true) {
			for (int i = 0; i < 5; i++) {
				if (GamePanel.shipselected == i) {
					GamePanel.shipArray[i].intX = evt.getX() - 10;
					GamePanel.shipArray[i].intY = evt.getY() - 10;
					System.out.println(GamePanel.shipArray[i].intX);
				}
			}
		}
		// help screen
		System.out.println("Selected Ship: " + HelpScreen.shipselected);
		if (blnGameStarted == true) {
			System.out.println("yay");
			for (int i = 0; i < 5; i++) {
				if (HelpScreen.shipselected == i) {
					HelpScreen.shipArray[i].intX = evt.getX() - 10;
					HelpScreen.shipArray[i].intY = evt.getY() - 10;
					System.out.println(HelpScreen.shipArray[i].intX);
				}
			}
		}
	}

	/**
	 * @param evt
	 */
	public void mouseMoved(MouseEvent evt) {
	}

	/**
	 * @param evt
	 */
	public void keyPressed(KeyEvent evt) {
	}

	/**
	 * @param evt
	 */
	public void keyReleased(KeyEvent evt) {
		blnSpace2 = false;
		if (evt.getKeyCode() == KeyEvent.VK_SPACE) {

			int intShipSelected = GamePanel.shipselected;
			Ship shipSelected = GamePanel.shipArray[intShipSelected];

			shipSelected.isHorizontal = true;

			for (int intShipOther = 0; intShipOther < 5; intShipOther++) {
				Ship shipOther = GamePanel.shipArray[intShipOther];
				if (intShipOther != intShipSelected && shipOther.intGridX > 0 & shipOther.intGridY > 0) {
					// if these two ships cover with each other after rotating
					if (shipSelected.getRectangle().intersects(shipOther.getRectangle()) == true) {
						shipSelected.isHorizontal = false;
						return;
					}
				}
			}
			System.out.println("pressing space once");
		}
		// help screen
		blnSpace2 = false;
		if (evt.getKeyCode() == KeyEvent.VK_SPACE) {

			int intShipSelected = HelpScreen.shipselected;
			Ship shipSelected = HelpScreen.shipArray[intShipSelected];

			shipSelected.isHorizontal = true;

			for (int intShipOther = 0; intShipOther < 5; intShipOther++) {
				Ship shipOther = HelpScreen.shipArray[intShipOther];
				if (intShipOther != intShipSelected && shipOther.intGridX > 0 & shipOther.intGridY > 0) {
					// if these two ships cover with each other after rotating
					if (shipSelected.getRectangle().intersects(shipOther.getRectangle()) == true) {
						shipSelected.isHorizontal = false;
						return;
					}
				}
			}
			System.out.println("pressing space once");
		}
	}

	/**
	 * @param evt
	 */
	public void keyTyped(KeyEvent evt) {
	}

	// constructor
	public BATTLESHIP() throws Exception {
		super();
		theTheme = new Themes();
		StartPanel = new BSStartPanel();
		GamePanel = new BSGamePanel();
		HelpScreen = new BSHelpPanel();
		/// start panel
		StartPanel.setLayout(null);
		StartPanel.setPreferredSize(new Dimension(1280, 720));
		/// update messaged - informing player of next step to enter the game
		StartPanel.thereceiving.setSize(1150, 70);
		StartPanel.thereceiving.setLocation(60, 100);
		StartPanel.add(StartPanel.thereceiving);
		/// player 1 (server) or player 2 (client)
		StartPanel.theserver.setSize(85, 40);
		StartPanel.theserver.setLocation(500, 420);
		StartPanel.add(StartPanel.theserver);
		StartPanel.theserver.addActionListener(this);
		StartPanel.theclient.setSize(85, 40);
		StartPanel.theclient.setLocation(630, 420);
		StartPanel.add(StartPanel.theclient);
		StartPanel.theclient.addActionListener(this);
		/// label - indicating to players that the below textfield is to enter their
		/// username
		StartPanel.theusernamelabel.setSize(200, 30);
		StartPanel.theusernamelabel.setLocation(575, 220);
		StartPanel.add(StartPanel.theusernamelabel);
		/// textfield - for players to enter their desired username
		StartPanel.theusername.setSize(100, 30);
		StartPanel.theusername.setLocation(555, 255);
		StartPanel.add(StartPanel.theusername);
		StartPanel.theusername.addActionListener(this);
		/// label - indicating to players that the below textfield is to enter the ip
		/// address (if necessary)
		StartPanel.theiplabel.setSize(200, 30);
		StartPanel.theiplabel.setLocation(600, 315);
		StartPanel.add(StartPanel.theiplabel);
		/// textfield - for players to enter the ip address
		StartPanel.theip.setSize(100, 30);
		StartPanel.theip.setLocation(555, 350);
		StartPanel.add(StartPanel.theip);
		StartPanel.theip.addActionListener(this);
		/// theme buttons
		StartPanel.theme1.setSize(30, 30);
		StartPanel.theme1.setLocation(550, 500);
		StartPanel.add(StartPanel.theme1);
		StartPanel.theme1.addActionListener(this);
		StartPanel.theme2.setSize(30, 30);
		StartPanel.theme2.setLocation(590, 500);
		StartPanel.add(StartPanel.theme2);
		StartPanel.theme2.addActionListener(this);
		StartPanel.theme3.setSize(30, 30);
		StartPanel.theme3.setLocation(630, 500);
		StartPanel.add(StartPanel.theme3);
		StartPanel.theme3.addActionListener(this);
		StartPanel.theentergame.setSize(110, 60);
		StartPanel.theentergame.setLocation(550, 550);
		StartPanel.add(StartPanel.theentergame);
		StartPanel.theentergame.addActionListener(this);
		/// Menu Bar
		StartPanel.themenubar.setSize(1280, 50);
		StartPanel.themenubar.setLocation(0, 0);
		StartPanel.themenubar.add(StartPanel.themenu);
		StartPanel.add(StartPanel.themenubar);
		StartPanel.themenu.add(StartPanel.themenuhelp);
		StartPanel.themenuhelp.addActionListener(this);
		StartPanel.themenu.setVisible(true);
		/// Back Button
		GamePanel.returntomain.setSize(100, 50);
		GamePanel.returntomain.setLocation(0, 0);
		HelpScreen.add(GamePanel.returntomain);
		GamePanel.returntomain.addActionListener(this);
		/// Frame
		theframe.addKeyListener(this);
		theframe.requestFocus();
		theframe.setContentPane(StartPanel);
		theframe.setResizable(false);
		theframe.pack();
		theframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theframe.setVisible(true);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	// main method
	public static void main(String[] args) throws Exception {
		new BATTLESHIP();
	}
}
