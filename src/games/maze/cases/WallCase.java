package games.maze.cases;

import org.newdawn.slick.Image;

import app.AppLoader;

import games.maze.Case;
import games.maze.World;

public class WallCase extends Case {

	private boolean destructible;
	private static Image image;

	static {
		image = AppLoader.loadPicture(World.DIRECTORY_IMAGES + "wall.png");
	}

	public WallCase(int i, int j, int size, boolean destructible) {
		super(i, j, size, image, false);
		this.destructible = destructible;
	}

	public boolean isDestructible() {
		return destructible;
	}

}
