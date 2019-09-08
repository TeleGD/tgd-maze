package games.maze.cases;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import games.maze.Case;
import games.maze.World;

public class WallCase extends Case {

	private boolean destructible;
	private static Image image;

	static {
		try {
			image = new Image(World.DIRECTORY_IMAGES + "wall.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public WallCase(int i, int j, int size, boolean destructible) {
		super(i, j, size, image, false);
		this.destructible = destructible;
	}

	public boolean isDestructible() {
		return destructible;
	}

}
