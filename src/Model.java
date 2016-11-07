import java.awt.Color;
import java.util.ArrayList;

public class Model {
	
	private MainBrick mainBrick;
	private MainBrick nextMainBrick;

	private int points;
	private int numberOfRows;
	private int numberOfBlocks;
	private int[][] coordinates = new int[17][11]; // Spielfeld 17 Felder hoch(y), 11 Felder breit (x)
	private Color[][] colorCoordinates = new Color[17][11]; // speichert für jedes Feld die Farbe
	private String[][] brickPositions = {{"def", "bhj"},
									     {"dei", "bgh", "ade", "bch"},
									     {"deh", "bdh", "bde", "beh"},
										 {"deg", "abh", "cde", "bhi"},
										 {"ehi"},
										 {"dhi", "ceh"},
										 {"egh", "bei"},
	};
	private Color[] colors = {Color.decode("#ff9d00"),  Color.decode("#aa00ff"), Color.decode("#5649ff"), Color.decode("#00d8ff"), Color.decode("#ff0000"), Color.decode("#6edd4f"), Color.yellow};

	public MainBrick getNextMainBrick(){
		return nextMainBrick;
	}
	public MainBrick getMainBrick(){
		return mainBrick;
	}
	public int[][] getCoordinates(){
		return coordinates;
	}
	public Color[][] getColorCoordinates(){
		return colorCoordinates;
	}
	public int getPoints() {
		return points;
	}
	public int getNumberOfBlocks() {
		return numberOfBlocks;
	}
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	/** Konstruktor **/
	public Model(){
		init();
	}
	
	public void init(){
		nextMainBrick = null;
		mainBrick = new MainBrick(true);
		nextMainBrick = new MainBrick(true);
		points = 0;
		numberOfRows = 0;
		numberOfBlocks = 0; // richtig?
		
		for (int i = 0; i < 17; i++){
			for (int e = 0; e < 11; e++){
				coordinates[i][e] = 0;
				colorCoordinates[i][e] = null;
			}
		}
	}
	
	/*********************************************************************/
	/*** Bei den folgenden 4 Methoden handelt es sich um Prüfmethoden, ***/
	/*** die durch die Methoden moveDown(), moveLeft(), moveRight(), *****/
	/*** und changeBlock() aufgerufen werden. ****************************/
	/*********************************************************************/
	
	/** Methode prüft, ob das Tetristeil nach unten verschoben werden kann. **/
	public boolean checkNextPosition(int x, int y, String bricks){
		y += 40;
		if (y > 640)
			return false;
		if (coordinates[y/40][x/40] == 1)
			return false;
		for (int i = 0; i < 3; i++){
			int[] xy = getRelativeBrickPosition(x, y, String.valueOf(bricks.charAt(i)));
			if (xy[1] > 640)
				return false;
			if (coordinates[xy[1]/40][xy[0]/40] == 1)
				return false;
		}
		return true;
	}
	
	/** Methode prüft, ob das Tetristeil nach links verschoben werden kann. **/
	public boolean checkNextLeftPosition(int x, int y, String bricks){
		x -= 40;
		if (x < 0)
			return false;
		if (coordinates[y/40][x/40] == 1)
			return false;
		for (int i = 0; i < 3; i++){
			int[] xy = getRelativeBrickPosition(x, y, String.valueOf(bricks.charAt(i)));
			if (xy[0] < 0)
				return false;
			if (coordinates[xy[1]/40][xy[0]/40] == 1)
				return false;
		}
		return true;
	}
	
	/** Methode prüft, ob das Tetristeil nach rechts verschoben werden kann. **/
	public boolean checkNextRightPosition(int x, int y, String bricks){
		x += 40;
		if (x > 400)
			return false;
		if (coordinates[y/40][x/40] == 1)
			return false;
		for (int i = 0; i < 3; i++){
			int[] xy = getRelativeBrickPosition(x, y, String.valueOf(bricks.charAt(i)));
			if (xy[0] > 400)
				return false;
			if (coordinates[xy[1]/40][xy[0]/40] == 1)
				return false;
		}
		return true;
	}
	
	/** 
	 * Methode prüft, ob das Tetristeil gedreht werden kann.
	 * Dabei ist zu beachten, dass sich nur die umliegenden Blöcke drehen;
	 * der mainBrick bleibt immer an der selben Stelle.
	 */
	public boolean checkChangePosition(int x, int y, String bricks){
		for (int i = 0; i < 3; i++){
			int[] xy = getRelativeBrickPosition(x, y, String.valueOf(bricks.charAt(i)));
			if (xy[0] > 400 || xy[0] < 0 || xy[1] > 640 || xy[1] < 0)
				return false;
			if (coordinates[xy[1]/40][xy[0]/40] == 1)
				return false;
		}
		return true;
	}
	

	/** Methode gibt den nächsten Block Status zurück. **/
	public int getNextBlockStatus(int blockType, int blockStatus){
		switch(blockType){
			case 0: if (blockStatus == 0) return 1; else return 0; 
			case 1: if (blockStatus < 3) return blockStatus+1; else return 0; 
			case 2: if (blockStatus < 3) return blockStatus+1; else return 0;
			case 3: if (blockStatus < 3) return blockStatus+1; else return 0;
			case 4: return 0;
			case 5: if (blockStatus == 0) return 1; else return 0; 
			case 6: if (blockStatus == 0) return 1; else return 0; 
		}
		return 0;
	}
	
	/** Methode berechnet die relative Position eines umliegenden Blocks. **/
	public int[] getRelativeBrickPosition(int x, int y, String relativeBrickPosition){
		int[] xy = new int[2];
		switch(relativeBrickPosition){
			case "a": 
				xy[0] = x - 40;
				xy[1] = y - 40;
				break;
			case "b": 
				xy[0] = x;
				xy[1] = y - 40;
				break;
			case "c": 
				xy[0] = x + 40;
				xy[1] = y - 40;
				break;
			case "d": 
				xy[0] = x - 40;
				xy[1] = y;
				break;
			case "e": 
				xy[0] = x + 40;
				xy[1] = y;
				break;
			case "f": 
				xy[0] = x + 80;
				xy[1] = y;
				break;
			case "g": 
				xy[0] = x - 40;
				xy[1] = y + 40;
				break;
			case "h": 
				xy[0] = x;
				xy[1] = y + 40;
				break;
			case "i": 
				xy[0] = x + 40;
				xy[1] = y + 40;
				break;
			case "j": 
				xy[0] = x;
				xy[1] = y + 80;
				break;
		}
		return xy;		
	}
	
	/** Die Methode prüft, ob eine Reihe voll ist. Ist dies der Fall, **/
	/** wird die Reihe gelöscht und darüberliegenden Blöcke werden nach unten verschoben. **/
	public void checkRowFull(){
		for (int i = 0; i < 17; i++){
			for (int e = 0; e < 11; e++){
				if (coordinates[i][e] == 0) break;
				if (e == 10){
					for (int k = 0; k < 11; k++){
						coordinates[i][k] = 0;
						colorCoordinates[i][k] = null;
					}
					for (int t = i; t > 0; t--){
						for (int r = 0; r < 11; r++){						
							coordinates[t][r] = coordinates[t-1][r];
							colorCoordinates[t][r] = colorCoordinates[t-1][r];
							
							// abschließend die erste Reihe auf 0 setzen
							if (t == 1)
								coordinates[0][r] = 0;
						}
					}
					numberOfRows++;
					points += 100;
				}
			}
		}
	}	
		
	/** Methode setzt das Tetristeil und erzeugt ein neues mainBrick-Objekt. **/
	public boolean setBlock(){
		int x = mainBrick.getX() / 40;
		int y = mainBrick.getY() / 40;
		coordinates[y][x] = 1;
		colorCoordinates[y][x] = mainBrick.getColor();
		for (int i = 0; i < 3; i++){
			x = mainBrick.getNormalBricks()[i].getX() / 40;
			y = mainBrick.getNormalBricks()[i].getY() / 40;
			coordinates[y][x] = 1;
			colorCoordinates[y][x] = mainBrick.getColor();
		}
		
		checkRowFull(); // prüfen ob Reihe voll ist
		
		// neues mainBrick-Objekt erzeugen
		int blockType = (int) (Math.random() * 7); // Werte zwischen 0 und 6
		String bricks = brickPositions[blockType][0];
		if (!checkNextPosition(200, -40, bricks))
			return false; // Game Over
		else mainBrick = new MainBrick(false);
		
		return true;
	}

	/*********************************************************************/
	/********************* Innere Klasse MainBrick ***********************/
	/*********************************************************************/
	
	/** Das mainBrick-Objekt entspricht dem Haupstein des Tetristeils. **/
	/** Dieses Teil verändert seine Position beim Drehen nicht. **/
	/** Wichtig: Das mainBrick-Objekt beinhaltet Referenzen auf alle 3 anderen normalBrick-Objekte **/
	class MainBrick{
			
		/*********************************************************************/
		/********************* Innere Klasse NormalBrick *********************/
		/*********************************************************************/
		
		/** Die normalBrick-Objekte umgeben den Hauptstein und veränern ihre Position beim Drehen des Tetristeils. **/
		class NormalBrick{
			private int x;
			private int y;
			
			public int getX(){
				return x;
			}
			public int getY(){
				return y;
			}
			public void setX(int x){
				this.x = x;
			}
			public void setY(int y){
				this.y = y;
			}
			// Konstruktor
			public NormalBrick(int[] xy){
				this.x = xy[0];
				this.y = xy[1];
			}
		} 
		
		/*********************************************************************/
		/********************** end NormalBrick Class ************************/
		
		private ArrayList<NormalBrick> normalBricks = new ArrayList<NormalBrick>();
		private int x;
		private int y;
		private int blockType;
		private int blockStatus;
		private Color color;
		
		public NormalBrick[] getNormalBricks(){
			NormalBrick[] normalBricks = new NormalBrick[3];
			for (int i = 0; i < 3; i++){
				normalBricks[i] = this.normalBricks.get(i);
			}
			return normalBricks;
		}
		public int getX(){
			return x;
		}
		public int getY(){
			return y;
		}
		public void setX(int x){
			this.x = x;
		}
		public void setY(int y){
			this.y = y;
		}
		public int getBlockType(){
			return blockType;
		}
		public int getBlockStatus(){
			return blockStatus;
		}
		public Color getColor(){
			return color;
		}
		public void setBlockStatus(int b){
			blockStatus = b;
		}
		
		/** Methode kopiert die Eigenschaften aus dem nextMainBrick-Objekt in das mainBrick-Objekt **/
		public void copyObject(MainBrick copy){
			x = copy.getX();
			y = copy.getY();
			blockType = copy.getBlockType();
			blockStatus = copy.getBlockStatus();
			color = copy.getColor();
			String bricks = brickPositions[blockType][blockStatus];
			
			normalBricks.add(new NormalBrick(getRelativeBrickPosition(200, 0, String.valueOf(bricks.charAt(0)))));
			normalBricks.add(new NormalBrick(getRelativeBrickPosition(200, 0, String.valueOf(bricks.charAt(1)))));
			normalBricks.add(new NormalBrick(getRelativeBrickPosition(200, 0, String.valueOf(bricks.charAt(2)))));
		}
		
		// Konstruktor
		public MainBrick(boolean next){
			if ((nextMainBrick != null) && (next == false)){
				copyObject(nextMainBrick);
				numberOfBlocks++;
				points += 10;
				nextMainBrick = new MainBrick(true);
			}
			else {
				x = 200;
				y = 0;
				blockType = (int) (Math.random() * 7); // Werte zwischen 0 und 6)
				blockStatus = 0;
				color = colors[blockType];
				String bricks = brickPositions[blockType][blockStatus];
	
				normalBricks.add(new NormalBrick(getRelativeBrickPosition(200, 0, String.valueOf(bricks.charAt(0)))));
				normalBricks.add(new NormalBrick(getRelativeBrickPosition(200, 0, String.valueOf(bricks.charAt(1)))));
				normalBricks.add(new NormalBrick(getRelativeBrickPosition(200, 0, String.valueOf(bricks.charAt(2)))));
			}
		}	
				
		public void moveLeft(){
			if (checkNextLeftPosition(x, y, brickPositions[blockType][blockStatus])) {
				x -= 40;
				getNormalBricks()[0].setX(getNormalBricks()[0].getX() - 40);
				getNormalBricks()[1].setX(getNormalBricks()[1].getX() - 40);
				getNormalBricks()[2].setX(getNormalBricks()[2].getX() - 40);
			}
		}
		public void moveRight(){
			if (checkNextRightPosition(x, y, brickPositions[blockType][blockStatus])){
				x += 40;
				getNormalBricks()[0].setX(getNormalBricks()[0].getX() + 40);
				getNormalBricks()[1].setX(getNormalBricks()[1].getX() + 40);
				getNormalBricks()[2].setX(getNormalBricks()[2].getX() + 40);
			}
		}
		public boolean moveDown(){
			if (checkNextPosition(x, y, brickPositions[blockType][blockStatus])){
				y += 40;
				getNormalBricks()[0].setY(getNormalBricks()[0].getY() + 40);
				getNormalBricks()[1].setY(getNormalBricks()[1].getY() + 40);
				getNormalBricks()[2].setY(getNormalBricks()[2].getY() + 40);
				return true;
			}
			else return setBlock();
		}
		public void changeBlock(){
			String newBricks = brickPositions[blockType][getNextBlockStatus(blockType, blockStatus)];
			if (checkChangePosition(x, y, newBricks)){
				blockStatus = getNextBlockStatus(blockType, blockStatus);
				for (int i = 0; i < 3; i++){
					int[] xy = getRelativeBrickPosition(x, y, String.valueOf(newBricks.charAt(i)));
					getNormalBricks()[i].setX(xy[0]);
					getNormalBricks()[i].setY(xy[1]);
				}	
			}
		}
	} 	
}
