package GameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


public class EndingState extends GameEngine{
	
	private BufferedImage background;
	
//	private String[] thanks = {
//		"START",
//		"QUIT",
//
//	};
	
	private Font titleFont;
	private Font smallFont;
	
	public EndingState(GameStateManager gsm) {
		this.gsm = gsm;	
		try{
			background = ImageIO.read(getClass().getResourceAsStream("/Background/New Star.jpg"));
			InputStream stream =  EndingState.class.getResourceAsStream("/Font/AtariClassic-gry3.ttf");
			titleFont = Font.createFont(Font.TRUETYPE_FONT, stream);
			titleFont = titleFont.deriveFont(70F);
			smallFont = titleFont;
			smallFont = smallFont.deriveFont(40f);

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
//		g.drawImage(pl.getImage(), (int)pl.getPosition()[0],(int)pl.getPosition()[1], null);
		g.setColor(Color.WHITE);
		drawStrCenter(g,titleFont,"THANKS FOR",440,320);
		drawStrCenter(g,titleFont,"PLAYING!",440,420);
		g.setFont(smallFont);

		
	}
	
	
	public void drawStrCenter(Graphics2D g,Font font,String str,int x, int y) {
		g.setFont(font);
		int strWidth = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
		int strHeight =(int) g.getFontMetrics().getStringBounds(str, g).getHeight();
		g.drawString(str,  x-strWidth/2 ,y-strHeight/2);
	}

	@Override
	public void keyPressed(int k) {
		gsm.setState(GameStateManager.MENU_STATE);
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
