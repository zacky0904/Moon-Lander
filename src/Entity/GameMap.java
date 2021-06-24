package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class GameMap {

	// position and vector
	protected final int tilesize = 4;
	protected int mapWidth;
	protected int mapHeight;
	
	protected boolean gridVisable;
	protected boolean mapInfoVisable;
	
	protected int col;
	protected int row;
	protected int[][] mapInfo;
	
	protected String[] infoBuffer = new String[5];
	
	
	protected float x;
	protected float y;
	

	protected int lzX;
	protected int lzY;
	
	protected int initX;
	protected int initY;
	
	protected int initSpeedX;
	protected int initSpeedY;
	
	protected float fuel1;
	protected float fuel2;
	
	
	protected BufferedImage source;
	
	public void loadImage(String s) {
		try {
			source = ImageIO.read(getClass().getResourceAsStream(s));
			mapWidth = source.getWidth();
			mapHeight = source.getHeight();
			row = (int)mapHeight/tilesize;
			col = (int)mapWidth/tilesize;
			mapInfo = new int[col][row];
			readMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setGridVisable(boolean flag) {
		gridVisable = flag;
	}
	
	public void setMapInfoVisable(boolean flag) {
		mapInfoVisable = flag;
	}
	
	public void setY(float y) {
		System.out.println(y);
		this.y = y;
	}
	
	public float getY() {
		return y;
	}
	
	public int[] getLandingPadPos() {
		int[] arr = new int[2];
		arr[0] = lzX;
		arr[1] = lzY;
		return arr;	
	}
	
	public int[] getInitialPoint() {
		int[] arr = new int[2];
		arr[0] = initX;
		arr[1] = initY;
		return arr;	
	}
	
	public int[] getInitialSpeed() {
		int[] arr = new int[2];
		arr[0] = initSpeedX;
		arr[1] = initSpeedY;
		return arr;	
	}
	
	public int[] mapSize() {
		int[] arr = new int[2];
		arr[0] = mapWidth;
		arr[1] = mapHeight;
		return arr;	
	}
	
	public float[] getFuel() {
		float[] arr = new float[2];
		arr[0] = fuel1;
		arr[1] = fuel2;
		return arr;	
	}
	
	public void loadMapFile(String s) {
		try {
			String strCurrentLine;
			InputStream stream = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			for(int i=0;i<infoBuffer.length;i++)
				if((strCurrentLine = br.readLine()) != null) 
					infoBuffer[i] = strCurrentLine;
//			strCurrentLine.indexOf("<Y>");
//			strCurrentLine.indexOf("<YEND>");
//	
//			System.out.println(infoBuffer[1].substring(infoBuffer[2].indexOf("<X>")+3,infoBuffer[0].indexOf("<XEND>")));
//			System.out.println(infoBuffer[1].substring(infoBuffer[2].indexOf("<Y>")+3,infoBuffer[0].indexOf("<YEND>")));
			
			initX = Integer.valueOf(infoBuffer[0].substring(infoBuffer[0].indexOf("<X>")+3,infoBuffer[0].indexOf("<XEND>")));
			initY = Integer.valueOf(infoBuffer[0].substring(infoBuffer[0].indexOf("<Y>")+3,infoBuffer[0].indexOf("<YEND>")));
			
			initSpeedX = Integer.valueOf(infoBuffer[1].substring(infoBuffer[1].indexOf("<X>")+3,infoBuffer[1].indexOf("<XEND>")));
			initSpeedY = Integer.valueOf(infoBuffer[1].substring(infoBuffer[1].indexOf("<Y>")+3,infoBuffer[1].indexOf("<YEND>")));
			
			lzX = Integer.valueOf(infoBuffer[2].substring(infoBuffer[2].indexOf("<X>")+3,infoBuffer[2].indexOf("<XEND>")));
			lzY = Integer.valueOf(infoBuffer[2].substring(infoBuffer[2].indexOf("<Y>")+3,infoBuffer[2].indexOf("<YEND>")));
			
			fuel1 = Integer.valueOf(infoBuffer[3].substring(infoBuffer[3].indexOf("<X>")+3,infoBuffer[3].indexOf("<XEND>")));
			
			fuel2 = Integer.valueOf(infoBuffer[4].substring(infoBuffer[4].indexOf("<X>")+3,infoBuffer[4].indexOf("<XEND>")));
			System.out.println(fuel2);
			
				
		}catch(IOException e) {
			e.printStackTrace();
		}

	}
	
	private void readMap() {
		//find border
		for(int i=0;i<mapWidth/tilesize;i++) {
			for(int j=0;j<mapHeight/tilesize;j++) {
				if((source.getRGB(i*tilesize,j*tilesize)& 0xFF000000)  != 0xFF000000) {
					mapInfo[i][j] = 0;
					if((i+1)!=mapWidth/tilesize)
						if(((source.getRGB((i+1)*tilesize,j*tilesize) & 0xFF000000) == 0xFF000000 ))
							mapInfo[i+1][j] = 1;
					if(i!=0)
						if(((source.getRGB((i-1)*tilesize,j*tilesize) & 0xFF000000) == 0xFF000000))
							mapInfo[i-1][j] = 1;
					if(j!=mapHeight/tilesize)
						if(((source.getRGB(i*tilesize,(j+1)*tilesize)& 0xFF000000) == 0xFF000000))
							mapInfo[i][j+1] = 1;
					if(j!=0)
						if(((source.getRGB(i*tilesize,(j-1)*tilesize)& 0xFF000000)== 0xFF000000))
							mapInfo[i][j-1] = 1;
				}
				
			}
		}
	}
	public int getTileSize() {
		return tilesize;
	}
	
	public int[][] getMapInfo() {
		return mapInfo;
	}
	
	public int getMapInfo(int x, int y) {
		if(x<0)
			x=0;
		if(y<0)
			y=0;
		if(x>col)
			x=col;
		if(y>row)
			y=row;
		if(x > col-1)
			x = col-1;
		if(y > row-1)
			x = row-1;
		return mapInfo[x][y];
	}
	
//	public void setPosition(float x, float y) {
//		this.x = x;
//		this.y = y;
//	}
	
	public BufferedImage getImage() {
		return source;
		
	}
	
	public void showInfo() {
		Graphics2D g = (Graphics2D) source.getGraphics();
		g.setColor(Color.YELLOW);
		for(int i=0;i<mapWidth/tilesize;i++) {
			for(int j=0;j<mapHeight/tilesize;j++) {
				if(mapInfo[i][j] == 1)
					g.fillRect((int)(x+i*tilesize), (int)(y+j*tilesize), tilesize, tilesize);
			}
		}	
		g.dispose();
	}
	
//	public void draw(java.awt.Graphics2D g) {
//		g.drawImage(source,(int)(x),(int)(y),	null);
//		g.setColor(Color.RED);
//		
//		if(gridVisable) {
//			for(int i=3;i<mapWidth;i+=tilesize)
//				g.drawLine(i, 0, i, mapHeight);
//			for(int i=3;i<mapHeight;i+=tilesize)
//				g.drawLine(0, i, mapWidth, i);
//		}
//		
//		if(mapInfoVisable) {
//
//				}
//			}
//		}
//		
//	}
	

}


