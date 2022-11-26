import java.awt.*;
import javax.swing.*;

public class BSStartPanel extends JPanel {
	// properties
	JTextArea thereceiving = new JTextArea("Welcome to Battleship! First, please enter your username.\n");
	JButton theserver = new JButton("Player 1");
	JButton theclient = new JButton("Player 2");
	JButton theme1 = new JButton("");
	JButton theme2 = new JButton("");
	JButton theme3 = new JButton("");
	JButton theentergame = new JButton("Enter Game");
	JTextField theusername = new JTextField("");
	JLabel theusernamelabel = new JLabel("Username");
	JTextField theip = new JTextField("127.0.0.1");
	JLabel theiplabel = new JLabel("IP");
	JScrollPane theScroll;

	// Menu Bar Server/Client
	JMenuBar themenubar = new JMenuBar();
	JMenu themenu = new JMenu("Menu");
	JMenuItem themenuhelp = new JMenuItem("Help");
	JMenuItem themenumain = new JMenuItem("Main");

	// methods
	public void paintComponent(Graphics g) {
		g.drawImage(BATTLESHIP.theTheme.imghomescreen, 0, 0, this);
	}

	// constructor
	public BSStartPanel() {
		super();
		this.setLayout(null);
		try {
			theme1.setIcon(new ImageIcon(BATTLESHIP.theTheme.allimgicons[0]));
		} catch (Exception ex) {
			System.out.println(ex);
		}
		try {
			theme2.setIcon(new ImageIcon(BATTLESHIP.theTheme.allimgicons[1]));
		} catch (Exception ex) {
			System.out.println("i get here");
			System.out.println(ex);
		}
		try {
			theme3.setIcon(new ImageIcon(BATTLESHIP.theTheme.allimgicons[2]));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

}
