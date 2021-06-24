package GameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Entity.DescentStage;

public class MenuState extends GameEngine{
	
	private AudioPlayer beep;
	private BufferedImage background;
	private DescentStage pl;
	int currentChoice = 0;
	private String[] options = {
		"START",
		"QUIT",

	};
	
	private Color titleColor;
	private Font titleFont;
	private Font smallFont;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;	
		try{
			background = ImageIO.read(getClass().getResourceAsStream("/Background/New Star.jpg"));
			InputStream stream =  MenuState.class.getResourceAsStream("/Font/AtariClassic-gry3.ttf");
			titleFont = Font.createFont(Font.TRUETYPE_FONT, stream);
			titleFont = titleFont.deriveFont(90F);
			smallFont = titleFont;
			smallFont = smallFont.deriveFont(40f);

		} catch(FontFormatException|IOException e) {e.printStackTrace();}

		beep = new AudioPlayer("/Audio/sfx_select.wav",-10f);
		init();
		
	}

	@Override
	public void init() {
		pl = new DescentStage();
		pl.setVectorA(0.5);
//		pl.setVectorX(Math.random()*2);
//		pl.setVectorY(Math.random()*2);
		pl.setPosition(-200, 300);
		titleColor = Color.WHITE;
	}

	@Override
	public void update() {
		if(pl.getPosition()[0] > 1400 || pl.getPosition()[0] < -100 || pl.getPosition()[1] > 800 || pl.getPosition()[1] < -100) {
			int rnd = (int) (Math.random()*4);
			if(rnd == 0) {
				pl.setPosition(-50, Math.random()*720);
				pl.setVectorX(1);
				int spd;
				if(Math.round(Math.random()) == 0)
					spd = -1;
				else 
					spd = 1;
				pl.setVectorY(spd);
			}
			if(rnd == 1) {
				pl.setPosition(1350, Math.random()*720);
				pl.setVectorX(-1);
				int spd;
				if(Math.round(Math.random()) == 0)
					spd = -1;
				else 
					spd = 1;
				pl.setVectorY(spd);
			}
			if(rnd == 2) {
				pl.setPosition( Math.random()*1280, -50);
				pl.setVectorY(1);
				int spd;
				if(Math.round(Math.random()) == 0)
					spd = -1;
				else 
					spd = 1;
				pl.setVectorX(spd);
			}
			if(rnd == 3) {
				pl.setPosition( Math.random()*1280,780);
				pl.setVectorY(-1);
				int spd;
				if(Math.round(Math.random()) == 0)
					spd = -1;
				else 
					spd = 1;
				pl.setVectorX(spd);
			}
			pl.setVectorA(-0.5);
		}
		
		pl.update();
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(background,0,0,null);
		g.drawImage(pl.getImage(), (int)pl.getPosition()[0],(int)pl.getPosition()[1], null);
		g.setColor(titleColor);
		drawStrCenter(g,titleFont,"LUNAR LANDER",640,200);
//		g.setFont(titleFont);
//		g.drawString("LUNAR LANDER", 100, 200);
		
		// draw menu options
		g.setFont(smallFont);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.YELLOW);
				drawStrCenter(g,smallFont,"["+options[i]+"]",640,620 + i * 60);
//				g.drawString("<"+options[i]+">", 100, 510 + i * 60);
			}
			else {
				g.setColor(Color.WHITE);
//				g.drawString(options[i], 100, 510 + i * 60);
				drawStrCenter(g,smallFont,options[i],640,620 + i * 60);
			}
			
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL_SELECTOR);
		}
		if(currentChoice == 1) {
			System.exit(0);
			// help
		}
	}
	
	public void drawStrCenter(Graphics2D g,Font font,String str,int x, int y) {
		g.setFont(font);
		int strWidth = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
		int strHeight =(int) g.getFontMetrics().getStringBounds(str, g).getHeight();
		g.drawString(str,  x-strWidth/2 ,y-strHeight/2);
	}

	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			beep.play();
			select();
			
		}
		if(k == KeyEvent.VK_UP) {
			
			currentChoice--;
			if(currentChoice < 0) 
				currentChoice = 0;
			else
				beep.play();
		}
		if(k == KeyEvent.VK_DOWN) {
			
			currentChoice++;
			if(currentChoice >= options.length)
				currentChoice = options.length-1;
			else
				beep.play();

		}

		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MousePressed(int m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MouseReleased(int m) {
		// TODO Auto-generated method stub
		
	}

}
