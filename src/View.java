import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.*;

public class View {
	private Model model;
	private JLayeredPane layeredPane;
	private JPanel startPanel;
	private JPanel pausePanel;
	private MainPanel mainPanel;
	private BlockPanel blockPanel;
	private ControlPanel controlPanel;
	
	private boolean gameRunning = false;

	public MainPanel getMainPanel(){
		return mainPanel;
	}
	public BlockPanel getBlockPanel(){
		return blockPanel;
	}
	public ControlPanel getControlPanel(){
		return controlPanel;
	}
	
	public void gameOver(){
		gameRunning = false;
		JOptionPane.showMessageDialog(mainPanel, "Spiel vorbei - Sie haben " + model.getPoints() + " Punkte erreicht.");
		controlPanel.getBtnStarten().setEnabled(true);
		controlPanel.getBtnPause().setEnabled(false);
		for (int i = 0; i < 4; i++)
			controlPanel.getRadioButtons()[i].setEnabled(true);
		blockPanel.toggleLabelUnknown(true);				
	}
	
	public void startGame(){
		gameRunning = true;
		layeredPane.setLayer(startPanel, 0); 
		controlPanel.getBtnStarten().setEnabled(false);
		controlPanel.getBtnPause().setEnabled(true);
		for (int i = 0; i < 4; i++)
			controlPanel.getRadioButtons()[i].setEnabled(false);
		blockPanel.toggleLabelUnknown(false);		
	}
	
	public void pause(boolean p){
		if (p){
			controlPanel.getBtnPause().setText("Spiel fortsetzen [P]");
			layeredPane.setLayer(pausePanel, new Integer(3));	
		}
		else {
			controlPanel.getBtnPause().setText("Spiel pausieren [P]");
			layeredPane.setLayer(pausePanel, new Integer(0));
		}
	}
	
	public View(Model model){
		this.model = model;
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(null);
		f.setTitle("JBlocks");
		f.setResizable(false);
		f.setSize(720, 720);
		f.getContentPane().setPreferredSize(new Dimension(680, 680));
		// defaultColor = f.getBackground();
		
		/* JFrame zentrieren */
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
		
		layeredPane = new JLayeredPane();
	    mainPanel = new MainPanel();
		mainPanel.setBounds(-1, 0, 441, 681); // Spielfeld 11x17
		layeredPane.add(mainPanel, new Integer(0)); 

		blockPanel = new BlockPanel();
		blockPanel.setBounds(440, 0, 240, 341);
		layeredPane.add(blockPanel, new Integer(0));
		
		controlPanel = new ControlPanel();
		controlPanel.setBounds(440, 341, 240, 340);
		layeredPane.add(controlPanel, new Integer(1)); 
		
		/** Panel wird zu Beginn angezeigt und verdeckt das mainPanel **/
		startPanel = new JPanel(null);
		startPanel.setBounds(0, 1, 439, 681);
		Icon startImage = new ImageIcon(getClass().getResource("start.png")); 
		JLabel labelStart = new JLabel(startImage);
		labelStart.setBounds(0, 0, 439, 681);
		startPanel.add(labelStart);
		layeredPane.add(startPanel, new Integer(2));
		
		/** Panel wird angezeigt, wenn das Spiel pausiert wird **/
		pausePanel = new JPanel(null);
		pausePanel.setBounds(0, 1, 439, 681);
		Icon pauseImage = new ImageIcon(getClass().getResource("pause.png")); 
		JLabel labelPause = new JLabel(pauseImage);
		labelPause.setBounds(0, 0, 439, 681);
		pausePanel.add(labelPause);
		layeredPane.add(pausePanel, new Integer(0));
		
		f.setLayeredPane(layeredPane);
		f.pack();
		f.setVisible(true);	
	}
	
	/** MainPanel auf dem das Spiel läuft **/
	class MainPanel extends JPanel{

		private static final long serialVersionUID = -4475992542971213213L;
		//private Controller controller;
		
		public MainPanel(){
			super(null); // Absolute Layout verwenden
			setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.GRAY));
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if (model.getMainBrick() == null) return;
			
			Color mainBrickColor = model.getMainBrick().getColor();
			Color[][] colorCoordinates = model.getColorCoordinates();
			
			g.setColor(Color.BLACK);
			g.fillRect(model.getMainBrick().getX(), model.getMainBrick().getY(), 41, 41);
			g.setColor(mainBrickColor);
			g.fillRect(model.getMainBrick().getX()+1, model.getMainBrick().getY()+1, 39, 39);
			
			g.setColor(Color.BLACK);
			g.fillRect(model.getMainBrick().getNormalBricks()[0].getX(), model.getMainBrick().getNormalBricks()[0].getY(), 41, 41);
			g.setColor(mainBrickColor);
			g.fillRect(model.getMainBrick().getNormalBricks()[0].getX()+1, model.getMainBrick().getNormalBricks()[0].getY()+1, 39, 39);
			
			g.setColor(Color.BLACK);
			g.fillRect(model.getMainBrick().getNormalBricks()[1].getX(), model.getMainBrick().getNormalBricks()[1].getY(), 41, 41);
			g.setColor(mainBrickColor);
			g.fillRect(model.getMainBrick().getNormalBricks()[1].getX()+1, model.getMainBrick().getNormalBricks()[1].getY()+1, 39, 39);
			
			g.setColor(Color.BLACK);
			g.fillRect(model.getMainBrick().getNormalBricks()[2].getX(), model.getMainBrick().getNormalBricks()[2].getY(), 41, 41);
			g.setColor(mainBrickColor);
			g.fillRect(model.getMainBrick().getNormalBricks()[2].getX()+1, model.getMainBrick().getNormalBricks()[2].getY()+1, 39, 39);
		
			int[][] coordinates = model.getCoordinates();
			for (int i = 0; i < 17; i++){
				for (int e = 0; e < 11; e++){
					if (coordinates[i][e] == 1){
						g.setColor(Color.BLACK);
						g.fillRect(e * 40, i * 40, 41, 41);
						g.setColor(colorCoordinates[i][e]);
						g.fillRect(e * 40 + 1, i * 40 + 1, 39, 39);
					}
				}
			}
		}
	}
	
	/** BlockPanel zeigt das nächste Teil in der Vorschau an **/
	class BlockPanel extends JPanel {
		private static final long serialVersionUID = -2670618096566007534L;
		private JLabel labelUnknown;
		private JLabel labelPoints;
		private JLabel labelBlocks;
		private JLabel labelRows;

		public void toggleLabelUnknown(boolean show){
			labelUnknown.setVisible(show);
		}

		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if (!gameRunning) return;
			
			Color mainBrickColor = model.getNextMainBrick().getColor();
			
			// Variable zur zentralen Darstellung des Tetristeils
			int c = 0;
			switch (model.getNextMainBrick().getBlockType()){
				case 0: c = 120; break;
				case 1: c = 100; break;
				case 2: c = 100; break;
				case 3: c = 100; break;
				case 4: c = 120; break;
				case 5: c = 100; break;
				case 6: c = 100; break;
			}
			
			g.setColor(Color.BLACK);
			g.fillRect(model.getNextMainBrick().getX()-c, model.getNextMainBrick().getY()+70, 41, 41);
			g.setColor(mainBrickColor);
			g.fillRect(model.getNextMainBrick().getX()-c+1, model.getNextMainBrick().getY()+70+1, 39, 39);
			
			g.setColor(Color.BLACK);
			g.fillRect(model.getNextMainBrick().getNormalBricks()[0].getX()-c, model.getNextMainBrick().getNormalBricks()[0].getY()+70, 41, 41);
			g.setColor(mainBrickColor);
			g.fillRect(model.getNextMainBrick().getNormalBricks()[0].getX()-c+1, model.getNextMainBrick().getNormalBricks()[0].getY()+70+1, 39, 39);
			
			g.setColor(Color.BLACK);
			g.fillRect(model.getNextMainBrick().getNormalBricks()[1].getX()-c, model.getNextMainBrick().getNormalBricks()[1].getY()+70, 41, 41);
			g.setColor(mainBrickColor);
			g.fillRect(model.getNextMainBrick().getNormalBricks()[1].getX()-c+1, model.getNextMainBrick().getNormalBricks()[1].getY()+70+1, 39, 39);
			
			g.setColor(Color.BLACK);
			g.fillRect(model.getNextMainBrick().getNormalBricks()[2].getX()-c, model.getNextMainBrick().getNormalBricks()[2].getY()+70, 41, 41);
			g.setColor(mainBrickColor);
			g.fillRect(model.getNextMainBrick().getNormalBricks()[2].getX()-c+1, model.getNextMainBrick().getNormalBricks()[2].getY()+70+1, 39, 39);
			
			labelPoints.setText(String.valueOf(model.getPoints()));
			labelBlocks.setText(String.valueOf(model.getNumberOfBlocks()));
			labelRows.setText(String.valueOf(model.getNumberOfRows()));
		}
		
		
		public BlockPanel(){
			super(null); // Absolute Layout verwenden
			setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
			
			Icon unknownImage = new ImageIcon(getClass().getResource("fragezeichen.png")); 
			labelUnknown = new JLabel(unknownImage);
			labelUnknown.setBounds(15, 30, 200, 200);
			labelUnknown.setVisible(true);
			add(labelUnknown);
			
			JLabel labelBlock = new JLabel("Nächstes Teil:");
			labelBlock.setBounds(15,-5,200,50);
			labelBlock.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
			labelBlock.setVisible(true);
			add(labelBlock);
			
			JLabel labelPointsTitle = new JLabel("Punkte:");
			labelPointsTitle.setBounds(15,210,150,50);
			labelPointsTitle.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
			labelPointsTitle.setVisible(true);
			add(labelPointsTitle);
			labelPoints = new JLabel("0");
			labelPoints.setHorizontalAlignment(SwingConstants.RIGHT);
			labelPoints.setBounds(130,210,93,50);
			labelPoints.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
			labelPoints.setVisible(true);
			add(labelPoints);
			
			JLabel labelBlocksTitle = new JLabel("Bauteile:");
			labelBlocksTitle.setBounds(15,250,200,50);
			labelBlocksTitle.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
			labelBlocksTitle.setVisible(true);
			add(labelBlocksTitle);
			labelBlocks = new JLabel("0");
			labelBlocks.setHorizontalAlignment(SwingConstants.RIGHT);
			labelBlocks.setBounds(130,250,93,50);
			labelBlocks.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
			labelBlocks.setVisible(true);
			add(labelBlocks);
			
			JLabel labelRowsTitle = new JLabel("Zeilen:");
			labelRowsTitle.setBounds(15,290,200,50);
			labelRowsTitle.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
			labelRowsTitle.setVisible(true);
			add(labelRowsTitle);
			labelRows = new JLabel("0");
			labelRows.setHorizontalAlignment(SwingConstants.RIGHT);
			labelRows.setBounds(130,290,93,50);
			labelRows.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
			labelRows.setVisible(true);
			add(labelRows);
		}
	}
	
	
	
	/** ControlPanel beinhaltet Button zur Steuerung **/
	class ControlPanel extends JPanel {
		
		private static final long serialVersionUID = -7384355806763417873L;
		private JButton btnStarten;
		private JButton btnPause;
		private JRadioButton radioButtons[] = new JRadioButton[4];
		private JComboBox<String> comboSteuerung;
		private JCheckBox checkMelodie;
		
		public JButton getBtnStarten(){
			return btnStarten;
		}
		public JButton getBtnPause(){
			return btnPause;
		}
		public JRadioButton[] getRadioButtons(){
			return radioButtons;
		}
		public JComboBox<String> getComboSteuerung(){
			return comboSteuerung;
		}
		public JCheckBox getCheckMelodie(){
			return checkMelodie;
		}
		
		public ControlPanel(){
			super(null); // Absolute Layout verwenden			
			btnStarten = new JButton("Spiel starten [G]");
			btnStarten.setBounds(10, 10, 220, 25);
			btnStarten.setFocusable(false);
			add(btnStarten);
			
			btnPause = new JButton("Spiel pausieren [P]");
			btnPause.setBounds(10, 45, 220, 25);
			btnPause.setFocusable(false);
			btnPause.setEnabled(false);
			add(btnPause);
			
			JPanel panelRadio = new JPanel(null);
			panelRadio.setBounds(10, 83, 220, 180);
			panelRadio.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY), "Schwierigkeitsgrad"));
			JRadioButton rb1 = new JRadioButton("Einfach");
			rb1.setBounds(20, 35, 100, 20);
			rb1.setFocusable(false);
			JRadioButton rb2 = new JRadioButton("Normal");
			rb2.setBounds(20, 70, 100, 20);
			rb2.setSelected(true);
			rb2.setFocusable(false);
			JRadioButton rb3 = new JRadioButton("Schwierig");
			rb3.setBounds(20, 105, 100, 20);
			rb3.setFocusable(false);
			JRadioButton rb4 = new JRadioButton("Extrem");
			rb4.setBounds(20, 140, 100, 20);
			rb4.setFocusable(false);
			ButtonGroup btnGroup = new ButtonGroup();
			btnGroup.add(rb1);
			btnGroup.add(rb2);
			btnGroup.add(rb3);
			btnGroup.add(rb4);
			panelRadio.add(rb1);
			panelRadio.add(rb2);
			panelRadio.add(rb3);
			panelRadio.add(rb4);
			radioButtons[0] = rb1;
			radioButtons[1] = rb2;
			radioButtons[2] = rb3;
			radioButtons[3] = rb4;
			add(panelRadio);
			
			String[] comboOptions = {"Mit Pfeiltasten steuern", "Mit W, A, S, D steuern"};
			comboSteuerung = new JComboBox<String>(comboOptions);
			comboSteuerung.setBounds(12, 270, 218, 25);
			comboSteuerung.setFocusable(false);
			add(comboSteuerung);
			
			checkMelodie = new JCheckBox("Melodie einschalten");
			checkMelodie.setBounds(10, 303, 142, 25);
			checkMelodie.setFocusable(false);
			checkMelodie.setSelected(true);
			add(checkMelodie);	
			
			JLabel labelMusic = new JLabel("(bensound.com)");
			labelMusic.setBounds(153, 303, 100, 25);
			labelMusic.setFont(new Font("serif", Font.PLAIN, 10));
			labelMusic.setFocusable(false);
			add(labelMusic);
		}
	}
}
