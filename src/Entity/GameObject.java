package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;



public class GameObject {
	
	protected boolean DISABLE = false;
	protected boolean VISIBLE = true;
	protected boolean BOUND_VISIBLE = false;
	
	// position and vector
	protected double x;
	protected double y;
	protected double angle;
	
	protected double dx;
	protected double dy;
	protected double da;
	
	// dimensions
	protected int width;
	protected int height;
	
	//out dimensions
	protected int oWidth;
	protected int oHeight;

	//hitbox dimensions
	protected int hWidth;
	protected int hHeight;
	
	protected Polygon hitboxs;
	
	protected BufferedImage source;
	protected BufferedImage output;
	
	
	public void disable(boolean flag) {
		DISABLE = flag;
	}
	
	public void visible(boolean flag) {
		VISIBLE = flag;
	}
	
	public void boundVisible(boolean flag) {
		BOUND_VISIBLE = flag;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx , double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setVectorX(double dx) {
		this.dx = dx;
	}
	
	public void setVectorY( double dy) {
		this.dy = dy;
	}
	
	public void setVectorA( double da) {
		this.da = da;
	}

	public void setHitbox(int w, int h) {
		hWidth = w;
		hHeight = h;
	}

	public double[] getPosition() {
		double[] ret = new double[2];
		ret[0] = x;
		ret[1] = y;
		return ret;
	}
	
	public double[] getOffsetPosition() {
		double[] ret = new double[2];
		ret[0] = x-oWidth/2;
		ret[1] = y-oHeight/2;
		return ret;
	}
	
	public double[] getVector() {
		double[] ret = new double[2];
		ret[0] = dx;
		ret[1] = dy;
		return ret;
	}
	
	public double getVectorX() {
		return dx;
	}
	
	public double getVectorY() {
		return dy;
	}
	
	public double getVectorA() {
		return da;
	}
	
	public void setAngle(double a) {
		angle = a;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public double[] getHitbox() {
		double[] ret = new double[2];
		ret[0] = hWidth;
		ret[1] = hHeight;
		return ret;
	}
	

	
	
	public void setImage(BufferedImage img) {
		source = img;
		output = source;
		width = source.getWidth();
		height = source.getHeight();
		oWidth = width;
		oHeight = height;
//		System.out.println("Width" + width);

	}
	
	public boolean checkCollid(GameObject gm2) {
		boolean collid = false;
		int ndx = (int) ((Math.abs(x+dx-gm2.getPosition()[0])+gm2.getVectorX()) - (hWidth+gm2.getHitbox()[0])/2+1);
		int ndy = (int) ((Math.abs(y+dy-gm2.getPosition()[1])+gm2.getVectorY()) - (hHeight+gm2.getHitbox()[1])/2+1);
		if( ndx <= 0 && ndy <= 0) collid = true;
		if( ndx < 0 && ndy < 0) {
			
			if(dy>0) {
				dy += ndy;
				ndx = 0;
			}
			else if(dy<0) {
				dy -= ndy;
				ndx = 0;
			}
			
			if(dx>0) {
				dx += ndx;
			}
			else if(dx<0) {
				dx -= ndx;
			}

			collid = true;
		}
	
		return collid;
	}
	
	public boolean checkCollid(GameMap map) {
		boolean xCollid = false;
		boolean yCollid = false;
		int tilesize = map.getTileSize();
		int xStart = (int) (x - hWidth/2 + dx)/tilesize;
		int yStart = (int) (y - hHeight/2 + dy)/tilesize;
		int xSize = hWidth/tilesize;
		int ySize = hHeight/tilesize;
		int ndx = (int) Math.ceil(Math.abs(dx)/tilesize);
		int ndy = (int) Math.ceil(Math.abs(dy)/tilesize);
//		System.out.println(ySize);
		if(dy>0) {
			for(int j=0;j<ndy;j++)
				for(int i=0;i<xSize;i++)
					if(map.getMapInfo( xStart+i, (int) (y + hHeight/2)/tilesize + j) == 1) {
						dy = j*tilesize;
						yCollid = true;
						break;
					}
		}
		if(dy<0) {
			for(int j=0;j<ndy;j++)
				for(int i=0;i<xSize;i++)
					if(map.getMapInfo(xStart+i, (int) (y - hHeight/2)/tilesize - j) == 1) {
						dy = -j*tilesize;;
						yCollid = true;
						break;
					}

		}
		if(dx>0) {
			for(int j=0;j<ndx;j++)
				for(int i=0;i<ySize;i++)
					if(map.getMapInfo((int) (x + hWidth/2)/tilesize + j, yStart+i) == 1) {
						dx = j*tilesize;
						xCollid = true;
						break;
					}
		}
		if(dx<0) {
			for(int j=0;j<ndx;j++)
				for(int i=0;i<ySize;i++)
					if(map.getMapInfo((int) (x - hWidth/2)/tilesize - j, yStart+i) == 1) {
						dx = -j*tilesize;
						xCollid = true;
						break;
					}
		}
		return xCollid || yCollid;
		 
	}

	
	public void drawBound() {
		if(output!=null) {
			Graphics2D graphics = output.createGraphics();
			graphics.setColor(Color.RED);
			graphics.drawRect((oWidth-hWidth)/2, (oHeight-hHeight)/2, hWidth-1, hHeight-1);
			graphics.drawOval((int)oWidth/2 - 5/2,(int)oHeight/2 - 5/2, 5, 5);
			graphics.dispose();
		}
	}
	
	public void  RotateImage() {
	    double radian = Math.toRadians(angle);
	    double sin = Math.abs(Math.sin(radian));
	    double cos = Math.abs(Math.cos(radian));

	    oWidth = (int) Math.floor((double) width * cos + (double) height * sin);
	    oHeight = (int) Math.floor((double) height * cos + (double) width * sin);
	    output = new BufferedImage(
	    		oWidth, oHeight, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D graphics = output.createGraphics();

	    graphics.setRenderingHint(
	            RenderingHints.KEY_INTERPOLATION,
	            RenderingHints.VALUE_INTERPOLATION_BICUBIC);

	    graphics.translate((oWidth - width) / 2, (oHeight - height) / 2);
	    graphics.rotate(radian, (double) (width / 2), (double) (height / 2));
	    graphics.drawImage(source, 0, 0, null);
	    graphics.dispose();
	
	}	
	
	public BufferedImage getImage() {
		if(angle != 0 && VISIBLE)
			 RotateImage();
		if(BOUND_VISIBLE)
			drawBound();
		if(VISIBLE)
			return output;
		else
			return output = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
	}

}
