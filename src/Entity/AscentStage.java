package Entity;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import GameEngine.GameStatus;




public class AscentStage extends GameObject {

	private final int FPS = 60;
	private final double loopDuration = 1/(float)FPS;
	//moon gravity(m/s^2)
	private final float gravity = 1.62f;
	private final float mass = 2000; //kg
	private float thrust = 0; //N
	
	private final float scale = 2;
	private final float pixelReference = 40;
	private final float meterReference = 7;
	private final float meterPixelRatio = pixelReference*scale/meterReference;
	
	private float speedX;
	private float speedY;
	private float fuel;
	
	private BufferedImage ascentStage;
	private BufferedImage[] flame = new BufferedImage[2];
	private BufferedImage[] RCSParticleLeft = new BufferedImage[2];
	private BufferedImage[] RCSParticleRight = new BufferedImage[2];
	
	private Animation FLAME = new Animation();
	private Animation PARTICLE_LEFT = new Animation();
	private Animation PARTICLE_RIGHT = new Animation();
	
	private BufferedImage image;
	
	private boolean FLAME_ANIMATION_FLAG = false;
	private boolean RCS_LEFT_ANIMATION_FLAG = false;
	private boolean RCS_RIGHT_ANIMATION_FLAG = false;
	
	private boolean EngineEngage = false;
	
	private AudioPlayer thrustSE;
	private AudioPlayer rcs1SE;
	private AudioPlayer rcs2SE;
	
	private boolean THRUST_ENGAGE;
	private boolean LEFT_RCS_ENGAGE;
	private boolean RIGHT_RCS_ENGAGE;
	private boolean SAS = true;
	
	public AscentStage() {
		
		super.setHitbox(28,16);
		super.da = 0;
		super.dx = 0;
		super.dy = 0;
		if(GameStatus.difficulty == 0)
			SAS = true;
		else
			SAS = false;
		try {
			//load image
			ascentStage = ImageIO.read(getClass().getResourceAsStream("/Tile/Apollo Lunar Lander.png")).getSubimage(46, 0, 32, 24);
			flame[0] = ImageIO.read(getClass().getResourceAsStream("/Tile/Apollo Lunar Lander.png")).getSubimage(46, 32, 8, 8);
			flame[1] = ImageIO.read(getClass().getResourceAsStream("/Tile/Apollo Lunar Lander.png")).getSubimage(58, 32, 8, 8);
			RCSParticleLeft[0] = ImageIO.read(getClass().getResourceAsStream("/Tile/Apollo Lunar Lander.png")).getSubimage(44, 50, 12, 8);
			RCSParticleLeft[1] = ImageIO.read(getClass().getResourceAsStream("/Tile/Apollo Lunar Lander.png")).getSubimage(44, 60, 12, 8);
			RCSParticleRight[0] = ImageIO.read(getClass().getResourceAsStream("/Tile/Apollo Lunar Lander.png")).getSubimage(62, 50, 12, 8);
			RCSParticleRight[1] = ImageIO.read(getClass().getResourceAsStream("/Tile/Apollo Lunar Lander.png")).getSubimage(62, 60, 12, 8);
			
			thrustSE = new AudioPlayer("/Audio/Driving.wav",2f);
			rcs1SE = new AudioPlayer("/Audio/Wind.wav",-10f);
			rcs2SE = new AudioPlayer("/Audio/Wind.wav",-10f);
			//init Animation
			FLAME.setFrames(flame);
			FLAME.setDelay(100);
			PARTICLE_LEFT.setFrames(RCSParticleLeft);
			PARTICLE_LEFT.setDelay(50);
			PARTICLE_RIGHT.setFrames(RCSParticleRight);
			PARTICLE_RIGHT.setDelay(50);

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void buildImage() {
		image = new BufferedImage(56, 32, BufferedImage.TYPE_INT_ARGB) ;
		Graphics2D imgBuffer = image.createGraphics();
		imgBuffer.drawImage(ascentStage, 12, 0, null);
		
		if(FLAME_ANIMATION_FLAG) {
			imgBuffer.drawImage(FLAME.getImage(), 24, 24, null);
		}
		
		if(RCS_LEFT_ANIMATION_FLAG) {
			imgBuffer.drawImage(PARTICLE_LEFT.getImage(), 1, 8, null);
		}
		
		if(RCS_RIGHT_ANIMATION_FLAG) {
			imgBuffer.drawImage(PARTICLE_RIGHT.getImage(), 44, 8, null);
		}
		
		imgBuffer.dispose();
		
	}
	
	public void init() {
		speedX = 0;
		speedY = 0;
		dx = 0;
		dy = 0;
		da = 0;
		angle = 0;
		THRUST_ENGAGE = false;
		LEFT_RCS_ENGAGE = false;
		RIGHT_RCS_ENGAGE = false;
		FLAME_ANIMATION_FLAG = false;
		RCS_LEFT_ANIMATION_FLAG = false;
		RCS_RIGHT_ANIMATION_FLAG = false;
	}
	
	public void caluculateSpeed() {
		speedY += gravity*loopDuration;
		
		if(THRUST_ENGAGE && fuel > 0) {
			thrust = 32000;
			if(!thrustSE.isRunning())
				thrustSE.play();
			FLAME_ANIMATION_FLAG = true;
			fuel -= 0.01;
			if(fuel <= 0)
				fuel = 0;
		}
		else {
			thrust = 0;
			thrustSE.stop();
			FLAME_ANIMATION_FLAG = false;
		}
		//calculate RCS

		if(SAS) {
			LeftRCS(LEFT_RCS_ENGAGE,0.8f);
			RightRCS(RIGHT_RCS_ENGAGE,0.8f);
			if(super.da > 0.8 )
				super.da = 0.8;
			if(super.da < -0.8)
				super.da = -0.8;
			if(!(LEFT_RCS_ENGAGE || RIGHT_RCS_ENGAGE)) {
				if(Math.abs(super.da) < 0.2 )
					super.da = 0;
				if(super.da<0)
					RightRCS(true,0.1f);
				if(super.da>0)
					LeftRCS(true,0.1f);

			}
		}
		else {
			LeftRCS(LEFT_RCS_ENGAGE,0.02f);
			RightRCS(RIGHT_RCS_ENGAGE,0.02f);
		}
	
		speedX += thrust/mass*Math.sin(Math.toRadians(angle))*loopDuration;
		speedY -= thrust/mass*Math.cos(Math.toRadians(angle))*loopDuration;
		super.dx = speedX/meterPixelRatio;
		super.dy = speedY/meterPixelRatio;

	}
	
	private void LeftRCS(boolean flag,float thrust) {
		if(flag) {
		super.da -= thrust;
		if(!rcs1SE.isRunning())
			rcs1SE.play();
		RCS_RIGHT_ANIMATION_FLAG = true;
		}else {
			rcs1SE.stop();
			RCS_RIGHT_ANIMATION_FLAG = false;
		}
	}
	
	private void RightRCS(boolean flag, float thrust) {
		if(flag) {
		super.da += thrust;
		if(!rcs2SE.isRunning())
			rcs2SE.play();
		RCS_LEFT_ANIMATION_FLAG = true;
		}else {
			rcs2SE.stop();
			RCS_LEFT_ANIMATION_FLAG = false;
		}
	}
	
	public void update() {
		super.x += super.dx;
		super.y += super.dy;
		super.angle += super.da;

		if(super.angle>360)
			super.angle = 0;
		if(super.angle<0)
			super.angle = 360;
		FLAME.update();
		PARTICLE_LEFT.update();
		PARTICLE_RIGHT.update();
		
		//image
		buildImage();
		setImage(image);
	}      
	
	public float[] getSpeed() {
		float speed[] = new float[2];
		speed[0] = speedX;
		speed[1] = speedY;
		return speed;
	}

	public void setMainThrust(boolean flag) {
		THRUST_ENGAGE = flag;
	}
	
	public void setLeftRCS(boolean flag) {
		LEFT_RCS_ENGAGE = flag;
		
	}
	public void setRightRCS(boolean flag) {
		RIGHT_RCS_ENGAGE = flag;
	}
	
	public float getFuel() {
		return fuel;
	}
	
	public void setFuel(float x) {
		fuel = x;
	}
	
	public void setEngine(boolean flag) {
		EngineEngage = flag;
	}
	
	public boolean  getEngine() {
		return EngineEngage;
	}

}
