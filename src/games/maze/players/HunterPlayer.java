package games.maze.players;

import app.AppPlayer;

import games.maze.Player;
import games.maze.World;

public class HunterPlayer extends Player {

	public HunterPlayer(World w, AppPlayer aplayer, int id) {
		super(w, aplayer, id);
	}

	public void collideWithPlayer(Player p) {
		if (p instanceof VictimPlayer) {
			world.endGame(this);
		}
	}

}
