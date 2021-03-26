package games.maze.cases;

import org.newdawn.slick.Image;

import app.AppLoader;

import games.maze.Case;
import games.maze.World;

public class FreeCase extends Case {

	private int playerId;
	private int bonusId;
	private static Image image;

	static {
		image = AppLoader.loadPicture(World.DIRECTORY_IMAGES + "ground.png");
	}

	public FreeCase(int i, int j, int size) {
		super(i, j, size, image, true);
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getBonusId() {
		return bonusId;
	}

	public void setBonusId(int bonusId) {
		this.bonusId = bonusId;
	}

}
