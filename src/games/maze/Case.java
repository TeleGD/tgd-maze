package games.maze;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Case {

	protected int posX;
	protected int posY;
	protected int i;
	protected int j;
	private int size;
	protected boolean passable;
	protected Image img;

	public Case(int i, int j, int size, Image img, boolean passable) {
		this.i = i;
		this.j = j;
		this.img = img;
		this.size = size;
		this.passable = passable;
	}

	public void render(GameContainer container, StateBasedGame game, Graphics context) {
		context.drawImage(img, j * size, i * size, (j + 1) * size, (i + 1) * size, 0, 0, img.getWidth() - 1, img.getHeight() - 1);
	}

	public boolean isPassable() {
		return passable;
	}

	public boolean isEquals(Case c) {
		return (c.i == this.i && c.j == this.j);
	}

	public int getSize() {
		return size;
	}

	public int[] getImgInitSize() {
		int temp[] = new int[2];
		temp[0] = img.getWidth();
		temp[1] = img.getHeight();
		return temp;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getPlayerId() {
		// TODO Auto - generated method stub
		return 0;
	}

	public void setPlayerId(int indexOf) {
		// TODO Auto - generated method stub
	}

}
