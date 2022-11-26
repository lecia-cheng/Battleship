import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Arrays;

public class BSExplosionAnimation extends JPanel implements ActionListener{
	//properties
	int intFrame = 0;
	int intTotalFrames = 6;
	BufferedImage explosion[] = new BufferedImage[intTotalFrames]; 
	Timer thetimer = new Timer(8000/60, this);
	 
	
	//methods 
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		//g.fillRect(0,0,40,40);
		g.drawImage(explosion[intFrame],0,0,null);
	}

	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == thetimer){
			if (intFrame < intTotalFrames-1){
				intFrame = intFrame + 1; 
			} else  {
				intFrame = 0;
			}
			repaint();
		}
	}
	//constructor 
	public BSExplosionAnimation(){
		super();
		setSize(40, 40);
		try{
			for(int n = 0; n<intTotalFrames; n++){
				explosion[n] = ImageIO.read(new File("animation/explosion"+n+".png"));
			}
		}catch(IOException e){
			System.out.println("unable to load image");
		}
		thetimer.start();
	}

	//testing 
	public static void main(String args[]) {
		BSExplosionAnimation explosionanimation = new BSExplosionAnimation();
		JFrame app = new JFrame("Animator test");
		app.setPreferredSize(new Dimension(800, 600));
		app.setResizable(true);
		app.pack();
		app.setVisible(true);
		app.add(explosionanimation, BorderLayout.CENTER);
		app.setSize(300,300);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(explosionanimation.getPreferredSize().width + 1000, explosionanimation.getPreferredSize().height + 600);
	}
}
