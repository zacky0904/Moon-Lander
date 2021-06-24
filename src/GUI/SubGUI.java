package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;


public class SubGUI {
	
	private boolean VISIBLE ;
	
	private Font font;
	private String str;
	

	

	public SubGUI() {
		try{
			InputStream stream =  GUI.class.getResourceAsStream("/Font/AtariClassic-gry3.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
			font = font.deriveFont(30F);
		} catch(FontFormatException|IOException e) {e.printStackTrace();}
		VISIBLE = true;

	}
	
	
	public void update() {


	}
	
	public void draw(Graphics2D g) {
		if(VISIBLE) {
			g.setFont(font);
			int strWidth = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
			int strHeight =(int) g.getFontMetrics().getStringBounds(str, g).getHeight();
			g.setColor(Color.YELLOW);
			g.drawString(str,  640-strWidth/2 ,250-strHeight/2);
		}
	}
	
	public void setString(String str) {
		this.str = str;
	}
	
	public void setVisible(boolean flag) {
		VISIBLE = flag;
	}

}
