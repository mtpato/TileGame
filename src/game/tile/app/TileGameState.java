package game.tile.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TileGameState implements GameState {
	Set<Integer> players = new HashSet<Integer>();
	TileNode board;
	HashMap<String,TileNode> tiles;
	int width;
	int height;
	HashMap<Integer,Integer> scores;
	boolean over = false;
	
	
	public TileGameState(Set<Integer> players, int height, int width) {
		this.players = players;
		this.height = height;
		this.width = width;
		
		
		tiles = new HashMap<String,TileNode>();
		scores = new HashMap<Integer,Integer>();
	}
	
}