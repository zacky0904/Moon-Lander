package GameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class How2PlayState extends GameEngine{
	
	private BufferedImage background;
	
	private String[] manual = {
		"1.USE UP,RIGHT,LEFT KEY TO CONTROL YOUR MOON LANDER",
		"2.LAND ON THE LANDING ZONE MARKED BY GREEN LIGHT",
		"3.EVACUATE BACK TO THE MOON ORBIT BY ASCENT STAGE",
		"4.YOUR SHIP MIGHT EXPLODE IF YOU CRASHED IN TO THE MOON SURFACE",
		"5.PRESSED R KEY TO RETRY",
		"6.ENJOY"

	};
	
	private Font smallFont;
	
	public How2PlayState(GameStateManager gsm) {
		this.gsm = gsm;	
		try{
			background = ImageIO.read(getClass().getResourceAsStream("/Background/New Star.jpg"));
			InputStream stream =  How2PlayState.class.getResourceAsStream("/Font/AtariClassic-gry3.ttf");
			smallFont = Font.createFont(Font.TRUETYPE_FONT, stream);
			smallFont = smallFont.deriveFont(15f);

		} catch(FontFormatException|IOException e) {e.printStackTrace();}

		init();
		
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(background,0,0,null);
		g.setColor(Color.WHITE);
		g.setFont(smallFont);
		for(int i =0 ;i<manual.length;i++) {
			g.setColor(Color.YELLOW);
			g.drawString(manual[i],30,230 + i * 60);
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
		gsm.setState(GameStateManager.LEVEL_SELECTOR);
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
