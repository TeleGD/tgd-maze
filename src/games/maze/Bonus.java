package games.maze;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Bonus {

	protected int posX;
	protected int posY;
	protected int ID;
	protected boolean picked;

	public Bonus(int x, int y) {
		posX = x;
		posY = y;
		picked = false;
	}

	public void update(GameContainer container, StateBasedGame game, Graphics context) {}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		if (!picked) {
			// context.drawImage(img, ?, ?)
		}
	}

}
