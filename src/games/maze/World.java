package games.maze;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import app.AppGame;
import app.AppWorld;

import games.maze.players.HunterPlayer;
import games.maze.players.VictimPlayer;

public class World extends AppWorld {

	public final static String DIRECTORY_SOUNDS = "/sounds/maze/";
	public final static String DIRECTORY_MUSICS = "/musics/maze/";
	public final static String DIRECTORY_IMAGES = "/images/maze/";
	public Board board;
	public ArrayList<Player> players;
	private int height;
	private int width;
	private int timer;

	public World(int ID) {
		super(ID);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {}

	@Override
	public void play(GameContainer container, StateBasedGame game) {
		AppGame appGame = (AppGame) game;
		int n = appGame.appPlayers.size();
		height = container.getHeight();
		width = container.getWidth();
		board = new Board(this, 50);
		players = new ArrayList<Player>();
		players.add(new VictimPlayer(this, appGame.appPlayers.get(0), 0));
		for (int i = 1; i < n; i++) {
			players.add(new HunterPlayer(this, appGame.appPlayers.get(i), i));
		}
		timer = 90000; // temps de la partie(90s)
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		super.update(container, game, delta);
		board.update(container, game, delta);
		for (Player p: players) {
			p.update(container, game, delta);
		}
		timer -= delta;
		if (timer <= 0) {
			endGame(players.get(0));
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		board.render(container, game, context);
		for (Player p: players) {
			p.render(container, game, context);
		}
	}

	public void endGame(Player p) {
		System.out.println("Le player " + p.getID() + " a gagné");
		// Met fin au jeu
		// le joueur p est le gagnant
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

}
