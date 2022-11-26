import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Themes {

	int intNumberOfThemes = 3;
	BufferedImage[] allimghomescreens = new BufferedImage[intNumberOfThemes];
	BufferedImage[] allimggamescreens = new BufferedImage[intNumberOfThemes];
	BufferedImage[] allimgicons = new BufferedImage[intNumberOfThemes];
	BufferedImage imghomescreen;
	BufferedImage imggamescreen;

	public Themes() throws Exception {
		BufferedReader txtReader = new BufferedReader(new FileReader("Themes.txt"));
		for (int i = 0; i < intNumberOfThemes; i++) {
			String strTheme = txtReader.readLine();
			String strIconFileName = "themeimgs/" + strTheme + "_icon.png";
			String strHomeFileName = "themeimgs/" + strTheme + "_home.png";
			String strGameFileName = "themeimgs/" + strTheme + "_background.png";

			System.out.println(strIconFileName);
			allimgicons[i] = ImageIO.read(new File(strIconFileName));
			System.out.println("yay");
			allimghomescreens[i] = ImageIO.read(new File(strHomeFileName));
			allimggamescreens[i] = ImageIO.read(new File(strGameFileName));
		}
		txtReader.close();
		changeTheme(0);
	}

	public void changeTheme(int intSelected) throws Exception {
		System.out.println("using theme: " + intSelected);
		imghomescreen = allimghomescreens[intSelected];
		imggamescreen = allimggamescreens[intSelected];
	}
}
