package GameEngine;

public class GameStatus {

	public static gamemode mode = gamemode.SPEEDRUN;
	public static int difficulty;
	public static int time;
	public static boolean debugFlag = false;
	
	public enum gamemode{
		SPEEDRUN,
		SINGLELEVEL,
	}
}
