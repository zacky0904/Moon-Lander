package GameEngine;

import GameLevel.LEVEL1;
import GameLevel.LEVEL2;
import GameLevel.LEVEL3;
import GameLevel.LEVEL4;
import GameLevel.LEVEL5;
import GameLevel.LEVEL6;
import GameLevel.LEVEL7;
import GameLevel.LEVEL8;
import GameLevel.LEVEL9;

public class GameStateManager {

	private GameEngine[] gameStates;
	private int currentState;
	
	public static final int MENU_STATE = 0;
	public static final int LEVEL_SELECTOR = 1;
	public static final int LEVEL1 = 2;
	public static final int LEVEL2 = 3;
	public static final int LEVEL3 = 4;
	public static final int LEVEL4 = 5;
	public static final int LEVEL5 = 6;
	public static final int LEVEL6 = 7;
	public static final int LEVEL7 = 8;
	public static final int LEVEL8 = 9;
	public static final int LEVEL9 = 10;
	public static final int ENDING_STATE = 11;
	public static final int HOW2PLAY_STATE = 12;
	
	public GameStateManager() {
		gameStates = new GameEngine[13];
		currentState = MENU_STATE;
		loadState(currentState);
	}
	
	private void loadState(int state) {
		if(state == MENU_STATE)
			gameStates[state] = new MenuState(this);
		if(state == LEVEL_SELECTOR)
			gameStates[state] = new LevelSelector(this);
		if(state == LEVEL1)
			gameStates[state] = new LEVEL1(this);
		if(state == LEVEL2)
			gameStates[state] = new LEVEL2(this);
		if(state == LEVEL3)
			gameStates[state] = new LEVEL3(this);
		if(state == LEVEL4)
			gameStates[state] = new LEVEL4(this);
		if(state == LEVEL5)
			gameStates[state] = new LEVEL5(this);
		if(state == LEVEL6)
			gameStates[state] = new LEVEL6(this);
		if(state == LEVEL7)
			gameStates[state] = new LEVEL7(this);
		if(state == LEVEL8)
			gameStates[state] = new LEVEL8(this);
		if(state == LEVEL9)
			gameStates[state] = new LEVEL9(this);
		if(state == ENDING_STATE)
			gameStates[state] = new EndingState(this);
		if(state == HOW2PLAY_STATE)
			gameStates[state] = new How2PlayState(this);
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public void update() {
		try {
			gameStates[currentState].update();
		} catch(Exception e) {}
	}
	
	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch(Exception e) {}
	}
	
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}

}
