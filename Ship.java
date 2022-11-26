import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Ship {

	int intWidth;
	int intHeight;

	int intChangedWidth;
	int intChangedHeight;

	int intX;
	int intY;

	int intChangedX;
	int intChangedY;

	int intOriginalX;
	int intOriginalY;

	int intGridY = -1;
	int intGridX = -1;

	int intSecondGridX = -1;
	int intSecondGridY = -1;

	BufferedImage shipImage;
	BufferedImage shipImageChanged;
	BufferedImage shipImageGrid;

	boolean isHorizontal = false;

	public Ship(int intShipNumber) throws Exception {
		System.out.println("Constructing ship number: " + intShipNumber);

		// Getting ship size
		String strShipTextFileName = "ships.txt";

		BufferedReader txtReader = new BufferedReader(new FileReader(strShipTextFileName));
		String[] strShipInfo = new String[4];

		for (int i = 0; i < 5; i++) {
			if (i == intShipNumber) {
				strShipInfo = txtReader.readLine().split(",");
			} else {
				txtReader.readLine();
			}
		}

		txtReader.close();

		// Get Image
		String strImageFileName = "ship" + intShipNumber + ".png";
		String strImageFileNameGrid = "ship" + intShipNumber + "board.png";
		String strImageFileNameChanged = "changedship" + intShipNumber + ".png";

		// Apply Properties
		intWidth = Integer.parseInt(strShipInfo[0]);
		intHeight = Integer.parseInt(strShipInfo[1]);
		intX = Integer.parseInt(strShipInfo[2]);
		intY = Integer.parseInt(strShipInfo[3]);
		intChangedWidth = Integer.parseInt(strShipInfo[0]);
		intChangedHeight = Integer.parseInt(strShipInfo[1]);
		intChangedX = Integer.parseInt(strShipInfo[2]);
		intChangedY = Integer.parseInt(strShipInfo[3]);
		intChangedX = intX;
		intChangedY = intY;
		shipImage = ImageIO.read(new File(strImageFileName));
		shipImageGrid = ImageIO.read(new File(strImageFileNameGrid));
		shipImageChanged = ImageIO.read(new File(strImageFileNameChanged));
		intOriginalX = intX;
		intOriginalY = intY;
		// intWidth = intChangedHeight;
		// intHeight = intChangedWidth;
	}

	public Rectangle getRectangle() {
		if (isHorizontal == true) {
			return new Rectangle(intGridX, intGridY, intHeight, intWidth);
		} else {
			return new Rectangle(intGridX, intGridY, intWidth, intHeight);
		}
	}

	public void resetShip() {
		intX = intOriginalX;
		intY = intOriginalY;
		intGridX = -1;
		intGridY = -1;
	}

	public void resetRotatedShip() {
		intX = 0;
		intY = 0;
	}

	public void moveShips() {
		intX = -100;
		intY = -100;
	}

}
