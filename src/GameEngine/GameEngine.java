package GameEngine;

public abstract class GameEngine {
	
	protected GameStateManager gsm;
	
	//input
	protected int[] mousePos = new int[2];
//	protected boolean[] mouseButtonPressed = new boolean[3];
//	protected boolean[] mouseButtonReleased =  new boolean[3];


//	protected int[] keyReleased;
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	public abstract void MousePressed(int m);
	public abstract void MouseReleased(int m);
	public void MouseMoved(int x, int y) {
		mousePos[0] = x;
		mousePos[1] = y;
	}

}
