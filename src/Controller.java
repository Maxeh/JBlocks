import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class Controller {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
	        public void run() {
				new Controller();
	        }
	    });
	}
	
	private View view;
	private Model model;
	private Timer gameTimer;
	private boolean pause = false;
	private boolean gameRunning = false;
	private Clip clip;
	
	public Controller(){
		model = new Model();
		view = new View(model);	
		setListenersAndBindings();
	}
		
	/** Methode wird aufgerufen, wenn die Tetristeile sich bis oben gestapelt haben und das Spiel damit vorbei ist. **/
	public void gameOver(){
		gameTimer.stop();
		gameRunning = false;
		view.gameOver();
		model.init();
	}
	
	/** Spielschleife, welche die Bauteile herunterfallen lässt. **/
	public void gameLoop(){
		ActionListener timerListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getMainBrick().moveDown()) { 
					view.getMainPanel().repaint();
					view.getBlockPanel().repaint();
				}
				else gameOver();				
			}
		};
		int speed = 500;
		for (int i = 0; i < 4; i++){
			if (view.getControlPanel().getRadioButtons()[i].isSelected())
				speed = (int) (500 - i * 0.8 * 100); // 500, 420, 340, 260
		}
		gameTimer = new Timer(speed, timerListener);
		gameTimer.start();	
	}
	
	/*********************************************************************/
	/*********************** Listener & Bindings *************************/
	/*********************************************************************/
	
	/** Die Methode setzt die Listeners und Bindings, welche zur Steuerung des Spiels benötigt werden. **/	
	public void setListenersAndBindings(){	
		
		class startAction extends AbstractAction{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) { 
				if (gameRunning) return;
				gameRunning = true;				
				gameLoop();	
				view.startGame();
			}
		}
		JButton startButton = view.getControlPanel().getBtnStarten();
		startButton.addActionListener((ActionListener) new startAction());
		startButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), "start");
		startButton.getActionMap().put("start", new startAction());
		
		/*********************************************************************/
		
		/** Binding für den PauseButton **/
		class pauseAction extends AbstractAction{
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!gameRunning) return;
				if (!pause) {
					pause = true;
					view.pause(true);
					gameTimer.stop();
				} else {
					pause = false;
					view.pause(false);
					gameTimer.start();
				}
			}
		}
		JButton pauseButton = view.getControlPanel().getBtnPause();
		pauseButton.addActionListener((ActionListener) new pauseAction());
		pauseButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("P"), "pause");
		pauseButton.getActionMap().put("pause", new pauseAction());
		
		/*********************************************************************/
		
		/** CheckBox für die Melodie des Spiels **/
		JCheckBox checkMelodie = view.getControlPanel().getCheckMelodie();
		checkMelodie.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (((JCheckBox)e.getSource()).isSelected()){
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				}
				else clip.stop();
			}
		});
		
		/** Melodie beim Start des Programms abspielen **/
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("music.wav").getAbsoluteFile());
			clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.loop(Clip.LOOP_CONTINUOUSLY);
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
		
		/*********************************************************************/
		
		/** KeyBindings für Steuerungstasten **/
		class keyAction extends AbstractAction {
			private static final long serialVersionUID = 1L;
			private String action;
			public keyAction(String action) {
				this.action = action;
			}
		    public void actionPerformed(ActionEvent e) {
		    	if (!gameRunning || pause) return;
				switch (action){
					case "left": model.getMainBrick().moveLeft(); break; 
					case "up": model.getMainBrick().changeBlock(); break;
					case "right": model.getMainBrick().moveRight(); break;
					case "down": model.getMainBrick().moveDown(); break;
				}
		        view.getMainPanel().repaint();
		        view.getBlockPanel().repaint();
		    }
		}
		final InputMap im = view.getMainPanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		final ActionMap am = view.getMainPanel().getActionMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		am.put("left",  new keyAction("left"));
		am.put("up",  new keyAction("up"));
		am.put("right",  new keyAction("right"));
		am.put("down",  new keyAction("down"));
				
		/** Tasten für die Steuerung festlegen **/
		JComboBox<String> comboSteuerung = view.getControlPanel().getComboSteuerung();
		comboSteuerung.addActionListener(new ActionListener(){
			@SuppressWarnings("rawtypes")
			@Override
			public void actionPerformed(ActionEvent e) {
				im.clear();
				am.clear();
				switch (((JComboBox) e.getSource()).getSelectedIndex()){
					// Mit Pfeiltasten steuern
					case 0: 
						im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
						im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
						im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
						im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
						am.put("left",  new keyAction("left"));
						am.put("up",  new keyAction("up"));
						am.put("right",  new keyAction("right"));
						am.put("down",  new keyAction("down"));
						break;
					// Mit W, A, S, D steuern
					case 1:
						im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "left");
						im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "up");
						im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "right");
						im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down");
						am.put("left",  new keyAction("left"));
						am.put("up",  new keyAction("up"));
						am.put("right",  new keyAction("right"));
						am.put("down",  new keyAction("down"));
						break;
				}
			}
		});
	}

}
