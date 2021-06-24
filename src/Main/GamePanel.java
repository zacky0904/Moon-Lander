package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import Audio.AudioPlayer;
import GameEngine.GameStateManager;


public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int SCALE = 1;
	
	//thread 
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long loopDuration = 1000/FPS;
	protected int runtimeFPS;
	
	//image
	private BufferedImage image;
	private Graphics2D g;
	private AudioPlayer bgMusic;
	
//	game state manager
	private GameStateManager gsm;
	
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
		bgMusic = new AudioPlayer("/Audio/Space Oddity 8bit.wav",-20f);
		bgMusic.play();
		
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		running = true ;
		gsm = new GameStateManager();
	}
	
	public void run() {
		
		init();
		
		// loop time
		long start;
		long total=1;
		long elapsed = 0;
		long wait;
		
		//game loop
		while(running) {
			
			start = System.nanoTime();
			runtimeFPS =  (int)(1000000000l/total);
			//game compute
			update();
			
			//render
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			
			wait = loopDuration - elapsed / 1000000;
			try {
				Thread.sleep(Math.max(0,wait));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			total = System.nanoTime()-start;

			
		}
		
	}
	
	private void update() {
		if(!bgMusic.isRunning())
			bgMusic.play();
		gsm.update();
	}
	
	private void draw() {
		g.setColor(Color.black);
		g.drawRect(0, 0, WIDTH, HEIGHT);
		gsm.draw(g);
//		g.setColor(Color.YELLOW);
//		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
//		g.drawString(""+runtimeFPS, 5, 30);
	}
	
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();
	}
	
	
	
//-----------------User Input------------------------------------------------------------------------------
	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key.getKeyCode());
	}
	
	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());
	}

	public void mouseMoved(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void keyTyped(KeyEvent key) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	
}
