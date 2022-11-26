import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.*;

public class BSWaterAnimation extends JPanel implements ActionListener {
	// properties
	int intFrame = 0;
	int intTotalFrames = 10;
	BufferedImage splash[] = new BufferedImage[intTotalFrames];
	Timer thetimer = new Timer(5000 / 60, this);

	// methods
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawImage(splash[intFrame], 0, 0, null);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == thetimer) {
			if (intFrame < intTotalFrames - 1) {
				intFrame = intFrame + 1;
			} else {
				intFrame = 0;
			}
			repaint();
		}
	}

	// constructor
	public BSWaterAnimation() {
		super();
		setSize(40, 40);
		try {
			for (int n = 0; n < intTotalFrames; n++) {
				splash[n] = ImageIO.read(new File("animation/splash" + n + ".png"));
			}
		} catch (IOException e) {
			System.out.println("unable to load image");
		}
		thetimer.start();
	}

	// testing
	public static void main(String args[]) {
		BSWaterAnimation splashanimation = new BSWaterAnimation();
		JFrame app = new JFrame("Animator test");
		app.setPreferredSize(new Dimension(800, 600));
		app.setResizable(true);
		app.pack();
		app.setVisible(true);
		app.add(splashanimation, BorderLayout.CENTER);
		app.setSize(300, 300);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(splashanimation.getPreferredSize().width + 1000, splashanimation.getPreferredSize().height + 600);
	}
}
