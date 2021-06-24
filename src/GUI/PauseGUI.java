package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

import Audio.AudioPlayer;

public class PauseGUI {
	private boolean VISIBLE ;
	private Font font;
	private Font titleFont;
	
	private int currentChoice = 0;
	private AudioPlayer beep;
	private String[] options = {
			"RESUME",
			"RETRY",
			"EXIT TO MENU"
		};
	
	public PauseGUI() {
		try{
			InputStream stream =  PauseGUI.class.getResourceAsStream("/Font/AtariClassic-gry3.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
			font = font.deriveFont(24F);
			titleFont = font;
			titleFont = titleFont.deriveFont(50F);
			beep = new AudioPlayer("/Audio/sfx_select.wav",-10f);
		} catch(FontFormatException|IOException e) {e.printStackTrace();}
	}
	
	
	public void update() {


	}
	
	public void draw(Graphics2D g) {
		if(VISIBLE) {
			g.setColor(Color.WHITE);
			drawStrCenter(g,titleFont,"PAUSE MENU",640,300);
			for(int i = 0; i < options.length; i++) {
				if(i == currentChoice) {
					g.setColor(Color.YELLOW);
					drawStrCenter(g,font,"["+options[i]+"]",640,400 + i * 60);
				}
				else {
					g.setColor(Color.WHITE);
					drawStrCenter(g,font,options[i],640,400 + i * 60);
				}
			}
		}
	}
	

	
	public void setChoice(int flag) {
		currentChoice += flag;
		if(currentChoice < 0)
			 currentChoice = 0;
		else
			beep.play();
		if(currentChoice > 2)
			currentChoice = 2;
		else
			beep.play();
	}
	
	public void beep() {
		beep.play();
	}
	
	public int getChoice() {
		return currentChoice;
	}
	
	public void setVisible(boolean flag) {
		VISIBLE = flag;
	}
	
	private void drawStrCenter(Graphics2D g,Font font,String str,int x, int y) {
		g.setFont(font);
		int strWidth = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
		int strHeight =(int) g.getFontMetrics().getStringBounds(str, g).getHeight();
		g.drawString(str,  x-strWidth/2 ,y-strHeight/2);
	}

}
