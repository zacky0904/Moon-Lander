package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import GameEngine.GameStatus;

public class GUI {
	private boolean VISIBLE ;
	private Font font;
	private int speedX;
	private int speedY;
	private float fuel;
	private long time;
	private int second;
	protected int s;
	protected int m;
	private String SpdX;
	private String SpdY;
	private String FUEL;
	private String mode;
	
	protected String SECOND;
	protected String MINUTE;
	
	
	private String level = "MAP1";

	
	DecimalFormat fmt1 = new DecimalFormat("+#,000;-#");
	DecimalFormat fmt2 = new DecimalFormat("0.00");
	DecimalFormat fmt3 = new DecimalFormat("00");
	
	public GUI() {
		try{
			InputStream stream =  GUI.class.getResourceAsStream("/Font/AtariClassic-gry3.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
			font = font.deriveFont(24F);
		} catch(FontFormatException|IOException e) {e.printStackTrace();}
		VISIBLE = true;

	}
	
	
	public void update() {
		second = (int) (time/1000000000);
		m = second/60;
		s = second%60;
		//System.out.println(s);
		SpdX = fmt1.format(speedX);
		SpdY = fmt1.format(-speedY);
		FUEL = fmt2.format(fuel);
		MINUTE = fmt3.format(m);
		SECOND = fmt3.format(s);
		if(GameStatus.difficulty == 0)
			mode = "A";
		else
			mode = "B";

	}
	
	public void draw(Graphics2D g) {
		if(VISIBLE) {
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("X: "+ SpdX + " M/S",  1000 ,30);
			g.drawString("Y: "+ SpdY + " M/S",  1000 ,60);
			g.drawString("FUEL: "+ FUEL,  1000 ,90 );
		
			g.drawString("T+"+ m +":" + s,  20 ,30 );
			g.drawString("LEVEL:" + level,  20 ,60 );
			g.drawString("MODE "+ mode,  20 ,90 );
		}
	}
	
	public void getLevel(String x) {
		level = x;
	}
//	public void getGameStatus(GameStages state) {
//		switch(state) {
//		case ASCENT_EXPLODE:
//			status = "FAILED";
//			break;
//		case DESCENT_EXPLODE:
//			status = "FAILED";
//			break;
//		case DESCENT_STAGE_RUNNING:
//			status = "LANDING";
//			break;
//		case ASCENT_STAGE_STANDBY:
//			status = "WAITING FOR COMMAND";
//			break;
//		case ASCENT_STAGE_RUNNING:
//			status = "EVACUATE";
//			break;
//		default:
//			break;
//		}
//		
//	}
	
	public void getSpeed(int x, int y) {
		speedX = x;
		speedY = y;
	}
	
	public void getFuel(float x) {
		fuel = x;
	}
	
	public void getTime(long x) {
		time = x;
	}

	public void setVisible(boolean flag) {
		VISIBLE = flag;
	}

}
