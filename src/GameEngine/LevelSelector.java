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

public class LevelSelector extends GameEngine {
	
	private BufferedImage background;
	private Font titleFont;
	private Font smallFont;
	private int currentChoice = 0;
	private int totalLevel = 9;
	private int levelChoice = 0;
	private int DifficultyChoice = 0;
	
	private AudioPlayer beep;
	
	private String[] options = {
			"LAUNCH!",
			"LEVEL SELECT ",
			" DIFFICULTY",
			"HOW TO PLAY",
			"RETURN"
		};
	
	public LevelSelector(GameStateManager gsm) {
		this.gsm = gsm;	
		try{
			background = ImageIO.read(getClass().getResourceAsStream("/Background/New Star.jpg"));
			InputStream stream =  MenuState.class.getResourceAsStream("/Font/AtariClassic-gry3.ttf");
			smallFont = Font.createFont(Font.TRUETYPE_FONT, stream);
			titleFont = smallFont;
			smallFont = smallFont.deriveFont(24F);
			titleFont = titleFont.deriveFont(70F);
			beep = new AudioPlayer("/Audio/sfx_select.wav",-10f);

		} catch(FontFormatException|IOException e) {e.printStackTrace();}
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(background, 0,0, null);
		g.setColor(Color.WHITE);
		g.setFont(titleFont);
//		drawStrCenter(g,titleFont,"GAME MODE",640,200);
		g.drawString("MENU", 100, 200);
		g.setFont(smallFont);
		
		if(DifficultyChoice == 0)
			options[2] = "DIFFICULTY: "+ "NORMAL";
		else
			options[2] = "DIFFICULTY: "+ "HARD";
		
		if(levelChoice == 0)
			options[1] = "LEVEL SELECT: "+ "FROM THE START";
		else
			options[1] = "LEVEL SELECT: "+ "STAGE "+levelChoice;
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.YELLOW);
				if(i == 1 || i == 2)
					g.drawString(" <"+options[i]+">" , 70, 400 + i * 60);
				else
					g.drawString("["+options[i]+"]", 70, 400 + i * 60);
			}
			else {
				g.setColor(Color.WHITE);
				if(i == 1 || i == 2)
					g.drawString("  "+options[i] , 70, 400 + i * 60);
				else
					g.drawString(" "+options[i], 70, 400 + i * 60);
			}
		}

	}
	
	private void select() {
		if(currentChoice == 0) {
			if(DifficultyChoice == 0)
				GameStatus.difficulty = 0;
			if(DifficultyChoice == 1)
				GameStatus.difficulty = 1;
			if(levelChoice == 0) {
				GameStatus.mode = GameStatus.gamemode.SPEEDRUN;
				gsm.setState(GameStateManager.LEVEL1);
			}else {
				GameStatus.mode = GameStatus.gamemode.SINGLELEVEL;
				if(levelChoice == 1)
					gsm.setState(GameStateManager.LEVEL1);
				if(levelChoice == 2)
					gsm.setState(GameStateManager.LEVEL2);
				if(levelChoice == 3)
					gsm.setState(GameStateManager.LEVEL3);
				if(levelChoice == 4)
					gsm.setState(GameStateManager.LEVEL4);
				if(levelChoice == 5)
					gsm.setState(GameStateManager.LEVEL5);
				if(levelChoice == 6)
					gsm.setState(GameStateManager.LEVEL6);
				if(levelChoice == 7)
					gsm.setState(GameStateManager.LEVEL7);
				if(levelChoice == 8)
					gsm.setState(GameStateManager.LEVEL8);
				if(levelChoice == 9)
					gsm.setState(GameStateManager.LEVEL9);
			}
				
		}
		if(currentChoice == 3) {
			gsm.setState(GameStateManager.HOW2PLAY_STATE);
		}
		if(currentChoice == 4) {
			gsm.setState(GameStateManager.MENU_STATE);
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
			if(currentChoice !=1 && currentChoice != 2)
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
				currentChoice = options.length - 1;
			else
				beep.play();
		}
		if(k == KeyEvent.VK_LEFT && currentChoice == 1) {
			levelChoice--;
			if(levelChoice < 0) 
				levelChoice = 0;
			else
				beep.play();
			
		}
		if(k == KeyEvent.VK_RIGHT && currentChoice == 1) {
			levelChoice++;
			if(levelChoice > totalLevel)
				levelChoice = totalLevel;
			else
				beep.play();
			
		}
		if(k == KeyEvent.VK_LEFT && currentChoice == 2) {
			DifficultyChoice--;
			if(DifficultyChoice < 0) 
				DifficultyChoice = 0;
			else
				beep.play();
			
		}
		if(k == KeyEvent.VK_RIGHT && currentChoice == 2) {
			DifficultyChoice++;
			if(DifficultyChoice > 1)
				DifficultyChoice = 1;
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
