package GameLevel;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Entity.DescentStage;
import Entity.Animation;
import Entity.AscentStage;
import Entity.GameMap;
import Entity.GameObject;
import Entity.GameStages;
import GUI.GUI;
import GUI.PauseGUI;
import GUI.SubGUI;
import GameEngine.GameEngine;
import GameEngine.GameStateManager;
import GameEngine.GameStatus;
import Main.GamePanel;

public class MainState extends GameEngine {
	private int counter = 0;
	private GUI gui;
	private SubGUI LANDSUCCES;
	private PauseGUI pause;
	
//	private int camPosX;
	private int camPosY;
	
	private int mapSizeX;
	private int mapSizeY;
			
//	private final int screenWidth = GamePanel.WIDTH;
	private final int screenHeight = GamePanel.HEIGHT;
	
	private int landPosX;
	private int landPosY;
	
	private int initPosX;
	private int initPosY;
	
	private int initSpdX;
	private int initSpdY;
	
	private float fuel1;
	private float fuel2;
	
	private DescentStage pl;
	private AscentStage pl2;
	private GameObject landingPad;
	private GameMap map;

	private BufferedImage background;
	private BufferedImage playGround;
	private BufferedImage tmpImg;
	private BufferedImage tmp[] = new BufferedImage[12];
	private Animation Explode = new Animation();
	private boolean drawDescentExplode = false;
	private boolean drawAscentExplode = false;
	private Graphics2D playGroundBuffer;
	
	private AudioPlayer ExplotionSE;
	
	private long start;
	private long elapsed;
	
	private int nextLevel;
	private String levelName;


	private GameStages currentState = GameStages.DESCENT_STAGE_INIT;
	private GameStages lastState;
	

	
	
	public MainState(String mapName,int nextLevel, GameStateManager gsm) {
		this.gsm = gsm;
		this.nextLevel = nextLevel;
		landingPad = new GameObject();
		map = new GameMap();
		levelName = mapName;
		try {
			background = ImageIO.read(getClass().getResourceAsStream("/Background/New Star.jpg"));
			landingPad.setImage(ImageIO.read(getClass().getResourceAsStream("/Tile/Apollo Lunar Lander.png")).getSubimage(0, 114, 120, 12));
			ExplotionSE = new AudioPlayer("/Audio/explosion09.wav",-10f);
			landingPad.setHitbox(120, 12);
			tmpImg = ImageIO.read(getClass().getResourceAsStream("/Tile/Explosion 8bit.png"));
			for(int i=0;i<12;i++) {
				tmp[i] = tmpImg.getSubimage(i*96, 0, 96, 96);
			}
			Explode.setFrames(tmp);
			Explode.setDelay(80);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		map.loadImage("/Maps/"+ mapName +".png");
		map.loadMapFile("/Maps/"+ mapName +".map");
		initPosX = map.getInitialPoint()[0];
		initPosY = map.getInitialPoint()[1];
		landPosX = map.getLandingPadPos()[0];
		landPosY = map.getLandingPadPos()[1];
		mapSizeX = map.mapSize()[0];
		mapSizeY = map.mapSize()[1];
		initSpdX = map.getInitialSpeed()[0];
		initSpdY = map.getInitialSpeed()[1];
		fuel1 = map.getFuel()[0];
		fuel2 = map.getFuel()[1];
		

		map.setMapInfoVisable(true);
		playGround =  new BufferedImage(mapSizeX, mapSizeY, BufferedImage.TYPE_INT_RGB);
		playGroundBuffer = (Graphics2D) playGround.getGraphics();
		pl = new DescentStage();
		pl2 = new AscentStage();
		pl2.visible(false);
		gui = new GUI();
		LANDSUCCES = new SubGUI();
		pause = new PauseGUI();
		pause.setVisible(false);
		landingPad.setPosition(landPosX, landPosY);
		start = System.nanoTime();
		
		//debug
		if(GameStatus.debugFlag) {
			landingPad.boundVisible(true);
			pl.boundVisible(true);
			pl2.boundVisible(true);
			map.showInfo();
		}
	}


	public void init() {}


	public void update() {
		elapsed = System.nanoTime() - start;
		gui.getTime(elapsed);
		gui.getLevel(levelName);
		gui.update();
//		System.out.println(currentState);
		switch(currentState){
			case DESCENT_STAGE_INIT:
				DESCENT_STAGE_INIT_PROC() ;
				break;
			case ASCENT_STAGE_INIT:
				ASCENT_STAGE_INIT_PROC() ;
				break;
			case DESCENT_STAGE_RUNNING:
				DESCENT_STAGE_RUNNING_PROC();
				break;
			case ASCENT_STAGE_RUNNING:
				ASCENT_STAGE_RUNNING_PROC();
				break;
			case DESCENT_EXPLODE:
				DESCENT_EXPLODE_PROC();
				break;
			case ASCENT_EXPLODE:
			 	ASCENT_EXPLODE_PROC();
			 	break;
			case DESCENT_STAGE_PASSED:
				DESCENT_STAGE_PASSED_PROC();
				break;
			case ASCENT_STAGE_PASSED:
				ASCENT_STAGE_PASSED_PROC();
				break;
			case ASCENT_STAGE_STANDBY:
				ASCENT_STAGE_STANDBY_PROC();
				break;
			case DESCENT_STAGE_PASSED_WAIT:
				DESCENT_STAGE_PASSED_WAIT_PROC();
				break;
			case PAUSE:
				PAUSE_PROC();
				break;
			default:
				break;
		}
	}
	//----------------------------------------GAME PROC----------------------------------------
	//DESCENT_STAGE_INIT_PROC
	public void DESCENT_STAGE_INIT_PROC() {
		pl.visible(true);
		pl.init();
		pl.setPosition(initPosX, initPosY);
		pl.setSpeed(initSpdX, initSpdY);
		pl.setFuel(fuel1);
		currentState = GameStages.DESCENT_STAGE_RUNNING;
		
	}
	
	//ASCENT_STAGE_INIT_PROC
	public void ASCENT_STAGE_INIT_PROC() {
		pl.visible(true);
		pl2.visible(true);
		pl2.init();
		pl2.setPosition(pl.getPosition()[0], pl.getPosition()[1]-6);
		pl2.setFuel(fuel2);
		calCamPos(pl2);
		pl2.update();
		currentState = GameStages.ASCENT_STAGE_STANDBY;
		
	}
	
	//DESCENT_STAGE_RUNNING_PROC
	public void  DESCENT_STAGE_RUNNING_PROC() {
		gui.setVisible(true);
		pl.caluculateSpeed();
		if(pl.checkCollid(map)) {
			ExplotionSE.play();
			currentState = GameStages.DESCENT_EXPLODE;
		}
		
		if(pl.checkCollid(landingPad)) {
			float angle = (float) pl.getAngle();
			if(angle > 180)
				angle = 360 - angle;
			if( Math.abs(pl.getSpeed()[0]) > 3|| Math.abs(pl.getSpeed()[1]) > 3 || angle > 5) {
				ExplotionSE.play();
				currentState = GameStages.DESCENT_EXPLODE;
			}
			else {
				currentState = GameStages.DESCENT_STAGE_PASSED;
			}
				
		}
		pl.update();
		gui.getSpeed((int)pl.getSpeed()[0], (int)pl.getSpeed()[1]);
		gui.getFuel(pl.getFuel());
		calCamPos(pl);
	}
	//ASCENT_STAGE_STANDBY_PROC
	public void  ASCENT_STAGE_STANDBY_PROC() {
		if(pl2.getEngine()) {
			LANDSUCCES.setVisible(true);
			LANDSUCCES.setString("ASCEND STAGE LAUNCHING...");
			pl2.setMainThrust(true);
			pl2.caluculateSpeed();
			pl2.setFuel(5);
			pl2.update();
			gui.getSpeed((int)pl2.getSpeed()[0], (int)pl2.getSpeed()[1]);
			counter++;
			if(counter > 40) {
				pl2.setEngine(false);
				pl2.setMainThrust(false);
				LANDSUCCES.setVisible(false);
				pl2.setFuel(fuel2);
				counter = 0;
				currentState = GameStages.ASCENT_STAGE_RUNNING;
				
				}
			
		}else {
			LANDSUCCES.setVisible(true);
			LANDSUCCES.setString("PRESS UP KEY TO LAUNCH ASCENT STAGE");
			counter = 0;
		}

	}
	//ASCENT_STAGE_RUNNING_PROC
	public void  ASCENT_STAGE_RUNNING_PROC() {
		pl2.caluculateSpeed();
		if(pl2.checkCollid(map)) 
			currentState = GameStages.ASCENT_EXPLODE;

		if(pl2.checkCollid(landingPad)) {
			currentState = GameStages.ASCENT_EXPLODE;
		}
//		if(pl2.checkCollid(pl)) {
//			currentState = GameStatus.ASCENT_EXPLODE;
//		}
		if(pl2.getPosition()[1] < -30)
			currentState = GameStages.ASCENT_STAGE_PASSED;
		pl2.update();
		gui.getSpeed((int)pl2.getSpeed()[0], (int)pl2.getSpeed()[1]);
		gui.getFuel(pl2.getFuel());
		calCamPos(pl2);
	}
	//DESCENT_EXPLODE_PROC
	public void DESCENT_EXPLODE_PROC() {
		Explode.update();
		if(Explode.hasPlayedOnce()) {
			
			currentState = GameStages.DESCENT_STAGE_INIT;
			Explode.setFrame(0);
			drawDescentExplode = false;
		}
			
		else {
			pl.visible(false);
			currentState = GameStages.DESCENT_EXPLODE;
			drawDescentExplode = true;
		}
	}
	//ASCENT_EXPLODE_PROC
	public void ASCENT_EXPLODE_PROC() {
		Explode.update();
		if(Explode.hasPlayedOnce()) {
			currentState = GameStages.ASCENT_STAGE_INIT;
			Explode.setFrame(0);
			drawAscentExplode = false;
		}
			
		else {
			if(!ExplotionSE.isRunning())
				ExplotionSE.play();
			pl2.visible(false);
			currentState = GameStages.ASCENT_EXPLODE;
			drawAscentExplode = true;
		}
	}	
	
	//DESCENT_STAGE_PASSED_PROC
	public void DESCENT_STAGE_PASSED_PROC() {
		pl.setAngle(0);
		pl.setVector(0, 0);
		pl.hideAscentStage(true);
		pl.init();
		pl.update();
		pl.disable(true);
		pl2.init();
		pl2.setPosition(pl.getPosition()[0], pl.getPosition()[1]-6);
		pl2.visible(true);
		pl2.update();
		gui.getSpeed(0, 0);
		gui.getFuel(pl2.getFuel());
		currentState = GameStages.DESCENT_STAGE_PASSED_WAIT;
		counter = 0;
	}
	
	//DESCENT_STAGE_PASSED_WAIT_PROC
	public void DESCENT_STAGE_PASSED_WAIT_PROC() {
		counter++;
		LANDSUCCES.setVisible(true);
		LANDSUCCES.setString("SUCCESSFULLY LANDED");
		if(counter > 80) {
			counter = 0;
			currentState = GameStages.ASCENT_STAGE_STANDBY;
			}
	}
	//ASCENT_STAGE_PASSED_PROC
	public void ASCENT_STAGE_PASSED_PROC() {
		counter++;
		pl2.init();
		gui.setVisible(false);
		LANDSUCCES.setVisible(true);
		LANDSUCCES.setString("MISSION ACCOMPLISHED");
		if(counter>100) {
			counter = 0;
			if(GameStatus.mode == GameStatus.gamemode.SINGLELEVEL)
				gsm.setState(GameStateManager.LEVEL_SELECTOR);
			if(GameStatus.mode == GameStatus.gamemode.SPEEDRUN)
				gsm.setState(nextLevel);
		}
			
	}
	//PAUSE_PROC
	public void PAUSE_PROC() {
		pause.setVisible(true);	
		gui.setVisible(false);
	}
	

	//----------------------------------------------------------------------------
	
	public void calCamPos(GameObject obj) {
		camPosY = (int)(-(obj.getPosition()[1] - 360));
		if(camPosY > 0)
			camPosY = 0;
		if(camPosY < -(mapSizeY-screenHeight))
			camPosY = -(mapSizeY-screenHeight);
		
	}

	public void draw(Graphics2D g) {
		playGroundBuffer.drawImage(background, 0, -camPosY, null);
		playGroundBuffer.drawImage(map.getImage(), 0, 0, null);
		playGroundBuffer.drawImage(pl2.getImage(), (int)pl2.getOffsetPosition()[0], (int)pl2.getOffsetPosition()[1], null);
		playGroundBuffer.drawImage(pl.getImage(), (int)pl.getOffsetPosition()[0], (int)pl.getOffsetPosition()[1], null);
		playGroundBuffer.drawImage(landingPad.getImage(), (int)landingPad.getOffsetPosition()[0], (int)landingPad.getOffsetPosition()[1], null);
		if(drawDescentExplode)	
			Explotion(pl);
		if(drawAscentExplode)	
			Explotion(pl2);
		
		g.drawImage(playGround, 0, camPosY, null);
		
		gui.draw(g);
		pause.draw(g);	
		LANDSUCCES.draw(g);
	
	}
	
	public void Explotion(GameObject obj) {
			playGroundBuffer.drawImage(Explode.getImage(), (int) (obj.getPosition()[0]-48), (int) (obj.getPosition()[1]-48), null);
	}
	
	public void keyPressed (int k) {
		if(k == KeyEvent.VK_UP) 		{
			pl.setMainThrust(true); pl2.setMainThrust(true); 
			if(currentState == GameStages.ASCENT_STAGE_STANDBY) 
				pl2.setEngine(true);
			if(currentState == GameStages.PAUSE) 
				pause.setChoice(-1);	
			}
		if(k == KeyEvent.VK_DOWN) {
			if(currentState == GameStages.PAUSE) 
				pause.setChoice(1);	
		}
		if(k == KeyEvent.VK_LEFT) 		{
			pl.setLeftRCS(true); 
			pl2.setLeftRCS(true);
			}
		if(k == KeyEvent.VK_RIGHT) 		{
			pl.setRightRCS(true); 
			pl2.setRightRCS(true);
			}
		if(k == KeyEvent.VK_W) 		{
			pl.setMainThrust(true); 
			pl2.setMainThrust(true); 
			if(currentState == GameStages.ASCENT_STAGE_STANDBY) 
				pl2.setEngine(true);
			}
		if(k == KeyEvent.VK_A) 		{
			pl.setLeftRCS(true); pl2.setLeftRCS(true);
			}
		if(k == KeyEvent.VK_D) 		{
			pl.setRightRCS(true); pl2.setRightRCS(true);
			}
		if(k == KeyEvent.VK_ESCAPE) 		{
			lastState = currentState;
			currentState = GameStages.PAUSE;
			}
		if(k == KeyEvent.VK_ENTER) {	
			if(currentState == GameStages.PAUSE) {
				pause.beep();
				pause.setVisible(false);
				if(pause.getChoice() == 0) {
					currentState = lastState;
				}
				if(pause.getChoice() == 1)
					currentState = GameStages.DESCENT_STAGE_INIT;
					
				if(pause.getChoice() == 2)
					gsm.setState(GameStateManager.LEVEL_SELECTOR);
			}
		}
		if(k == KeyEvent.VK_R) {
			if(currentState == GameStages.DESCENT_STAGE_RUNNING)
				currentState = GameStages.DESCENT_STAGE_INIT;
			if(currentState == GameStages.ASCENT_STAGE_RUNNING)
				currentState = GameStages.ASCENT_STAGE_INIT;
		}
//		if(k == KeyEvent.VK_UP) pl.setVectorY(-5);
//		if(k == KeyEvent.VK_DOWN) pl.setVectorY(5);
//		if(k == KeyEvent.VK_LEFT) pl.setVectorX(-5);
//		if(k == KeyEvent.VK_RIGHT) pl.setVectorX(5);

		
	}
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_UP) 		{pl.setMainThrust(false); pl2.setMainThrust(false);}
		if(k == KeyEvent.VK_LEFT) 		{pl.setLeftRCS(false); pl2.setLeftRCS(false);}
		if(k == KeyEvent.VK_RIGHT) 		{pl.setRightRCS(false); pl2.setRightRCS(false);}
		if(k == KeyEvent.VK_W) 		{pl.setMainThrust(false); pl2.setMainThrust(false);}
		if(k == KeyEvent.VK_A) 		{pl.setLeftRCS(false); pl2.setLeftRCS(false);}
		if(k == KeyEvent.VK_D) 		{pl.setRightRCS(false); pl2.setRightRCS(false);}
//		if(k == KeyEvent.VK_UP) pl.setVectorY(0);
//		if(k == KeyEvent.VK_DOWN) pl.setVectorY(0);
//		if(k == KeyEvent.VK_LEFT) pl.setVectorX(0);
//		if(k == KeyEvent.VK_RIGHT) pl.setVectorX(0);

		
	}

	@Override
	public void MousePressed(int m) {}
	public void MouseReleased(int m) {}


}
